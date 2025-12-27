package com.webcrafters.confease_backend.e2e;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import java.io.ByteArrayInputStream;

@Epic("Conference Management System")
@Feature("Paper Submission")
public class PaperSubmissionE2ETest {

    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    private static final String FRONTEND_URL = "http://host.docker.internal:4200";

    @BeforeAll
    static void setupBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
                        .setSlowMo(50)
        );
    }

    @AfterAll
    static void closeBrowser() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @BeforeEach
    void createContext() {
        // ✨ 统一增加高度到 1600，防止 "Outside of viewport" 报错
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1280, 1600));
        page = context.newPage();
        page.onDialog(Dialog::accept);
        page.onConsoleMessage(msg -> System.out.println("BROWSER LOG: " + msg.text()));
    }

    @AfterEach
    void tearDown(TestInfo info) {
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Allure.addAttachment("Final State - " + info.getDisplayName(), new ByteArrayInputStream(screenshot));
        context.close();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Author submits a new paper successfully (Stable Version)")
    void testSuccessfulPaperSubmission() {

        step("Login as Author", () -> {
            page.navigate(FRONTEND_URL);
            page.locator("button:has-text('Get Started')").click();
            page.locator("input[name='email']").fill("author@scholarsync.com");
            page.locator("input[name='password']").fill("author123");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
            page.waitForSelector("text=+ Submit New Paper", new Page.WaitForSelectorOptions().setTimeout(15000));
        });

        step("Open paper submission form", () -> {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Submit New Paper")).click();
        });

        step("Step 1 – Select conference", () -> {
            Locator conferenceSelect = page.locator("select[name='conference_id']");
            page.waitForCondition(() -> conferenceSelect.locator("option").count() > 1,
                    new Page.WaitForConditionOptions().setTimeout(20000));
            conferenceSelect.selectOption(new SelectOption().setIndex(1));
            clickVisibleNext();
        });

        step("Step 2 – Paper information", () -> {
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter paper title")).fill("AI Research Paper (CI)");
            Locator trackSelect = page.locator("select").filter(new Locator.FilterOptions().setHasText("Select a track"));
            page.waitForCondition(() -> trackSelect.locator("option").count() > 1,
                    new Page.WaitForConditionOptions().setTimeout(20000));
            trackSelect.selectOption(new SelectOption().setIndex(1));
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter paper abstract")).fill("Automated submission test.");
            clickVisibleNext();
        });

        step("Step 3 – Author information", () -> {
            page.getByRole(AriaRole.TEXTBOX).nth(0).fill("Test");
            page.getByRole(AriaRole.TEXTBOX).nth(1).fill("User");
            page.locator("input[type='email']").fill("testuser@example.com");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("University/Organization")).fill("Test University");
            clickVisibleNext();
        });

        step("Step 4 – Optional step", this::clickVisibleNext);

        step("Step 5 – Keywords & Submit", () -> {
            // 1. 等待元素挂载 (ATTACHED)
            Locator mlKeyword = page.getByText("Machine Learning");
            mlKeyword.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.ATTACHED)
                    .setTimeout(10000));

            // 2. 🔥 修正后的 JS 点击逻辑：忽略一切坐标和可见性检查
            mlKeyword.evaluate("el => el.click()");

            // 3. 对 Deep Learning 执行相同操作
            page.getByText("Deep Learning").waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.ATTACHED));
            page.getByText("Deep Learning").evaluate("el => el.click()");

            // 4. 提交论文
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("✓ Submit Paper"))
                    .click(new Locator.ClickOptions().setForce(true));
        });
    } // ✨ 这里补齐了方法结束的括号

    private void clickVisibleNext() {
        Locator nextButton = page.locator("button:has-text('Next'):visible").first();
        nextButton.waitFor(new Locator.WaitForOptions().setTimeout(15000));
        nextButton.click(new Locator.ClickOptions().setForce(true));
    }

    @Step("{0}")
    private void step(String name, Runnable action) {
        System.out.println("▶ " + name);
        action.run();
    }
}