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
public class ScholarSyncE2ETest {

    private static Playwright playwright;
    private static Browser browser;

    private BrowserContext context;
    private Page page;

    private static final String BASE_URL = "http://host.docker.internal:4200";
    private static final String PDF_PATH = "src/test/resources/sample.pdf";

    // ===================== SETUP =====================

    @BeforeAll
    static void beforeAll() {
        ensurePdfExists();

        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)   // Jenkins = true
                        .setSlowMo(80)
        );
    }

    @AfterAll
    static void afterAll() {
        if (playwright != null) playwright.close();
    }

    @BeforeEach
    void beforeEach() {
        context = browser.newContext(
                new Browser.NewContextOptions()
                        .setViewportSize(1280, 800)
        );
        page = context.newPage();

        // Auto accept alerts
        page.onDialog(dialog -> dialog.accept());

        // Log browser errors (VERY IMPORTANT)
        page.onConsoleMessage(msg -> {
            if ("error".equals(msg.type())) {
                System.out.println("❌ Browser error: " + msg.text());
            }
        });
    }

    @AfterEach
    void afterEach(TestInfo info) {
        byte[] screenshot = page.screenshot(
                new Page.ScreenshotOptions().setFullPage(true)
        );
        Allure.addAttachment(
                "Final State - " + info.getDisplayName(),
                new ByteArrayInputStream(screenshot)
        );
        context.close();
    }

    // ===================== TEST =====================

    @Test
    @Severity(SeverityLevel.CRITICAL)
    public void fullPaperSubmissionFlow() {

        String ts = String.valueOf(System.currentTimeMillis());
        String email = "author" + ts + "@scholarsync.com";
        String password = "author123";

        step("Open Homepage", () -> {
            page.navigate(BASE_URL);
            page.waitForLoadState();
            page.click("button.btn-primary:has-text('Get Started')");
        });

        step("Register User", () -> {
            page.waitForURL("**/auth");

            Locator switchBtn =
                    page.locator(".auth-switch button:has-text('Create an account')");
            if (switchBtn.isVisible()) switchBtn.click();

            page.fill("input[name='fName']", "Timothy");
            page.fill("input[name='lName']", "Test");
            page.fill("input[name='regEmail']", email);
            page.fill("input[name='regPass']", password);
            page.fill("input[name='aff']", "UUM");
            page.fill("input[name='cnt']", "Malaysia");

            page.click("button.btn-primary-lg:has-text('Create Account')");

            // wait for register view to disappear
            page.locator("input[name='regEmail']").waitFor(
                    new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.HIDDEN)
                            .setTimeout(15000)
            );
        });

        step("Login", () -> {
            page.waitForSelector("input[name='email']");
            page.fill("input[name='email']", email);
            page.fill("input[name='password']", password);
            page.click("button.btn-primary-lg:has-text('Sign In')");
        });

        step("Open Submission Wizard", () -> {
            page.waitForURL("**/paper-submission",
                    new Page.WaitForURLOptions().setTimeout(30000));

            page.click("button.btn-primary:has-text('Submit New Paper')");
        });

        step("Select Conference (CORRECT WAY)", () -> {

            // 🔥 WAIT FOR BACKEND API (NOT DOM)
            page.waitForResponse(
                    response ->
                            response.url().contains("/api/conferences")
                                    && response.status() == 200,
                    new Page.WaitForResponseOptions().setTimeout(20000)
            );

            Locator select = page.locator("select[name='conference_id']");
            select.waitFor();

            // wait until REAL options exist (ignore placeholder)
            page.waitForCondition(() -> {
                int count = page.locator(
                        "select[name='conference_id'] option[value]:not([value=''])"
                ).count();
                return count > 0;
            });

            int optionCount = page.locator(
                    "select[name='conference_id'] option[value]:not([value=''])"
            ).count();
            System.out.println("🟢 Conferences loaded: " + optionCount);

            // select first real conference
            select.selectOption(new SelectOption().setIndex(1));

            page.locator("button[matsteppernext]").first().click();
        });

        step("Upload PDF", () -> {
            page.setInputFiles("input[type='file']", Paths.get(PDF_PATH));
            page.locator("button[matsteppernext]").click();
        });

        step("Final Submit", () -> {
            Locator submitBtn =
                    page.locator("button.btn-primary:has-text('Submit Paper')");
            submitBtn.waitFor(new Locator.WaitForOptions().setTimeout(15000));
            submitBtn.click();
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
                Files.writeString(pdf.toPath(), "Dummy PDF for E2E testing");
            }
        } catch (IOException e) {
            throw new RuntimeException("PDF setup failed", e);
        }
    }
}
