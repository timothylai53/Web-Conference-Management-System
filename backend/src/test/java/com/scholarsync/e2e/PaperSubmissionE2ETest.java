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
@Feature("Paper Submission Flow")
public class PaperSubmissionE2ETest {

    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    // 🔥 适配 Jenkins：必须通过 host.docker.internal 访问宿主机端口
    private static final String BASE_URL = "http://host.docker.internal:4200";

    @BeforeAll
    static void setupBrowser() {
        ensurePdfExists();
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true) // 🔥 适配 Jenkins 容器环境
                        .setSlowMo(100)
        );
    }

    @AfterAll
    static void closeBrowser() {
        if (playwright != null) playwright.close();
    }

    @BeforeEach
    void createContext() {
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1280, 800));
        page = context.newPage();
        // 自动处理所有对话框/弹窗
        page.onDialog(Dialog::accept);
        // 打印控制台日志以便调试
        page.onConsoleMessage(msg -> System.out.println("浏览器日志 >> [" + msg.type() + "] " + msg.text()));
    }

    @AfterEach
    void tearDown(TestInfo info) {
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Allure.addAttachment("Final State - " + info.getDisplayName(), new ByteArrayInputStream(screenshot));
        context.close();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Author Submission Journey")
    @Description("验证已注册作者能否成功登录并完成论文提交向导")
    public void testSuccessfulPaperSubmission() {

        step("Login as Author", () -> {
            page.navigate(BASE_URL);
            page.click("button:has-text('Get Started')");

            page.locator("input[name='email']").fill("author@scholarsync.com");
            page.locator("input[name='password']").fill("author123");

            // 点击登录并显式等待 URL 跳转
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

            // 🔥 核心修复：等待 Angular 路由完成跳转
            page.waitForURL("**/paper-submission", new Page.WaitForURLOptions().setTimeout(15000));

            // 等待主页面元素可见
            Locator dashboardBtn = page.locator("button:has-text('Submit New Paper')");
            dashboardBtn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

            Assertions.assertTrue(dashboardBtn.isVisible(), "登录失败：未能进入论文提交页面");
        });

        step("Navigate to Submission Form & Select Conference", () -> {
            page.click("button:has-text('Submit New Paper')");

            // 🔥 核心修复：等待下拉框 API 数据加载完成
            Locator confSelect = page.locator("select[name='conference_id']");
            page.waitForCondition(() -> confSelect.locator("option").count() > 1);

            // 确保 data.sql 中 ID=1 的会议存在
            confSelect.selectOption("1");
            page.click("button:has-text('Next')");
        });

        step("Fill Paper Details & Select Track", () -> {
            page.waitForSelector("input[placeholder*='title']");
            page.fill("input[placeholder*='title']", "AI Research Paper v1");

            // 等待 Track 下拉框加载并选择
            Locator trackSelect = page.locator("select").filter(new Locator.FilterOptions().setHasText("Select a track"));
            trackSelect.waitFor();
            trackSelect.selectOption(new SelectOption().setLabel("Artificial Intelligence"));

            page.fill("textarea[placeholder*='abstract']", "This abstract discusses AI advancements in conference management.");
            page.click("button:has-text('Next')");
        });

        step("Enter Author Information", () -> {
            page.waitForSelector("input[name='author_fname']"); // 假设 name 属性
            page.fill("input[name='author_fname']", "Test");
            page.fill("input[name='author_lname']", "User");
            page.fill("input[type='email']", "testauthor@example.com");
            page.click("button:has-text('Next')");
        });

        step("Upload File and Submit", () -> {
            // 跳过关键词或进入文件上传步骤
            page.click("button:has-text('Next')");

            // 自动生成并上传 PDF
            Path pdfPath = Paths.get("src/test/resources/sample.pdf");
            page.setInputFiles("input[type='file']", pdfPath);

            page.click("button:has-text('Next')");
            page.click("button:has-text('Submit Paper')");

            System.out.println("✅ 论文提交流程端到端测试成功！");
        });
    }

    @Step("{0}")
    private void step(String name, Runnable action) {
        System.out.println("▶ Section: " + name);
        action.run();
    }

    private static void ensurePdfExists() {
        try {
            Path dirPath = Paths.get("src/test/resources");
            if (!Files.exists(dirPath)) Files.createDirectories(dirPath);
            File pdfFile = new File("src/test/resources/sample.pdf");
            if (!pdfFile.exists()) {
                Files.writeString(pdfFile.toPath(), "Test PDF content for Playwright.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}