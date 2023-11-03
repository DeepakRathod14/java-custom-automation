package framework.pageobject.api.correlation;

public class Correlation {

	private static Correlation ce=null;
	
	private AFeature afeature = null;
	private BFeature bfeature = null;
	
	private Correlation() {
		initObjects();
	}
	
	public static Correlation getInstance() {
		if(ce==null) {
			ce = new Correlation();
		}
		return ce;
	}
	
	private void initObjects() {
		
	}
	
	public AFeature getAFeature() {
		return afeature;
	}
	
	public BFeature getBFeature() {
		return bfeature;
	}
}
