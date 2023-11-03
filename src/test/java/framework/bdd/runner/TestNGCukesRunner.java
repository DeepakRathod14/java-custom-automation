/*
 *
 */
package framework.bdd.runner;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.model.Report;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import framework.basescript.BaseTestScript;
import framework.utilities.MavenProperty;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.util.Date;

/**
 * The Class TestNGCukesRunner. This class will load cucumber setting as per
 * given and execute before and after annotation and also load and destroy
 * cucumber objects.
 */
@CucumberOptions(
		features = { "src/test/java/framework/bdd/feature/ui/" },
		tags = "@Form",
		monochrome = true,
		glue = {"framework.bdd.stepdef.ui" },
		plugin = {"pretty","framework.bdd.runner.CustomReportListener:" },
		publish = true
)

public class TestNGCukesRunner extends AbstractTestNGCucumberTests {

	private Logger log = LogManager.getLogger(TestNGCukesRunner.class);
	private BaseTestScript script = null;
	private long startTime;
	private long endTime;

	@BeforeSuite
	//@Parameters({ "Browser" })
	public void setup(ITestContext xmlTest) {
		script = new BaseTestScript();
		script.setupDriver();
		script.loadUrl();
	}

	@BeforeClass
	public void lockStartTime() {
		startTime = System.currentTimeMillis();
		printLog("Execution Start Time : " + startTime);
		Report.builder().build().setStartTime(new Date(startTime));
	}

	@AfterClass
	public void lockEndTime() {
		endTime = System.currentTimeMillis();
		Report.builder().build().setEndTime(new Date(endTime));
		printLog("===========================================================================");
		printLog("TEST CASE TOTAL EXECUTION TIME : " + (endTime - startTime) / 60 + " Second");
		printLog("===========================================================================");
	}

	@AfterSuite
	public void tearDown() {
		script.closeBrowser();
	}

	protected void loadProperties() {
		printLog("Load maven property");
		MavenProperty.getMavenProperties();
	}

	public void printLog(String message) {
		log.info(message);
		Reporter.log(message);
	}
}
