import java.io.FileNotFoundException;
import java.util.*;

public class RailroadAE<NodeType, EdgeType extends Number> extends DijkstraGraph<NodeType, EdgeType>
		implements RailroadInterface<NodeType, EdgeType> {
	
	//returns the nodes hashtable
	@Override
	public Hashtable<String,NodeType> getNodes(){
		Hashtable<String, NodeType> ht = new Hashtable<>();
		for(NodeType n: nodes.keySet()){
			StationDW s= (StationDW)n;
			//System.out.println(s.getName());
			ht.put(s.getName(), (NodeType) s);
		}
		return ht;

	}
	
	//returns size value(amount of vertices/edges there are)
	@Override
	public int getSize() {
		return getNodeCount();
	}

	//gets the optimal route and returns list of objects containing the vertices/edges along the
	//path, if either location or destination does not exist, it will throw exception
	@Override
	public List<?> getRoute(NodeType location, NodeType destination) throws NoSuchElementException {
		return shortestPathData(location, destination);
	}
	
	//same as above but with implementation of stops 
	//along the way(stops must be provided in order of when they wish to occur)
	@SuppressWarnings("unchecked")
	@Override
	public List<?> getRouteWithStops(NodeType location, List<NodeType> stops) throws NoSuchElementException {
		List<NodeType> finalRoute = new ArrayList<NodeType>();
		finalRoute.add(location);
		
		List<NodeType> addition = new ArrayList<NodeType>();
		
		for(NodeType stop : stops){
			
			addition = (List<NodeType>) getRoute( finalRoute.get(finalRoute.size()-1) , stop);
			
			for(int i = 1; i < addition.size(); i++) {
				finalRoute.add(addition.get(i));
			}
			
		}
		return finalRoute;
	}
	
	//gets distance between two points(by adding weight values of all edges on a path),
	//returns its int value, or an exceptions if either param doesn’t exist
	@Override
	public Double getDistance(NodeType location, NodeType destination) throws NoSuchElementException {
		return (Double) shortestPathCost(location, destination);
	}

}
