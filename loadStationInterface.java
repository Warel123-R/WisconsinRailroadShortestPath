import java.io.FileNotFoundException;

public interface loadStationInterface {
    public railNetworkDW<StationDW, Double> loadRailroad (String filename) throws FileNotFoundException;

}
