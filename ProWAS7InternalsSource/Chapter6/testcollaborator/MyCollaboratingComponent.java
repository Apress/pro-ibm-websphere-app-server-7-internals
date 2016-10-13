package testcollaborator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.ibm.ejs.ras.Tr;
import com.ibm.ejs.ras.TraceComponent;
import com.ibm.ws.exception.ComponentDisabledException;
import com.ibm.ws.exception.ConfigurationError;
import com.ibm.ws.exception.ConfigurationWarning;
import com.ibm.ws.exception.RuntimeError;
import com.ibm.ws.exception.RuntimeWarning;
import com.ibm.ws.runtime.deploy.DeployedObjectEvent;
import com.ibm.ws.runtime.deploy.DeployedObjectHandler;
import com.ibm.ws.runtime.service.ApplicationMgr;
import com.ibm.ws.webcontainer.WebContainerService;
import com.ibm.ws.webcontainer.metadata.WebComponentMetaData;
import com.ibm.ws.webcontainer.webapp.collaborator.WebAppInvocationCollaborator;
import com.ibm.wsspi.runtime.component.WsComponentImpl;
import com.ibm.wsspi.runtime.service.WsServiceRegistry;

@SuppressWarnings("deprecation")
public class MyCollaboratingComponent extends WsComponentImpl implements
		DeployedObjectHandler, WebAppInvocationCollaborator {
	
	private static final String MyCollaboratingComponent = "testCollaborator/MyCollaboratingComponent";
	private static TraceComponent tc = Tr.register(MyCollaboratingComponent);
	

	public boolean start(DeployedObjectEvent doe) throws RuntimeError,
			RuntimeWarning {
		showStatusMessage("start", doe);		
		return true;
	}

	public void stop(DeployedObjectEvent doe) {
		showStatusMessage("stop", doe);
	}
	
	private void showStatusMessage(String status, DeployedObjectEvent doe) {
		try {
			FileWriter outlog = new FileWriter("/tmp/logs/WAS7Collaborator.log", true);
			outlog.write("****************" + "\n");
			outlog.write(status + " with DeployedObjectEvent at: " + new Date().toString() + "\n");
			outlog.write("Deployed Object Name is: " + doe.getDeployedObject().getName() + "\n");

			// Make sure that things get written
			outlog.flush();			
			outlog.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void preInvoke(WebComponentMetaData wcmd) {
		showCollaboratorMetadataMessage("preInvoke", wcmd);
	}
	
	public void postInvoke(WebComponentMetaData wcmd) {
		showCollaboratorMetadataMessage("postInvoke", wcmd);		
	}

	private void showCollaboratorMetadataMessage(String method, WebComponentMetaData wcmd) {
		try {
			FileWriter outlog = new FileWriter("/tmp/logs/WAS7Collaborator.log", true);
			outlog.write("****************" + "\n");
			outlog.write(method + " with WebComponentMetaData at: " + new Date().toString() + "\n");
			outlog.write("J2EE Module Name is: " + wcmd.getJ2EEName().getModule() + "\n");
			outlog.write("Name is: " + wcmd.getName() + "\n");
			outlog.write("Web Component Description is: " + wcmd.getWebComponentDescription() + "\n");
			outlog.write("Implementation Class is: " + wcmd.getImplementationClass() + "\n");

			// Make sure that things get written
			outlog.flush();			
			outlog.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void preInvoke(WebComponentMetaData wcmd, ServletRequest req,
			ServletResponse resp) {
		showCollaboratorHandlerMessage("preInvoke", wcmd, req, resp);		}

	public void postInvoke(WebComponentMetaData wcmd, ServletRequest req,
			ServletResponse resp) {
		showCollaboratorHandlerMessage("postInvoke", wcmd, req, resp);				
	}

	private void showCollaboratorHandlerMessage(String method, WebComponentMetaData wcmd,
			ServletRequest req, ServletResponse resp) {
		try {
			FileWriter outlog = new FileWriter("/tmp/logs/WAS7Collaborator.log", true);
			outlog.write("****************" + "\n");
			outlog.write(method + " with WebComponentMetaData and servlet request/response at: " + new Date().toString() + "\n");
			outlog.write("J2EE Module Name is: " + wcmd.getJ2EEName().getApplication() + "\n");
			outlog.write("Name is: " + wcmd.getName() + "\n");
			outlog.write("Web Component Description is: " + wcmd.getWebComponentDescription() + "\n");
			outlog.write("Implementation Class is: " + wcmd.getImplementationClass() + "\n");
            outlog.write("Servlet Request Content Length is: " + req.getContentLength() + "\n");
            outlog.write("Servlet Request Remote Host is: " + req.getRemoteHost() + "\n");
			outlog.write("Servlet Response Content Type is: " + resp.getContentType() + "\n");

			// Make sure that things get written
			outlog.flush();			
			outlog.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
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
		System.out.println("Initialising MyCollaboratingComponent and adding collaborator");
		
		// To declare this object as a collabrator to the web container get access 
		// to the web container service and pass it a reference to its web app 
		// collaborator method. Then we will get called before and after calls 
		// into the servlet interface.
		try {
			WebContainerService service = (WebContainerService) WsServiceRegistry.getService(
					this,  Class.forName("com.ibm.ws.webcontainer.WebContainerService"));
			service.addWebAppCollaborator(this);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		// Now do the same to get called when deployed objects start
		// or stop but use the Application Manager service
		try {
			ApplicationMgr appmgr = (ApplicationMgr) WsServiceRegistry.getService(
					this,  Class.forName("com.ibm.ws.runtime.service.ApplicationMgr"));
			appmgr.addDeployedObjectHandler(this);
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
		System.out.println("Starting MyCollaboratingComponent");		
		if(tc.isEntryEnabled()) {
            Tr.exit(tc, "start");
		}
	}

	public void stop() {
		if(tc.isEntryEnabled()) {
            Tr.entry(tc, "stop");
		}		
		System.out.println("Stopping MyCollaboratingComponent");		
		if(tc.isEntryEnabled()) {
            Tr.exit(tc, "stop");
		}
	}	
}
