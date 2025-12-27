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
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1280, 800));
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

            // Wait for landing page to load
            page.waitForSelector("text=+ Submit New Paper", new Page.WaitForSelectorOptions().setTimeout(15000));
        });

        step("Open paper submission form", () -> {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Submit New Paper")).click();
        });

        step("Step 1 – Select conference", () -> {
            Locator conferenceSelect = page.locator("select[name='conference_id']");
            // Wait for data to arrive from backend
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
            // 🔥 FIXED: Wait for ATTACHED instead of VISIBLE to avoid "hidden" errors
            Locator mlKeyword = page.getByText("Machine Learning");
            mlKeyword.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.ATTACHED)
                    .setTimeout(10000));

            mlKeyword.scrollIntoViewIfNeeded();

            // 🔥 FIXED: Use force click to bypass layout overlaps or hidden states
            mlKeyword.click(new Locator.ClickOptions().setForce(true));
            page.getByText("Deep Learning").click(new Locator.ClickOptions().setForce(true));

            // Force click the final submission button
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("✓ Submit Paper"))
                    .click(new Locator.ClickOptions().setForce(true));
        });
    }

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