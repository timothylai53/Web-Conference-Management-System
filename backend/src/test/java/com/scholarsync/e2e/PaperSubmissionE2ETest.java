package com.webcrafters.confease_backend.e2e;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Epic("Conference Management System")
@Feature("Paper Submission")
public class PaperSubmissionE2ETest {

    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    // 🔥 Use host.docker.internal for Jenkins, but ensure backend is ready
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
        // Print logs to Jenkins console to debug API failures
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
    public void testSuccessfulPaperSubmission() {

        step("Login as Author", () -> {
            page.navigate(BASE_URL);
            page.click("button:has-text('Get Started')");
            page.locator("input[name='email']").fill("author@scholarsync.com");
            page.locator("input[name='password']").fill("author123");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

            // Wait for dashboard to load
            page.waitForSelector("text=+ Submit New Paper", new Page.WaitForSelectorOptions().setTimeout(15000));
        });

        step("Navigate to Submission Form", () -> {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Submit New Paper")).click();

            Locator confSelect = page.locator("select[name='conference_id']").selectOption("1");
            // 🔥 Wait for the specific data to arrive from the API
            page.waitForCondition(() -> confSelect.innerText().contains("Conference"),
                    new Page.WaitForConditionOptions().setTimeout(15000));

            confSelect.selectOption(new SelectOption().setIndex(1)); // Pick first available
            page.locator("button:has-text('Next'):visible").first().click(new Locator.ClickOptions().setForce(true));
        });

        step("Fill Paper Details & Select Track", () -> {
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter paper title")).waitFor();
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter paper title")).fill("AI Research Paper v1");

            Locator trackSelect = page.locator("select").filter(new Locator.FilterOptions().setHasText("Select a track"));

            // 🔥 Jenkins Fix: Wait for the specific Track label to appear in the HTML
            // This is safer than just counting options
            page.waitForCondition(() -> trackSelect.innerText().contains("Artificial Intelligence"),
                    new Page.WaitForConditionOptions().setTimeout(15000));

            trackSelect.selectOption(new SelectOption().setLabel("Artificial Intelligence"));

            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter paper abstract")).fill("This abstract discusses AI advancements.");
            page.locator("button:has-text('Next'):visible").first().click(new Locator.ClickOptions().setForce(true));
        });

        step("Enter Author Information", () -> {
            page.getByRole(AriaRole.TEXTBOX).first().waitFor();
            page.getByRole(AriaRole.TEXTBOX).first().fill("Test");
            page.getByRole(AriaRole.TEXTBOX).nth(1).fill("User");
            page.locator("input[type=\"email\"]").fill("testuser@example.com");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("University/Organization")).fill("Test University");
            page.locator("button:has-text('Next'):visible").first().click(new Locator.ClickOptions().setForce(true));
        });

        step("Select Keywords and Finalize", () -> {
            page.locator("button:has-text('Next'):visible").first().click(new Locator.ClickOptions().setForce(true));
            page.getByText("Machine Learning").click();
            page.getByText("Deep Learning").click();
        });

        step("Submit and Verify Success", () -> {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("✓ Submit Paper")).click();
            System.out.println("✅ Paper Submission Test Passed");
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
            Path pdfPath = dirPath.resolve("sample.pdf");
            if (!Files.exists(pdfPath)) {
                Files.writeString(pdfPath, "Dummy PDF content");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}