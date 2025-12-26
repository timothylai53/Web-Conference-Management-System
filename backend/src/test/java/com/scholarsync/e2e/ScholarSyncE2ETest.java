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
@Feature("End-to-End Testing")
public class ScholarSyncE2ETest {

    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    // 🔥 适配 Jenkins：必须使用 host.docker.internal 访问前端容器
    private static final String BASE_URL = "http://host.docker.internal:4200";

    @BeforeAll
    static void setupBrowser() {
        ensurePdfExists();
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true) // 🔥 Jenkins 运行必须为 true
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

        // 🔥 处理注册成功的 JavaScript Alert 弹窗
        page.onDialog(Dialog::accept);

        // 调试日志输出
        page.onConsoleMessage(msg -> System.out.println("浏览器日志 >> [" + msg.type() + "] " + msg.text()));
    }

    @AfterEach
    void tearDown(TestInfo info) {
        // 测试失败时截取全屏附件
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Allure.addAttachment("Final State - " + info.getDisplayName(), new ByteArrayInputStream(screenshot));
        context.close();
    }

    // ================== TEST 1: FULL SUBMISSION FLOW ==================
    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("User can register, login, and submit a paper")
    public void testFullPaperSubmissionFlow() {
        String ts = String.valueOf(System.currentTimeMillis());
        String email = "author" + ts + "@confease.com";

        step("1. Navigate and Switch to Register", () -> {
            page.navigate(BASE_URL);
            page.click("button:has-text('Get Started')");
            page.waitForURL("**/auth");
            Locator switchBtn = page.locator("button:has-text('Create an account')");
            if (switchBtn.isVisible()) switchBtn.click();
        });

        step("2. Fill Registration Form", () -> {
            page.fill("input[name='fName']", "Test");
            page.fill("input[name='lName']", "Author");
            page.fill("input[name='regEmail']", email);
            page.fill("input[name='regPass']", "password123");
            page.fill("input[name='aff']", "UTM Melaka");
            page.fill("input[name='cnt']", "Malaysia");
            page.click("button:has-text('Create Account')");
        });

        step("3. Login to System", () -> {
            // 等待注册成功后自动切回的登录表单
            Locator loginEmail = page.locator("input[name='email']").first();
            loginEmail.waitFor(new Locator.WaitForOptions().setTimeout(10000));
            loginEmail.fill(email);
            page.fill("input[name='password']", "password123");
            page.click("button:has-text('Sign In')");
        });

        step("4. Paper Submission Wizard", () -> {
            page.waitForURL("**/paper-submission", new Page.WaitForURLOptions().setTimeout(30000));
            page.click("button:has-text('Submit New Paper')");

            // 🔥 核心修复：解决下拉框超时问题
            Locator confSelect = page.locator("select[name='conference_id']");
            confSelect.waitFor();

            // 等待直到下拉框里除了“请选择”之外还有其他选项加载出来
            page.waitForCondition(() -> confSelect.locator("option").count() > 1);
            System.out.println("检测到可用会议数量: " + (confSelect.locator("option").count() - 1));

            page.selectOption("select[name='conference_id']", new SelectOption().setValue("1"));
            page.click("button[matsteppernext]");
        });

        step("5. Upload File and Final Submit", () -> {
            // 连点 Next 到 Step 4 (Upload)
            page.click("button[matsteppernext]"); // Step 2
            page.click("button[matsteppernext]"); // Step 3

            Path pdfPath = Paths.get("src/test/resources/sample.pdf");
            page.setInputFiles("input[type='file']", pdfPath);

            page.click("button[matsteppernext]"); // Step 5
            page.click("button.btn-primary:has-text('Submit Paper')");
            System.out.println("✅ E2E Paper Submission Completed!");
        });
    }

    // ================== TEST 2: INVALID LOGIN ==================
    @Test
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidLogin() {
        step("Attempt Login with Wrong Credentials", () -> {
            page.navigate(BASE_URL + "/auth");
            page.fill("input[name='email']", "nonexistent@test.com");
            page.fill("input[name='password']", "wrongpass");
            page.click("button:has-text('Sign In')");

            // 验证是否依然留在 Auth 页面，没有跳转
            Assertions.assertTrue(page.url().contains("/auth"), "Should stay on auth page");
        });
    }

    @Step("{0}")
    private void step(String name, Runnable action) {
        System.out.println("▶ " + name);
        action.run();
    }

    private static void ensurePdfExists() {
        try {
            Path dirPath = Paths.get("src/test/resources");
            if (!Files.exists(dirPath)) Files.createDirectories(dirPath);
            File pdfFile = new File("src/test/resources/sample.pdf");
            if (!pdfFile.exists()) Files.writeString(pdfFile.toPath(), "Dummy PDF content.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}