package mywas7component;

import java.lang.reflect.Method;
import java.rmi.RemoteException;

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
		DeployedObjectHandler, BeanMetaDataStore, EJBContainer,
		ResourceEventListener, MetaDataFactory, DeployedObjectListener {

	public boolean start(DeployedObjectEvent arg0) throws RuntimeError,
			RuntimeWarning {
		// TODO Auto-generated method stub
		return false;
	}

	public void stop(DeployedObjectEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public Object get(J2EEName arg0) throws CSIException {
		// TODO Auto-generated method stub
		return null;
	}

	public void put(J2EEName arg0, Object arg1) throws CSIException {
		// TODO Auto-generated method stub
		
	}

	public void remove(J2EEName arg0) {
		// TODO Auto-generated method stub
		
	}

	public void addCollaborator(AfterActivationCollaborator arg0) {
		// TODO Auto-generated method stub
		
	}

	public void addCollaborator(BeforeActivationAfterCompletionCollaborator arg0) {
		// TODO Auto-generated method stub
		
	}

	public void addCollaborator(BeforeActivationCollaborator arg0) {
		// TODO Auto-generated method stub
		
	}

	public void addCollaborator(EJBCallbackCollaborator arg0) {
		// TODO Auto-generated method stub
		
	}

	public void addCollaborator(RemoveCollaborator arg0) {
		// TODO Auto-generated method stub
		
	}

	public void addCollaborator(EJBComponentInitializationCollaborator arg0) {
		// TODO Auto-generated method stub
		
	}

	public void addCollaborator(EJBModuleInitializationCollaborator arg0) {
		// TODO Auto-generated method stub
		
	}

	public WSEJBEndpointManager createWebServiceEndpointManager(J2EEName arg0,
			Method[] arg1) throws EJBException, EJBConfigurationException {
		// TODO Auto-generated method stub
		return null;
	}

	public WSEJBEndpointManager createWebServiceEndpointManager(J2EEName arg0,
			Class<Provider<?>> arg1) throws EJBException,
			EJBConfigurationException {
		// TODO Auto-generated method stub
		return null;
	}

	public void enlistInvocationCallback(InvocationCallback arg0, Object arg1)
			throws IllegalStateException {
		// TODO Auto-generated method stub
		
	}

	public void flush() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	public AccessIntent getAccessIntent(EntityContext arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public BasicEJBInfo getBasicEJBInfo(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public CMRHelper getCMRHelper() {
		// TODO Auto-generated method stub
		return null;
	}

	public ClassLoader getClassLoader(J2EEName arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public InvocationToken getCurrentInvocationToken() {
		// TODO Auto-generated method stub
		return null;
	}

	public PMTxInfo getCurrentPMTxInfo()
			throws CSITransactionRolledbackException {
		// TODO Auto-generated method stub
		return null;
	}

	public EJBDynamicQueryHelper getEJBDynamicQueryHelper() {
		// TODO Auto-generated method stub
		return null;
	}

	public EJBHome getEJBHome(J2EEName arg0) throws EJBContainerException {
		// TODO Auto-generated method stub
		return null;
	}

	public EJBLocalHome getEJBLocalHome(J2EEName arg0)
			throws EJBContainerException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getIsolationLevel(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getIsolationLevel(Object arg0, PortabilityLayerExt arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	public J2EEName getJ2EEName(Object arg0) throws EJBContainerException {
		// TODO Auto-generated method stub
		return null;
	}

	public J2EENameFactory getJ2EENameFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	public PersistenceManager[] getPersistenceManagers() {
		// TODO Auto-generated method stub
		return null;
	}

	public EJBServantManager getServantManager() {
		// TODO Auto-generated method stub
		return null;
	}

	public void releaseCMRHelper(CMRHelper arg0) {
		// TODO Auto-generated method stub
		
	}

	public void releaseEJBDynamicQueryHelper(EJBDynamicQueryHelper arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setClusterNameService(EJBClusterNameService arg0)
			throws EJBContainerException {
		// TODO Auto-generated method stub
		
	}

	public void setEJBAccessIntent(EJBAccessIntent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setRollBackOnly() {
		// TODO Auto-generated method stub
		
	}

	public void setWebServiceHandlerResolver(WSEJBHandlerResolver arg0) {
		// TODO Auto-generated method stub
		
	}

	public void resourceFactoryEvent(ResourceEvent arg0) throws RuntimeError,
			RuntimeWarning {
		// TODO Auto-generated method stub
		
	}

	public void resourceProviderEvent(ResourceEvent arg0) throws RuntimeError,
			RuntimeWarning {
		// TODO Auto-generated method stub
		
	}

	public MetaData createMetaData(MetaDataFactoryMgr arg0, DeployedObject arg1)
			throws RuntimeError, RuntimeWarning {
		// TODO Auto-generated method stub
		return null;
	}

	public void destroyMetaData(MetaDataFactoryMgr arg0, DeployedObject arg1)
			throws RuntimeError, RuntimeWarning {
		// TODO Auto-generated method stub
		
	}

	public void stateChanged(DeployedObjectEvent arg0) throws RuntimeError,
			RuntimeWarning {
		// TODO Auto-generated method stub
		
	}

}
