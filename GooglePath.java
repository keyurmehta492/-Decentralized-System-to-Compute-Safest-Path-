package Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.lang.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GooglePath {

	String key = "<API Key>";
	String baseUrl = "https://maps.googleapis.com/maps/api/directions/json?";
	URL url;
	HttpURLConnection con;
	JSONObject myResponse;
	StringBuilder sb = new StringBuilder();	
	JSONObject response;
	BufferedReader in;
	String inputLine;
	StringBuffer content = null;
	ArrayList<Steps>  latlng;
	ArrayList<Route> allRoutes;
	
	JSONObject getAllRoutes(String source, String destination) {
		
		try {
			//create http request URL with parameters
			sb.setLength(0);
			sb.append(baseUrl);
			sb.append("origin="+URLEncoder.encode(source, "UTF-8"));
			sb.append("&destination="+URLEncoder.encode(destination, "UTF-8"));
			sb.append("&key="+key);
			sb.append("&alternatives=true");
			url = new URL(sb.toString());
			
			//call http request
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);
			
			int status = con.getResponseCode();
			System.out.println("The response code is : " + status);
			
			//get the response
			if(status == 200) {
				in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				
				content = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					content.append(inputLine);
				}
			}

			in.close();
			con.disconnect();
			
			response = new JSONObject(content.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}//getAllRoutes
	
	public ArrayList<Route> getAllSteps(JSONObject response){
		
		JSONArray routes;
		allRoutes = new ArrayList<Route>();
		try {
			routes = response.getJSONArray("routes");
		
		//Traverse through each routes
		for(int i=0;i<routes.length();i++){
			Route r = new Route();
			r.setId(i);
			
			JSONObject route = routes.getJSONObject(i);
			JSONArray legs = route.getJSONArray("legs");
			JSONObject leg = legs.getJSONObject(0);

			//get distance of the route
			r.setDistance(leg.getJSONObject("distance").optString("text"));
		
			JSONArray steps = leg.getJSONArray("steps");
			
			latlng = new ArrayList<Steps>();
			
			//add each step in the route
			for(int j = 0 ; j < steps.length() ; j++){
				Steps step = new Steps();
				
				step.setLat(steps.getJSONObject(j).optJSONObject("end_location").optDouble("lat"));
				step.setLng(steps.getJSONObject(j).optJSONObject("end_location").optDouble("lng"));
				
			    latlng.add(step);
			}
			
			r.setStep(latlng);
			
			allRoutes.add(r);
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return allRoutes;
	}//getAllSteps
	
	public JSONObject getSafestPath(JSONObject response, int returnRouteIndex) {
		
		try {
			JSONArray routes = response.getJSONArray("routes");
			JSONArray OnlyRoute = new JSONArray();
			//Traverse through each routes
			for(int i=0;i<routes.length();i++){
				if(i == returnRouteIndex){
					//System.out.println("Removed" + i);
					OnlyRoute.put(routes.get(i));
				}

			}
//
			response.remove("routes");
			//System.out.println(response.toString());
			response.put("routes", OnlyRoute);
//			System.out.println("====================");
//			System.out.println(response.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
		
	}
	
}//class GooglePath
