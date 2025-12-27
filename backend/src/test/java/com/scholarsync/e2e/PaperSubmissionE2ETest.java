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

    /* =======================
       Browser lifecycle
       ======================= */

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
        context = browser.newContext(
                new Browser.NewContextOptions()
                        .setViewportSize(1280, 1600)
        );
        page = context.newPage();

        page.onDialog(Dialog::accept);
        page.onConsoleMessage(msg ->
                System.out.println("BROWSER LOG: " + msg.text()));
    }

    @AfterEach
    void tearDown(TestInfo info) {
        byte[] screenshot = page.screenshot(
                new Page.ScreenshotOptions().setFullPage(true)
        );
        Allure.addAttachment(
                "Final State - " + info.getDisplayName(),
                new ByteArrayInputStream(screenshot)
        );
        context.close();
    }

    /* =======================
       Test case
       ======================= */

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Author submits a new paper successfully (Improved Stable Version)")
    void testSuccessfulPaperSubmission() {

        step("Login as Author", () -> {
            page.navigate(FRONTEND_URL);

            jsClick(page.locator("button:has-text('Get Started')"));

            page.locator("input[name='email']").fill("author@scholarsync.com");
            page.locator("input[name='password']").fill("author123");

            jsClick(page.getByRole(
                    AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName("Sign In")
            ));

            page.waitForSelector(
                    "text=+ Submit New Paper",
                    new Page.WaitForSelectorOptions().setTimeout(15000)
            );
        });

        step("Open paper submission form", () -> {
            jsClick(page.getByRole(
                    AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName("+ Submit New Paper")
            ));
        });

        step("Step 1 – Select conference", () -> {
            Locator conferenceSelect = page.locator("select[name='conference_id']");

            page.waitForCondition(
                    () -> conferenceSelect.locator("option").count() > 1,
                    new Page.WaitForConditionOptions().setTimeout(20000)
            );

            conferenceSelect.selectOption(new SelectOption().setIndex(1));
            clickNext();
        });

        step("Step 2 – Paper information", () -> {
            page.getByRole(
                    AriaRole.TEXTBOX,
                    new Page.GetByRoleOptions().setName("Enter paper title")
            ).fill("AI Research Paper (CI)");

            Locator trackSelect = page.locator("select")
                    .filter(new Locator.FilterOptions().setHasText("Select a track"));

            page.waitForCondition(
                    () -> trackSelect.locator("option").count() > 1,
                    new Page.WaitForConditionOptions().setTimeout(20000)
            );

            trackSelect.selectOption(new SelectOption().setIndex(1));

            page.getByRole(
                    AriaRole.TEXTBOX,
                    new Page.GetByRoleOptions().setName("Enter paper abstract")
            ).fill("Automated submission test.");

            clickNext();
        });

        step("Step 3 – Author information", () -> {
            page.getByRole(AriaRole.TEXTBOX).nth(0).fill("Test");
            page.getByRole(AriaRole.TEXTBOX).nth(1).fill("User");

            page.locator("input[type='email']").fill("testuser@example.com");

            page.getByRole(
                    AriaRole.TEXTBOX,
                    new Page.GetByRoleOptions().setName("University/Organization")
            ).fill("Test University");

            clickNext();
        });

        step("Step 4 – Optional step", this::clickNext);

        step("Step 5 – Keywords & Submit", () -> {

            // Wait for Step 5 to fully render
            page.waitForSelector(
                    "text=Keywords",
                    new Page.WaitForSelectorOptions().setTimeout(15000)
            );

            // Keywords – JS click (most stable)
            jsClick(page.getByText("Machine Learning"));
            jsClick(page.getByText("Deep Learning"));

            // Submit – scroll + JS click (fix viewport issue)
            Locator submitBtn = page.locator("button:has-text('Submit Paper')");
            submitBtn.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(15000));

            submitBtn.scrollIntoViewIfNeeded();
            page.waitForTimeout(500);
            submitBtn.evaluate("el => el.click()");
        });
    }

    /* =======================
       Helpers
       ======================= */

    private void clickNext() {
        Locator nextBtn = page.locator("button:has-text('Next'):visible").first();
        nextBtn.waitFor(new Locator.WaitForOptions().setTimeout(15000));
        nextBtn.scrollIntoViewIfNeeded();
        page.waitForTimeout(300);
        nextBtn.evaluate("el => el.click()");
    }

    private void jsClick(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(15000));
        locator.scrollIntoViewIfNeeded();
        page.waitForTimeout(300);
        locator.evaluate("el => el.click()");
    }

    @Step("{0}")
    private void step(String name, Runnable action) {
        System.out.println("▶ " + name);
        action.run();
    }
}
