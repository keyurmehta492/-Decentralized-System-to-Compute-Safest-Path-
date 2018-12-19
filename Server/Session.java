package Server;

import java.io.Serializable;
import java.util.ArrayList;

public class Session implements Serializable{


	private static final long serialVersionUID = 1L;

	String source;
	String destination;
	String response;
	ArrayList<Route> allRoutes;
	String message;
    int n;
    
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getServer() {
		return n;
	}
	public void setServer(int n) {
		this.n = n;
	}
	public ArrayList<Route> getAllRoutes() {
		return allRoutes;
	}
	public void setAllRoutes(ArrayList<Route> allRoutes) {
		this.allRoutes = allRoutes;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
}
