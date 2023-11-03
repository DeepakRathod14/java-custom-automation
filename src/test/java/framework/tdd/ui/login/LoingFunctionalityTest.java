package framework.tdd.ui.login;

import org.testng.annotations.Test;

import framework.basescript.BaseTestScript;
import framework.pageobject.PracticeForm;
import framework.utilities.InitFactory;
import framework.utilities.Verify;

public class LoingFunctionalityTest extends BaseTestScript {

	@Test
	public void scenario01() {
		printReportLog("Scenario-1 execution Start");
		printReportLog("Scenario-1 execution End");
		printReportLog("Thread Detail : "+Thread.currentThread().getId());
		PracticeForm form = (PracticeForm) InitFactory.getBasePageObject().detectPage();
		form.setFirstName("Deepak")
				.setLastName("Rathod")
				.setEmail("deepak.rathod3@gmail.com")
				.selectMale()
				.setMobile(9173668187l);
		//Verify.assertStep(true, false, "Demo for Failuer");
		softAssert.assertThat(10).isGreaterThan(11);
	}
	
	@Test(dependsOnMethods = {"scenario01"})
	public void scenario02() {
		printReportLog("Scenario-2 execution Start");
		printReportLog("Scenario-2 execution End");
		printReportLog("Thread Detail : "+Thread.currentThread().getId());
		Verify.assertStep(true, false, "Demo for Failure");
	}

	@Test
	public void scenario03() {
		printReportLog("Scenario-3 execution Start");
		printReportLog("Scenario-3 execution End");
		printReportLog("Thread Detail : "+Thread.currentThread().getId());
		Verify.assertStep(true, true, "Demo for Success");
	}

}
