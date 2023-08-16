
public class StationFD implements StationInterface {

  public String name;
  public double coordinatelat;
  public double coordinatelong;

  public StationFD(String name, double coordinatelat, double coordinatelong) {
    this.name = name;
    this.coordinatelat = coordinatelat;
    this.coordinatelong = coordinatelong;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public double getCoordinatesLat() {
    return this.coordinatelat;
  }

  @Override
  public double getCoordinatesLong() {
    return this.coordinatelong;
  }

}
