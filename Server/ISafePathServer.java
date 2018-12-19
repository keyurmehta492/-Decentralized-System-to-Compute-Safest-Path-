package Server;

public interface ISafePathServer extends java.rmi.Remote{

	Session getRoute(Session session) throws java.rmi.RemoteException;
}
