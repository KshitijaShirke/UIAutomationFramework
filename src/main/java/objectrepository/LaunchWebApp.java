package objectrepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import excelreadwrite.Locator;
import io.github.bonigarcia.wdm.WebDriverManager;

public class LaunchWebApp {

	public static WebDriver driver = null;

	// Main Directory of the project
	public static final String currentDir = System.getProperty("user.dir");

	// Global locator file
	public static final Locator locator = new Locator(currentDir + "//locator.properties");

	// Global test data excel file
	public static final String testDataExcelFileName = "TestData.xlsx";

	public void beforeClass() throws IOException {

		WebDriverManager.chromedriver().setup();

		Map<String, String> mobileEmulation = new HashMap<>();
		mobileEmulation.put("deviceName", "iPhone 6/7/8 Plus");

		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
		// disable info bars
		chromeOptions.addArguments("disable-infobars");

		chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
		driver = new ChromeDriver(chromeOptions);

		// Maximize window
		driver.manage().window().fullscreen();
		// get URL
		driver.get("https://demo.midtrans.com/");
	}

	public void commonFlow(XSSFRow row) throws Exception {
		driver.findElement(locator.getLocator("buy_now_button")).click();
		execute(locator.getLocator("checkout_button"));

		// switch to frame
		driver.switchTo().frame(0);

		// click on continue
		new WebDriverWait(driver, 20)
				.until(ExpectedConditions.presenceOfElementLocated(locator.getLocator("continue_button")));
		execute(locator.getLocator("continue_button"));

		// enter details
		execute(locator.getLocator("credit_debit_card_option"));
		new WebDriverWait(driver, 30)
				.until(ExpectedConditions.presenceOfElementLocated(locator.getLocator("credit_card_number_field")));
		driver.findElement(locator.getLocator("credit_card_number_field")).sendKeys(row.getCell(1).toString());
		driver.findElement(locator.getLocator("expiry_date_field")).sendKeys(row.getCell(2).toString());
		driver.findElement(locator.getLocator("cvv_field")).sendKeys(row.getCell(3).toString());
		driver.findElement(locator.getLocator("pay_now_button")).click();

		// switch to frame & enter otp
		driver.switchTo().frame(0);
		new WebDriverWait(driver, 50)
				.until(ExpectedConditions.presenceOfElementLocated(locator.getLocator("otp_field")));
		driver.findElement(locator.getLocator("otp_field")).sendKeys(row.getCell(4).toString());
		execute(locator.getLocator("ok_button"));
	}

	public WebDriver getDriver() {
		// TODO Auto-generated method stub
		return driver;
	}

	public void execute(By locator) {
		WebElement element = driver.findElement(locator);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
	}
}
