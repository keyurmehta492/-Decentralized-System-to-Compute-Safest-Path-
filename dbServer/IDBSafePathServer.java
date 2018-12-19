package dbServer;

import java.util.ArrayList;

import Server.Route;

public interface IDBSafePathServer extends java.rmi.Remote{

	ArrayList<Route> findIncidents(ArrayList<Route> allRoute) throws java.rmi.RemoteException;
}
