package mywas7component;

import com.ibm.ejs.ras.Tr;
import com.ibm.ejs.ras.TraceComponent;
import com.ibm.ws.exception.ComponentDisabledException;
import com.ibm.ws.exception.ConfigurationError;
import com.ibm.ws.exception.ConfigurationWarning;
import com.ibm.ws.exception.RuntimeError;
import com.ibm.ws.exception.RuntimeWarning;
import com.ibm.wsspi.runtime.component.WsComponentImpl;
import com.ibm.wsspi.runtime.service.WsServiceRegistry;

@SuppressWarnings("deprecation")
public class MyComponent extends WsComponentImpl 
implements MyComponentInterface {
	
    private static final String MyComponent = "mywas7component/MyComponent";
	private static final String MyComponentInterface = "mywas7component/MyComponentInterface";
	private static TraceComponent tc = Tr.register(MyComponent);
		
	// Our service interface method declared in the MyComponentInterface Service Interface File
	public void sayHello() {
		System.out.println("Hello World!");
	}

	// All methods below are from the WsComponentImpl class.
	// Typically, all WAS components and services use the TraceComponent facilities to 
	// declare their entry and exit points for use when tracing is enabled inside WAS.
	public void destroy() {
		if(tc.isEntryEnabled()) {
            Tr.entry(tc, "destroy");
		}
		
		System.out.println("Destroying MyComponent");
		
		if(tc.isEntryEnabled()) {
            Tr.exit(tc, "destroy");
		}
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getState() {
		// TODO Auto-generated method stub
		return null;
	}

	// Components are initialised before use
	public void initialize(Object arg0) throws ComponentDisabledException,
			ConfigurationWarning, ConfigurationError {
		if(tc.isEntryEnabled()) {
            Tr.entry(tc, "initialize");
		}
		
		System.out.println("Initialising MyComponent and adding service");
		
		// To make our Service interface available to other callers we use the 
		// addService method of the WAS Service Registry to declare this object 
		// as providing the given interface.
		try {
			WsServiceRegistry.addService(this, Class.forName(MyComponentInterface));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(tc.isEntryEnabled()) {
            Tr.exit(tc, "initialize");
		}
	}

	public void start() throws RuntimeError, RuntimeWarning {
		if(tc.isEntryEnabled()) {
            Tr.entry(tc, "start");
		}
		
		System.out.println("Starting MyComponent");
		
		if(tc.isEntryEnabled()) {
            Tr.exit(tc, "start");
		}
	}

	public void stop() {
		if(tc.isEntryEnabled()) {
            Tr.entry(tc, "stop");
		}
		
		System.out.println("Stopping MyComponent");
		
		if(tc.isEntryEnabled()) {
            Tr.exit(tc, "stop");
		}
	}
}
