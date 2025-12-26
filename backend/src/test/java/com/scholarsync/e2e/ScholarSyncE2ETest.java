package com.scholarsync.e2e;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Epic("Conference Management System")
@Feature("Paper Submission")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ScholarSyncE2ETest {

    private static Playwright playwright;
    private static Browser browser;

    private BrowserContext context;
    private Page page;

    private static final String BASE_URL = "http://host.docker.internal:4200";
    private static final String PDF_PATH = "src/test/resources/sample.pdf";

    // ===================== SETUP =====================

    @BeforeAll
    static void setupBrowser() {
        ensurePdfExists();

        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)     // Jenkins = true
                        .setSlowMo(80)
        );
    }

    @AfterAll
    static void teardownBrowser() {
        if (playwright != null) playwright.close();
    }

    @BeforeEach
    void createContext() {
        context = browser.newContext(
                new Browser.NewContextOptions()
                        .setViewportSize(1280, 800)
        );

        page = context.newPage();

        // Auto-accept alerts
        page.onDialog(dialog -> {
            System.out.println("⚠️ Dialog: " + dialog.message());
            dialog.accept();
        });

        // Print browser errors
        page.onConsoleMessage(msg -> {
            if ("error".equals(msg.type())) {
                System.out.println("❌ Browser error: " + msg.text());
            }
        });
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

        if (context != null) context.close();
    }

    // ===================== TEST =====================

    @Test
    @Story("Register → Login → Submit Paper")
    @Severity(SeverityLevel.CRITICAL)
    public void testFullPaperSubmissionFlow() {

        String ts = String.valueOf(System.currentTimeMillis());
        String email = "author" + ts + "@scholarsync.com";
        String password = "author123";

        step("Open Homepage", () -> {
            page.navigate(BASE_URL);
            page.waitForLoadState();

            page.locator("button.btn-primary:has-text('Get Started')")
                    .waitFor();
            page.locator("button.btn-primary:has-text('Get Started')")
                    .click();
        });

        step("Register New User", () -> {
            page.waitForURL("**/auth");

            Locator switchBtn = page.locator(
                    ".auth-switch button:has-text('Create an account')"
            );
            if (switchBtn.isVisible()) {
                switchBtn.click();
            }

            page.fill("input[name='fName']", "Timothy");
            page.fill("input[name='lName']", "Test");
            page.fill("input[name='regEmail']", email);
            page.fill("input[name='regPass']", password);
            page.fill("input[name='aff']", "UUM");
            page.fill("input[name='cnt']", "Malaysia");

            page.click("button.btn-primary-lg:has-text('Create Account')");

            // IMPORTANT: wait for register view to disappear
            page.locator("input[name='regEmail']")
                    .waitFor(new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.HIDDEN)
                            .setTimeout(15000));
        });

        step("Login", () -> {
            page.waitForSelector("input[name='email']");

            page.fill("input[name='email']", email);
            page.fill("input[name='password']", password);

            page.click("button.btn-primary-lg:has-text('Sign In')");
        });

        step("Go to Paper Submission", () -> {
            page.waitForURL("**/paper-submission",
                    new Page.WaitForURLOptions().setTimeout(30000));

            page.locator("button.btn-primary:has-text('Submit New Paper')")
                    .waitFor();
            page.click("button.btn-primary:has-text('Submit New Paper')");
        });

        step("Wizard Step 1 – Select Conference", () -> {
            Locator select = page.locator("select[name='conference_id']");
            select.waitFor();

            // 🔥 CRITICAL FIX: wait for async options
            page.waitForSelector(
                    "select[name='conference_id'] option",
                    new Page.WaitForSelectorOptions().setTimeout(15000)
            );

            // Debug safety
            int count = page.locator(
                    "select[name='conference_id'] option"
            ).count();
            System.out.println("🟢 Conference options loaded: " + count);

            select.selectOption(new SelectOption().setIndex(1));

            page.locator("button[matsteppernext]").first().click();
        });

        step("Upload PDF", () -> {
            page.setInputFiles(
                    "input[type='file']",
                    Paths.get(PDF_PATH)
            );

            page.locator("button[matsteppernext]").click();
        });

        step("Final Submit", () -> {
            Locator submitBtn = page.locator(
                    "button.btn-primary:has-text('Submit Paper')"
            );

            submitBtn.waitFor(
                    new Locator.WaitForOptions().setTimeout(15000)
            );
            submitBtn.click();

            System.out.println("🎉 Paper submitted successfully!");
        });
    }

    // ===================== HELPERS =====================

    @Step("{0}")
    private void step(String name, Runnable action) {
        System.out.println("▶ " + name);
        action.run();
    }

    private static void ensurePdfExists() {
        try {
            Path dir = Paths.get("src/test/resources");
            if (!Files.exists(dir)) Files.createDirectories(dir);

            File pdf = new File(PDF_PATH);
            if (!pdf.exists()) {
                Files.writeString(
                        pdf.toPath(),
                        "Dummy PDF for Playwright testing"
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("PDF setup failed", e);
        }
    }
}
