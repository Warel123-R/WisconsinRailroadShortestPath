import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;

public interface RailRoadBackendInterface {
    public void loadData(String filename) throws FileNotFoundException;
    public String getStats(); //Displays statistics about railway network
    public ArrayList<StationInterface> getRoute(ArrayList<StationInterface> path); //Takes a path from the FD and return the shortest route through the path of nodes
    public ArrayList<?> getEdgesInRoute(ArrayList<StationInterface> route); //Takes a route from the FD and returns the edges in the route
    public Hashtable<String, StationInterface> getNodes();
}
