package framework.basescript;

import com.aventstack.extentreports.Status;
import com.google.common.hash.Hashing;
import framework.utilities.*;
import framework.utilities.softasst.AutomationSoftAsserts;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

//@Listeners({AssertJSoftAssertListener.class, TestsListener.class, ContReportPortalListener.class})
public class BaseTestScript extends ExtentConfig {

	private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
	private Logger log = LogManager.getLogger(BaseTestScript.class);
	// private static ThreadLocal<WebDriver> thread = new ThreadLocal<WebDriver>();
	public static WebDriver driver = null;
	private static final String BDD_TESTNG_RUNNER = "TestNGCukesRunnerForUI";
	protected AutomationSoftAsserts softAssert;
	private long startTime;
	private long endTime;
	protected String username;
	protected String password;
	private Method testMethod;
	private String uniqueTcMeta;
	private String testName;

	@BeforeSuite
	public void setupReport(ITestContext xmlTest){
		if (!getXmlClassName(xmlTest).equals(BDD_TESTNG_RUNNER)) {
			printLog("TDD UI Test cases going to execute, hence we configure extent report");
			setupExtentReport();
		}
		printLog("Executing test cases from test (XML) : " + getXmlClassName(xmlTest));
	}

	@BeforeTest(description = "Creating Test for Report")
	public void launchBrowser(ITestContext xmlTest) {
		if (xmlTest.getCurrentXmlTest().getXmlClasses().get(0).getName().contains(".ui."))
			setupDriver();
		if (!getXmlClassName(xmlTest).equals(BDD_TESTNG_RUNNER)) {
			printLog("Extent report going to configure Test Name in HTML report : " + getXmlClassName(xmlTest));
			test.set(extent.get().createTest(xmlTest.getCurrentXmlTest().getName(), "This is a sample test"));
		}
		printLog("Extent report going to configure Test Name in HTML report : " + xmlTest.getName());
		getTest().log(Status.INFO, "Test Case Execution Start");
		// Need to revalidated below line
		LogginOutputStream.redirectPrintsToLogger();
		loadProperties();
		loadUrl();
		softAssert = framework.utilities.softasst.SoftAssertFactory.getSoftAssert();
	}

	@BeforeMethod(description = "Creating Node for scenario")
	public void beforeMethod(Method method) {
		node.set(getTest().createNode(method.getName()));
		printLog("Extent report invoke method for create 'TestNode' in HTML report as " + method.getName());
		printReportLog("Scenario is start");
	}

	public void setupDriver() {
		String browser = System.getProperty("Browser")!=null?System.getProperty("Browser"):
				MavenProperty.getMavenProperties().getProperty("Browser");
		printLog("Going to load browser " + browser);
		if (browser.equalsIgnoreCase(BrowserName.CHROME.getValue())) {
			//WebDriverManager.chromedriver().setup();
			System.setProperty("webdriver.chrome.driver", "C:\\Projects\\CRM\\auto\\chromedriver-win64\\chromedriver.exe");
			driver = new ChromeDriver();
			printLog("Driver load successfully..");
		} else if (browser.equalsIgnoreCase(BrowserName.FF.getValue())) {
			//WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			printLog("Driver load successfully..");
		} else if (browser.equalsIgnoreCase(BrowserName.IE.getValue())) {
			//WebDriverManager.iedriver().setup();
			driver = new EdgeDriver();
			printLog("Driver load successfully..");
		} else if (browser.equalsIgnoreCase(BrowserName.HEADLESS.getValue())) {
			ChromeOptions options = new ChromeOptions();
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver(options);
			printLog("Driver load successfully..");
		} else {
			log.error("Given WebDriver is not implemented... " + browser);
			System.exit(0);
		}
		driver.manage().window().maximize();
		InitFactory.setDriver(driver);
		InitFactory.getBasePageObject().initMapping();
	}

	public void loadUrl() {
		username = MavenProperty.getMavenProperties().getProperty("Username");
		password = MavenProperty.getMavenProperties().getProperty("Password");
		printLog("Going to load Url : " + MavenProperty.getMavenProperties().getProperty(Configuration.URL));
		try {
			driver.get(MavenProperty.getMavenProperties().getProperty(Configuration.URL));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@BeforeClass
	public void lockStartTime() {
		startTime = System.currentTimeMillis();
		printLog("Execution Start Time : " + startTime);
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		printLog("Extent report going to validate scenario result in HTML page after scenario execution done");
		printReportLog("Scenario is end");
		getNode().log(Status.INFO, "Scenario Execution Completed");
		printResultInReport(result, driver);
	}


	@AfterClass
	public void lockEndTime() {
		endTime = System.currentTimeMillis();
		printLog("===========================================================================");
		printLog("TEST CASE TOTAL EXECUTION TIME : " + (endTime - startTime) / 60 + " Second");
		printLog("===========================================================================");
	}

	@AfterTest
	public void afterTest() {
		getTest().log(Status.INFO, "Test Execution Completed");
		printLog("Extent report print data into HTML report after test execution done");
		printLog("Going to close browser");
		closeBrowser();
	}

	public void closeBrowser(){
		if(driver!=null)
			driver.quit();
	}
	@AfterSuite
	public void afterSuite(){
		closeReport();
	}

	protected void loadProperties() {
		printLog("Load maven property");
		MavenProperty.getMavenProperties();
	}

	@Override
	public void printLog(String message) {
		log.info(message);
		Reporter.log(message);
	}

	//================================ METHODS ARE UNDER PROGRESS =============================
	public SoftAssertions getSoftAssert() {
		return softAssert;
	}

	public String getTestCaseName() {
		return this.testName;
	}

	public String getSanitizedTestCaseName() {
		return this.testName.replace("-", "");
	}

	public String getTestCaseMethodName() {
		return testMethod.getName();
	}

	public String getTestCaseMeta() {
		return uniqueTcMeta;
	}

	public void cleanUpThreadLocalPool() {
		// SshConfigProvider.provideConfig().remove();
	}

	public void prepareBeforeMethod(Method testMethod) {
		this.testMethod = testMethod;
		this.uniqueTcMeta = Hashing.sha256()
				.hashString(this.getTestCaseName() + "_" + framework.utilities.Faker.timeStampInSeconds(),
						StandardCharsets.UTF_8)
				.toString();
		printLog("create unique Testcase meta as : "+this.uniqueTcMeta);
		printLog("Extent report invoke method for create 'TestNode' in HTML report as " + testMethod.getName());
		//createNodeInReport(testMethod);
	}

	public final void closeDbConnection() {
		printReportLog("Closing connection of database");
		// ItsPlatformDB.closeDbConnections();
	}


	public final void closeConnectionPool() {
		// clearActiveSessions();
		// sshConnectionPool().closeConnectionPool();
	}
}
