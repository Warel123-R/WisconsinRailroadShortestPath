import java.io.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * This class reads through the .dot file of rail networks and extracts the distances between stations as well as the names
 * and coordinates of different railway stations
 */
public class loadStationDW implements loadStationInterface{
    railNetworkDW<StationDW, Double> rn; //the railNetwork we are returning which will have the nodes and edges in it

    /**
     * This method will read through the .dot file's contents and parse through it to find the station names,
     * the longitude coordinates, the latitude coordinates, and then pass this information to the railNetwork class
     * to make the Dijkstra graph
     * @param filename the name of the file to read with the data in it
     * @return a railNetwork object that represents the Dijkstra graph with the nodes and edges
     * @throws FileNotFoundException if the file is not found
     */
    @Override
    public railNetworkDW<StationDW, Double> loadRailroad(String filename) throws FileNotFoundException {
        //for the purposes of reading our .dot file's contents, we are reading it the same way we would a text file:
        Scanner sc = new Scanner(new FileReader(filename));

        //first line not needed
        String getridofstring=sc.nextLine();

        //initialize the railNetwork object that will represent our Dijkstra graph
        rn = new railNetworkDW<StationDW, Double>();

        while(sc.hasNextLine()){
            String station =  sc.nextLine().trim();
            //If we get to the end, break
            if(station.equals("}"))break;
            String firstnode="";
            String secondnode="";
            double distance=0;
            double longsource=0;
            double latsource=0;
            
            double longdest=0;
            double latdest=0;

            int firstdash=0;
            boolean foundend=false;

            for (int i = 0; i < station.length(); i++) {
                //find the position of the "--" and use it to find the names of the source and destinations
                if(i!=station.length()-1&&station.substring(i, i+2).equals("--")){
                    firstnode=station.substring(0, i).trim();
                    firstdash=i+2;

                }
                //use the position of the "[" to find the end position of the secondnode string
                if(station.charAt(i) == '['){
                    secondnode=station.substring(firstdash, i).trim();

                    String firstcoords="";
                    String secondcoords="";
                    int tocoord=0;

                    //find the position of the label attribute and since our label contains coordinates,
                    //loop through additional times to find the comma and to find the longitude and latitude of the
                    //source and destination nodes
                    if(station.substring(i+1, i+7).equals("label=")) {
                        for (int j = i + 8; j < station.length(); j++) {
                            if (j!=station.length()-1&&station.substring(j, j + 2).equals("to")) {
                                firstcoords = station.substring(i + 8, j).trim();
                                tocoord = j + 2;
                            }
                            if (station.charAt(j) == '"') {
                                secondcoords = station.substring(tocoord, j).trim();
                            }
                        }

                        for (int j = 0; j < firstcoords.length(); j++) {
                            if(firstcoords.charAt(j)==','){
                                latsource=Double.parseDouble(firstcoords.substring(1,j).trim());
                                longsource=Double.parseDouble(firstcoords.substring(j+1,firstcoords.length()-1).trim());
                            }
                        }
                        for (int j = 0; j < secondcoords.length(); j++) {
                            if(secondcoords.charAt(j)==','){
                                latdest=Double.parseDouble(secondcoords.substring(1,j).trim());
                                longdest=Double.parseDouble(secondcoords.substring(j+1,secondcoords.length()-1).trim());
                            }
                        }
                    }
                }
                //find the distance= part of the .dot file and extract the weight of the edge
                if(!foundend&&station.substring(i, i+9).equals("distance=")){
                    for (int j = i+9; j < station.length(); j++) {
                        if(station.charAt(j)==']'){
                            distance = Double.parseDouble(station.substring(i+9, j));
                        }
                    }
                    foundend=true;
                }
            }
            //now, pass all of this to the MakeGraph method of the railNetwork, which will make the graph with the
            //nodes and the edges
            rn.MakeGraph(rn, firstnode, secondnode, distance, longsource, latsource, longdest, latdest);

        }

        return rn;
    }
}
