package extentreports;

import java.util.HashMap;
import java.util.Map;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class ExtentTestManager {

	// extentTestMap holds the information of thread ids and ExtentTest
	// instances
	static Map extentTestMap = new HashMap();

	// extentreports instance created by calling getReporter() method from
	// ExtentManager
	static ExtentReports extent = ExtentManager.getReporter();

	// return ExtentTest instance in extentTestMap by using current thread id
	public static synchronized ExtentTest getTest() {
		return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
	}

	// test ends and ExtentTest instance got from extentTestMap via current
	// thread id
	public static synchronized void endTest() {
		extent.endTest((ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId())));
	}

	// instance of ExtentTest created and put into extentTestMap with current
	// thread id
	public static synchronized ExtentTest startTest(String testName, String desc) {
		ExtentTest test = extent.startTest(testName, desc);
		extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
		return test;
	}
}
