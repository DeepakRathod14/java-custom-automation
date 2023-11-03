package framework.pageobject.api.payment;


import framework.baseclass.apis.AbstractWebEndpoint;
import io.restassured.specification.RequestSpecification;

public class CardPaymentEndpoint extends AbstractWebEndpoint{

	public CardPaymentEndpoint(RequestSpecification requestSpecification) {
		super(requestSpecification);
	}

	public CardPaymentEndpoint getAllAFeature() {
		// This is getAPI
		return this;
	}

	public CardPaymentEndpoint createAFeature() {
		return this;
	}

	public CardPaymentEndpoint updateAFeature() {
		return this;
	}

	public CardPaymentEndpoint deleteAFeature() {
		return this;
	}
}
