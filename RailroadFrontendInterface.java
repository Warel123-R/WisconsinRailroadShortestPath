import java.util.ArrayList;

/*
 * stores each user click as a node in an ArrayList “path” with the head of the array (first click)
 * being the starting position.
 */
public interface RailroadFrontendInterface {

  //protected ArrayList<Node> path = new ArrayList<Node>();

  public void loadData();

  public void addStop(StationInterface station); // stores node in “path”

  public void clear(); // button which removes all nodes from “path”

  public ArrayList<?> findRoute(); // runs on “Run” button click,
  // passes “this.path” to backend which returns route as an
  // ArrayList<edge> which will pass to JavaFX class to
  // highlight said route

  public void displayStats(); // number of stations in data set
  // total distance of railway (all edge lengths)
}
