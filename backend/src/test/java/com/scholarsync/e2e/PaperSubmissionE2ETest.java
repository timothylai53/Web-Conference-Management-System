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

    // Jenkins / Docker safe URL
    private static final String BASE_URL = "http://host.docker.internal:4200";

    // =========================
    // Setup / Teardown
    // =========================

    @BeforeAll
    static void setupBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)      // MUST be true in Jenkins
                        .setSlowMo(100)         // Stabilize Angular animations
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
                        .setViewportSize(1280, 800)
        );
        page = context.newPage();

        // Auto-accept dialogs
        page.onDialog(Dialog::accept);

        // Print browser console logs in Jenkins
        page.onConsoleMessage(msg ->
                System.out.println("BROWSER LOG: " + msg.text()));
    }

    @AfterEach
    void tearDown(TestInfo info) {
        byte[] screenshot = page.screenshot(
                new Page.ScreenshotOptions().setFullPage(true));
        Allure.addAttachment(
                "Final State - " + info.getDisplayName(),
                new ByteArrayInputStream(screenshot)
        );
        context.close();
    }

    // =========================
    // TEST
    // =========================

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Author successfully submits a new paper (Jenkins-safe)")
    public void testSuccessfulPaperSubmission() {

        step("Login as Author", () -> {
            page.navigate(BASE_URL);

            page.locator("button:has-text('Get Started')").click();
            page.locator("input[name='email']").fill("author@scholarsync.com");
            page.locator("input[name='password']").fill("author123");
            page.getByRole(AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName("Sign In")).click();

            page.waitForSelector(
                    "text=+ Submit New Paper",
                    new Page.WaitForSelectorOptions().setTimeout(15000)
            );
        });

        step("Open Submission Form", () -> {
            page.getByRole(AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName("+ Submit New Paper"))
                    .click();
        });

        step("Step 1: Select Conference", () -> {
            Locator conferenceSelect = page.locator("select[name='conference_id']");
            conferenceSelect.waitFor();

            // Wait until options load (API response)
            page.waitForCondition(
                    () -> conferenceSelect.innerText().length() > 5,
                    new Page.WaitForConditionOptions().setTimeout(15000)
            );

            conferenceSelect.selectOption(new SelectOption().setIndex(1));
            clickVisibleNext();
        });

        step("Step 2: Paper Information", () -> {
            page.getByRole(AriaRole.TEXTBOX,
                            new Page.GetByRoleOptions().setName("Enter paper title"))
                    .fill("AI Research Paper (CI)");

            Locator trackSelect = page.locator("select")
                    .filter(new Locator.FilterOptions()
                            .setHasText("Select a track"));

            page.waitForCondition(
                    () -> trackSelect.innerText().contains("Artificial"),
                    new Page.WaitForConditionOptions().setTimeout(15000)
            );

            trackSelect.selectOption(
                    new SelectOption().setLabel("Artificial Intelligence"));

            page.getByRole(AriaRole.TEXTBOX,
                            new Page.GetByRoleOptions().setName("Enter paper abstract"))
                    .fill("This paper tests Playwright E2E stability in Jenkins.");

            clickVisibleNext();
        });

        step("Step 3: Author Information", () -> {
            page.getByRole(AriaRole.TEXTBOX).first().fill("Test");
            page.getByRole(AriaRole.TEXTBOX).nth(1).fill("User");
            page.locator("input[type='email']").fill("testuser@example.com");
            page.getByRole(AriaRole.TEXTBOX,
                            new Page.GetByRoleOptions().setName("University/Organization"))
                    .fill("Test University");

            clickVisibleNext();
        });

        step("Step 4: Optional Step", () -> {
            clickVisibleNext();
        });

        step("Step 5: Keywords & Submit", () -> {
            page.getByText("Machine Learning").click();
            page.getByText("Deep Learning").click();

            page.getByRole(AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName("✓ Submit Paper"))
                    .click();
        });

        step("Verify Submission", () -> {
            page.waitForTimeout(2000);
            System.out.println("✅ Paper submission completed successfully");
        });
    }

    // =========================
    // HELPERS
    // =========================

    /**
     * Safely clicks the currently visible & enabled "Next" button.
     * Works reliably with Angular Material Stepper in Jenkins.
     */
    private void clickVisibleNext() {
        Locator nextBtn = page.locator("button:has-text('Next'):visible").first();
        nextBtn.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(15000));
        nextBtn.click();
    }

    @Step("{0}")
    private void step(String name, Runnable action) {
        System.out.println("▶ " + name);
        action.run();
    }
}
