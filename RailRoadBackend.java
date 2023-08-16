// --== CS400 File Header Information ==--
// Name: Aarav Mohan
// Email: amohan33@wisc.edu
// Group and Team: CQ Blue
// Group TA: Rahul Choudhary
// Lecturer: Florian Heimerl
// Notes to Grader: N/A

import java.io.FileNotFoundException;
import java.util.*;

/**
 * A class to implement backend methods for the RailRoad Router Project
 * @author Aarav Mohan
 *
 */
public class RailRoadBackend implements RailRoadBackendInterface{
  RailroadInterface<StationInterface, Double> railroad; //Railroad data structure
  loadStationInterface loader; //Loader class from DW
    
  
  /**
   * A constructor to initialize loader
   * @param loadStationInterface loader to be initialized
   *
   */
  public RailRoadBackend(loadStationInterface loader) {
    this.loader = loader;
    this.railroad= new RailroadAE<>();
  }
  /**
   * A constructor to initialize loader and railroad data structure
   * 
   *@param loadStationInterface loader to be initialized
   *@param RailroadInterface to be initialized
   */
  public RailRoadBackend(loadStationInterface loader, RailroadInterface railroad) {
    this.railroad = railroad;
    this.loader = loader;
  }
  
  @Override
  /**
   * This method loads the data using a method from the DW class into the data structure
   *
   * @param filename filename of the file with the data to be loaded into the data structure
   */
  public void loadData(String filename) throws FileNotFoundException {
    //Loading data structure into first graph

   railNetworkDW<StationDW, Double> routes = loader.loadRailroad(filename);
   //Converting graph into Railroad data structure

    Set<StationDW> setOfKeys = routes.graph.nodes.keySet();
    Iterator<StationDW> itr = setOfKeys.iterator();
    while (itr.hasNext()) {
      StationDW currstation = itr.next();
      //System.out.println(currstation.getName());



      railroad.insertNode(currstation);
    }


    Set<StationDW> setOfKeys2 = routes.graph.nodes.keySet();
    Iterator<StationDW> itr2 = setOfKeys2.iterator();
    while (itr2.hasNext()) {
      StationDW currstation = itr2.next();
      for (Object o : routes.graph.nodes.get(currstation).edgesLeaving) {

        //cast the predecessor and successor of the edge to station objects
        BaseGraph2.Node pred = (((BaseGraph2.Edge) o).predecessor);
        StationDW s = (StationDW) pred.data;

        BaseGraph2.Node successor = (((BaseGraph2.Edge) o).successor);
        StationDW s2 = (StationDW) successor.data;

        railroad.insertEdge(s ,s2, (Double)((BaseGraph2.Edge) o).data);
      }
    }

  }

  @Override
  /**
   * This method returns the statistics of the railroad using data from the data structure
   *
   * @return String that contains the data about the railroad
   */
  public String getStats() {
    //Returning string with relevant stats
    return "Total number of stations = " + railroad.getNodeCount() + " and railways between stations = " + railroad.getEdgeCount();
  }

  @Override
  /**
   * This method calculates the shortest path when given a list with a starting location and destination or a 
   * starting location and destination with stops inbetween
   *
   * @param path an arraylist of nodes that are the starting location and destination which may includes stops inbetween
   * @return an arraylist of edges/vertices that are the shortest path
   */
  public ArrayList<StationInterface> getRoute(ArrayList<StationInterface> path) {
    //Checking if path has stops
    if(path.size() > 2) {

     //Since it does, removing first node that is the origin location and assigning it to a temp variable
    StationInterface temp = path.get(0);
    path.remove(0);
    ArrayList<StationInterface> shortestPath = (ArrayList<StationInterface>) railroad.getRouteWithStops(temp, path);
      /*for (int i = 0; i < shortestPath.size(); i++) {
        System.out.println(shortestPath.get(i).getName());
      }*/
      path.add(0, temp);
    return shortestPath;
    }else {
      //Since it doesn't loading first and second nodes as origin and destination
      ArrayList<StationInterface> shortestPath = (ArrayList<StationInterface>) railroad.getRoute(path.get(0), path.get(1));
      return shortestPath;
    }
  }

  @Override
  /**
   * This method calcualates and returns the edges in a given route. 
   *
   * @param route an arraylist that is the shortest path trhough a graph
   * @return an arraylist of doubles that are the values of the edges in the given shortest route
   */
  public ArrayList<?> getEdgesInRoute(ArrayList<StationInterface> route) {
    //Checking edges between nodes and adding them to a list
    ArrayList<Double> edges = new ArrayList<Double>();
    for (int i = 0; i < route.size()-1; i++) {
      edges.add(railroad.getDistance(route.get(i), route.get(i+1)));
    }
    return edges;
  }

  public Hashtable<String, StationInterface> getNodes(){
    return (Hashtable<String, StationInterface>) this.railroad.getNodes();
  }


}
