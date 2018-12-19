package LoadBalancer;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import Server.ISafePathServer;
import Server.Session;

public class LoadBalancerImpl extends UnicastRemoteObject implements ILoadBalancer {
    
	protected LoadBalancerImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;
	
	String name;
    List<ISafePathServer> serverList;
    boolean flag = true;
    Session session;
    ISafePathServer iSafePathServer;
    int cnt =0;
    protected LoadBalancerImpl(String name, List<ISafePathServer> serverList) throws RemoteException {
        this.name = name;
        this.serverList = serverList;
    }

    @Override
    public Session getDirection(String source, String destination) throws RemoteException {
        session = new Session();
        System.out.println("Request cnt: " + cnt++);
        session.setSource(source);
        session.setDestination(destination);
        
        if(flag){
            iSafePathServer = serverList.get(0);
        	//name = "//thunder.cs.iupui.edu:4444/SafePathServer";
            this.flag = false;
            session.setServer(0);
            System.out.println("Forwarding request to Application Server 1...");
            System.out.println("Source: " + source + " \t Desination: " + destination);
        }else{
            iSafePathServer = serverList.get(1);
            //name = "//lightning.cs.iupui.edu:4444/SafePathServer";
            this.flag = true;
            System.out.println("Forwarding request to Application Server 2...");
            System.out.println("Source: " + source + " \t Desination: " + destination);
            session.setServer(1);
        }
        
       
		try {
		//	iSafePathServer = (ISafePathServer) Naming.lookup(name);
			session = iSafePathServer.getRoute(session);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			session.setMessage("403");
		}
		
        
        int counter = 1;
        System.out.println(session.getMessage());
        // In cases where server crashes it connects to the backup server
     
        switch (session.getMessage()){
           case "200": 
        	   break;
        	 
           case "403": while(session.getMessage().equalsIgnoreCase("403")){
                               System.out.println("Reconnecting , attempt "+counter);
                               int n = session.getServer();
                               if(n == 0){
                                   System.out.println("Server 1 unavailable, connecting to Server 2");
                                   iSafePathServer = serverList.get(1);
                                   session.setServer(1);
                               }else{
                                   System.out.println("Server 2 unavailable, connecting to Server 1");
                                   iSafePathServer = serverList.get(0);
                                   session.setServer(0);
                               }
                               try{
                               session = iSafePathServer.getRoute(session);
                               } catch (Exception e) {
						   			// TODO Auto-generated catch block
						   			//e.printStackTrace();
						   			session.setMessage("403");
						   			}	
                               counter++;
                               if(counter == 10){
                                   System.out.println("Sorry, both Servers are temporarily unavailable!!");
                                   return session;
                               }
                        }
                        break;
           case "400": 
        	   break;
        	   
       }
      
        return session;
    }

    @Override
    public void sendGPS(Session session, double[] currentLocation, boolean humanFlag) throws RemoteException {
       // session.setCurrentLocation(currentLocation);
        if(humanFlag){
            System.out.println("Alert Server");
        }else{
            System.out.println("All good!");
        }
    }

}
