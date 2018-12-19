package Client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import LoadBalancer.ILoadBalancer;
import Server.Session;

public class Client extends UnicastRemoteObject {
  
	private static final long serialVersionUID = 1L;

	protected Client() throws RemoteException {
    }

    public static String source;
    public static String destination;
	private static Scanner sc;
	static Session session;
	
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException, InterruptedException {
        String name = "//rain.cs.iupui.edu:4444/LoadBalancer";
        ILoadBalancer myInterface = (ILoadBalancer) Naming.lookup(name);
        if (myInterface == null) {
            System.out.println("Interface Error: Client ");
            System.exit(-1);
        }
        // Prompts client to input source and destination
        ClientView();
        
        session = myInterface.getDirection(source, destination);
        
        // Prompts for invalid request
        while(session.getMessage().equalsIgnoreCase("400")){
            System.out.println("Invalid request, please try again!!");
            ClientView();
            session = myInterface.getDirection(source, destination);
           
        }
        // if both servers crash
        if(session.getMessage().equalsIgnoreCase("403")){
            System.out.println("Sorry, Servers are temporarily unavailable!!");
            System.exit(-1);
        }
        System.out.println("\n==========================================");
        System.out.println("The distance of the safest and shortest route is: "+session.getAllRoutes().get(0).getDistance());
        System.out.println("==========================================");
        System.out.println("\nThe JSON Response is: ");
        System.out.println(session.getResponse());
        System.out.println("==========================================");
        System.out.println("\nThe waypoints for selected route are : ");
        for(int i=0;i<session.getAllRoutes().size();i++){
            //System.out.println("Route: " + session.getAllRoutes().get(i).getId());
            for(int j=0;j< session.getAllRoutes().get(i).getStep().size();j++){
                System.out.print(session.getAllRoutes().get(i).getStep().get(j).getLat() + "\t");
                System.out.println(session.getAllRoutes().get(i).getStep().get(j).getLng());
            }
        }
        //System.out.println("source: " + session.getSource() + " destination: " + session.getDestination());
       // GPSSimulator simulator = new GPSSimulator(myInterface, session);
       // simulator.startGPS();
    }

    public static void ClientView() {
        System.out.println("==========================================");
        System.out.println("Welcome to the Navigation Application");
        System.out.println("==========================================");
        
        sc = new Scanner(System.in);
        System.out.println("Enter the Source:");
        source = sc.nextLine();
        System.out.println("Enter the Destination:");
        destination = sc.nextLine();
        
        if(source.equalsIgnoreCase(destination)){
            System.out.println("Invalid request, please try again");
            ClientView();
        }
    }
}
