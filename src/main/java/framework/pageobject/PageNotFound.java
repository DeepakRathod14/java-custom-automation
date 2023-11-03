package framework.pageobject;

import framework.baseclass.BasePageObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class PageNotFound extends BasePageObject {

    public PageNotFound(WebDriver driver) {
        super(driver);
        logger.error("Given Page object is not found in repository");
        System.err.println("Given Page object is not found in repository");
    }

    private static Logger logger =  LogManager.getLogger((PageNotFound.class));
}
