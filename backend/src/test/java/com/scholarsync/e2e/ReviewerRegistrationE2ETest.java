package com.webcrafters.confease_backend.e2e;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import java.io.ByteArrayInputStream;
import java.util.UUID;

@Epic("Conference Management System")
@Feature("Reviewer Management")
public class ReviewerRegistrationE2ETest {

    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    // 🔥 适配 Jenkins：必须使用 host.docker.internal 访问前端容器
    private static final String BASE_URL = "http://host.docker.internal:4200";

    private String dynamicFirstName;
    private String dynamicLastName;
    private String dynamicEmail;

    @BeforeAll
    static void setupBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true) // 🔥 Jenkins 环境必须为 true
                        .setSlowMo(100)
        );
    }

    @AfterAll
    static void closeBrowser() {
        if (playwright != null) playwright.close();
    }

    @BeforeEach
    void createContext() {
        // 生成唯一数据，防止数据库冲突
        String uniqueID = UUID.randomUUID().toString().substring(0, 8);
        dynamicFirstName = "Reviewer" + uniqueID;
        dynamicLastName = "Test" + uniqueID;
        dynamicEmail = "rev_" + uniqueID + "@scholarsync.com";

        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1280, 800));
        page = context.newPage();
        page.onDialog(Dialog::accept); // 自动处理弹窗
    }

    @AfterEach
    void tearDown(TestInfo info) {
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Allure.addAttachment("Final State - " + info.getDisplayName(), new ByteArrayInputStream(screenshot));
        context.close();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Reviewer 注册申请并由 Admin 审批通过的完整流程")
    public void testReviewerRegistrationAndApproval() {

        step("Navigate to Reviewer Application", () -> {
            page.navigate(BASE_URL);
            // 点击首页的 'Become a Reviewer' 按钮
            page.click("button:has-text('Become a Reviewer')");

            // 🔥 核心修复：确保进入了表单页面再开始填写
            page.waitForSelector("text=Become a Reviewer", new Page.WaitForSelectorOptions().setTimeout(10000));        });

        step("Fill Reviewer Application Form", () -> {
            System.out.println("正在注册新 Reviewer: " + dynamicEmail);

            // 🔥 核心修复：根据图片改用 getByLabel 定位
            page.getByLabel("First Name *").fill(dynamicFirstName);
            page.getByLabel("Last Name *").fill(dynamicLastName);
            page.getByLabel("Email *").fill(dynamicEmail);

            // 根据图片，这些字段有占位符
            page.getByPlaceholder("University/Institution").fill("UTM Melaka");
            page.getByLabel("Country").fill("Malaysia");

            // 密码字段处理
            page.getByLabel("Password *", new Page.GetByLabelOptions().setExact(true)).fill("123456");
            page.getByLabel("Confirm Password *").fill("123456");

            // 专业领域和简介
            page.getByPlaceholder("e.g., Machine Learning, Computer Vision, NLP").fill("AI Research");
            page.getByLabel("Brief Bio").fill("I am an expert in AI research with 5 years experience.");

            page.click("button:has-text('Submit Application')");
            // 等待提交接口完成
            page.waitForLoadState(LoadState.NETWORKIDLE);
        });

        step("Admin Login & Navigate to Approvals", () -> {
            page.navigate(BASE_URL + "/auth");
            page.fill("input[name='email']", "admin@scholarsync.com");
            page.fill("input[name='password']", "admin123");
            page.click("button:has-text('Sign In')");

            // 等待进入 Admin 面板
            Locator adminLink = page.locator("a:has-text('Admin Panel'), button:has-text('Admin Panel')");
            adminLink.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
            adminLink.click();

            page.waitForURL("**/admin/**");
        });

        step("Find and Approve Specific Reviewer", () -> {
            // 在 Admin 表格中寻找包含刚才注册 Email 的行
            Locator reviewerRow = page.locator("tr").filter(new Locator.FilterOptions().setHasText(dynamicEmail));

            // 🔥 核心修复：如果数据没刷出来，尝试刷新页面
            if (!reviewerRow.isVisible()) {
                page.reload();
                page.waitForLoadState(LoadState.NETWORKIDLE);
            }

            // 等待表格行可见
            reviewerRow.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15000));

            // 点击该行内的审批按钮
            reviewerRow.locator("button:has-text('Approve')").click();

            System.out.println("✅ 成功审批通过: " + dynamicEmail);
            page.waitForTimeout(2000);
        });
    }

    @Step("{0}")
    private void step(String name, Runnable action) {
        System.out.println("▶ Section: " + name);
        action.run();
    }
}