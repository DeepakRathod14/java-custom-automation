package framework.pageobject.api.payment;


import framework.baseclass.apis.AbstractWebEndpoint;
import framework.bean.users.get.userall.UserAllDto;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
public class OnlinePaymentEndpoint extends AbstractWebEndpoint{
	
	private static final String ONLINE ="/online";
	private static final String ONLINE_PRAM =ONLINE+"{ID}";
	
	public OnlinePaymentEndpoint(RequestSpecification requestSpecification) {
		super(requestSpecification);
	}

	public UserAllDto getAllUsersData1() {
		System.out.println(getResponse(ONLINE).then().log().all());
		return getResponse(ONLINE).as(UserAllDto.class);
	}
	
	public UserAllDto getAllUsersData() {
		ValidatableResponse validatableResponse = get(ONLINE);
		UserAllDto responseAsDto = extractAsDto(validatableResponse, UserAllDto.class);
        return responseAsDto;
	}

	public ValidatableResponse getUser1(String userID) {
		return get(ONLINE_PRAM, userID);
	}

	
	public UserAllDto getUser(int userID) {
		ValidatableResponse validatableResponse = get(ONLINE_PRAM, userID);
		UserAllDto responseAsDto = extractAsDto(validatableResponse, UserAllDto.class);
        return responseAsDto;
	}
	
	
//	public ValidatableResponse createBFeature() { 
//		//return given().get(baseURI+CREATE).then();
//	}
	
	public OnlinePaymentEndpoint updateBFeature() {
		return this;
	}
	
	public OnlinePaymentEndpoint deleteBFeature() {
		return this;
	}
	
	
}
