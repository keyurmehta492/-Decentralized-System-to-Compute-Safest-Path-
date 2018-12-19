package dbServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import Server.Route;

public class dbServer extends UnicastRemoteObject implements IDBSafePathServer {

	ArrayList<Route> allRoute;
	protected dbServer() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;

	@Override
	public ArrayList<Route> findIncidents(ArrayList<Route> allRoute) throws RemoteException {
		
		IncidentCalculation ic = new IncidentCalculation();
		allRoute = ic.findIncidents(allRoute);
		return allRoute;
	}

}
