package framework.bdd.stepdef.ui;

import framework.pageobject.PracticeForm;
import framework.utilities.InitFactory;
import framework.utilities.Verify;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SampleStepDef{

	@Given("Open orelly home page")
	public void open_orelly_home_page() {
		PracticeForm form = (PracticeForm) InitFactory.getBasePageObject().detectPage();
		form.setFirstName("Deepak");
		form.setLastName("Rathod");
		form.setEmail("deepak.rathod3@gmail.com");
		form.selectMale();
		form.setMobile(9173668187l);
		Verify.assertStep(true, false, "Demo for Failuer");
	}
	
	@When("Login with valid username")
	public void login_with_valid_username() {
	}

	@Then("logout")
	public void logout() {
	}	
}
