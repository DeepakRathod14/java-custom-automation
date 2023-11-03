package framework.pageobject.api.payment;

import framework.baseclass.apis.AbstractWebService;
import io.restassured.specification.RequestSpecification;

public class PaymentModule extends AbstractWebService{

	private static final String PAYMENT = "payment";
	private static final ThreadLocal<PaymentModule> WEB_API = new ThreadLocal<>();
	private static final ThreadLocal<PaymentModule> WEB_API_WITH_SWAGGER = new ThreadLocal<>();
	private CardPaymentEndpoint cardPayEndpoint = null;
	private OnlinePaymentEndpoint onlinePayEndpoint = null;
	private POSPaymentEndpoint pointOfSaleEndpoint = null;
	
	protected PaymentModule(boolean useSwagger) {
		super(PAYMENT, useSwagger);
	}
	
	public static PaymentModule getInstance() {
		if (WEB_API.get() == null) {
			initCorrelationApi(false);
		}
		return WEB_API.get();
	}
	
	public static void remove() {
		WEB_API.remove();
		WEB_API_WITH_SWAGGER.remove();
	}
	
	private static void initCorrelationApi(boolean useSwaggerValidationFilter) {
		if (!useSwaggerValidationFilter) {
			WEB_API.set(new PaymentModule(false));
		} else {
			WEB_API_WITH_SWAGGER.set(new PaymentModule(false));
		}
	}
	
	@Override
	protected void initEndPoints() {
		initUnversionedEndPoints();
	}

	private void initUnversionedEndPoints() {
		RequestSpecification specification = getDefaultSpecification();
		initOnlinePaymentEndpoint(specification);
		initCardPaymentEndpoint(specification);
		initPOSPaymentEndpoint(specification);
	}
	
	private void initOnlinePaymentEndpoint(RequestSpecification requestSpecification) {
		onlinePayEndpoint = new OnlinePaymentEndpoint(requestSpecification);
	}
	
	private void initCardPaymentEndpoint(RequestSpecification requestSpecification) {
		cardPayEndpoint = new CardPaymentEndpoint(requestSpecification);
	}
	
	private void initPOSPaymentEndpoint(RequestSpecification requestSpecification) {
		pointOfSaleEndpoint = new POSPaymentEndpoint(requestSpecification);
	}
	
	public CardPaymentEndpoint getCardPaymentGetway() {
		return cardPayEndpoint;
	}
	
	public OnlinePaymentEndpoint getOnlinePaymentGetway() {
		return onlinePayEndpoint;
	}
	
	public POSPaymentEndpoint getPosPaymentGetway() {
		return pointOfSaleEndpoint;
	}
}
