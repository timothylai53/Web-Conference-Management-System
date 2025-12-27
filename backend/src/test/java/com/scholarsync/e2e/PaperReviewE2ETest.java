package com.webcrafters.confease_backend.e2e;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import java.io.ByteArrayInputStream;
import java.nio.file.Paths;
import java.util.UUID;
import com.microsoft.playwright.options.WaitForSelectorState;

@Epic("Conference Management System")
@Feature("Full Conference Lifecycle")
public class PaperReviewE2ETest {

    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    private static final String BASE_URL = "http://host.docker.internal:4200";

    private String uniqueID;
    private String randomPaperTitle;
    private String randomReviewerEmail;
    private String randomFirstName;
    private String randomLastName;

    @BeforeAll
    static void setupBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true).setSlowMo(150)
        );
    }

    @AfterAll
    static void closeBrowser() {
        if (playwright != null) playwright.close();
    }

    @BeforeEach
    void setup() {
        uniqueID = UUID.randomUUID().toString().substring(0, 5);
        randomPaperTitle = "Research Paper " + uniqueID;
        randomReviewerEmail = "rev_" + uniqueID + "@scholarsync.com";
        randomFirstName = "AutoReviewer";
        randomLastName = uniqueID;

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
    @Severity(SeverityLevel.BLOCKER)
    public void testFullConferenceWorkflow() {

        step("1. Author: Submit Random Paper", () -> {
            page.navigate(BASE_URL);
            page.click("button:has-text('Get Started')");
            page.locator("input[name='email']").fill("author@scholarsync.com");
            page.locator("input[name='password']").fill("author123");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Submit New Paper")).click();

            // Wait for backend data to populate the dropdown (handling slow startup)
            Locator confSelect = page.locator("select[name='conference_id']");
            confSelect.locator("option[value='1']").waitFor();
            confSelect.selectOption("1");

            page.locator("button:has-text('Next'):visible").first().click(new Locator.ClickOptions().setForce(true));

            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter paper title")).fill(randomPaperTitle);
            page.locator("select").filter(new Locator.FilterOptions().setHasText("Select a track"))
                    .selectOption(new SelectOption().setLabel("Artificial Intelligence"));
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter paper abstract")).fill("Abstract for " + randomPaperTitle);

            page.waitForTimeout(500);
            page.locator("button:has-text('Next'):visible").first().click(new Locator.ClickOptions().setForce(true));

            page.getByRole(AriaRole.TEXTBOX).first().fill("Author" + uniqueID);
            page.getByRole(AriaRole.TEXTBOX).nth(1).fill("Last" + uniqueID);
            page.locator("input[type='email']").fill("auth_" + uniqueID + "@test.com");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("University/Organization")).fill("UUM");

            page.waitForTimeout(500);
            page.locator("button:has-text('Next'):visible").first().click(new Locator.ClickOptions().setForce(true));

            page.waitForTimeout(500);
            page.locator("button:has-text('Next'):visible").first().click(new Locator.ClickOptions().setForce(true));

            page.getByText("Machine Learning").click();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("✓ Submit Paper")).click();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("➜")).click();
        });

        step("2. Register Random Reviewer", () -> {
            page.navigate(BASE_URL);
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("🎓 Become a Reviewer")).click();

            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("First Name *")).fill(randomFirstName);
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Last Name *")).fill(randomLastName);
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email *")).fill(randomReviewerEmail);
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password *").setExact(true)).fill("password123");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Confirm Password *")).fill("password123");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Expertise Area *")).fill("Computer Science");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit Application")).click();

            // Explicitly wait for success to ensure DB persistence before Admin checks
            page.waitForSelector("text=Application submitted", new Page.WaitForSelectorOptions().setTimeout(10000));
        });

        step("3. Admin: Approve Reviewer", () -> {
            page.navigate(BASE_URL);
            page.click("button:has-text('Get Started')");
            page.locator("input[name='email']").fill("admin@scholarsync.com");
            page.locator("input[name='password']").fill("admin123");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Admin Panel")).click();

            // Wait for the table to render initial rows
            page.locator("table tr").nth(1).waitFor();

            Locator row = page.locator("tr").filter(new Locator.FilterOptions().setHasText(randomReviewerEmail));
            try {
                // FIXED: Used WaitForSelectorState.VISIBLE to match Playwright Java API requirements
                row.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
            } catch (Exception e) {
                System.out.println("Reviewer not found, reloading page...");
                page.reload();
                row.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
            }

            row.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Approve")).click();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("➜")).click();
        });

        step("4. Chair: Assign Paper", () -> {
            page.navigate(BASE_URL);
            page.click("button:has-text('Get Started')");
            page.locator("input[name='email']").fill("chair@scholarsync.com");
            page.locator("input[name='password']").fill("chair123");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Review Setup")).waitFor();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Review Setup")).click();

            Locator targetRow = page.locator("tr").filter(new Locator.FilterOptions().setHasText(randomPaperTitle));
            targetRow.waitFor();

            String reviewerNamePart = randomFirstName + " " + randomLastName;
            Locator selectDropdown = targetRow.locator("select");

            page.waitForCondition(() -> selectDropdown.innerText().contains(reviewerNamePart));

            String optionValue = (String) selectDropdown.locator("option").evaluateAll(
                    "options => options.find(o => o.textContent.includes('" + reviewerNamePart + "'))?.value"
            );

            if (optionValue != null) {
                selectDropdown.selectOption(optionValue);
            } else {
                throw new RuntimeException("Could not find value for reviewer: " + reviewerNamePart);
            }

            page.waitForTimeout(500);
            targetRow.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Assign")).click();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("➜")).click();
        });

        step("5. Reviewer: Submit Review", () -> {
            page.navigate(BASE_URL);
            page.click("button:has-text('Get Started')");
            page.locator("input[name='email']").fill(randomReviewerEmail);
            page.locator("input[name='password']").fill("password123");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Review")).click();
            page.getByRole(AriaRole.SPINBUTTON).first().fill("10");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Provide constructive feedback")).fill("Good work.");
            page.getByRole(AriaRole.COMBOBOX).selectOption("Accepted");
            page.getByPlaceholder("=Low, 5=High").fill("5");

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit Review")).click();
        });
    }

    @Step("{0}")
    private void step(String name, Runnable action) {
        System.out.println("▶️ Section: " + name);
        action.run();
    }
}