package com.scholarsync.e2e;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Epic("Conference Management System")
@Feature("Paper Submission")
public class ScholarSyncE2ETest {

    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeAll
    static void launchBrowser() {
        ensurePdfExists();
        playwright = Playwright.create();
        // Jenkins 容器环境必须开启无头模式
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(true)
                .setSlowMo(100));
    }

    @AfterAll
    static void closeBrowser() {
        if (playwright != null) playwright.close();
    }

    @BeforeEach
    void createContext() {
        context = browser.newContext();
        page = context.newPage();

        // 调试：将浏览器内部日志输出到 Jenkins 控制台
        page.onConsoleMessage(msg -> System.out.println("浏览器日志 >> [" + msg.type() + "] " + msg.text()));
        page.onPageError(err -> System.out.println("浏览器崩溃/错误 >> " + err));
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        // 自动截图：在 Allure 报告中保留最后操作画面
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Allure.addAttachment("Final State - " + testInfo.getDisplayName(), new ByteArrayInputStream(screenshot));
        if (context != null) context.close();
    }

    @Test
    @Story("User can register, login and submit a paper")
    @Description("完整的端到端测试：从注册新账号开始，通过登录进入系统并提交论文。")
    @Severity(SeverityLevel.CRITICAL)
    public void testFullPaperSubmissionFlow() {

        step("Navigate to Homepage", () -> {
            page.navigate("http://host.docker.internal:4200/");
            page.locator("button.btn-primary:has-text('Get Started')").waitFor();
            page.locator("button.btn-primary:has-text('Get Started')").click();
        });

        step("Register Test User (New Account)", () -> {
            page.waitForURL("**/auth");

            // 切换到注册页面
            Locator toggleBtn = page.locator(".auth-switch button:has-text('Create an account')");
            if (toggleBtn.isVisible()) {
                toggleBtn.click();
            }

            // 根据你的 HTML 属性填表
            page.locator("input[name='fName']").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            page.locator("input[name='fName']").fill("Author");
            page.locator("input[name='lName']").fill("Test");
            page.locator("input[name='regEmail']").fill("author@scholarsync.com");
            page.locator("input[name='regPass']").fill("author123");
            page.locator("input[name='aff']").fill("UTM Melaka");
            page.locator("input[name='cnt']").fill("Malaysia");

            // 点击“Create Account”按钮
            page.locator("button.btn-primary-lg:has-text('Create Account')").click();
            System.out.println("Registration submitted...");
        });

        step("Login to System", () -> {
            // 确保回到登录视图
            page.navigate("http://host.docker.internal:4200/auth");

            // 填写登录表单
            page.locator("input[name='email']").fill("author@scholarsync.com");
            page.locator("input[name='password']").fill("author123");

            // 点击“Sign In”按钮
            page.locator("button.btn-primary-lg:has-text('Sign In')").click();
        });

        step("Start Paper Submission Wizard", () -> {
            // 等待页面跳转至论文提交页
            page.waitForURL("**/paper-submission", new Page.WaitForURLOptions().setTimeout(60000));
            page.locator("button.btn-primary:has-text('Submit New Paper')").click();
        });

        step("Wizard Step 1: Select Conference", () -> {
            page.locator("select[name='conference_id']").waitFor();
            page.selectOption("select[name='conference_id']", new SelectOption().setValue("1"));
            page.locator("button[matsteppernext]").click();
        });

        step("Wizard Step 2: Paper Info", () -> {
            page.locator("input[placeholder*='title']").fill("E2E-Automated-Paper-" + System.currentTimeMillis());
            page.locator("select").filter(new Locator.FilterOptions().setHasText("Select a track")).selectOption("1");
            page.locator("textarea[placeholder*='abstract']").fill("Automated abstract content for testing purposes.");
            page.locator("button[matsteppernext]:visible").click();
        });

        step("Wizard Step 4: Upload File", () -> {
            Path pdfPath = Paths.get("src/test/resources/sample.pdf");
            page.setInputFiles("input[type='file']", pdfPath);
            page.locator("button[matsteppernext]:visible").click();
        });

        step("Wizard Step 5: Final Submission", () -> {
            page.locator("button.btn-primary:has-text('Submit Paper')").click();
            System.out.println("E2E Test Flow Completed Successfully!");
        });
    }

    @Step("{0}")
    private void step(String stepName, Runnable runnable) {
        runnable.run();
    }

    private static void ensurePdfExists() {
        try {
            Path dirPath = Paths.get("src/test/resources");
            if (!Files.exists(dirPath)) Files.createDirectories(dirPath);
            File pdfFile = new File("src/test/resources/sample.pdf");
            if (!pdfFile.exists()) {
                Files.writeString(pdfFile.toPath(), "Dummy PDF content for testing.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}