
public class railNetworkBD <NodeType, EdgeType extends Number>
extends BaseGraph<NodeType,EdgeType> implements railNetworkInterface{
  railNetworkInterface<StationInterface, Double> graph;



  @Override
  public void addStation(StationDW added) {

  }

  @Override
  public void removeStation(StationDW removed) {

  }

  @Override
  public double getEdgeLength(StationDW location, StationDW destination) {
    return 0;
  }
}
