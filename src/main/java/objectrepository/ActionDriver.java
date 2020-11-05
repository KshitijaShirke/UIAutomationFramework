package objectrepository;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ActionDriver {
	WebDriver driver;

	public ActionDriver() {
		driver = LaunchWebApp.driver;
	}

	public void click(By locator) {
		driver.findElement(locator).click();
	}

	public void type(By locator, String testData) {
		driver.findElement(locator).sendKeys(testData);
	}

	public void explicitWait(int timeDuration, By locator) {
		new WebDriverWait(driver, timeDuration).until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	public String getText(By locator) {
		return driver.findElement(locator).getText();
	}

	public void getMainScreen() {
		driver.switchTo().defaultContent();
	}

	public void switchToFrame() {
		driver.switchTo().frame(0);
	}
}
