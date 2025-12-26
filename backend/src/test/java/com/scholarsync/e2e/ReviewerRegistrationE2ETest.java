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

    private static final String BASE_URL = "http://host.docker.internal:4200";

    private String dynamicFirstName;
    private String dynamicLastName;
    private String dynamicEmail;

    @BeforeAll
    static void setupBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
                        .setSlowMo(150)
        );
    }

    @AfterAll
    static void closeBrowser() {
        if (playwright != null) playwright.close();
    }

    @BeforeEach
    void createContext() {
        String uniqueID = UUID.randomUUID().toString().substring(0, 8);
        dynamicFirstName = "Reviewer" + uniqueID;
        dynamicLastName = "Test" + uniqueID;
        dynamicEmail = "rev_" + uniqueID + "@scholarsync.com";

        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1280, 800));
        page = context.newPage();
        page.onDialog(Dialog::accept);
        // 打印日志方便在 Jenkins Console 查看动态生成的 Email
        page.onConsoleMessage(msg -> System.out.println("浏览器日志 >> " + msg.text()));
    }

    @AfterEach
    void tearDown(TestInfo info) {
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Allure.addAttachment("Final State - " + info.getDisplayName(), new ByteArrayInputStream(screenshot));
        context.close();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Full flow: Reviewer registration followed by Admin approval")
    public void testReviewerRegistrationAndApproval() {

        step("Navigate and Submit Reviewer Application", () -> {
            page.navigate(BASE_URL);
            // 确保页面加载完成
            page.waitForLoadState(LoadState.NETWORKIDLE);
            page.click("button:has-text('Become a Reviewer')");

            page.fill("input[placeholder*='First Name']", dynamicFirstName);
            page.fill("input[placeholder*='Last Name']", dynamicLastName);
            page.fill("input[type='email']", dynamicEmail);
            page.fill("input[placeholder*='Affiliation']", "UTM Melaka");
            page.fill("input[placeholder*='Country']", "Malaysia");

            // 选填项与必填项
            page.locator("input[type='password']").first().fill("123456");
            page.locator("input[type='password']").last().fill("123456");
            page.fill("input[placeholder*='Expertise']", "AI & Security");
            page.fill("textarea", "This is an automated reviewer application.");

            page.click("button:has-text('Submit Application')");
            // 等待提交请求完成
            page.waitForLoadState(LoadState.NETWORKIDLE);
        });

        step("Login as Admin & Navigate to Panel", () -> {
            page.navigate(BASE_URL + "/auth");
            page.fill("input[name='email']", "admin@scholarsync.com");
            page.fill("input[name='password']", "admin123");
            page.click("button:has-text('Sign In')");

            // 等待 Admin 链接出现并进入后台
            Locator adminLink = page.locator("a:has-text('Admin Panel'), button:has-text('Admin Panel')");
            adminLink.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
            adminLink.click();

            page.waitForURL("**/admin/**");
        });

        step("Approve Specific Reviewer Application", () -> {
            // 🔥 关键点：如果你的后台有多个 Tab（如 Users, Reviews），需要先点击 'Approvals' 菜单
            Locator approvalTab = page.locator("a:has-text('Approvals'), button:has-text('Approvals'), .nav-link:has-text('Approvals')");
            if (approvalTab.isVisible()) {
                approvalTab.click();
            }

            // 🔥 核心修复：寻找包含特定 Email 的表格行
            // 使用更通用的选择器，防止组件库（如 Angular Material）生成的复杂结构
            Locator reviewerRow = page.locator("tr").filter(new Locator.FilterOptions().setHasText(dynamicEmail));

            // 如果第一时间没找到，尝试刷新页面（防止后端数据写入延迟）
            if (!reviewerRow.isVisible()) {
                System.out.println("⚠️ 未发现记录，尝试刷新页面...");
                page.reload();
                page.waitForLoadState(LoadState.NETWORKIDLE);
            }

            // 等待行可见
            reviewerRow.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(20000));

            // 在该行内点击审批按钮
            Locator approveBtn = reviewerRow.locator("button:has-text('Approve')");
            approveBtn.click();

            System.out.println("✅ 成功审批 Reviewer: " + dynamicEmail);

            // 验证该行是否消失或状态变为已审批
            page.waitForTimeout(2000);
        });
    }

    @Step("{0}")
    private void step(String name, Runnable action) {
        System.out.println("▶ Section: " + name);
        action.run();
    }
}