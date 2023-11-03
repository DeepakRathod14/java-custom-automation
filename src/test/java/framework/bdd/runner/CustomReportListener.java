package framework.bdd.runner;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import framework.utilities.Screenshot;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.gherkin.model.Given;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import framework.basescript.BaseTestScript;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.EmbedEvent;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.HookTestStep;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestRunStarted;
import io.cucumber.plugin.event.TestSourceRead;
import io.cucumber.plugin.event.TestStepFinished;
import io.cucumber.plugin.event.TestStepStarted;
import io.cucumber.plugin.event.WriteEvent;
import io.cucumber.testng.AbstractTestNGCucumberTests;

public class CustomReportListener extends AbstractTestNGCucumberTests implements EventListener {

	private ExtentSparkReporter spark;
	private ExtentReports extent;
	ExtentTest scenario;
	ExtentTest step;
	Map<String, ExtentTest> feature = new HashMap<String, ExtentTest>();

	public CustomReportListener() {
	}

	public CustomReportListener(String str) {
	}

	@Override
	public void setEventPublisher(EventPublisher publisher) {
		publisher.registerHandlerFor(TestRunStarted.class, this::runStarted);
		publisher.registerHandlerFor(TestRunFinished.class, this::runFinished);
		publisher.registerHandlerFor(TestSourceRead.class, this::featureRead);
		publisher.registerHandlerFor(TestCaseStarted.class, this::ScenarioStarted);
		publisher.registerHandlerFor(TestStepStarted.class, this::stepStarted);
		publisher.registerHandlerFor(TestStepFinished.class, this::stepFinished);
		publisher.registerHandlerFor(EmbedEvent.class, this::embedEventhandler);
		publisher.registerHandlerFor(WriteEvent.class, this::writeEventhandler);
		publisher.registerHandlerFor(EmbedEvent.class, this::stepEmbedded);
	};

	// Here we create the reporter
	private void runStarted(TestRunStarted event) {
		spark = new ExtentSparkReporter("./Reports/CucumberExtentReport.html");
		extent = new ExtentReports();
		spark.config().setTheme(Theme.STANDARD);
		// Create extent report instance with spark reporter
		extent.attachReporter(spark);
	};

	// TestRunFinished event is triggered when all feature file executions are
	// completed
	private void runFinished(TestRunFinished event) {
		extent.flush();
	};

	// This event is triggered when feature file is read
	// here we create the feature node
	private void featureRead(TestSourceRead event) {
		String featureSource = event.getUri().toString();
		String featureName = featureSource.split(".*/")[1];
		if (feature.get(featureSource) == null) {
			feature.putIfAbsent(featureSource, extent.createTest(featureName));
		}
	};

	// This event is triggered when Test Case is started
	// here we create the scenario node
	private void ScenarioStarted(TestCaseStarted event) {
		String featureName = event.getTestCase().getUri().toString();

		scenario = feature.get(featureName).createNode(event.getTestCase().getName());
	};

	// step started event
	// here we creates the test node
	private void stepStarted(TestStepStarted event) {

		String stepName = " ";
		String keyword = "Triggered the hook :";

		// We checks whether the event is from a hook or step
		if (event.getTestStep() instanceof PickleStepTestStep) {
			// TestStepStarted event implements PickleStepTestStep interface
			// WHich have additional methods to interact with the event object
			// So we have to cast TestCase object to get those methods
			PickleStepTestStep steps = (PickleStepTestStep) event.getTestStep();
			stepName = steps.getStep().getText();
			keyword = steps.getStep().getKeyword();

		} else {
			// Same with HoojTestStep
			HookTestStep hoo = (HookTestStep) event.getTestStep();
			stepName = hoo.getHookType().name();
		}

		step = scenario.createNode(Given.class, keyword + " " + stepName);
	};

	// This is triggered when TestStep is finished
	private void stepFinished(TestStepFinished event) {
		if (event.getResult().getStatus().toString() == "PASSED") {
			step.log(Status.PASS, "This passed");
		} else if (event.getResult().getStatus().toString() == "SKIPPED") {
			step.log(Status.SKIP, "This step was skipped ");
		} else {
			step.log(Status.FAIL, event.getTestCase().getName());
			attachedScreenshots(event);
		}
	};

	private void stepEmbedded(EmbedEvent event) {
		byte[] a = event.getData();

	};

	public void attachedScreenshots(TestStepFinished event) {
		String screen;
		try {
			screen = Screenshot.getScreenShotForReport(BaseTestScript.driver, event.getTestCase().getName());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		step.log(Status.FAIL, event.getResult().getError(), MediaEntityBuilder.createScreenCaptureFromPath(screen).build());
	}

	private void embedEventhandler(EmbedEvent embed) {

	}

	private void writeEventhandler(WriteEvent write) {

	}
}
