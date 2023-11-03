package framework.pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import framework.baseclass.BasePageObject;

public class LoginPage extends BasePageObject{

	public LoginPage(WebDriver driver) {
		super(driver);
	}
	

	public LoginPage doLoing(String username, String password) {
		sendKeys(By.id("Email"), username);
		sendKeys(By.id("Password"), password);
		click(By.id("btnSignIn"));
		sleep(1);
		return this;
	}

	public BasePageObject forgotPassword(){
		click(By.id("btnForgot"));
		sleep(1);
		return detectPage();
	}

	public String verifyErrorMessage(){
		return getText(By.id("lblMessage"));
	}
}
