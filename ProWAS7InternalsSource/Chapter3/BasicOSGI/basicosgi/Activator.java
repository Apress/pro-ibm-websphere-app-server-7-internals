package basicosgi;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		// Create the service and its metadata for the registry to use
		SimpleMaths service = new SimpleMathsImpl();
		Hashtable<String, String> metadata = new Hashtable<String, String>();
		metadata.put("Version", "1.0");		
		context.registerService(SimpleMaths.class.getName(), service, metadata);
	}
	
	public void stop(BundleContext context) throws Exception {
	}
}
