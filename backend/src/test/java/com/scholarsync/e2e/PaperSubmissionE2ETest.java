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

    // 🔥 Using host.docker.internal for Jenkins/Docker environments
    private static final String BASE_URL = System.getenv("APP_URL") != null
            ? System.getenv("APP_URL")
            : "http://host.docker.internal:4200";

    @BeforeAll
    static void setupBrowser() {
        ensurePdfExists();
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true) // Required for CI/Jenkins
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
        // Useful for Jenkins Console Logs
        page.onConsoleMessage(msg -> System.out.println("Browser Log: " + msg.text()));
    }

    @AfterEach
    void tearDown(TestInfo info) {
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Allure.addAttachment("Final State - " + info.getDisplayName(), new ByteArrayInputStream(screenshot));
        context.close();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify end-to-end paper submission in Jenkins environment")
    public void testSuccessfulPaperSubmission() {

        step("Login as Author", () -> {
            page.navigate(BASE_URL + "/auth"); // Navigate directly to auth to save time
            page.locator("input[name='email']").fill("author@scholarsync.com");
            page.locator("input[name='password']").fill("author123");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

            // Wait for dashboard element to confirm login
            Locator submitBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Submit New Paper"));
            submitBtn.waitFor(new Locator.WaitForOptions().setTimeout(15000));
            Assertions.assertTrue(submitBtn.isVisible(), "Login failed: Submit button not found");
        });

        step("Select Conference", () -> {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Submit New Paper")).click();

            // Wait for options to load from API
            Locator confSelect = page.locator("select[name='conference_id']");
            confSelect.waitFor();
            confSelect.selectOption("1");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        });

        step("Fill Paper Details", () -> {
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter paper title")).fill("Jenkins CI/CD Test Paper");

            // Select track specifically
            Locator trackSelect = page.locator("select").filter(new Locator.FilterOptions().setHasText("Select a track"));
            trackSelect.selectOption(new SelectOption().setLabel("Artificial Intelligence"));

            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter paper abstract")).fill("Automated abstract for Jenkins run.");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        });

        step("Author Info and File Upload", () -> {
            // Fill Author (using generic textbox locators if names are dynamic)
            page.getByRole(AriaRole.TEXTBOX).first().fill("Jenkins");
            page.getByRole(AriaRole.TEXTBOX).nth(1).fill("Bot");
            page.locator("input[type='email']").fill("jenkins-test@example.com");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();

            // Keyword Step
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();

            // Upload File
            Path pdfPath = Paths.get("src/test/resources/sample.pdf");
            page.setInputFiles("input[type='file']", pdfPath);

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        });

        step("Confirm Submission", () -> {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("✓ Submit Paper")).click();
            page.waitForTimeout(2000);
            System.out.println("✅ Jenkins E2E Submission Successful");
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
                Files.writeString(pdfFile.toPath(), "Test PDF content.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}