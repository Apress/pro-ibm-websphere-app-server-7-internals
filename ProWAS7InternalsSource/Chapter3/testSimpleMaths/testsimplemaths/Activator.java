package testsimplemaths;

import java.math.BigDecimal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import basicosgi.SimpleMaths;

public class Activator implements BundleActivator {

	private SimpleMaths service;
	private ServiceTracker BigDecimalServiceTracker;
	
	public void start(BundleContext context) throws Exception {
		// Create a tracker and track the service
		ServiceTracker SimpleMathsServiceTracker = new ServiceTracker(context, SimpleMaths.class.getName(), null);
		SimpleMathsServiceTracker.open();
		
		// grab the service
		service = (SimpleMaths) SimpleMathsServiceTracker.getService();
		BigDecimal result = service.add(new BigDecimal(5.1), new BigDecimal(5.3));
		System.out.println("The result is " + result.toString());
	}
	
	public void stop(BundleContext context) throws Exception {
		// Close the service tracker
		BigDecimalServiceTracker.close();
		BigDecimalServiceTracker = null;		
		service = null;
	}
}
