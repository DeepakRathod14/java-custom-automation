package framework.basescript;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import framework.utilities.Screenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.IOException;

public class ExtentConfig {
    protected static ThreadLocal<ExtentReports> extent = new ThreadLocal<>();
    protected static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    protected static ThreadLocal<ExtentTest> node = new ThreadLocal<>();


    protected void setupExtentReport() {

        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/Reports/ExtentReport.html");
        //Applying Look & Feel
        htmlReporter.config().setReportName(System.getProperty("user.name"));
        htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
        htmlReporter.config().setChartVisibleOnOpen(true);
        htmlReporter.config().setDocumentTitle("Automation Report");
        htmlReporter.config().setTestViewChartLocation("TOP");
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().getEnableExceptionView();
        htmlReporter.config().getEnableTestRunnerLogsView();
        extent.set(new ExtentReports());
        extent.get().attachReporter(htmlReporter);
    }

    public ExtentTest getTest() {
        return test.get();
    }

    public ExtentTest getNode() {
        return node.get();
    }

    public void closeReport() {
        for (String msg : Reporter.getOutput()) {
            extent.get().addTestRunnerOutput("<br/>");
            extent.get().addTestRunnerOutput(" -  " + msg);
        }
        extent.get().flush();
    }

    protected void printLog(String message) {
        Reporter.log(message);
    }

    protected void printReportLog(String message) {
        getNode().log(Status.PASS, message);
    }

    protected void printResultInReport(ITestResult result, WebDriver driver) {
        if (result.getStatus() == ITestResult.FAILURE) {
            node.get().log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " - Test Case FAILED", ExtentColor.RED));
            node.get().log(Status.FAIL,
                    MarkupHelper.createLabel("Failed Due to - " + result.getThrowable(), ExtentColor.RED));
            if (result.getTestClass().getName().contains("ui")) {
                try {
                    String screen = Screenshot.getScreenShotForReport(driver, result.getName());
                    getNode().log(Status.FAIL, MediaEntityBuilder.createScreenCaptureFromPath(screen).build());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (result.getStatus() == ITestResult.SKIP) {
            node.get().log(Status.SKIP,
                    MarkupHelper.createLabel(result.getName() + " - Test Case is SKIPPED", ExtentColor.ORANGE));
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            node.get().log(Status.PASS,
                    MarkupHelper.createLabel(result.getName() + " Test Case is PASSED", ExtentColor.GREEN));
        }
    }

    public String getXmlClassName(ITestContext xmlTest) {
        return xmlTest.getCurrentXmlTest().getClasses().get(0).getName().substring(14);
    }
}
