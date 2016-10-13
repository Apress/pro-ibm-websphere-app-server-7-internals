package was7monitor;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;



/**
 * Utility class to output the Eclipse environment details to a log
 * 
 * @author colinrenouf
 *
 */
public class Utility {
	
	/**
	 * Output the extensions, extension points, and bundle details to a log
	 * 
	 * @param context
	 */
	@SuppressWarnings("unchecked")
	public static void output(BundleContext context, IRegistryChangeListener regListener) {
		// Get the details of the extension registry and enumerate it, outputting to a og
		try {
			FileWriter outlog = new FileWriter("/tmp/logs/WASMonitorStartup."  
					+ new Date().toString() + ".log", true);
			outlog.write("Initial Output: Getting Platform Registry.\n");
			outlog.flush();
			
			// For pure Eclipse and not OSGI use Platform.getExtensionRegistry.
			// For the OSGI way of doing things, use a ServiceTracker.
			// With compatibility both should work.
			IExtensionRegistry reg = Platform.getExtensionRegistry();				
			if (reg != null) {
				outlog.write("Registry is: " + reg.toString() + "\n");
				outlog.flush();
				for (IExtensionPoint point: reg.getExtensionPoints()) {
					if ((point.getUniqueIdentifier() != null) 
							&& !point.getUniqueIdentifier().toLowerCase().equals("null")) {
						outlog.write("ID: " + point.getUniqueIdentifier() + "\n");
					}
					
					for (IExtension ext: point.getExtensions()) {
						if ((ext.getUniqueIdentifier() != null) &&
								!ext.getUniqueIdentifier().toLowerCase().equals("null")) {
							outlog.write("-->Extension:" + ext.getUniqueIdentifier() + "\n");
							for (IConfigurationElement element: ext.getConfigurationElements()) {
								try {
									Object obj = element.createExecutableExtension("class");
									if (obj != null) {									
										Class clazz = obj.getClass();
										if (clazz != null) {
											outlog.write("\t--->Class: " + clazz.getName() + "\n");
											for (Method method: clazz.getMethods()) {
												if (method != null) {
													outlog.write("\t----->Method: " + method.toString() + "\n");
												}
											}
										}
									}
								} catch (CoreException e) {
									outlog.write("Could not dynamically create an extension for " 
											+ point.getUniqueIdentifier() + "\n");
								}
							}							
						}
					}
				}

				// Make sure that things get written
				outlog.flush();
			
				// Enumerate the bundles
				Bundle bundles[] = context.getBundles();
				for (Bundle bundle: bundles)  {
					outlog.write("Bundle Name: " 
						+ ((bundle.getSymbolicName() != null) ? bundle.getSymbolicName() : "null")   
						+ ", ID: " 
						+ bundle.getBundleId() 
						+ ", State: " 
						+ bundle.getState()
						+ "\n");
				
					// If there are fragments for this bundle then output this
					Bundle fragments[] = Platform.getFragments(bundle);
					if (null != fragments) {
						for (Bundle fragment: fragments)  {
							outlog.write("Fragment Bundle Name: " 
								+ ((fragment.getSymbolicName() != null) ? fragment.getSymbolicName() : "null")   
								+ ", ID: " 
								+ fragment.getBundleId() 
								+ ", State: " 
								+ fragment.getState()
								+ "\n");
						}		
					}
				
					// Make sure everything gets written
					outlog.flush();
								
					// Output the service registry information for the bundle
					if (null != bundle.getRegisteredServices()) {
						for (ServiceReference sr: bundle.getRegisteredServices()) {
							if (sr != null) {
								outlog.write("\t-->Service Reference:" + sr.toString() + "\n");
								if (sr.getUsingBundles() != null) {
									for (Bundle b: sr.getUsingBundles()) {
										outlog.write("\t---->Used By: "
												+ ((b.getSymbolicName() != null) 
														? b.getSymbolicName() : "null") + "\n");
									}
								}
								// For the service get the class representing the object
								// and use reflection to enumerate it
								Object obj = context.getService(sr);
								if (obj != null) {									
									Class clazz = obj.getClass();
									if (clazz != null) {
										outlog.write("\t--->Class: " + clazz.getName() + "\n");
										for (Method method: clazz.getMethods()) {
											if (method != null) {
												outlog.write("\t----->Method: " + method.toString() + "\n");
											}
										}
									}
								}					
								context.ungetService(sr);
								outlog.flush();
							}
						}
					}
				}
			} else {
					outlog.write("Registry is null.\n");					
			}
			
			// Make sure everything gets properly written
			outlog.flush();
			outlog.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Output the registry changes to a log
	 * 
	 * @param event
	 */
	public static void outputRegistryChange(IRegistryChangeEvent event) {
		// Get the details of the extension registry change and output them
		try {
			FileWriter outlog = new FileWriter("/tmp/logs/WASMonitorRegistryChanges." 
					+ new Date().toString() + ".log", true);
			outlog.write("Registry change at: " + new Date().toString() + "\n");
			outlog.flush();
			IExtensionDelta[] deltas = event.getExtensionDeltas();
			for (int i = 0; i < deltas.length; i++) {
				if (deltas[i].getKind() == IExtensionDelta.ADDED) {
					outlog.write("Extension " + deltas[i].getExtension().getUniqueIdentifier() + "Added.\n");
				} else {
					outlog.write("Extension " + deltas[i].getExtension().getUniqueIdentifier() + "Removed.\n");			
				}			
				outlog.flush();
			}			

			// Make sure that things get written
			outlog.flush();			
			outlog.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Output the job changes to a log
	 * 
	 * @param event
	 */
	public static void outputJobChange(IJobChangeEvent event, String change) {
		// Get the details of the job change and output it
		try {
			FileWriter outlog = new FileWriter("/tmp/logs/WASMonitorJobChanges."
					+ new Date().toString() + ".log", true);
			outlog.write("Job change at: " + new Date().toString() + "\n");
			Job job = event.getJob();
			outlog.write("Job " + job.getName() + change + ".\n");
			
			// Make sure that things get written
			outlog.flush();			
			outlog.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}		
	}
	
	/**
	 * Output the bundle changes to a log
	 */
	public static void outputBundleChange(BundleEvent event) {
		// Get the details of the bundle change and output it
		try {
			FileWriter outlog = new FileWriter("/tmp/logs/WASMonitorBundleChanges." 
					+ new Date().toString() + ".log", true);
			outlog.write("Bundle change at: " + new Date().toString() + "\n");
			outlog.write("Bundle: " 
					+ ((event.getBundle().getSymbolicName() != null) 
							? event.getBundle().getSymbolicName() : "null")
					+ ", ID: " + event.getBundle().getBundleId());			
			switch (event.getType()) {
				case BundleEvent.INSTALLED: 
					outlog.write(" INSTALLED.\n");
					break;
					
				case BundleEvent.RESOLVED:
					outlog.write(" RESOLVED.\n");
					break;
					
				case BundleEvent.STARTED:
					outlog.write(" STARTED.\n");
					break;
					
				case BundleEvent.STARTING:
					outlog.write(" STARTING.\n");
					break;
					
				case BundleEvent.STOPPED:
					outlog.write(" STOPPED.\n");
					break;
					
				case BundleEvent.STOPPING:
					outlog.write(" STOPPING.\n");
					break;
					
				case BundleEvent.UNINSTALLED:
					outlog.write(" UNINSTALLED.\n");
					break;
					
				case BundleEvent.UNRESOLVED:
					outlog.write(" UNRESOLVED.\n");
					break;
					
				case BundleEvent.UPDATED:
					outlog.write(" UPDATED.\n");
					break;
			}			

			// Make sure that things get written
			outlog.flush();			
			outlog.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}		
	}
	
	/**
	 * Output the service changes to a log
	 */
	public static void outputServiceChange(BundleContext context, ServiceEvent event) {
		// Get the details of the bundle change and output it
		try {
			FileWriter outlog = new FileWriter("/tmp/logs/WASMonitorServiceChanges." 
					+ new Date().toString() + ".log", true);
			outlog.write("Service change at: " + new Date().toString() + "\n");
			ServiceReference sr = event.getServiceReference();
			if (sr != null) {
				outlog.write("Service Reference: " + sr.toString() 
						+ ", Bundle: " 
						+ ((sr.getBundle().getSymbolicName() != null) 
								? sr.getBundle().getSymbolicName() : "null")					
								+ ", Bundle ID: " 
								+ sr.getBundle().getBundleId());

				switch (event.getType()) {
				case ServiceEvent.MODIFIED: 
					outlog.write(" MODIFIED.\n");
					break;

				case ServiceEvent.REGISTERED:
					outlog.write(" REGISTERED.\n");
					break;

				case ServiceEvent.UNREGISTERING:
					outlog.write(" UNREGISTERING.\n");
					break;
				}			

				if (sr.getUsingBundles() != null) {
					for (Bundle b: sr.getUsingBundles()) {
						outlog.write("\t---->Used By: "
								+ ((b.getSymbolicName() != null) 
										? b.getSymbolicName() : "null") + "\n");
					}
				}
				
				// For the service get the class representing the object
				// and use reflection to enumerate it
				Object obj = context.getService(sr);
				if (obj != null) {									
					Class clazz = obj.getClass();
					if (clazz != null) {
						outlog.write("\t--->Class: " + clazz.getName() + "\n");
						for (Method method: clazz.getMethods()) {
							if (method != null) {
								outlog.write("\t----->Method: " + method.toString() + "\n");
							}
						}
					}
				}					
				context.ungetService(sr);
			}
			// Make sure that things get written
			outlog.flush();			
			outlog.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}		
	}
}
