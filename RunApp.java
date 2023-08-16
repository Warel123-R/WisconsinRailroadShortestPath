import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RunApp {
    public static void main(String [] args) throws FileNotFoundException {
        loadStationDW ls = new loadStationDW();
        railNetworkDW<StationDW, Double> networkloaded=null;

        networkloaded= ls.loadRailroad("rail.dot");

        RailroadInterface<StationDW, Double> finalgraph = new RailroadAE<StationDW, Double>();

        Set<StationDW> setOfKeys = networkloaded.graph.nodes.keySet();
        Iterator<StationDW> itr = setOfKeys.iterator();
        while (itr.hasNext()) {
            StationDW currstation = itr.next();
            finalgraph.insertNode(currstation);
        }

        Set<StationDW> setOfKeys2 = networkloaded.graph.nodes.keySet();
        Iterator<StationDW> itr2 = setOfKeys2.iterator();
        while (itr2.hasNext()) {
            StationDW currstation = itr2.next();
            for (Object o : networkloaded.graph.nodes.get(currstation).edgesLeaving) {

                //cast the predecessor and successor of the edge to station objects
                BaseGraph2.Node pred = (((BaseGraph2.Edge) o).predecessor);
                StationDW s = (StationDW) pred.data;

                BaseGraph2.Node successor = (((BaseGraph2.Edge) o).successor);
                StationDW s2 = (StationDW) successor.data;

                finalgraph.insertEdge(s ,s2, (Double)((BaseGraph2.Edge) o).data);
            }
        }
        StationDW nodestart= networkloaded.getStation("GreenBay");
        StationDW nodeend = networkloaded.getStation("Watertown");
        List<StationDW> routelist = (List<StationDW>)finalgraph.getRoute(nodestart, nodeend);
        for(StationDW s: routelist){
            System.out.println(s.getName());
        }
        RailRoadBackend backend = new RailRoadBackend(ls);
    }
}
