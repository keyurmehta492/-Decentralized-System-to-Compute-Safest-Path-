package LoadBalancer;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import Server.ISafePathServer;

public class LoadBalancer extends UnicastRemoteObject {

    private static final long serialVersionUID = 1L;
    private static Scanner sc;

    protected LoadBalancer() throws RemoteException {
    }

    public static void main(String[] args) throws RemoteException {
        String name;
        List<ISafePathServer> serverList = new ArrayList<>(Arrays.asList(null, null));
        for (int i = 1; i <= 2; i++) {
            System.out.println("Connecting Server " + i);
            // server 1 : thunder.cs.iupui.edu
            // server 2: lightning.cs.iupui.edu
            if (i == 1) {
                name = "//thunder.cs.iupui.edu:4444/SafePathServer";
                System.out.println(name);
            } else {
                name = "//lightning.cs.iupui.edu:4444/SafePathServer";
                System.out.println(name);
            }
            try {
                ISafePathServer iSafePathServer = (ISafePathServer) Naming.lookup(name);
                if (iSafePathServer == null) {
                    System.out.println("Interface Error: Server " + i + " : " + name);
                }
                serverList.set(i - 1, iSafePathServer);
            } catch (Exception e) {
                System.out.println("Look up Error: Server " + i + " : " + name);
                e.printStackTrace();
                System.exit(-1);
            }
        }
        System.out.println("Server's Connected!");
        name = "//rain.cs.iupui.edu:4444/LoadBalancer";
        ILoadBalancer myInterface = new LoadBalancerImpl(name, serverList);
        try {
            Naming.rebind(name, myInterface);
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Load Balancer Started..");
    }
}
