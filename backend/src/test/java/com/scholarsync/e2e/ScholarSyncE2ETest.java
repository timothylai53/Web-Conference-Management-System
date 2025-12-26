package com.scholarsync.e2e;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.SelectOption;
import io.qameta.allure.*;
import org.junit.jupiter.api.*; // 使用 JUnit 5
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
        // 重要：在 Jenkins 运行时必须 setHeadless(true)
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(true)
                .setSlowMo(100));
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createContext() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    @Test
    @Story("User can submit a paper successfully")
    @Description("End-to-end test for logging in and submitting a research paper via the wizard.")
    @Severity(SeverityLevel.CRITICAL)
    public void testFullPaperSubmissionFlow() throws InterruptedException {

        step("Navigate to Homepage", () -> {
            page.navigate("http://host.docker.internal:4200/");
            page.locator("button.btn-primary:has-text('Get Started')").click();
        });

        step("Login to System", () -> {
            page.waitForURL("**/auth");
            page.locator("input[name='email']").fill("author@scholarsync.com");
            page.locator("input[name='password']").fill("author123");
            page.locator("button:has-text('Sign In')").click();
        });

        step("Start Paper Submission Wizard", () -> {
            page.waitForURL("**/paper-submission");
            page.locator("button.btn-primary:has-text('Submit New Paper')").click();
        });

        step("Wizard Step 1: Select Conference", () -> {
            page.locator("select[name='conference_id']").waitFor();
            page.selectOption("select[name='conference_id']", new SelectOption().setValue("1"));
            page.locator("button[matsteppernext]").click();
        });

        step("Wizard Step 2: Paper Info", () -> {
            page.locator("input[placeholder='Enter paper title']").fill("Testing-CI-CD-" + System.currentTimeMillis());
            Locator trackSelect = page.locator("select").filter(new Locator.FilterOptions().setHasText("Select a track"));
            trackSelect.selectOption(new SelectOption().setValue("1"));
            page.locator("textarea[placeholder*='abstract']").fill("Automated testing abstract content.");
            page.locator("button[matsteppernext]:visible").click();
        });

        step("Wizard Step 3: Authors", () -> {
            page.locator(".form-group:has-text('First Name') input").fill("Author");
            page.locator(".form-group:has-text('Last Name') input").fill("Test");
            page.locator(".form-group:has-text('Email') input").fill("author@scholarsync.com");
            page.locator("input[placeholder='University/Organization']").fill("UTM");
            page.locator(".checkbox-label input[type='checkbox']").check();
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
            page.locator("input[placeholder='Type and press Enter']").fill("DevOps");
            page.locator("button.btn-primary:has-text('Add')").click();
            page.locator("button.btn-primary:has-text('Submit Paper')").click();
        });

        System.out.println("Test successful!");
    }

    // Helper method for Allure steps visibility
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
                Files.writeString(pdfFile.toPath(), "Dummy PDF content for testing.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}