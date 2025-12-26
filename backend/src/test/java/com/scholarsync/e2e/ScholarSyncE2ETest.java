package com.scholarsync.e2e;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Epic("Conference Management System")
@Feature("Paper Submission")
public class ScholarSyncE2ETest {

    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeAll
    static void launchBrowser() {
        ensurePdfExists();
        playwright = Playwright.create();
        // Headless mode is required for Jenkins environment
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(true)
                .setSlowMo(100));
    }

    @AfterAll
    static void closeBrowser() {
        if (playwright != null) playwright.close();
    }

    @BeforeEach
    void createContext() {
        context = browser.newContext();
        page = context.newPage();

        // --- Debugging: Prints browser console errors directly to Jenkins Console Output ---
        page.onConsoleMessage(msg -> {
            System.out.println("浏览器日志 >> [" + msg.type() + "] " + msg.text());
        });

        page.onPageError(err -> {
            System.out.println("浏览器崩溃/错误 >> " + err);
        });
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        // --- Allure Attachment: Saves a full-page screenshot of the final state ---
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Allure.addAttachment("Final State - " + testInfo.getDisplayName(), new ByteArrayInputStream(screenshot));

        if (context != null) context.close();
    }

    @Test
    @Story("User can register, login and submit a paper")
    @Description("Full E2E flow: Registration (to ensure account exists), Login, and Paper Submission.")
    @Severity(SeverityLevel.CRITICAL)
    public void testFullPaperSubmissionFlow() {

        step("Navigate to Homepage", () -> {
            // Using host.docker.internal to reach the host's 4200 port from the Jenkins container
            page.navigate("http://host.docker.internal:4200/");
            page.locator("button.btn-primary:has-text('Get Started')").waitFor();
            page.locator("button.btn-primary:has-text('Get Started')").click();
        });

        step("Register Test User (Ensure Account Exists)", () -> {
            page.waitForURL("**/auth");

            // Toggle to registration mode if the button is visible
            Locator createAccountBtn = page.locator("text=Create an account");
            if (createAccountBtn.isVisible()) {
                createAccountBtn.click();
            }

            // FIXED: Using 'name' attribute because placeholder is 'John'
            page.locator("input[name='fName']").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            page.locator("input[name='fName']").fill("Author");

            // Assuming Last Name follows the pattern name='lName'
            page.locator("input[name='lName']").fill("Test");

            // Fill registration Email and Password (use .last() to target the registration form)
            page.locator("input[name='email']").last().fill("author@scholarsync.com");
            page.locator("input[name='password']").last().fill("author123");

            // Fill Affiliation (Based on your HTML pattern, likely name='affiliation')
            page.locator("input[placeholder*='University'], input[name*='affiliation']").last().fill("UTM Melaka");

            // Submit Registration
            page.locator("button:has-text('Sign Up')").click();
            System.out.println("Registration submitted...");
        });

        step("Login to System", () -> {
            // Navigate back to auth to ensure we are on the login form
            page.navigate("http://host.docker.internal:4200/auth");

            // Target the first instances of email/password for the login form
            page.locator("input[name='email']").first().fill("author@scholarsync.com");
            page.locator("input[name='password']").first().fill("author123");
            page.locator("button:has-text('Sign In')").click();
        });

        step("Start Paper Submission Wizard", () -> {
            // High timeout for fresh database initialization
            page.waitForURL("**/paper-submission", new Page.WaitForURLOptions().setTimeout(60000));
            page.locator("button.btn-primary:has-text('Submit New Paper')").click();
        });

        step("Wizard Step 1: Select Conference", () -> {
            page.locator("select[name='conference_id']").waitFor();
            page.selectOption("select[name='conference_id']", new SelectOption().setValue("1"));
            page.locator("button[matsteppernext]").click();
        });

        step("Wizard Step 2: Paper Info", () -> {
            page.locator("input[placeholder*='title']").fill("E2E-Test-Paper-" + System.currentTimeMillis());

            // Locate the Track select dropdown
            Locator trackSelect = page.locator("select").filter(new Locator.FilterOptions().setHasText("Select a track"));
            trackSelect.selectOption(new SelectOption().setValue("1"));

            page.locator("textarea[placeholder*='abstract']").fill("This is an automated test abstract generated during the Jenkins pipeline execution.");
            page.locator("button[matsteppernext]:visible").click();
        });

        step("Wizard Step 3: Authors", () -> {
            // Use checkboxes or role selectors as per your UI
            page.locator("input[type='checkbox']").first().check();
            page.locator("button[matsteppernext]:visible").click();
        });

        step("Wizard Step 4: Upload File", () -> {
            Path pdfPath = Paths.get("src/test/resources/sample.pdf");
            page.setInputFiles("input[type='file']", pdfPath);
            page.selectOption("select", "PDF");
            page.locator("input[type='number']").fill("1");
            page.locator("button[matsteppernext]:visible").click();
        });

        step("Wizard Step 5: Keywords & Submit", () -> {
            page.locator("input[placeholder*='Keyword']").fill("DevOps");
            page.locator("button.btn-primary:has-text('Add')").click();

            // Final submission
            page.locator("button.btn-primary:has-text('Submit Paper')").click();
            System.out.println("E2E Test Flow Completed Successfully!");
        });
    }

    @Step("{0}")
    private void step(String stepName, Runnable runnable) {
        runnable.run();
    }

    private static void ensurePdfExists() {
        try {
            Path dirPath = Paths.get("src/test/resources");
            if (!Files.exists(dirPath)) Files.createDirectories(dirPath);
            File pdfFile = new File("src/test/resources/sample.pdf");
            if (!pdfFile.exists()) {
                Files.writeString(pdfFile.toPath(), "Dummy PDF content for automated testing.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}