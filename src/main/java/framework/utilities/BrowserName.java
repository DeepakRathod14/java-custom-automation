package framework.utilities;

public enum BrowserName {

	CHROME("chrome"),
	FF("firefox"),
	IE("ie"),
	HEADLESS("htmlUnitDriver");

	private String browser;

	BrowserName(String browser) {
		this.browser = browser;
	}

	public String getValue() {
		return this.browser;
	}
}
