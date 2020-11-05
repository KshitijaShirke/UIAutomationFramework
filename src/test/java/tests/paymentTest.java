package tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import excelreadwrite.ExcelUtil;
import excelreadwrite.TestListener;
import extentreports.ExtentTestManager;
import objectrepository.LaunchWebApp;
import pages.Payment;

@Listeners({ TestListener.class })
public class paymentTest extends LaunchWebApp {

	Payment payment;

	@BeforeMethod
	public void setupTestData() throws Exception {
		beforeClass();
		ExcelUtil.setExcelFileSheet("PaymentData");
	}

	@Test(description = "Success Credit Card Payment", testName = "Success Credit Card Payment")
	public void successfulPayment() throws Exception {
		ExtentTestManager.getTest().setDescription("Success Credit Card Payment");
		ExcelUtil.setRowNumber(1);
		ExcelUtil.setColumnNumber(6);
		payment = new Payment();
		payment.successFulPayment(ExcelUtil.getRowData(1));
	}

	@Test(description = "Failed Credit Card Payment", testName = "Failed Credit Card Payment")
	public void failedPayment() throws Exception {
		ExtentTestManager.getTest().setDescription("Failed Credit Card Payment");
		ExcelUtil.setRowNumber(2);
		ExcelUtil.setColumnNumber(6);
		payment = new Payment();
		payment.failedPayment(ExcelUtil.getRowData(2));
	}

	@AfterMethod
	public void tearDown() {
		driver.quit();
	}
}
