package com.webcrafters.confease_backend.e2e;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Epic("Conference Management System")
@Feature("Paper Submission")
public class PaperSubmissionE2ETest {

    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    // Jenkins / Docker frontend URL
    private static final String BASE_URL = "http://host.docker.internal:4200";

    // =========================
    // Setup / Teardown
    // =========================

    @BeforeAll
    static void setupBrowser() {
        ensurePdfExists();

        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
                        .setSlowMo(100)
                        .setArgs(List.of(
                                "--no-sandbox",
                                "--disable-dev-shm-usage",
                                "--disable-gpu"
                        ))
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

        // Auto accept alerts
        page.onDialog(Dialog::accept);

        // Print browser console logs to Jenkins
        page.onConsoleMessage(msg ->
                System.out.println("BROWSER LOG: " + msg.text())
        );
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

    // =========================
    // TEST
    // =========================

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Author submits a paper successfully (Jenkins-safe)")
    void testSuccessfulPaperSubmission() {

        step("Login as Author", () -> {
            page.navigate(BASE_URL);

            page.getByRole(AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName("Get Started"))
                    .click();

            page.locator("input[name='email']")
                    .fill("author@scholarsync.com");

            page.locator("input[name='password']")
                    .fill("author123");

            page.getByRole(AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName("Sign In"))
                    .click();

            page.waitForSelector(
                    "text=+ Submit New Paper",
                    new Page.WaitForSelectorOptions().setTimeout(20000)
            );
        });

        step("Conference Selection", () -> {
            page.getByRole(AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName("+ Submit New Paper"))
                    .click();

            Locator conferenceSelect =
                    page.locator("select[name='conference_id']");

            conferenceSelect.waitFor();

            // Wait until API data is loaded
            page.waitForCondition(
                    () -> conferenceSelect.innerText().trim().length() > 0,
                    new Page.WaitForConditionOptions().setTimeout(20000)
            );

            conferenceSelect.selectOption(new SelectOption().setIndex(1));

            waitForAngular();
            clickEnabledNext();
        });

        step("Paper Details & Track", () -> {
            page.getByRole(AriaRole.TEXTBOX,
                            new Page.GetByRoleOptions().setName("Enter paper title"))
                    .fill("AI Research Paper v1");

            Locator trackSelect = page.locator("select")
                    .filter(new Locator.FilterOptions()
                            .setHasText("Select a track"));

            page.waitForCondition(
                    () -> trackSelect.innerText().contains("Artificial Intelligence"),
                    new Page.WaitForConditionOptions().setTimeout(20000)
            );

            trackSelect.selectOption(
                    new SelectOption().setLabel("Artificial Intelligence")
            );

            page.getByRole(AriaRole.TEXTBOX,
                            new Page.GetByRoleOptions().setName("Enter paper abstract"))
                    .fill("This abstract discusses AI advancements.");

            waitForAngular();
            clickEnabledNext();
        });

        step("Author Information", () -> {
            page.getByRole(AriaRole.TEXTBOX).first().fill("Test");
            page.getByRole(AriaRole.TEXTBOX).nth(1).fill("User");
            page.locator("input[type='email']").fill("testuser@example.com");

            page.getByRole(AriaRole.TEXTBOX,
                            new Page.GetByRoleOptions().setName("University/Organization"))
                    .fill("Test University");

            waitForAngular();
            clickEnabledNext();
        });

        step("Keywords Selection", () -> {
            waitForAngular();
            clickEnabledNext();

            page.getByText("Machine Learning").click();
            page.getByText("Deep Learning").click();
        });

        step("Submit Paper", () -> {
            page.getByRole(AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName("✓ Submit Paper"))
                    .waitFor();

            page.getByRole(AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName("✓ Submit Paper"))
                    .click();

            System.out.println("✅ Paper Submission completed successfully");
        });
    }

    // =========================
    // HELPERS
    // =========================

    private void clickEnabledNext() {
        Locator nextButton = page.locator("button")
                .filter(new Locator.FilterOptions()
                        .setHasText("Next"))
                .filter(new Locator.FilterOptions()
                        .setHasNot(page.locator("[disabled]")))
                .filter(new Locator.FilterOptions()
                        .setVisible(true))
                .first();

        nextButton.waitFor(
                new Locator.WaitForOptions().setTimeout(20000)
        );
        nextButton.click();
    }

    private void waitForAngular() {
        // Allow Angular Material validation & change detection
        page.waitForTimeout(400);
    }

    @Step("{0}")
    private void step(String name, Runnable action) {
        System.out.println("▶ STEP: " + name);
        try {
            action.run();
        } catch (Exception e) {
            Allure.addAttachment(
                    "Failure - " + name,
                    new ByteArrayInputStream(page.screenshot())
            );
            throw e;
        }
    }

    private static void ensurePdfExists() {
        try {
            Path dir = Paths.get("src/test/resources");
            if (!Files.exists(dir)) Files.createDirectories(dir);

            Path pdf = dir.resolve("sample.pdf");
            if (!Files.exists(pdf)) {
                Files.writeString(pdf, "Dummy PDF for Jenkins");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
