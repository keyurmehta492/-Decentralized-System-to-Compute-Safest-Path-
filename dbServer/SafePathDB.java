package dbServer;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class SafePathDB {

	public static void main(String[] args) throws RemoteException {
		
		
		int port = 4444;
		String hostName;
		String bindLocation;
		
		hostName = "//in-csci-rrpc01.cs.iupui.edu";
		
		
		System.setSecurityManager(new SecurityManager());
		
		IDBSafePathServer ic = new dbServer();
		  
        System.out.println("Starting DB server.. ");
         
        bindLocation = hostName + ":" + port + "/DBServer";
        
        try{
            Naming.rebind(bindLocation,ic);
        }catch(Exception e){
            System.out.println(e);
        }
        System.out.println("DB Server Started at: " + bindLocation);
			
	}//main

}//DBSafePathServer