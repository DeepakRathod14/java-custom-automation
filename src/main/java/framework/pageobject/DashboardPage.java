package framework.pageobject;

import framework.baseclass.BasePageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends BasePageObject {
    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean verifyLeftNav(){
        return driver.findElement(By.xpath("//div[@class='strip_nav_container']")).isDisplayed();
    }
}
