package com.webcrafters.confease_backend.e2e;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import java.io.ByteArrayInputStream;
import java.util.UUID; // Used for unique string generation

@Epic("Conference Management System")
@Feature("Reviewer Management")
public class ReviewerRegistrationE2ETest {

    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    private static final String BASE_URL = "http://localhost:4200";

    // Dynamic variables for this specific test run
    private String dynamicFirstName;
    private String dynamicLastName;
    private String dynamicEmail;

    @BeforeAll
    static void setupBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setSlowMo(150)
        );
    }

    @AfterAll
    static void closeBrowser() {
        if (playwright != null) playwright.close();
    }

    @BeforeEach
    void createContext() {
        // Generate random data for each test run
        String uniqueID = UUID.randomUUID().toString().substring(0, 8);
        dynamicFirstName = "Reviewer" + uniqueID;
        dynamicLastName = "Test" + uniqueID;
        dynamicEmail = "rev_" + uniqueID + "@scholarsync.com";

        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1280, 800));
        page = context.newPage();
        page.onDialog(Dialog::accept);
    }

    @AfterEach
    void tearDown(TestInfo info) {
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Allure.addAttachment("Final State - " + info.getDisplayName(), new ByteArrayInputStream(screenshot));
        context.close();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Full flow: Reviewer registration with random data followed by Admin approval")
    public void testReviewerRegistrationAndApproval() {

        step("Navigate to Reviewer Application", () -> {
            page.navigate(BASE_URL);
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("🎓 Become a Reviewer")).click();
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("First Name *")).waitFor();
        });

        step("Fill Reviewer Application Form with Random Data", () -> {
            System.out.println("Registering: " + dynamicFirstName + " " + dynamicLastName + " (" + dynamicEmail + ")");

            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("First Name *")).fill(dynamicFirstName);
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Last Name *")).fill(dynamicLastName);
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email *")).fill(dynamicEmail);

            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Affiliation")).fill("Test University");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Country")).fill("Test Country");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password *").setExact(true)).fill("123456");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Confirm Password *")).fill("123456");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Expertise Area *")).fill("Machine Learning");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Brief Bio")).fill("Auto-generated bio for " + dynamicFirstName);

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit Application")).click();
            page.waitForLoadState(LoadState.NETWORKIDLE);
        });

        step("Login as Admin", () -> {
            page.navigate(BASE_URL);
            page.click("button:has-text('Get Started')");
            page.locator("input[name='email']").waitFor();
            page.locator("input[name='email']").fill("admin@scholarsync.com");
            page.locator("input[name='password']").fill("admin123");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

            Locator adminLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Admin Panel"));
            adminLink.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            adminLink.click();

            page.waitForURL(url -> url.contains("/admin"), new Page.WaitForURLOptions().setTimeout(15000));
        });

        step("Approve Specific Reviewer Application", () -> {
            // Find the row containing the SPECIFIC dynamic email we just created
            Locator reviewerRow = page.locator("tr").filter(new Locator.FilterOptions().setHasText(dynamicEmail));

            // Wait for the specific data to appear in the Admin table
            reviewerRow.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

            Locator approveBtn = reviewerRow.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Approve"));

            if (approveBtn.isVisible()) {
                approveBtn.click();
                System.out.println("✅ Successfully approved: " + dynamicEmail);
                page.waitForTimeout(2000);
            } else {
                Assertions.fail("Approve button not found for specific email: " + dynamicEmail);
            }
        });
    }

    @Step("{0}")
    private void step(String name, Runnable action) {
        System.out.println("▶ Section: " + name);
        try {
            action.run();
        } catch (Exception e) {
            Allure.addAttachment("Step Failure - " + name, new ByteArrayInputStream(page.screenshot()));
            throw e;
        }
    }
}