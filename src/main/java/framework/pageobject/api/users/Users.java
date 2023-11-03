package framework.pageobject.api.users;

import framework.baseclass.apis.AbstractWebService;
import framework.pageobject.api.correlation.AFeature;
import framework.pageobject.api.correlation.BFeature;
import io.restassured.specification.RequestSpecification;

public class Users extends AbstractWebService {
	private static final String USERS = "users";
	private static final ThreadLocal<Users> WEB_API = new ThreadLocal<>();
	private static final ThreadLocal<Users> WEB_API_WITH_SWAGGER = new ThreadLocal<>();
	private AFeature afeature;
	private BFeature bfeature;

	protected Users(boolean withSwagger) {
		super(USERS, withSwagger);
	}

	public static Users UsersApi() {
		if (WEB_API.get() == null) {
			initCorrelationApi(false);
		}
		return WEB_API.get();
	}

	private static void initCorrelationApi(boolean useSwaggerValidationFilter) {
		if (!useSwaggerValidationFilter) {
			WEB_API.set(new Users(false));
		} else {
			WEB_API_WITH_SWAGGER.set(new Users(false));
		}
	}

	public static void remove() {
		WEB_API.remove();
		WEB_API_WITH_SWAGGER.remove();
	}

	@Override
	protected void initEndPoints() {
		initUnversionedEndPoints();
	}

	private void initUnversionedEndPoints() {
		RequestSpecification specification = getDefaultSpecification();
		initAFeature(specification);
		initBFeature(specification);
	}

	private void initAFeature(RequestSpecification requestSpecification) {
		afeature = new AFeature(requestSpecification);
	}

	private void initBFeature(RequestSpecification requestSpecification) {
		bfeature = new BFeature(requestSpecification);
	}

	public AFeature getAFeature() {
		return afeature;
	}

	public BFeature getBFeature() {
		return bfeature;
	}

}
