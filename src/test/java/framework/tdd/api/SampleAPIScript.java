package framework.tdd.api;

import framework.basescript.BaseTestScript;
import org.testng.annotations.Test;
import framework.bean.users.get.userall.UserAllDto;
import framework.pageobject.api.users.Users;

public class SampleAPIScript extends BaseTestScript {

	@Test
	public void scen01() {
		//ValidatableResponse res = Users.UsersApi().getBFeature().getAllUsersData1();
		//System.out.println(res.statusCode(200));
		printReportLog("Scenario-1 execution Start");
		printReportLog("Scenario-1 execution End");
		printReportLog("Thread Detail : "+Thread.currentThread().getId());
		
		
		UserAllDto user = Users.UsersApi().getBFeature().getAllUsersData1();
		//TimeEntry.getTimeEntryApi().feature().addEntry()/update/delete/etc..
		System.out.println(user.getPage());
		System.out.println(user.getPerPage());
	}
	
	public void scen02() {
//		Correlation.getInstance()
//			.getBFeature()
//			.createBFeature().body(Matchers.containsString("abc"));
//		
//		Correlation.getInstance()
//			.getBFeature()
//			.getAllBFeature().statusCode(200);
//		
//		Correlation.getInstance().getBFeature().getBFeature("abc").statusCode(404);

	}
}
