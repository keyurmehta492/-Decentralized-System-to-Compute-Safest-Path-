package Server;

import java.io.Serializable;
import java.util.ArrayList;

public class Steps implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	Double lat;
	Double lng;
	ArrayList<Incidents> incident;
	
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public ArrayList<Incidents> getIncident() {
		return incident;
	}
	public void setIncident(ArrayList<Incidents> incident) {
		this.incident = incident;
	}
}
