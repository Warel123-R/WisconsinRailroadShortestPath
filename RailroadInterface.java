import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.List;
import java.util.NoSuchElementException;

public interface RailroadInterface<NodeType, EdgeType extends Number> extends GraphADT<NodeType, EdgeType>{
int size = 0;//how many vertices/edges in the object(up to	discretion)

Hashtable<String,?> getNodes(); //returns the nodes hashtable

int getSize(); //returns size value(amount of vertices/edges there are)

//gets the optimal route and returns list of objects containing the vertices/edges along the
//path, if either location or destination does not exist, it will throw exception
public List<?> getRoute(NodeType location, NodeType destination) throws NoSuchElementException;
	
//same as above but with implementation of stops along the way(stops must be provided in order of when they wish to occur)
public List<?> getRouteWithStops(NodeType location, List<NodeType> stops) throws NoSuchElementException;

//gets distance between two points(by adding weight values of all edges on a path),
//returns its int value, or an exceptions if either param doesn’t exist
public Double getDistance(NodeType location, NodeType destination) throws NoSuchElementException;
}
