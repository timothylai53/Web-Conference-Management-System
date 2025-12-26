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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ScholarSyncE2ETest {

    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    // 定义测试资源路径
    private static final String PDF_PATH = "src/test/resources/sample.pdf";

    @BeforeAll
    static void launchBrowser() {
        // 确保 PDF 文件存在
        ensurePdfExists();

        playwright = Playwright.create();
        // 设置 headless=false 可以让你看到浏览器运行过程，调试时很有用
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(true) // 如果需要看界面，改为 false
                .setSlowMo(100));
    }

    @AfterAll
    static void closeBrowser() {
        if (playwright != null) playwright.close();
    }

    @BeforeEach
    void createContext() {
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1280, 720));
        page = context.newPage();

        // 自动处理所有 Alert 弹窗
        page.onDialog(dialog -> {
            System.out.println("⚠️ 自动处理弹窗: " + dialog.message());
            dialog.accept();
        });

        // 打印控制台日志，方便调试 Angular 错误
        page.onConsoleMessage(msg -> {
            if ("error".equals(msg.type())) {
                System.out.println("❌ 浏览器控制台错误: " + msg.text());
            }
        });
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        // 失败或结束时截图
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Allure.addAttachment("Final State - " + testInfo.getDisplayName(), new ByteArrayInputStream(screenshot));

        if (context != null) context.close();
    }

    @Test
    @Story("User can register, login and submit a paper")
    @Severity(SeverityLevel.CRITICAL)
    public void testFullPaperSubmissionFlow() {

        // --- 1. 准备动态数据 (防止账号冲突) ---
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String dynamicEmail = "author" + timeStamp + "@scholarsync.com";
        String fixedPassword = "author123";

        System.out.println("🚀 开始测试，使用测试账号: " + dynamicEmail);

        step("Navigate to Homepage", () -> {
            page.navigate("http://host.docker.internal:4200/");
            page.waitForLoadState(); // 等待页面加载完成

            Locator startBtn = page.locator("button.btn-primary:has-text('Get Started')");
            startBtn.waitFor();
            startBtn.click();
        });

        step("Register Test User (New Account)", () -> {
            page.waitForURL("**/auth");

            // 检查是否需要切换到注册页
            Locator switchToRegister = page.locator(".auth-switch button:has-text('Create an account')");
            if (switchToRegister.isVisible()) {
                switchToRegister.click();
            }

            // 填写注册信息
            page.locator("input[name='fName']").fill("Timothy");
            page.locator("input[name='lName']").fill("Test");
            page.locator("input[name='regEmail']").fill(dynamicEmail); // 使用动态邮箱
            page.locator("input[name='regPass']").fill(fixedPassword);
            page.locator("input[name='aff']").fill("UUM");
            page.locator("input[name='cnt']").fill("Malaysia");

            // 提交注册
            page.locator("button.btn-primary-lg:has-text('Create Account')").click();

            // 🔥 [关键修复]：等待注册输入框消失，这代表前端已经切换视图了
            // 如果这里超时，说明注册失败（后端报错）
            page.locator("input[name='regEmail']").waitFor(
                    new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(15000)
            );
            System.out.println("✅ 注册提交成功，视图已切换");
        });

        step("Login to System", () -> {
            // 定位登录框
            Locator loginEmailInput = page.locator("input[name='email']").first();

            // 显式等待登录框出现
            loginEmailInput.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));

            // 输入刚才注册的账号
            loginEmailInput.fill(dynamicEmail);
            page.locator("input[name='password']").first().fill(fixedPassword);

            page.locator("button.btn-primary-lg:has-text('Sign In')").click();
            System.out.println("✅ 登录请求已发送");
        });

        step("Start Paper Submission Wizard", () -> {
            // 等待路由跳转到 submission 页面
            page.waitForURL("**/paper-submission", new Page.WaitForURLOptions().setTimeout(30000));

            Locator newPaperBtn = page.locator("button.btn-primary:has-text('Submit New Paper')");
            newPaperBtn.waitFor();
            newPaperBtn.click();
        });

        step("Wizard Step 1: Select Conference", () -> {
            // 等待下拉框加载
            Locator conferenceSelect = page.locator("select[name='conference_id']");
            conferenceSelect.waitFor();
            // 选择第一个会议 (假设 value='1' 存在，如果不存在改为 index)
            conferenceSelect.selectOption(new SelectOption().setIndex(1));

            page.locator("button[matsteppernext]").first().click();
        });

        // 假设中间有文件上传步骤 (通常是 paper submission 的核心)
        // 如果你的步骤不同，可以注释掉这一段
        /* step("Wizard Step: Upload File", () -> {
            // 寻找文件上传输入框 (input type='file')
            // setInputFiles 会自动处理文件选择对话框
            page.setInputFiles("input[type='file']", Paths.get(PDF_PATH));
            page.locator("button[matsteppernext]").click();
        });
        */

        step("Wizard Step: Final Submission", () -> {
            // 寻找最后的提交按钮
            Locator submitBtn = page.locator("button.btn-primary:has-text('Submit Paper')");

            // 如果前面步骤没走完，这个按钮可能不可点击，这里做一个容错等待
            submitBtn.waitFor(new Locator.WaitForOptions().setTimeout(10000));
            submitBtn.click();

            System.out.println("🎉 E2E 测试流程全部完成！");
        });
    }

    // --- 辅助方法 ---

    @Step("{0}")
    private void step(String stepName, Runnable runnable) {
        System.out.println("🔹 Step: " + stepName);
        runnable.run();
    }

    private static void ensurePdfExists() {
        try {
            Path dirPath = Paths.get("src/test/resources");
            if (!Files.exists(dirPath)) Files.createDirectories(dirPath);
            File pdfFile = new File(PDF_PATH);
            if (!pdfFile.exists()) {
                Files.writeString(pdfFile.toPath(), "This is a dummy PDF for automated testing.");
                System.out.println("📄 已生成测试用 PDF 文件");
            }
        } catch (IOException e) {
            System.err.println("创建测试文件失败: " + e.getMessage());
        }
    }
}