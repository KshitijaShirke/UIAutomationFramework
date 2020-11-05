package excelreadwrite;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import extentreports.ExtentManager;
import extentreports.ExtentTestManager;
import extentreports.SendMail;
import objectrepository.LaunchWebApp;

public class TestListener extends LaunchWebApp implements ITestListener {

	private static String getTestMethodName(ITestResult iTestResult) {
		return iTestResult.getMethod().getConstructorOrMethod().getName();
	}

	public void onTestFailure(ITestResult result) {
		try {

			// Get driver from BaseTest and assign to local driver variable.
			Object testClass = result.getInstance();
			WebDriver driver = ((LaunchWebApp) testClass).getDriver();

			// Take base64Screenshot screenshot.
			String base64Screenshot = "data:image/png;base64,"
					+ ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);

			String msg = result.getThrowable().getMessage();

			// Short msg for no such element exception
			String shortMsg = msg.split("  ")[0];
			System.out.println(shortMsg);

			// Assertion failed
			if (msg.contains("asserts")) {

				ExcelUtil.setCellData("FAILED : " + msg, ExcelUtil.getRowNumber(), ExcelUtil.getColumnNumber());
				// For test failed
				ExtentTestManager.getTest().log(LogStatus.FAIL,
						"Test Failed" + ExtentTestManager.getTest().addBase64ScreenShot(base64Screenshot) + "" + msg);

				// Exception
			} else if (msg.contains("no such element")) {

				ExcelUtil.setCellData("FAILED", ExcelUtil.getRowNumber(), ExcelUtil.getColumnNumber());
				// Extentreports log and screenshot operations for failed tests.
				ExtentTestManager.getTest().log(LogStatus.ERROR, "Test Failed"
						+ ExtentTestManager.getTest().addBase64ScreenShot(base64Screenshot) + " " + shortMsg);
			}

			// test cases failed
			else {

				ExcelUtil.setCellData("FAILED", ExcelUtil.getRowNumber(), ExcelUtil.getColumnNumber());
				// For test failed
				ExtentTestManager.getTest().log(LogStatus.ERROR,
						"Test Failed" + ExtentTestManager.getTest().addBase64ScreenShot(base64Screenshot) + msg);
			}

			String testClassName = result.getTestClass().getName().toString().trim();
			String methodName = result.getName().toString().trim();
			takeScreenShot(testClassName, methodName);

			Log.info("Test " + result.getName() + " failed");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		// Start operation for extentreports
		Method method = result.getMethod().getConstructorOrMethod().getMethod();
		String testName = method.getAnnotation(Test.class).testName();
		System.out.println(testName);
		ExtentTestManager.startTest(testName, "");
		ExtentTestManager.getTest().log(LogStatus.INFO, "Test Started");
		Log.startTestCase(result.getName());
	}

	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		context.setAttribute("AndroidDriver", driver);
	}

	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		ExtentTestManager.endTest();
		ExtentTestManager.getTest().log(LogStatus.INFO, "Test Finished");
		// write or update test information to report
		ExtentManager.getReporter().flush();
		SendMail sendMail = new SendMail();
//		sendMail.sendMail();
		Log.endTestCase(context.getName());
	}

	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		// Extentreports log operation for skipped tests.
		ExtentTestManager.getTest().log(LogStatus.SKIP, "Test Skipped");
		Log.info("Test " + result.getName() + " skipped");
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		System.out.println("Test failed but it is in defined success ratio " + getTestMethodName(result));

	}

	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		try {
			ExcelUtil.setCellData("PASSED", ExcelUtil.getRowNumber(), ExcelUtil.getColumnNumber());

			// Extentreports log operation for passed tests.
			ExtentTestManager.getTest().log(LogStatus.PASS, "Test Passed");
			Log.info("Test " + result.getName() + " passed");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getCurrentTimeDate() {
		DateFormat format = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
		String timeStamp = format.format(new Date());
		return timeStamp;
	}

	public void takeScreenShot(String className, String methodName) {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String dest = System.getProperty("user.dir") + "//ErrorScreenshots//" + className + "//" + methodName + "_"
				+ getCurrentTimeDate() + ".png";
		File destination = new File(dest);
		try {
			FileUtils.copyFile(source, destination);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
