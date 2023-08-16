import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.NoSuchElementException;

public class RailroadBD<NodeType, EdgeType extends Number> extends              
DijkstraGraph<NodeType, EdgeType> implements RailroadInterface<NodeType,        
EdgeType> {

  @Override
  public Hashtable<String, ?> getNodes() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getSize() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public List<?> getRoute(NodeType location, NodeType destination) throws NoSuchElementException {
    ArrayList<StationInterface> route = new ArrayList<StationInterface>();
    StationBD testStation1 = new StationBD("Station 1", 1.2, 2.2);
    StationBD testStation2 = new StationBD("Station 2", 1.2, 2.2);
    route.add(testStation1);
    route.add(testStation2);
    return route;
  }

  @Override
  public List<?> getRouteWithStops(NodeType location, List<NodeType> stops)
      throws NoSuchElementException {
    ArrayList<StationInterface> route = new ArrayList<StationInterface>();
    route.add((StationInterface) location);
    route.add((StationInterface) stops.get(0));
    return route;
  }

  @Override
  public Double getDistance(NodeType location, NodeType destination) throws NoSuchElementException {
    return 2.0;
  }

}
