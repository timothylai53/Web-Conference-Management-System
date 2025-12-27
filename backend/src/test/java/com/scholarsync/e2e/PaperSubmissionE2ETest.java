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

    // 🔥 For Jenkins/Docker: host.docker.internal points to the host machine
    private static final String BASE_URL = "http://host.docker.internal:4200";

    @BeforeAll
    static void setupBrowser() {
        ensurePdfExists();
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
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
        page.onDialog(Dialog::accept);
        // Print browser logs to Jenkins console for network debugging
        page.onConsoleMessage(msg -> System.out.println("Browser Log >> [" + msg.type() + "] " + msg.text()));
    }

    @AfterEach
    void tearDown(TestInfo info) {
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Allure.addAttachment("Final State - " + info.getDisplayName(), new ByteArrayInputStream(screenshot));
        context.close();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    public void testSuccessfulPaperSubmission() {

        step("Login as Author", () -> {
            page.navigate(BASE_URL);
            page.click("button:has-text('Get Started')");

            page.locator("input[name='email']").fill("author@scholarsync.com");
            page.locator("input[name='password']").fill("author123");

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

            // Wait for Angular routing
            page.waitForURL("**/paper-submission", new Page.WaitForURLOptions().setTimeout(15000));

            Locator dashboardBtn = page.locator("button:has-text('Submit New Paper')");
            dashboardBtn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

            Assertions.assertTrue(dashboardBtn.isVisible(), "Login failed: Dashboard button not visible.");
        });

        step("Navigate to Submission Form & Select Conference", () -> {
            page.click("button:has-text('Submit New Paper')");

            Locator confSelect = page.locator("select[name='conference_id']");
            confSelect.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.ATTACHED));

            try {
                // Wait for the API to populate the dropdown (ignoring the placeholder)
                page.waitForCondition(() -> (int) confSelect.locator("option").count() > 1,
                        new Page.WaitForConditionOptions().setTimeout(15000));
            } catch (TimeoutError e) {
                System.err.println("TIMEOUT: Dropdown did not load options. HTML: " + confSelect.innerHTML());
                throw e;
            }

            // Attempt to select ID 1, fallback to the first available if not found
            try {
                confSelect.selectOption("1");
            } catch (Exception e) {
                System.out.println("ID 1 not found. Selecting index 1 as fallback.");
                confSelect.selectOption(new SelectOption().setIndex(1));
            }

            page.locator("button:has-text('Next'):visible").first().click(new Locator.ClickOptions().setForce(true));
        });

        step("Fill Paper Details & Select Track", () -> {
            page.waitForSelector("input[placeholder*='title']");
            page.fill("input[placeholder*='title']", "AI Research Paper v1");

            Locator trackSelect = page.locator("select").filter(new Locator.FilterOptions().setHasText("Select a track"));
            trackSelect.waitFor();

            // Ensure tracks are loaded
            page.waitForCondition(() -> (int) trackSelect.locator("option").count() > 1);
            trackSelect.selectOption(new SelectOption().setLabel("Artificial Intelligence"));

            page.fill("textarea[placeholder*='abstract']", "This abstract discusses AI advancements.");

            page.locator("button:has-text('Next'):visible").first().click(new Locator.ClickOptions().setForce(true));
        });

        step("Enter Author Information", () -> {
            page.waitForSelector("input[type='email']");
            // Use generic textboxes to be more robust against changing name attributes
            page.getByRole(AriaRole.TEXTBOX).first().fill("Test");
            page.getByRole(AriaRole.TEXTBOX).nth(1).fill("User");
            page.locator("input[type='email']").fill("testauthor@example.com");

            page.locator("button:has-text('Next'):visible").first().click(new Locator.ClickOptions().setForce(true));
        });

        step("Upload File and Submit", () -> {
            // Proceed to upload section
            page.locator("button:has-text('Next'):visible").first().click(new Locator.ClickOptions().setForce(true));

            Path pdfPath = Paths.get("src/test/resources/sample.pdf");
            page.setInputFiles("input[type='file']", pdfPath);

            page.locator("button:has-text('Next'):visible").first().click(new Locator.ClickOptions().setForce(true));

            // Final submit
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("✓ Submit Paper")).click();
            System.out.println("✅ Paper submission E2E test passed successfully!");
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