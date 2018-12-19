package Server;

import java.io.Serializable;
import java.util.ArrayList;

public class Route implements Serializable{


	private static final long serialVersionUID = 1L;

	int id;
	String distance;
	ArrayList<Steps> step;
	
	public int getId() {
		return id;
	}
	public ArrayList<Steps> getStep() {
		return step;
	}
	public void setStep(ArrayList<Steps> step) {
		this.step = step;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	
}
