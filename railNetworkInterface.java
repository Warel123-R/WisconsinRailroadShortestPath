public interface railNetworkInterface<NodeType, EdgeType extends Number> {
    public void addStation(StationDW added);
    public void removeStation(StationDW removed);
    public double getEdgeLength(StationDW location, StationDW destination);

}
