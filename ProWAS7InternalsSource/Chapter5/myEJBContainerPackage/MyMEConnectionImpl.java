package myEJBContainerPackage;

import com.ibm.ws.sib.comms.server.mesupport.MEConnectionImpl;
import com.ibm.wsspi.sib.core.exception.SIConnectionDroppedException;
import com.ibm.wsspi.sib.core.exception.SIConnectionLostException;

public class MyMEConnectionImpl extends MEConnectionImpl {
	public void myMethod() throws SIConnectionDroppedException, SIConnectionLostException {
		jfapSend(null, 1, 1, true, null);
		jfapExchange(null, 1, 1, true);
	}

}
