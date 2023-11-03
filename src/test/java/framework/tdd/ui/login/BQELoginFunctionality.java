package framework.tdd.ui.login;

import framework.basescript.BaseTestScript;
import framework.pageobject.DashboardPage;
import framework.pageobject.LoginPage;
import framework.utilities.InitFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BQELoginFunctionality extends BaseTestScript {

    @Test(priority = 1)
    public void nagativeScenario(){
        printReportLog("Browser open, Url load & verify login page should open");
        LoginPage loginPage = (LoginPage) InitFactory.getBasePageObject().detectPage();
        printReportLog("BQE Login is detected and start login");
        String errorMsg = loginPage.doLoing("testing@qa.com", "abc")
                .verifyErrorMessage();
        printReportLog("attempt invalid login and start validating");
        Assert.assertEquals(errorMsg,"Your login attempt was not successful. Please try again");
        printReportLog("verify invalid login scenario");
    }

    //@Test(priority = 2)
    public void positiveScenario(){
        printReportLog("Browser open, Url load & verify login page should open");
        LoginPage loginPage = (LoginPage) InitFactory.getBasePageObject().detectPage();
        printReportLog("BQE Login is detected and start login");
        loginPage.doLoing(username, password);
        printReportLog("BQE login is successfully and verify lending page");
        DashboardPage dashboardPage = (DashboardPage) InitFactory.getBasePageObject().detectPage();
        Assert.assertTrue(dashboardPage.verifyLeftNav());
        printReportLog("verify valid login scenario");
    }
}
