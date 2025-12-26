package com.webcrafters.confease_backend.e2e;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Epic("Conference Management System")
@Feature("End-to-End Flow")
public class ScholarSyncE2ETest {

    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    // 🔥 适配 Jenkins 容器：访问宿主机的前端容器端口
    private static final String BASE_URL = "http://host.docker.internal:4200";

    @BeforeAll
    static void setupBrowser() {
        ensurePdfExists();
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true) // 🔥 Jenkins 必须开启无头模式
                        .setSlowMo(100)
        );
    }

    @AfterAll
    static void closeBrowser() {
        if (playwright != null) playwright.close();
    }

    @BeforeEach
    void createContext() {
        context = browser.newContext(
                new Browser.NewContextOptions().setViewportSize(1280, 800)
        );
        page = context.newPage();

        // 🔥 核心修复：自动处理注册成功后的 alert() 弹窗，防止浏览器冻结
        page.onDialog(Dialog::accept);

        // 🔥 核心调试：将浏览器报错直接打印到 Jenkins 控制台
        page.onConsoleMessage(msg -> System.out.println("浏览器日志 >> [" + msg.type() + "] " + msg.text()));
        page.onPageError(err -> System.out.println("浏览器崩溃 >> " + err));
    }

    @AfterEach
    void tearDown(TestInfo info) {
        // 自动截图并附加到 Allure 报告
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Allure.addAttachment("Final State - " + info.getDisplayName(), new ByteArrayInputStream(screenshot));
        context.close();
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("User Registration and Paper Submission")
    @Description("完整的流程：注册、登录、并完成论文提交向导。")
    public void testFullFlow() {
        String ts = String.valueOf(System.currentTimeMillis());
        String testEmail = "author" + ts + "@confease.com";

        step("Navigate to Landing Page", () -> {
            page.navigate(BASE_URL);
            page.locator("button:has-text('Get Started')").waitFor();
            page.click("button:has-text('Get Started')");
        });

        step("Register New User", () -> {
            page.waitForURL("**/auth");
            // 切换到注册视图
            Locator switchBtn = page.locator(".auth-switch button:has-text('Create an account')");
            if (switchBtn.isVisible()) switchBtn.click();

            // 填写表单：使用 HTML 里的 name 属性
            page.fill("input[name='fName']", "Author");
            page.fill("input[name='lName']", "Test");
            page.fill("input[name='regEmail']", testEmail);
            page.fill("input[name='regPass']", "author123");
            page.fill("input[name='aff']", "UTM Melaka");
            page.fill("input[name='cnt']", "Malaysia");

            // 点击提交：这将触发 alert() 并由 onDialog 自动处理
            page.click("button.btn-primary-lg:has-text('Create Account')");
            System.out.println("注册请求已发送: " + testEmail);
        });

        step("Login to System", () -> {
            // 注册成功后视图会自动切回 Login
            Locator loginEmail = page.locator("input[name='email']").first();
            loginEmail.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15000));

            loginEmail.fill(testEmail);
            page.fill("input[name='password']", "author123");
            page.click("button.btn-primary-lg:has-text('Sign In')");
        });

        step("Start Paper Submission Wizard", () -> {
            // 等待登录接口响应并跳转
            page.waitForURL("**/paper-submission", new Page.WaitForURLOptions().setTimeout(60000));
            page.click("button.btn-primary:has-text('Submit New Paper')");
        });

        step("Wizard Step 1: Select Conference", () -> {
            Locator confSelect = page.locator("select[name='conference_id']");
            confSelect.waitFor();
            // 确保 data.sql 已成功插入 ID 为 1 的会议
            page.selectOption("select[name='conference_id']", new SelectOption().setValue("1"));
            page.click("button[matsteppernext]");
        });

        step("Wizard Step 4: Upload File", () -> {
            Path pdfPath = Paths.get("src/test/resources/sample.pdf");
            page.setInputFiles("input[type='file']", pdfPath);
            page.click("button[matsteppernext]:visible");
        });

        step("Final Submission", () -> {
            page.click("button.btn-primary:has-text('Submit Paper')");
            System.out.println("✅ 整个端到端测试流程成功跑通！");
        });
    }

    @Step("{0}")
    private void step(String name, Runnable action) {
        System.out.println("▶ 执行步骤: " + name);
        action.run();
    }

    private static void ensurePdfExists() {
        try {
            Path dirPath = Paths.get("src/test/resources");
            if (!Files.exists(dirPath)) Files.createDirectories(dirPath);
            File pdfFile = new File("src/test/resources/sample.pdf");
            if (!pdfFile.exists()) Files.writeString(pdfFile.toPath(), "Test PDF content.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}