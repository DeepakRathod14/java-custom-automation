package framework.pageobject.api.correlation;

import framework.baseclass.apis.AbstractWebEndpoint;
import io.restassured.specification.RequestSpecification;

public class AFeature extends AbstractWebEndpoint{

	public AFeature(RequestSpecification requestSpecification) {
		super(requestSpecification);
	}

	public AFeature getAllAFeature() {
		// This is getAPI
		return this;
	}

	public AFeature createAFeature() {
		return this;
	}

	public AFeature updateAFeature() {
		return this;
	}

	public AFeature deleteAFeature() {
		return this;
	}
}
