import java.util.Iterator;
import java.util.Set;

/**
 * This is the railNetwork clas, which represents a Dijkstra graph and extends the BaseGraph
 * @param <NodeType> the NodeType, which is Station in our case
 * @param <EdgeType> the EdgeType, which is double in our case
 */
public class railNetworkDW<NodeType, EdgeType extends Number>
        extends BaseGraph2<NodeType,EdgeType> implements railNetworkInterface {
    railNetworkDW<StationDW, Double> graph; //this represents the Dijkstra graph

    /**
     * This method adds nodes and edges to the Dijkstra graph
     * @param graph the graph that we are adding to
     * @param firstnode the name of the source node
     * @param secondnode the name of the destination node
     * @param distance the distance between the two nodes (edge weight)
     * @param longsource the longitude coordinate of the source node
     * @param latsource the latitude coordinate of the source node
     * @param longdest the longitude coordinate of the destination node
     * @param latdest the latitude coordinate of the destination node
     */
    public void MakeGraph(railNetworkDW<StationDW, Double> graph, String firstnode, String secondnode, double distance, double longsource, double latsource, double longdest, double latdest){
        this.graph=graph;
        //create the two station objects
        StationDW s1= new StationDW(firstnode, latsource, longsource);
        StationDW s2= new StationDW(secondnode, latdest, longdest);
        //make sure the graph doesn't already contain the nodes for the Stations. If the graph already does contain
        //the Station nodes, then find the reference to the Station in the graph with the getStation method
        if(!graph.containsNode(s1)){
            graph.insertNode(s1);
        }
        else{
            s1=this.getStation(s1.getName());
        }
        if(!graph.containsNode(s2)){
            graph.insertNode(s2);
        }
        else{
            s2=this.getStation(s2.getName());
        }

        //now insert two edges, since our graph is bidirectional
        graph.insertEdge(s1, s2, distance);
        graph.insertEdge(s2, s1, distance);


    }

    /**
     * This method finds a reference to a Station object if it exists in the graph. If it does not exist, it returns null
     * @param stationname the name of the Station we are trying to find a reference for in the graph
     * @return the Station object reference if it exists in the graph, and null otherwise
     */
    public StationDW getStation(String stationname){
        //loop through all of the stations and if the station name matches the argument to the method, then return a
        //reference to that station that we found
        Set<StationDW> setOfKeys = graph.nodes.keySet();
        Iterator<StationDW> itr = setOfKeys.iterator();
        while (itr.hasNext()) {
            StationDW s = itr.next();
            if(s.getName().equals(stationname)){
                return s;
            }
        }
        return null;
    }

    /**
     * This method adds a Station to the graph
     * @param added the Station we are adding
     */
    public void addStation(StationDW added){

        StationDW newadded = this.getStation(added.getName());
        //this means the Station doesn't already exist in the graph. If it is not null, then it already exists and
        //we don't need to do anything
        if(newadded==null){
            graph.insertNode(added);
        }

    }

    /**
     * This method removes a Station from the graph
     * @param removed the station we are trying to remove from the graph
     */
    public void removeStation(StationDW removed){
        //find a reference to the station and then remove it
        StationDW toremove=this.getStation(removed.getName());
        graph.removeNode(toremove);
    }

    @Override
    public double getEdgeLength(StationDW location, StationDW destination) {
        Set<StationDW> setOfKeys2 = this.graph.nodes.keySet();
        Iterator<StationDW> itr2 = setOfKeys2.iterator();
        while (itr2.hasNext()) {
            StationDW currstation = itr2.next();
            for (Object o : this.graph.nodes.get(currstation).edgesLeaving) {

                BaseGraph2.Node pred = (((BaseGraph2.Edge) o).predecessor);
                StationDW s = (StationDW) pred.data;

                BaseGraph2.Node successor = (((BaseGraph2.Edge) o).successor);
                StationDW s2 = (StationDW) successor.data;

                //find an edge with the predecessor, successor, and weight matching and if all of these match, set
                //the variable to true
                if(s.equals(location) && s2.equals(destination))
                {
                    return (Double)((BaseGraph2.Edge) o).data;
                }

            }
        }
        return -1;
    }

    /**
     * This method adds an Edge between two Stations (adds a Rail)
     * @param source the source Station
     * @param destination the destination Station
     * @param distance the weight of the edge (distance between the stations)
     */
    public void addRail(StationDW source, StationDW destination, double distance){
        //find a reference to the source and destination station and insert bidirectional edge (both ways)
        source = this.getStation(source.getName());
        destination=this.getStation(destination.getName());
        graph.insertEdge(source, destination, distance);
        graph.insertEdge(destination, source, distance);
    }

    /**
     * This method removes an Edge between two Stations (removes a Rail)
     * @param source the source Station
     * @param destination the destination Station
     */
    public void removeRail(StationDW source, StationDW destination){
        //find a reference to the source and destination station and remove the bidirectional edge (both ways)
        source = this.getStation(source.getName());
        destination=this.getStation(destination.getName());
        graph.removeEdge(source, destination);
        graph.removeEdge(destination, source);
    }




}
