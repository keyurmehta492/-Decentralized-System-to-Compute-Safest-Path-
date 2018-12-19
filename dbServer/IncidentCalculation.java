package dbServer;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Server.Incidents;
import Server.Route;
import Server.Steps;

public class IncidentCalculation {

	// JDBC driver name and database URL
	final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	final String DB_URL = "jdbc:mysql://localhost/mehtake_db";
	
	//  Database credentials
	final String USER = "mehtake";
	final String PASS = "oodp507";
	 
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;

	IncidentCalculation() throws RemoteException{
		
	}//Constructor
	
	public ArrayList<Route> findIncidents(ArrayList<Route> allRoutes)  {
		
			try{
			//Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			//Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);

			//Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			System.out.println("Route size: " + allRoutes.size());
			
			for(int i=0;i<allRoutes.size();i++){
				Route route = allRoutes.get(i);
				for(int j=0;j<allRoutes.get(i).getStep().size();j++){
					
					ArrayList<Incidents> incident = new ArrayList<Incidents>();
					
					Steps step = route.getStep().get(j);
					double lat = step.getLat();
					double lng = step.getLng();
					
					double rightLong= lng+0.01;
					double leftLong=lng-0.01;
					double topLat= lat+0.01;
					double bottomLat=lat-0.01;
					
					sql = "SELECT * FROM crime_data where Latitude <='"+topLat+"' and Latitude >='"+bottomLat+
							"' and Longitude <='"+rightLong+"' and Longitude >="+leftLong;
					//		"' and Longitude <='"+rightLong+"' and Longitude >="+leftLong;
					
					rs = stmt.executeQuery(sql);
					
					//Extract data from result set
					while(rs.next()){
						//Retrieve by column name
						int incidentcode  = rs.getInt("incident_code");
						int crimeid = rs.getInt("crime_id");
						int hour = rs.getInt("Hour");
						int cost = rs.getInt("Cost");
						double lati = rs.getDouble("Latitude");
						double longi = rs.getDouble("Longitude");
						int month = rs.getInt("Month");
						int day = rs.getInt("Day");
						int year = rs.getInt("Year");
						int zip = rs.getInt("Zipcode");
						int cell = rs.getInt("Cell");
						int crimeCellCount = rs.getInt("CrimeCellCount");

						Incidents ic = new Incidents(incidentcode,crimeid,hour,cost,lati,longi,month,day,year,zip,cell,crimeCellCount);
						incident.add(ic);
												
					}//while rs
					
					System.out.println("Route: " + i + " Step: " + j + "\t total incidents: " + incident.size());
					
					step.setIncident(incident);
					
					//Closing resultSet connections
					rs.close();
					
				}//Step loop
			
			}//route loop
	
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}//end try

		return allRoutes;
	}//findIncidents
	
}