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

        // 🔥 核心修复 1：自动处理注册成功后的 alert('Registration Successful...') 弹窗
        page.onDialog(dialog -> {
            System.out.println("自动处理弹窗 >> " + dialog.message());
            dialog.accept();
        });

        page.onConsoleMessage(msg -> System.out.println("浏览器日志 >> [" + msg.type() + "] " + msg.text()));
        page.onPageError(err -> System.out.println("浏览器崩溃/错误 >> " + err));
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Allure.addAttachment("Final State - " + testInfo.getDisplayName(), new ByteArrayInputStream(screenshot));
        if (context != null) context.close();
    }

    @Test
    @Story("User can register, login and submit a paper")
    @Severity(SeverityLevel.CRITICAL)
    public void testFullPaperSubmissionFlow() {

        step("Navigate to Homepage", () -> {
            page.navigate("http://host.docker.internal:4200/");
            page.locator("button.btn-primary:has-text('Get Started')").waitFor();
            page.locator("button.btn-primary:has-text('Get Started')").click();
        });

        step("Register Test User (New Account)", () -> {
            page.waitForURL("**/auth");

            // 切换到注册模式
            Locator toggleBtn = page.locator(".auth-switch button:has-text('Create an account')");
            if (toggleBtn.isVisible()) {
                toggleBtn.click();
            }

            // 填写注册表单
            page.locator("input[name='fName']").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            page.locator("input[name='fName']").fill("Author");
            page.locator("input[name='lName']").fill("Test");
            page.locator("input[name='regEmail']").fill("author@scholarsync.com");
            page.locator("input[name='regPass']").fill("author123");
            page.locator("input[name='aff']").fill("UTM Melaka");
            page.locator("input[name='cnt']").fill("Malaysia");

            // 点击“Create Account”按钮，这将触发 API 请求和 alert()
            page.locator("button.btn-primary-lg:has-text('Create Account')").click();
            System.out.println("Registration submitted...");
        });

        step("Login to System", () -> {
            // 🔥 核心修复 2：不要在此处 page.navigate()，因为 Angular 内部已经执行了 isRegister = false
            // 直接等待登录表单的 email 输入框出现即可
            Locator loginEmail = page.locator("input[name='email']").first();
            loginEmail.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));

            loginEmail.fill("author@scholarsync.com");
            page.locator("input[name='password']").first().fill("author123");

            page.locator("button.btn-primary-lg:has-text('Sign In')").click();
            System.out.println("Login request sent.");
        });

        step("Start Paper Submission Wizard", () -> {
            // 增加等待时间，确保登录接口返回并完成跳转
            page.waitForURL("**/paper-submission", new Page.WaitForURLOptions().setTimeout(60000));
            page.locator("button.btn-primary:has-text('Submit New Paper')").click();
        });

        step("Wizard Step 1: Select Conference", () -> {
            page.locator("select[name='conference_id']").waitFor();
            page.selectOption("select[name='conference_id']", new SelectOption().setValue("1"));
            page.locator("button[matsteppernext]").click();
        });

        step("Wizard Step 5: Final Submission", () -> {
            page.locator("button.btn-primary:has-text('Submit Paper')").waitFor();
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
                Files.writeString(pdfFile.toPath(), "Dummy PDF content for testing.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}