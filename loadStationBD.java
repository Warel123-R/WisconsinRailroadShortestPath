import java.io.FileNotFoundException;

public class loadStationBD implements loadStationInterface {

  @Override
  public railNetworkDW<StationDW, Double> loadRailroad(String filename)
      throws FileNotFoundException {
    if(filename.equals("hi")) {
      throw new FileNotFoundException("E");
    }
    return new railNetworkDW<StationDW, Double>();
  }

  
}
