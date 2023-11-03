package framework.baseclass;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.aventstack.extentreports.model.Report;
import framework.pageobject.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;

import org.testng.Reporter;

/**
 * BasePageObject is parent class of UI page objects.
 * This page contains all generic methods of UI action and initialized page objects
 * @param <T>
 */
public class BasePageObject<T> {

	ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
	protected WebDriver driver;
	private static Logger log = LogManager.getLogger(BasePageObject.class);// Log4j2
	private JavascriptExecutor scriptExecutor;
	protected Actions build;
	private Map<String, BasePageObject<T>> mapper = new ConcurrentHashMap<String, BasePageObject<T>>();
	
	public BasePageObject(WebDriver driver) {
		log.info("BasePageObject constructor is invoke with driver copy "+driver);
		tlDriver.set(driver);
		this.driver = driver;
		this.build = new Actions(this.driver);
	}
	
	@SuppressWarnings("unchecked")
	public void initMapping() {
		log.info("Initialized Mapping of urls against its pageObject");
		mapper.put("/webapp/", PageFactory.initElements(driver, LoginPage.class));
		mapper.put("automation-practice-form", PageFactory.initElements(driver, PracticeForm.class));
		mapper.put("/webapp/Account/ResetPassword",PageFactory.initElements(driver, ForgotPassword.class));
		mapper.put("/webapp/home/Dashboard", PageFactory.initElements(driver, DashboardPage.class));
	}
	
	public BasePageObject<T> detectPage() {
        log.info(driver.getCurrentUrl());
        String url = driver.getCurrentUrl(); //https://www.toolsqa.com/123-practice-form?dfksfk/fsdakfajs
        String key = mapper.keySet().stream()
        		.filter(partialKey -> url.endsWith(partialKey))
        		.findFirst()
        		.orElseGet(()-> "Mapping Not Found");
        return mapper.getOrDefault(key, PageFactory.initElements(driver, PageNotFound.class));
    }

	public BasePageObject click(By by){
		try {
			WebElement element = getHighlightElement(by);
			focusOnElement(element);
			element.click();
		}catch (Exception e){
			log.error(e);
			Reporter.log(e.getMessage());
		}
		return this;
	}

	public BasePageObject sendKeys(By by, String value){
		try {
			WebElement element = getHighlightElement(by);
			focusOnElement(element);
			element.clear();
			element.sendKeys(value);
		}catch (Exception e){
			log.error(e);
			Reporter.log(e.getMessage());
		}
		return this;
	}

	public String getText(By by){
		try{
			WebElement element = getHighlightElement(by);
			focusOnElement(element);
			return element.getText();
		}catch (Exception e){
			log.error(e);
			Reporter.log(e.getMessage());
		}
		return "";
	}
	public void focusOnElement(WebElement element) {
		build.moveToElement(element).build().perform();
	}
	
	/**
	 * Redefined method of Selenium which will vertical scroll to page. This is
	 * more stable Redefined method of CMEWebElement.
	 *
	 * @author Deepak.Rathod
	 * @param verticalScroll the vertical scroll
	 * @category BQE-WebElement
	 */
	public void scroll(int verticalScroll) {
		scriptExecutor.executeScript("window.scrollBy(0," + verticalScroll + ")");
	}

	/**
	 * Redefined method of Selenium which will scroll to bottom of the page. This is
	 * more stable Redefined method of CMEWebElement.
	 *
	 * @author Deepak.Rathod
	 * @category BQE-WebElement
	 */
	public void scrollToBottom()
	{
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

	/**
	 * Redefined method of Selenium which will scroll to Top of the page. This is
	 * more stable Redefined method of CMEWebElement.
	 *
	 * @author Deepak.Rathod
	 * @category BQE-WebElement
	 */
	public void scrollToTop()
	{
		Boolean vertscrollStatus = (Boolean) scriptExecutor.executeScript("return document.documentElement.scrollHeight>document.documentElement.clientHeight;");
		if (vertscrollStatus)
		{
			driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.HOME);
		}
		else
		{
			scriptExecutor.executeScript("scrollBy(0, -1000)");
		}
	}

	/**
	 * Redefined method of Selenium which will scroll to particular element if
	 * that element hide under scroll. This is more stable Redefined method of
	 * CMEWebElement.
	 *
	 * @author Deepak.Rathod
	 * @param by the by
	 * @category BQE-WebElement
	 */
	public void scrollToElement(By by) {
		try
		{
			WebElement element = getHighlightElement(by);
			scriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
		}
		catch (NoSuchElementException e)
		{
			e.printStackTrace();
			//Verify.assertTrue(false, "Fail to ScrollToElement on windiw : "+by+" On Page : " + e.getMessage());
			//Needs to check why Verify class from test is not visible here.
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
		}
	}

	
	/**
	 * Redefined method of Selenium which will highlight webElement by
	 * javaScriptExecutor, so we can get see on screen which element is going to
	 * perform This is more stable.
	 *
	 * @author Deepak.Rathod
	 * @param web Element Object the by
	 * @return the highlight element
	 * @category BQE-WebElement
	 */
	public WebElement getHighlightElement(final By ele) {
		WebElement element=null;
		try
		{
			Wait<WebDriver> wait = new FluentWait<>(driver)
					.pollingEvery(Duration.ofMillis(600))
					.ignoring(NoSuchElementException.class);
			element = wait.until((Function<WebDriver, WebElement>) driver -> driver.findElement(ele));
			// Element is now available for further use.
			((JavascriptExecutor) driver)
			.executeScript("arguments[0].style.border='2px solid red'", element);
		}
		catch (Exception e)
		{
			log.info(e.getMessage());
		}
		return element;
	}

	public BasePageObject sleep(int timeInSec){
		try {
			Thread.sleep(1000*timeInSec);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return this;
	}
}
