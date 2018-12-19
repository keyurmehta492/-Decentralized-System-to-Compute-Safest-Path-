package Server;

import java.io.Serializable;

public class Incidents implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int IncidentCode;
	private int CrimeID;
    private int hour;
    private int cost;
    private double lat;
    private double longi;
    private int month;
    private int day;
    private int year;
	private int zip;
    private int cell;
    private int crimeCellCount;
 
    public Incidents() {
    	
    }
    
    public Incidents(int IncidentCode, int CrimeID, int hour, int cost, double lat, double longi, int month, int day, int year, int zip, int cell, int crimeCellCount){
        super();
        this.IncidentCode = IncidentCode;
		this.CrimeID = CrimeID;
        this.hour = hour;
        this.cost = cost;
        this.lat = lat;
        this.longi = longi;
        this.month = month;
        this.day = day;
        this.year = year;
		this.zip = zip;
        this.cell = cell;
        this.crimeCellCount = crimeCellCount;
    }

    public int getIncidentCode() {
        return IncidentCode;
    }
    public void setIncidentCode(int IncidentCode) {
        this.IncidentCode = IncidentCode;
    }
	
	 public int getCrimeID() {
        return CrimeID;
    }

    public void setCrimeID(int CrimeID) {
        this.CrimeID = CrimeID;
    }
    public int getHour() {
        return hour;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public int getCost() {
        return cost;
    }
    public void setCost(int cost) {
        this.cost = cost;
    }
    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLongi() {
        return longi;
    }
    public void setLongi(double longi) {
        this.longi = longi;
    }
    public int getmonth() {
        return month;
    }
    public void setmonth(int month) {
        this.month = month;
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
	
	 public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public int getCrimeCellCount() {
        return crimeCellCount;
    }

    public void setCrimeCellCount(int crimeCellCount) {
        this.crimeCellCount = crimeCellCount;
    }

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    @Override
    public String toString() {
        return "pastData [IncidentCode=" + IncidentCode + ", hour=" + hour + ", cost=" + cost + ", lat=" + lat + ", longi=" + longi +", month=" + month + ", day="+ day +", year="+ year + ", zip=" +zip +", cell=" +cell+", crimecellcount="+crimeCellCount+"]";
    }

}
