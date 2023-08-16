import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class RailroadFrontendFD implements RailroadFrontendInterface {

  @SuppressWarnings("rawtypes")
  protected loadStationInterface loader = new loadStationDW();
  protected RailRoadBackendInterface backend = new RailRoadBackend(loader);
  public ArrayList<StationInterface> path = new ArrayList<StationInterface>();
  
  /**
   * Prompts the user for a file to load and attempts to load in the data to the DijkstraGraph
   */
  @Override
  public void loadData() {
    
    try {
      backend.loadData("rail.dot");
    } catch (Exception e) {
      System.out.println("Error loading data");
    }

    
    /*Scanner scnr = new Scanner(System.in);
    System.out.println("Please enter the name of the file you would like to load:");

    String fileName;
    try {
      fileName = scnr.nextLine();
      backend.loadData(fileName); 
    } catch (NoSuchElementException e) {
      System.out.println("Not a valid file name");
    } catch (FileNotFoundException ee) {
      System.out.println("File not found");
    } catch (Exception eee) {
      System.out.println("File can not be loaded");
    } finally {
      scnr.close();
    }*/
    
  }

  /**
   * Adds a NodeType object to the path ArrayList data field
   */
  @Override
  public void addStop(StationInterface station) {
    path.add(station);
  }

  /**
   * Empties the path ArrayList data field so that the user can start over
   */
  @Override
  public void clear() {
    if (this.path == null || this.path.isEmpty()) {
      return;
    }
    path.clear();
  }

  @SuppressWarnings("unchecked")
  @Override
  public ArrayList<StationInterface> findRoute() {
    if(path.size() < 2) {
      System.out.println("Please select at least a starting and ending station for your route");
    } 
    return backend.getRoute(path);
  }

  /**
   * Prints stats about our data held within the Dijkstra graph
   */
  @Override
  public void displayStats() {
    System.out.println(backend.getStats());
  }
  
  /**
   * Allows the javaFX class to access the data held within the graph so that it can be iterated
   * through and properly displayed on the UI
   * 
   * @return Hashtable of stations to be displayed on the map
   */
  @SuppressWarnings("unchecked")
  public Hashtable<String, StationInterface> getNodes() {
    return backend.getNodes();
  }
    
}
