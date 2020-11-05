package pages;

import static objectrepository.LaunchWebApp.locator;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.testng.asserts.SoftAssert;

import objectrepository.ActionDriver;
import objectrepository.LaunchWebApp;

public class Payment {

	static ActionDriver actionDriver;
	static LaunchWebApp launchWebApp;

	public Payment() {
		actionDriver = new ActionDriver();
		launchWebApp = new LaunchWebApp();
	}

	public void successFulPayment(XSSFRow row) throws Exception {
		launchWebApp.commonFlow(row);

		// go to main screen
		actionDriver.getMainScreen();
		actionDriver.explicitWait(50, locator.getLocator("success_msg"));
		SoftAssert successAssert = new SoftAssert();
		successAssert.assertEquals(actionDriver.getText(locator.getLocator("success_msg")), row.getCell(5).toString());
		successAssert.assertAll();
	}

	public void failedPayment(XSSFRow row) throws Exception {
		launchWebApp.commonFlow(row);

		Thread.sleep(3000);
		actionDriver.switchToFrame();
		System.out.println(actionDriver.getText(locator.getLocator("failed_transaction_msg")));
		SoftAssert failedAssert = new SoftAssert();
		failedAssert.assertEquals(actionDriver.getText(locator.getLocator("failed_transaction_msg")),
				row.getCell(5).toString());
		failedAssert.assertAll();
	}
}
