package framework.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

public class Verify {
	
	private static Logger log = LogManager.getLogger(Verify.class);
	
	public static void assertStep(boolean actual, boolean expected, String message) {
		log.info("Verify : "+message);
		if(actual!=expected) { //If actual & expected not match
			log.info("===================================================");
			log.info("Actual and expected value is not match : "+false);
			log.info("===================================================");
			Assert.assertTrue(false); //It mark scenario as fail with help of TestNG
		}
		log.info("verify successfully...!");
		Assert.assertEquals(actual, expected); //verify successfully
	}
	
	public static void assertTrue(boolean actual, String message) {
		Assert.assertTrue(false, message);	
	}
	
}
