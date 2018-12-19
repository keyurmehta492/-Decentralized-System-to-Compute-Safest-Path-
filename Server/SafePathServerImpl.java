package Server;

import static java.util.Arrays.asList;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import dbServer.IDBSafePathServer;

public class SafePathServerImpl  {

	
    private String source,destination;
    GooglePath path;
    JSONObject response;
    ArrayList<Route> allRoutes;
    double[] NormCrimecost;
    double[] incidentRank;
    Incidents temp = new Incidents();
    final double THRESHOLD = 0.15;
    
    int port = 4444;
    String dbServerPath = "//in-csci-rrpc01.cs.iupui.edu:" + port + "/DBServer";
    
    SafePathServerImpl() throws RemoteException{
        path = new GooglePath();
        NormCrimecost = new double[18];
        incidentRank = new double[18];
        
        //get the db server reference
        
        
        normalizedValues();


    }//constructor

    public void normalizedValues(){
        
    	//Incidnets cost calculated by CDASH
    	Integer[] Crimecost = {500,3922,4860,5032,5251,5265,5480,3523,6170,7864,10534,11000,16428,19537,21398,30000,41247,50000};

        int min = Collections.min(asList(Crimecost));
        int max = Collections.max(asList(Crimecost));

        System.out.println("Incidents Normalized Value");
        //System.out.println(min + "\t" + max);
        
        //Normalized the incident rank and its cost
        for(int i=0;i<18;i++){

            NormCrimecost[i] = (double) ((double)((i+1) - 1) / (18 - 1)) * (1 - 0.1) + 0.1;
            incidentRank[i] =  (double) ((double)(Crimecost[i] - min) / (max - min)) * (10-1) + 1;

            System.out.println(NormCrimecost[i] + "\t" + incidentRank[i]);
        }
    }

    
    public Session getRoute(Session session) throws RemoteException  {

        source = session.getSource();
        destination = session.getDestination();
        IDBSafePathServer dbSer = null;
        try {
        	dbSer = (IDBSafePathServer) Naming.lookup(dbServerPath);
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
        //get the paths from google 
        System.out.println("\n==========================================");
        
        System.out.println("Getting possible paths...");
        System.out.println("Source: " + source + " \t Desination:" + destination);
        try {
            response = path.getAllRoutes(source, destination);
        }catch (Exception e){
            session.setMessage("400");
            return session;
        }
        session.setResponse(response.toString());
        //get all steps of paths
        System.out.println("Get all steps of all routes...");
        allRoutes = path.getAllSteps(response);
        System.out.println("Total routes retrieved: " + allRoutes.size());
        
        //get all crime data
        System.out.println("Getting crime incidents on all routes...");
        allRoutes = dbSer.findIncidents(allRoutes);
        session.setAllRoutes(allRoutes);

        if(allRoutes.size()== 1) {
            session.setMessage("200");
            return session;
        }
        if(allRoutes.size()== 0) {
            session.setMessage("400");
            return session;
        }

        //calculate the safest path
        double[] routeDistance = new double[allRoutes.size()];
        double[] routeCost = new double[allRoutes.size()];
        HashMap<Double,Integer> RouteSeq = new HashMap<Double,Integer>(); 
        
        System.out.println("\n==========================================");
        for(int i=0;i<allRoutes.size();i++){
            String temp = allRoutes.get(i).getDistance();
            routeDistance[i] = Double.parseDouble(temp.substring(0, temp.indexOf(' ')));
            RouteSeq.put(routeDistance[i],i);
            System.out.println("The distance of route " + i + " is: " + routeDistance[i]);
            
            routeCost[i] = calcCrimeCost(allRoutes.get(i).getStep());
            System.out.println("The cost of route " + i + " is: " + routeCost[i]);
        }
        
        System.out.println("==========================================");
        //sort with distance
        System.out.println("Sorted Routes");
        
        sortArray(routeCost, routeDistance);
        for(int i=0;i<allRoutes.size();i++){
            System.out.println("The distance of route " + i + " is: " + routeDistance[i]);
            System.out.println("The cost of route " + i + " is: " + routeCost[i]);
        }
//        for(int i=0;i<routeCost.length;i++){
//        	System.out.println(routeCost[i] + "\t" + routeDistance[i]);
//        }
        
        double minCost = Arrays.stream(routeCost).min().getAsDouble();
//        double minDistance = Arrays.stream(routeDistance).min().getAsDouble();

       // System.out.println(minCost +" \t " +minDistance);
        //find distance of that route from JSON and delete remaining.
        int returnRouteIndex = 0;

        for(int i=0; i<routeCost.length; i++){
            if(routeCost[i] == minCost){
                returnRouteIndex = i;
                break;
            }
            if(((routeCost[i] - minCost) / routeCost[i])<= THRESHOLD){
                returnRouteIndex = i;
                break;
            }
        }

        returnRouteIndex = RouteSeq.get(routeDistance[returnRouteIndex]);
        System.out.println("\n==========================================");
        System.out.println("Return route: " + returnRouteIndex);
        response = path.getSafestPath(response,returnRouteIndex);
        session.setResponse(response.toString());
        allRoutes = path.getAllSteps(response);
        
        session.setAllRoutes(allRoutes);
        session.setMessage("200");
        return session;
    }//getRoute

    private void sortArray(double[] routeCost, double[] routeDistance) {
        int mid;
        for(int i=0; i<routeDistance.length; i++){
                mid = i;
                for (int j = i+1; j < routeDistance.length; j++)
                    if (routeDistance[j] < routeDistance[mid])
                        mid = j;
                // swap the values
                double tmp1 = routeDistance[mid];
                routeDistance[mid] = routeDistance[i];
                routeDistance[i] = tmp1;
                double tmp2 = routeCost[mid];
                routeCost[mid] = routeCost[i];
                routeCost[i] = tmp2;
            }
    }

    double calcCrimeCost(ArrayList<Steps> steps){

        ArrayList<Integer> incidents = new ArrayList<Integer>();
        ArrayList<String> dateStart = new ArrayList<String>();
        double finalCost = 0;
        for(int i=0;i<steps.size();i++){
            for(int j=0; j < steps.get(i).getIncident().size();j++){
                temp = steps.get(i).getIncident().get(j);

                incidents.add(temp.getIncidentCode());
                dateStart.add(temp.getmonth()+"/"+temp.getDay()+"/"+temp.getYear()+" "+
                        temp.getHour()+":00:00");

            }
            //System.out.println("incidents size: " + incidents.size());
            
            //calculate time
            double[] time = CalcuTime(dateStart);
            // calculate incident probabilities
            HashMap<Integer, Double> probabilities = CalcuProbability(incidents);
            // compute cost
            for(int j=0; j < incidents.size();j++){
                finalCost +=  (probabilities.get(incidents.get(j)) * time[j]) + (incidentRank[incidents.get(j)-1] * NormCrimecost[incidents.get(j) -1]);
            }
        }
        // finalCost is the summation of the incidents that took place in that route
        return finalCost;
    }


    // Time calculation
    double[] CalcuTime(ArrayList<String> dateStart){
        double[] time = new double[dateStart.size()];
        // converting to simple date format
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Calendar c = Calendar.getInstance();
        // incident date
        Date d1 = null;
        // current date
        Date d2 = new Date();
        for(int i=0; i<dateStart.size(); i++){
            // time calculation for each date
            try {
                d1 = format.parse(dateStart.get(i));
                // hours difference
                long diffHours = (d2.getTime() - d1.getTime()) / (60 * 60 * 1000) % 24;
                // get day of week
                c.setTime(d2);
                int dayOfWeek1 = c.get(Calendar.DAY_OF_WEEK);
                c.setTime(d1);
                int dayOfWeek2 = c.get(Calendar.DAY_OF_WEEK);
                // Comparing day of week
                if(dayOfWeek1 == dayOfWeek2)
                    time[i] = 0.5;
                else if( ((dayOfWeek1>0 && dayOfWeek1<6) && (dayOfWeek2>0 && dayOfWeek2<6)) || (dayOfWeek1>5 && dayOfWeek2>5))
                    time[i] = 0.25;
                else
                    time[i] = 0.125;
                // Comparing hours
                if(diffHours < 5)
                    time[i] += 0.5;
                else if(diffHours < 12)
                    time[i] += 0.25;
                else
                    time[i] += 0.125;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return time;
    }

    // Incident Calculation
    HashMap<Integer, Double> CalcuProbability(ArrayList<Integer> incidents){
        HashMap<Integer, Double> probabilities = new HashMap<Integer, Double>();
        // Compute total number of each incidents
        for (Integer i : incidents) {
            if(probabilities.containsKey(i))
            {
                probabilities.put(i, probabilities.get(i)+1.0);
            }
            else
            {
                probabilities.put(i, 1.0);
            }
        }
        // total number of incidents in that step
        double total = (double) incidents.size();
        Iterator it = probabilities.entrySet().iterator();
        // Compute probabilities
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            double value = (double) pair.getValue();
            // Probability of event to reoccur = No. of time an event occurred / total number of events
            double probability = value/total;
            probabilities.put((int) pair.getKey(), probability);
        }
        return probabilities;
    }

}//class SafePathServerImpl