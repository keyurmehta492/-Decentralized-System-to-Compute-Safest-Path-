package Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AppServer extends UnicastRemoteObject implements ISafePathServer{


	private static final long serialVersionUID = 1L;
	Session session;
	
	protected AppServer() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	public Session getRoute(Session session) throws RemoteException {
		
		SafePathServerImpl ser = new SafePathServerImpl();
		session = ser.getRoute(session);
		
		return session;
		
	}
}
