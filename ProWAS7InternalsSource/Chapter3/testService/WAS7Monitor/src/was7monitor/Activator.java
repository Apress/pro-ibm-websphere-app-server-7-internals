package was7monitor;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator 
extends Plugin 
implements IRegistryChangeListener, IJobChangeListener, BundleListener, ServiceListener {

	// The plug-in ID
	public static final String PLUGIN_ID = "WAS7Monitor";

	// The shared instance
	private static Activator plugin;
	private static BundleContext bundleContext;
	
	/**
	 * The constructor
	 */
	public Activator() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		bundleContext = context;
		System.out.println("WAS7Monitor:Bundle Activator start method called");		
		
		// Add a JobManager listener
		IJobManager manager = Platform.getJobManager();
		manager.addJobChangeListener(this);
		
		// Now output the OSGI artefact inforamtion		
		// Add a bundle listener
		context.addBundleListener(this);
		
		// Add a service listener
		context.addServiceListener(this);
		
		// Output the Eclipse runtime artefact information
		Utility.output(context, this);
		
		// Add a registry change listener
		// For pure Eclipse and not OSGI use Platform.getExtensionRegistry.
		// For the OSGI way of doing things, use a ServiceTracker.
		// With compatibility both should work.		
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		if (reg != null) {
			reg.addRegistryChangeListener(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		System.out.println("WAS7Monitor:Bundle Activator stop method called");	
		
		// Remove our extension registry listener
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		if (reg != null) {
			reg.removeRegistryChangeListener(this);
		}
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Call our utility method to output the registry change
	 * 
	 * @param event
	 */
	public void registryChanged(IRegistryChangeEvent event) {
		Utility.outputRegistryChange(event);
	}

	/**
	 * Call our utility method to output the job changes
	 * 
	 */
	public void aboutToRun(IJobChangeEvent event) {
		Utility.outputJobChange(event, " about to run");
	}

	public void awake(IJobChangeEvent event) {
		Utility.outputJobChange(event, " awake");
	}

	public void done(IJobChangeEvent event) {
		Utility.outputJobChange(event, " done");
	}

	public void running(IJobChangeEvent event) {
		Utility.outputJobChange(event, " running");
	}

	public void scheduled(IJobChangeEvent event) {
		Utility.outputJobChange(event, " scheduled");		
	}

	public void sleeping(IJobChangeEvent event) {
		Utility.outputJobChange(event, " sleeping");
	}

	/**
	 * Call our utility method to output the bundle changes
	 * 
	 * @param event
	 */
	public void bundleChanged(BundleEvent event) {
		Utility.outputBundleChange(event);
	}

	/**
	 * Call our utility method to output the service changes
	 * 
	 * @param event
	 */
	public void serviceChanged(ServiceEvent event) {
		Utility.outputServiceChange(bundleContext, event);
	}
}
