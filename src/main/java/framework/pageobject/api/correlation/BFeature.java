package framework.pageobject.api.correlation;

import framework.baseclass.apis.AbstractWebEndpoint;
import framework.bean.users.get.userall.UserAllDto;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
public class BFeature extends AbstractWebEndpoint{
	
	private static final String USERS ="/users";
	private static final String USERS_PRAM =USERS+"{ID}";
	
	public BFeature(RequestSpecification requestSpecification) {
		super(requestSpecification);
	}

	public UserAllDto getAllUsersData1() {
		System.out.println(getResponse(USERS).then().log().all());
		return getResponse(USERS).as(UserAllDto.class);
	}
	
	public UserAllDto getAllUsersData() {
		ValidatableResponse validatableResponse = get(USERS);
		UserAllDto responseAsDto = extractAsDto(validatableResponse, UserAllDto.class);
        return responseAsDto.sorted();
	}

	public ValidatableResponse getUser1(String userID) {
		return get(USERS_PRAM, userID);
	}

	
	public UserAllDto getUser(int userID) {
		ValidatableResponse validatableResponse = get(USERS_PRAM, userID);
		UserAllDto responseAsDto = extractAsDto(validatableResponse, UserAllDto.class);
        return responseAsDto.sorted();
	}
	
	
//	public ValidatableResponse createBFeature() { 
//		//return given().get(baseURI+CREATE).then();
//	}
	
	public BFeature updateBFeature() {
		return this;
	}
	
	public BFeature deleteBFeature() {
		return this;
	}
	
	
}
