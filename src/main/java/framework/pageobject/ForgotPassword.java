package framework.pageobject;

import framework.baseclass.BasePageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ForgotPassword extends BasePageObject {
    public ForgotPassword(WebDriver driver) {
        super(driver);
    }

    public ForgotPassword setEmailID(String email){
        sendKeys(By.id("email"), email);
        return this;
    }

    public ForgotPassword selectCaptch(){
        click(By.id("recaptcha-anchor"));
        return this;
    }

    public BasePageObject clickOnContinue(){
        click(By.id("btnContinue"));
        return this;
    }

    public boolean verifyForgotPasswordPage(){
        return driver.findElement(By.id("email")).isDisplayed();
    }
}
