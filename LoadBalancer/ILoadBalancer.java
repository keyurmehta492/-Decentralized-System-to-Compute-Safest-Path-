package LoadBalancer;

import java.rmi.RemoteException;

import Server.Session;

public interface ILoadBalancer extends java.rmi.Remote {
    Session getDirection(String source, String destination) throws RemoteException;

    void sendGPS(Session session, double[] currentLocation, boolean humanFlag) throws RemoteException;
}
