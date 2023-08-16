/**
 * This class represents a Station object
 */
public class StationDW implements StationInterface {
    public String name; //the name of the station
    public double coordinatelat; //the latitude coordinate of the station
    public double coordinatelong; //the longitude coordinate of the station

    /**
     * The contructor for the Station
     * @param name the name of the station
     * @param coordinatelat the latitude coordinate of the station
     * @param coordinatelong the longitude coordinate of the station
     */
    public StationDW(String name, double coordinatelat, double coordinatelong){
        this.name=name;
        this.coordinatelat=coordinatelat;
        this.coordinatelong=coordinatelong;
    }

    /**
     * The getter for the name of the Station
     * @return the name of the Station
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * The getter for the latitude coordinate of the station
     * @return the latitude coordinate of the station
     */
    @Override
    public double getCoordinatesLat() {
        return coordinatelat;
    }

    /**
     * The getter for the longtiude coordinate of the station
     * @return the longitude coordinate of the station
     */
    @Override
    public double getCoordinatesLong() {
        return coordinatelong;
    }

    /**
     * The .equals method which is used to check if two stations are the same
     * @param o another station we are comparing the station to
     * @return true if the stations are the same and false otherwise
     */
    @Override
    public boolean equals(Object o){
        StationDW otherstation= (StationDW)o;
        return this.name.equals(otherstation.getName()) && this.coordinatelat== otherstation.getCoordinatesLat()
                && this.coordinatelong==otherstation.getCoordinatesLong();
    }
}
