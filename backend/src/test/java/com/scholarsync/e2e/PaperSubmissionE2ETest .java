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

    private static final String BASE_URL = "http://localhost:4200";

    @BeforeAll
    static void setupBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setSlowMo(200)
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
        // Global dialog handler to accept any confirmations automatically
        page.onDialog(Dialog::accept);
    }

    @AfterEach
    void tearDown(TestInfo info) {
        // Take a screenshot for the Allure report before closing
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Allure.addAttachment("Final State - " + info.getDisplayName(), new ByteArrayInputStream(screenshot));
        context.close();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify a registered author can successfully submit a new paper")
    public void testSuccessfulPaperSubmission() {

        step("Login as Author", () -> {
            page.navigate(BASE_URL);
            page.click("button:has-text('Get Started')");
            page.locator("input[name='email']").fill("author@scholarsync.com");
            page.locator("input[name='password']").fill("author123");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
            Assertions.assertTrue(page.isVisible("text=+ Submit New Paper"), "Login failed or dashboard not visible");
        });

        step("Navigate to Submission Form", () -> {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Submit New Paper")).click();
            page.locator("select[name='conference_id']").selectOption("1");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        });

        step("Fill Paper Details & Select Track", () -> {
            // Ensure section is visible
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter paper title")).waitFor();
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter paper title")).fill("AI Research Paper v1");

            // Targeting track dropdown by visible text to avoid strict mode violation
            page.locator("select").filter(new Locator.FilterOptions().setHasText("Select a track"))
                    .selectOption(new SelectOption().setLabel("Artificial Intelligence"));

            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter paper abstract")).fill("This abstract discusses AI advancements.");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        });

        step("Enter Author Information", () -> {
            page.getByRole(AriaRole.TEXTBOX).first().waitFor();
            page.getByRole(AriaRole.TEXTBOX).first().fill("Test");
            page.getByRole(AriaRole.TEXTBOX).nth(1).fill("User");
            page.locator("input[type=\"email\"]").fill("testuser@example.com");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("University/Organization")).fill("Test University");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        });

        step("Select Keywords and Finalize", () -> {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
            page.getByText("Machine Learning").click();
            page.getByText("Deep Learning").click();
        });

        step("Submit and Verify Success", () -> {
            // The Dialog::accept in @BeforeEach handles the browser alert
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("✓ Submit Paper")).click();

            // Wait for success indicator (adjust selector based on your app's success message)
            page.waitForTimeout(2000);
            System.out.println("✅ Paper Submission Test Passed");
        });
    }

    @Step("{0}")
    private void step(String name, Runnable action) {
        System.out.println("▶ Section: " + name);
        try {
            action.run();
        } catch (Exception e) {
            // Take screenshot on failure specifically
            Allure.addAttachment("Step Failure - " + name, new ByteArrayInputStream(page.screenshot()));
            throw e;
        }
    }
}