package framework.tdd.ui.login;

import org.testng.annotations.Test;
import framework.basescript.BaseTestScript;

public class LoingFunctionalityTest2 extends BaseTestScript {

	@Test
	public void sampleScenario() {
		printReportLog("Scenario-1 execution Start");
		printReportLog("Scenario-1 execution End");
		printReportLog("Thread Detail : "+Thread.currentThread().getId());
		softAssert.assertThat(true).isFalse();
		//softAssert.assertAll();
	}
	
	@Test
	public void scenario02() {
		printReportLog("Scenario-2 execution Start");
		printReportLog("Scenario-2 execution End");
		printReportLog("Thread Detail : "+Thread.currentThread().getId());
	}

	//@Test
	public void scenario03() {
		printReportLog("Scenario-3 execution Start");
		printReportLog("Scenario-3 execution End");
		printReportLog("Thread Detail : "+Thread.currentThread().getId());
		//Verify.assertStep(true, false, "Scenario-3 should match");
	}

}
