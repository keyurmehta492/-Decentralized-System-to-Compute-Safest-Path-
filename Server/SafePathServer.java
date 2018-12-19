package Server;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class SafePathServer {

	public static void main(String[] args) throws RemoteException {
		
		int number = 0;
		int port = 4444;
		String hostName;
		String bindLocation;
		
		System.setSecurityManager(new SecurityManager());
		
		ISafePathServer sp = new AppServer();
		
        if(args.length < 1){
            System.out.println("You should enter the Server number");
            System.exit(-1);
        }else{
            number = Integer.parseInt(args[0]);
        }
        
        System.out.println("Starting server "+ number);
        
        if(number==1)
        	hostName = "thunder";
        else
        	hostName = "lightning";
        
        bindLocation = "//" + hostName + ".cs.iupui.edu:" + port + "/SafePathServer";
        
        try{
            Naming.rebind(bindLocation, sp);
        }catch(Exception e){
            System.out.println(e);
        }
        System.out.println("Server Started at: " + bindLocation);
	
	}//main

}//SafePathServer
