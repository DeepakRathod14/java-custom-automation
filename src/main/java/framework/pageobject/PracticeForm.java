package framework.pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import framework.baseclass.BasePageObject;

public class PracticeForm<T> extends BasePageObject<T>{

	public PracticeForm(WebDriver driver) {
		super(driver);
	}

	public PracticeForm setFirstName(String fName) {
		sendKeys(By.id("firstName"), fName);
		return this;
	}
	
	public PracticeForm setLastName(String lName) {
		sendKeys(By.id("lastName"), lName);
		return this;
	}
	
	public PracticeForm setEmail(String email) {
		sendKeys(By.id("userEmail"), email);
		return this;
	}
	
	public PracticeForm selectMale() {
		click(By.xpath("//label[text()='Male']"));
		return this;
	}

	public PracticeForm setMobile(long mobileNum) {
		sendKeys(By.id("//input[@id='userNumber']"), String.valueOf(mobileNum));
		return this;
	}
	
	public BasePageObject<T> clickOnSubmitBtn(){
		click(By.id("userEmail"));
		return detectPage();
	}
}
