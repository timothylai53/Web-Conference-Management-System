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

    // Jenkins-safe frontend URL
    private static final String FRONTEND_URL = "http://host.docker.internal:4200";

    /* =========================
       Browser lifecycle
       ========================= */

    @BeforeAll
    static void setupBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)   // REQUIRED in Jenkins
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
                        .setViewportSize(1280, 800)
        );
        page = context.newPage();

        // Auto-accept dialogs
        page.onDialog(Dialog::accept);

        // Print browser logs in Jenkins console
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

    /* =========================
       TEST
       ========================= */

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Author submits a new paper successfully (Jenkins-safe)")
    void testSuccessfulPaperSubmission() {

        step("Login as Author", () -> {
            page.navigate(FRONTEND_URL);

            page.locator("button:has-text('Get Started')").click();
            page.locator("input[name='email']").fill("author@scholarsync.com");
            page.locator("input[name='password']").fill("author123");
            page.getByRole(AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName("Sign In")).click();

            page.waitForSelector("text=+ Submit New Paper",
                    new Page.WaitForSelectorOptions().setTimeout(15000));
        });

        step("Open paper submission form", () -> {
            page.getByRole(AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName("+ Submit New Paper")).click();
        });

        step("Step 1 – Select conference", () -> {
            Locator conferenceSelect = page.locator("select[name='conference_id']");

            // 🔥 CRITICAL FIX: wait until backend data arrives
            page.waitForCondition(
                    () -> conferenceSelect.locator("option").count() > 1,
                    new Page.WaitForConditionOptions().setTimeout(20000)
            );

            conferenceSelect.selectOption(new SelectOption().setIndex(1));
            clickVisibleNext();
        });

        step("Step 2 – Paper information", () -> {
            page.getByRole(AriaRole.TEXTBOX,
                            new Page.GetByRoleOptions().setName("Enter paper title"))
                    .fill("AI Research Paper (CI)");

            Locator trackSelect = page.locator("select")
                    .filter(new Locator.FilterOptions().setHasText("Select a track"));

            page.waitForCondition(
                    () -> trackSelect.locator("option").count() > 1,
                    new Page.WaitForConditionOptions().setTimeout(20000)
            );

            trackSelect.selectOption(new SelectOption().setIndex(1));

            page.getByRole(AriaRole.TEXTBOX,
                            new Page.GetByRoleOptions().setName("Enter paper abstract"))
                    .fill("Automated submission from Jenkins Playwright test.");

            clickVisibleNext();
        });

        step("Step 3 – Author information", () -> {
            page.getByRole(AriaRole.TEXTBOX).nth(0).fill("Test");
            page.getByRole(AriaRole.TEXTBOX).nth(1).fill("User");
            page.locator("input[type='email']").fill("testuser@example.com");
            page.getByRole(AriaRole.TEXTBOX,
                            new Page.GetByRoleOptions().setName("University/Organization"))
                    .fill("Test University");

            clickVisibleNext();
        });

        step("Step 4 – Optional step", this::clickVisibleNext);

        step("Step 5 – Keywords & Submit", () -> {
            page.getByText("Machine Learning").click();
            page.getByText("Deep Learning").click();

            page.getByRole(AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName("✓ Submit Paper")).click();
        });
    }

    /* =========================
       Helpers
       ========================= */

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
