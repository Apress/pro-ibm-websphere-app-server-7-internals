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
import com.ibm.wsspi.runtime.component.WsComponent;
import com.ibm.ejs.container.*;
import com.ibm.CORBA.iiop.ClientDelegate;
import com.ibm.CORBA.iiop.IOR;
import com.ibm.ejs.cm.portability.PortabilityLayerExt;
import com.ibm.ejs.container.BeanId;
import com.ibm.ejs.container.BeanManagedBeanO;
import com.ibm.ejs.container.BeanMetaData;
import com.ibm.ejs.container.BindingsHelper;
import com.ibm.ejs.container.CMRHelperStack;
import com.ibm.ejs.container.ContainerEJBException;
import com.ibm.ejs.container.ContainerException;
import com.ibm.ejs.container.ContainerProperties;
import com.ibm.ejs.container.ContainerTx;
import com.ibm.ejs.container.EJBConfigurationException;
import com.ibm.ejs.container.EJBContainerDiagnosticModule;
import com.ibm.ejs.container.EJBDynamicQueryHelperImpl;
import com.ibm.ejs.container.EJBInfoImpl;
import com.ibm.ejs.container.EJBTimerTaskInfoService;
import com.ibm.ejs.container.EJSContainer;
import com.ibm.ejs.container.EJSDeployedSupport;
import com.ibm.ejs.container.EJSHome;
import com.ibm.ejs.container.EJSWrapperBase;
import com.ibm.ejs.container.EJSWrapperCommon;
import com.ibm.ejs.container.HomeInternal;
import com.ibm.ejs.container.HomeOfHomes;
import com.ibm.ejs.container.HomeRecord;
import com.ibm.ejs.container.MDBInternalHome;
import com.ibm.ejs.container.WrapperId;
import com.ibm.ejs.container.drs.SfDRSCache;
import com.ibm.ejs.container.drs.ws390.SfPlatformHelper;
import com.ibm.ejs.container.util.EJBLocalInterfaceInfo;
import com.ibm.ejs.container.util.EJBLocalInterfaceInfoRefAddr;
import com.ibm.ejs.container.util.EJBLocalInterfaceObjectFactory;
import com.ibm.ejs.container.util.EJSPlatformHelper;
import com.ibm.ejs.container.util.ExceptionUtil;
import com.ibm.ejs.container.util.SFSBReferenceObjectFactory;
import com.ibm.ejs.csi.ComponentMetaDataCollaborator;
import com.ibm.ejs.csi.ContainerConfigImpl;
import com.ibm.ejs.csi.ContainerExtensionFactoryBaseImpl;
import com.ibm.ejs.csi.ContainerExtensionFactoryPMEImpl;
import com.ibm.ejs.csi.EJBModuleMetaDataImpl;
import com.ibm.ejs.csi.FileBeanStore;
import com.ibm.ejs.csi.OrbUtilsImpl;
import com.ibm.ejs.csi.SessionHandleFactoryImpl;
import com.ibm.ejs.jms.listener.MDBListenerManager;
import com.ibm.ejs.oa.AdapterAlreadyExistsException;
import com.ibm.ejs.oa.EJSOAImpl;
import com.ibm.ejs.oa.EJSRootOAImpl;
import com.ibm.ejs.oa.UserKey;
import com.ibm.ejs.ras.Tr;
import com.ibm.ejs.ras.TraceComponent;
import com.ibm.ejs.util.ByteArray;
import com.ibm.ejs.util.FastHashtable;
import com.ibm.ejs.util.cache.BackgroundLruEvictionStrategy;
import com.ibm.ejs.util.cache.Bucket;
import com.ibm.ejs.util.cache.Cache;
import com.ibm.ejs.util.cache.SimpleLimitStrategy;
import com.ibm.ejs.util.cache.SweepLruEvictionStrategy;
import com.ibm.ejs.util.dopriv.SetContextClassLoaderPrivileged;
import com.ibm.websphere.appprofile.accessintent.AccessIntent;
import com.ibm.websphere.cpmi.PersistenceManager;
import com.ibm.websphere.csi.AfterActivationCollaborator;
import com.ibm.websphere.csi.BeanMetaDataStore;
import com.ibm.websphere.csi.BeforeActivationAfterCompletionCollaborator;
import com.ibm.websphere.csi.BeforeActivationCollaborator;
import com.ibm.websphere.csi.CSIException;
import com.ibm.websphere.csi.CSITransactionRolledbackException;
import com.ibm.websphere.csi.ContainerExtensionFactory;
import com.ibm.websphere.csi.EJBCallbackCollaborator;
import com.ibm.websphere.csi.EJBComponentInitializationCollaborator;
import com.ibm.websphere.csi.EJBComponentMetaData;
import com.ibm.websphere.csi.EJBContainerException;
import com.ibm.websphere.csi.EJBDynamicQueryHelper;
import com.ibm.websphere.csi.EJBModuleInitializationCollaborator;
import com.ibm.websphere.csi.EJBModuleMetaData;
import com.ibm.websphere.csi.EJBServantManager;
import com.ibm.websphere.csi.HomeWrapperSet;
import com.ibm.websphere.csi.J2EEName;
import com.ibm.websphere.csi.J2EENameFactory;
import com.ibm.websphere.csi.ManagedContainer;
import com.ibm.websphere.csi.PMTxInfo;
import com.ibm.websphere.csi.PassivationPolicy;
import com.ibm.websphere.csi.PoolManager;
import com.ibm.websphere.csi.RemoveCollaborator;
import com.ibm.websphere.csi.SecurityCollaborator;
import com.ibm.websphere.csi.StatefulSessionKeyFactory;
import com.ibm.websphere.csi.UOWControl;
import com.ibm.websphere.ejbcontainer.EJBFactory;
import com.ibm.websphere.management.AdminServiceFactory;
import com.ibm.websphere.naming.JndiHelper;
import com.ibm.websphere.naming.WsnBatchCompleteResults;
import com.ibm.websphere.naming.WsnOptimizedJndiContext;
import com.ibm.websphere.scheduler.Scheduler;
import com.ibm.ws.ActivitySession.ActivitySessionService;
import com.ibm.ws.appprofile.accessintent.EJBAccessIntent;
import com.ibm.ws.asynchbeans.AsynchBeansService;
import com.ibm.ws.bootstrap.ExtClassLoader;
import com.ibm.ws.cpi.JDBCPersisterFactoryImpl;
import com.ibm.ws.cpmi.association.CMRHelper;
import com.ibm.ws.csi.EJBClusterNameService;
import com.ibm.ws.ejbcontainer.BasicEJBInfo;
import com.ibm.ws.exception.ComponentDisabledException;
import com.ibm.ws.exception.ConfigurationError;
import com.ibm.ws.exception.ConfigurationWarning;
import com.ibm.ws.exception.RuntimeError;
import com.ibm.ws.exception.RuntimeWarning;
import com.ibm.ws.exception.WsNestedException;
import com.ibm.ws.ffdc.FFDCFilter;
import com.ibm.ws.management.collaborator.DefaultRuntimeCollaborator;
import com.ibm.ws.metadata.ComponentDataObject;
import com.ibm.ws.metadata.ComponentDataObjectFields;
import com.ibm.ws.metadata.ConfigReader;
import com.ibm.ws.metadata.MDOFields;
import com.ibm.ws.metadata.MetaDataDiagnosticModule;
import com.ibm.ws.metadata.MetaDataOrchestrator;
import com.ibm.ws.metadata.MetaDataSources;
import com.ibm.ws.metadata.ModuleDataObject;
import com.ibm.ws.metadata.WCCMConfigReader;
import com.ibm.ws.metadata.annotations.AnnotationConfigReader;
import com.ibm.ws.metadata.ejb.EJBMDOrchestrator;
import com.ibm.ws.naming.jbatch.WsnBatchModeCNContext;
import com.ibm.ws.naming.jbatch.WsnBatchResultExt;
import com.ibm.ws.naming.jndicos.CNContext;
import com.ibm.ws.naming.util.IndirectJndiLookupFactoryImpl;
import com.ibm.ws.naming.util.ReferenceData;
import com.ibm.ws.pmi.server.PmiBeanFactoryImpl;
import com.ibm.ws.pmi.server.PmiRegistry;
import com.ibm.ws.runtime.deploy.DeployedApplication;
import com.ibm.ws.runtime.deploy.DeployedModule;
import com.ibm.ws.runtime.deploy.DeployedObject;
import com.ibm.ws.runtime.deploy.DeployedObjectEvent;
import com.ibm.ws.runtime.deploy.DeployedObjectHandler;
import com.ibm.ws.runtime.deploy.DeployedObjectListener;
import com.ibm.ws.runtime.deploy.EJBCollaborator;
import com.ibm.ws.runtime.deploy.EJBModuleCollaborator;
import com.ibm.ws.runtime.metadata.ApplicationMetaData;
import com.ibm.ws.runtime.metadata.MetaData;
import com.ibm.ws.runtime.metadata.MetaDataEvent;
import com.ibm.ws.runtime.metadata.MetaDataFactory;
import com.ibm.ws.runtime.metadata.ModuleMetaData;
import com.ibm.ws.runtime.resource.ResourceEvent;
import com.ibm.ws.runtime.resource.ResourceEventListener;
import com.ibm.ws.runtime.service.ApplicationMgr;
import com.ibm.ws.runtime.service.EJBContainer;
import com.ibm.ws.runtime.service.MessageListenerManager;
import com.ibm.ws.runtime.service.MetaDataFactoryMgr;
import com.ibm.ws.runtime.service.MultibrokerDomain;
import com.ibm.ws.runtime.service.ORB;
import com.ibm.ws.runtime.service.ResourceMgr;
import com.ibm.ws.runtime.service.Server;
import com.ibm.ws.runtime.service.VariableMap;
import com.ibm.ws.scheduler.SchedulerConfigHelper;
import com.ibm.ws.scheduler.SchedulerConfigService;
import com.ibm.ws.scheduler.SchedulerService;
import com.ibm.ws.scheduler.config.SchedulerConfiguration;
import com.ibm.ws.scheduler.exception.SchedulerDataStoreException;
import com.ibm.ws.security.auth.j2c.WSDefaultPrincipalMapping;
import com.ibm.ws.security.core.SecurityContext;
import com.ibm.ws.security.cred.AuthDataCredential;
import com.ibm.ws.security.util.AccessController;
import com.ibm.ws.security.util.ServerIdentityHelper;
import com.ibm.ws.util.ImplFactory;
import com.ibm.ws.util.InvocationCallback;
import com.ibm.ws.util.InvocationToken;
import com.ibm.ws.util.StatefulBeanEnqDeqFactory;
import com.ibm.ws.util.WSThreadLocal;
import com.ibm.wsspi.asynchbeans.WorkManagerConfiguration;
import com.ibm.wsspi.drs.DRSSettings;
import com.ibm.wsspi.ejbcontainer.WSEJBEndpointManager;
import com.ibm.wsspi.ejbcontainer.WSEJBHandlerResolver;
import com.ibm.wsspi.injectionengine.InjectionEngineAccessor;
import com.ibm.wsspi.injectionengine.MessageDestinationLinkFactory;
import com.ibm.wsspi.runtime.config.ConfigObject;
import com.ibm.wsspi.runtime.config.ContainerConfig;
import com.ibm.wsspi.runtime.service.WsServiceRegistry;
import java.beans.beancontext.BeanContextServices;
import java.io.File;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.JarFile;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EntityContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameParser;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.rmi.CORBA.Stub;
import org.eclipse.jst.j2ee.common.MessageDestination;
import org.eclipse.jst.j2ee.commonarchivecore.internal.ModuleFile;
import org.eclipse.jst.j2ee.ejb.AssemblyDescriptor;
import org.eclipse.jst.j2ee.ejb.EJBJar;



public class MyEJBContainer extends ContainerImpl implements
		DeployedObjectHandler, DeployedObjectListener, BeanMetaDataStore,
		MetaDataFactory, ResourceEventListener, EJBContainer {

	public void initialize(Object arg0) throws ComponentDisabledException,
	ConfigurationWarning, ConfigurationError {

		setState("INITIALIZING");
		loadComponents("META-INF/ws-ejbcontainer-startup.xml");
		initializeComponents();
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

	public String getName() {
		return null;
	}

	public String getState() {
		return null;
	}

	public boolean start(DeployedObjectEvent arg0) throws RuntimeError,
			RuntimeWarning {
		return false;
	}

	public void stop(DeployedObjectEvent arg0) {
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

	public WSEJBEndpointManager createWebServiceEndpointManager(J2EEName arg0,
			Class<Provider<?>> arg1) throws EJBException,
			EJBConfigurationException {
		return null;
	}

	public AccessIntent getAccessIntent(EntityContext arg0) {
		return null;
	}
}
