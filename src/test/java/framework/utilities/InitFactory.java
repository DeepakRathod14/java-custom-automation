package framework.utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import framework.baseclass.BasePageObject;

public class InitFactory {

	private static BasePageObject basePageObject;
	private static Screenshot screenshot;
	private static InitFactory factory;
	private static WebDriver driver;
	
	private InitFactory(WebDriver driver) {
		initObjects(driver);
	}

	public static void setDriver(WebDriver driver) {
		InitFactory.driver = driver;
		getInstance();
	}
	/**
	 * Driver copy 
	 * Current Page Object (InitFactory Object)
	 * constructor call
	 * initObjects(driver);
	 * It will create each pageobject
	 * @return
	 */
	
	private static InitFactory getInstance() {
		if(factory==null) {
			factory = (new InitFactory(InitFactory.driver));
		}
		return factory;
	}
	
	public InitFactory initObjects(WebDriver driver) {
		initBasePaseObject(driver);
		initScreenShotObject(driver);
		return this;
	}

	private InitFactory initBasePaseObject(WebDriver driver) {
		basePageObject = PageFactory.initElements(driver, BasePageObject.class);
		return this;
	}
	
	private InitFactory initScreenShotObject(WebDriver driver) {
		screenshot = PageFactory.initElements(driver, Screenshot.class);
		return this;
	}

	
	public static BasePageObject getBasePageObject() {
		return basePageObject;
	}
	
	public static Screenshot getScreenShotObject() {
		return screenshot;
	}

}
