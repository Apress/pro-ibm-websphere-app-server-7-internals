package myEJBContainerPackage;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import javax.ejb.EJBException;
import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EntityContext;
import javax.xml.ws.Provider;
import com.ibm.ejs.cm.portability.PortabilityLayerExt;
import com.ibm.ejs.container.EJBConfigurationException;
import com.ibm.websphere.appprofile.accessintent.AccessIntent;
import com.ibm.websphere.cpmi.PersistenceManager;
import com.ibm.websphere.csi.AfterActivationCollaborator;
import com.ibm.websphere.csi.BeanMetaDataStore;
import com.ibm.websphere.csi.BeforeActivationAfterCompletionCollaborator;
import com.ibm.websphere.csi.BeforeActivationCollaborator;
import com.ibm.websphere.csi.CSIException;
import com.ibm.websphere.csi.CSITransactionRolledbackException;
import com.ibm.websphere.csi.EJBCallbackCollaborator;
import com.ibm.websphere.csi.EJBComponentInitializationCollaborator;
import com.ibm.websphere.csi.EJBContainerException;
import com.ibm.websphere.csi.EJBDynamicQueryHelper;
import com.ibm.websphere.csi.EJBModuleInitializationCollaborator;
import com.ibm.websphere.csi.EJBServantManager;
import com.ibm.websphere.csi.J2EEName;
import com.ibm.websphere.csi.J2EENameFactory;
import com.ibm.websphere.csi.PMTxInfo;
import com.ibm.websphere.csi.RemoveCollaborator;
import com.ibm.ws.appprofile.accessintent.EJBAccessIntent;
import com.ibm.ws.cpmi.association.CMRHelper;
import com.ibm.ws.csi.EJBClusterNameService;
import com.ibm.ws.ejbcontainer.BasicEJBInfo;
import com.ibm.ws.exception.RuntimeError;
import com.ibm.ws.exception.RuntimeWarning;
import com.ibm.ws.runtime.component.ContainerImpl;
import com.ibm.ws.runtime.deploy.DeployedObject;
import com.ibm.ws.runtime.deploy.DeployedObjectEvent;
import com.ibm.ws.runtime.deploy.DeployedObjectHandler;
import com.ibm.ws.runtime.deploy.DeployedObjectListener;
import com.ibm.ws.runtime.metadata.MetaData;
import com.ibm.ws.runtime.metadata.MetaDataFactory;
import com.ibm.ws.runtime.resource.ResourceEvent;
import com.ibm.ws.runtime.resource.ResourceEventListener;
import com.ibm.ws.runtime.service.EJBContainer;
import com.ibm.ws.runtime.service.MetaDataFactoryMgr;
import com.ibm.ws.util.InvocationCallback;
import com.ibm.ws.util.InvocationToken;
import com.ibm.wsspi.ejbcontainer.WSEJBEndpointManager;
import com.ibm.wsspi.ejbcontainer.WSEJBHandlerResolver;

@SuppressWarnings("deprecation")
public class MyEJBContainer extends ContainerImpl implements
		DeployedObjectHandler, DeployedObjectListener, BeanMetaDataStore,
		MetaDataFactory, ResourceEventListener, EJBContainer {
	
	public void initialize(Object arg0) throws ComponentDisabledException,
	ConfigurationWarning, ConfigurationError {
		setState("INITIALIZING");
		loadComponents("META-INF/ws-ejbcontainer-startup.xml");
		initializeComponents(null, null);
		setState("INITIALIZED");		
	}

	public void start() throws RuntimeError, RuntimeWarning {
		setState("STARTING");
		startComponents();
		setState("STARTED");
	}

	public void stop() {
		setState("STOPPING");
		stopComponents();
		setState("STOPPED");
	}	
		
	public void destroy() {
		setState("DESTROYING");
		destroyComponents();
		setState("DESTROYED");
	}

	public void stateChanged(DeployedObjectEvent arg0) throws RuntimeError,
			RuntimeWarning {
	}

	public Object get(J2EEName arg0) throws CSIException {
		return null;
	}

	public void put(J2EEName arg0, Object arg1) throws CSIException {
	}

	public void remove(J2EEName arg0) {
	}

	public MetaData createMetaData(MetaDataFactoryMgr arg0, DeployedObject arg1)
			throws RuntimeError, RuntimeWarning {
		return null;
	}

	public void destroyMetaData(MetaDataFactoryMgr arg0, DeployedObject arg1)
			throws RuntimeError, RuntimeWarning {
	}

	public void resourceFactoryEvent(ResourceEvent arg0) throws RuntimeError,
			RuntimeWarning {
	}

	public void resourceProviderEvent(ResourceEvent arg0) throws RuntimeError,
			RuntimeWarning {
	}

	public void addCollaborator(AfterActivationCollaborator arg0) {
	}

	public void addCollaborator(BeforeActivationAfterCompletionCollaborator arg0) {
	}

	public void addCollaborator(BeforeActivationCollaborator arg0) {
	}

	public void addCollaborator(EJBCallbackCollaborator arg0) {
	}

	public void addCollaborator(RemoveCollaborator arg0) {
	}

	public void addCollaborator(EJBComponentInitializationCollaborator arg0) {
	}

	public void addCollaborator(EJBModuleInitializationCollaborator arg0) {
	}

	public WSEJBEndpointManager createWebServiceEndpointManager(J2EEName arg0,
			Method[] arg1) throws EJBException, EJBConfigurationException {
		return null;
	}

	public WSEJBEndpointManager createWebServiceEndpointManager(J2EEName arg0,
			Class<Provider<?>> arg1) throws EJBException,
			EJBConfigurationException {
		return null;
	}

	public void enlistInvocationCallback(InvocationCallback arg0, Object arg1)
			throws IllegalStateException {
	}

	public void flush() throws RemoteException {
	}

	public AccessIntent getAccessIntent(EntityContext arg0) {
		return null;
	}

	public BasicEJBInfo getBasicEJBInfo(Object arg0) {
		return null;
	}

	public CMRHelper getCMRHelper() {
		return null;
	}

	public ClassLoader getClassLoader(J2EEName arg0) {
		return null;
	}

	public InvocationToken getCurrentInvocationToken() {
		return null;
	}

	public PMTxInfo getCurrentPMTxInfo()
			throws CSITransactionRolledbackException {
		return null;
	}

	public EJBDynamicQueryHelper getEJBDynamicQueryHelper() {
		return null;
	}

	public EJBHome getEJBHome(J2EEName arg0) throws EJBContainerException {
		return null;
	}

	public EJBLocalHome getEJBLocalHome(J2EEName arg0)
			throws EJBContainerException {
		return null;
	}

	public int getIsolationLevel(int arg0) {
		return 0;
	}

	public int getIsolationLevel(Object arg0, PortabilityLayerExt arg1) {
		return 0;
	}

	public J2EEName getJ2EEName(Object arg0) throws EJBContainerException {
		return null;
	}

	public J2EENameFactory getJ2EENameFactory() {
		return null;
	}

	public PersistenceManager[] getPersistenceManagers() {
		return null;
	}

	public EJBServantManager getServantManager() {
		return null;
	}

	public void releaseCMRHelper(CMRHelper arg0) {
	}

	public void releaseEJBDynamicQueryHelper(EJBDynamicQueryHelper arg0) {
	}

	public void setClusterNameService(EJBClusterNameService arg0)
			throws EJBContainerException {
	}

	public void setEJBAccessIntent(EJBAccessIntent arg0) {
	}

	public void setRollBackOnly() {
	}

	public void setWebServiceHandlerResolver(WSEJBHandlerResolver arg0) {
	}

	public boolean start(DeployedObjectEvent arg0) throws RuntimeError,
			RuntimeWarning {
		// TODO Auto-generated method stub
		return false;
	}

	public void stop(DeployedObjectEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
