package framework.pageobject.api.payment;

import framework.baseclass.apis.AbstractWebEndpoint;
import io.restassured.specification.RequestSpecification;

public class POSPaymentEndpoint extends AbstractWebEndpoint{

	private static final String POI ="/poi";
	private static final String POI_PARAM =POI+"{ID}";
	
	public POSPaymentEndpoint(RequestSpecification requestSpecification) {
		super(requestSpecification);
	}
}
