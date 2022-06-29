import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Stream;

@Slf4j
public class TestBankingProject {

    private static final String BANKING_PROJECT_URL = "https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login";
    private final long TIMEOUT = 5000;
    private final String name = "Name";
    private final String lastName = "Surname";
    private final String postCode = "PostCode";
    private final String currency = "Dollar";

    private WebDriver webDriver;

    @BeforeClass
    public void setUp() {
        String chromedriverPath;
        try (Stream<Path> walk = Files.walk(Paths.get("src/test/resources/"))) {
            chromedriverPath = walk
                    .filter(p -> !Files.isDirectory(p))
                    .map(p -> p.toString().toLowerCase())
                    .filter(f -> f.endsWith("chromedriver.exe"))
                    .findFirst().get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.setProperty("webdriver.chrome.driver", chromedriverPath);
        webDriver = new ChromeDriver(new ChromeOptions().addArguments("--incognito").addArguments("--start-maximized"));
    }

    @AfterClass(alwaysRun = true)
    public void tearUp() {
        webDriver.close();
    }

    @Test
    public void createClient() {
        try {
            webDriver.get(BANKING_PROJECT_URL);
            WebDriverWait webDriverWait = new WebDriverWait(webDriver, TIMEOUT);
            webDriverWait.until(
                    webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

            log.info(" click on \"Bank Manager Login\"");
            WebElement managerElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[2]/div/div[1]/div[2]/button")));
            managerElement.click();

            log.info("Add Customer");
            WebElement addCustomerElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("body > div > div > div.ng-scope > div > div.center > button:nth-child(1)")));
            addCustomerElement.click();

            log.info("fill customer form button");
            WebElement nameForm = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/div/form/div[1]/input")));
            nameForm.sendKeys(name);
            WebElement lastNameForm = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/div/form/div[2]/input")));
            lastNameForm.sendKeys(lastName);
            WebElement postCodeForm = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/div/form/div[3]/input")));
            postCodeForm.sendKeys(postCode);

            log.info("click on submit button");
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/div/form/button"))).click();

            webDriverWait.until(ExpectedConditions.alertIsPresent()).accept();
            log.info("completed 'createClient' test");
        } catch (Throwable throwable) {
            File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
            screenshot.renameTo(new File("src/test/screenshot_" + new Random().nextInt(5000) + ".png"));
            Assert.fail(String.format("Unexpected error: %s", throwable.getMessage()));
        }
    }

    @Test
    public void openAccountForClient() {
        try {
            webDriver.get(BANKING_PROJECT_URL);
            WebDriverWait webDriverWait = new WebDriverWait(webDriver, TIMEOUT);
            webDriverWait.until(
                    webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

            log.info(" click on \"Bank Manager Login\"");
            WebElement managerElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[2]/div/div[1]/div[2]/button")));
            managerElement.click();


            log.info("click 'Open Account'");
            WebElement openAccount = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[2]/div/div[1]/button[2]")));
            openAccount.click();

            log.info("fill account submission form");
            Select customerAccountElement = new Select(webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"userSelect\"]"))));
            customerAccountElement.selectByVisibleText(name + " " + lastName);
            Select currencyAccountElement = new Select(webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"currency\"]"))));
            currencyAccountElement.selectByVisibleText(currency);
            log.info("Submit form");
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//html/body/div/div/div[2]/div/div[2]/div/div/form/button"))).click();

            webDriverWait.until(ExpectedConditions.alertIsPresent()).accept();
            log.info("completed 'openAccountForClient' test");
        } catch (Throwable throwable) {
            File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
            screenshot.renameTo(new File("src/test/screenshot_" + new Random().nextInt(5000) + ".png"));
            Assert.fail(String.format("Unexpected error: %s", throwable.getMessage()));
        }
    }
}