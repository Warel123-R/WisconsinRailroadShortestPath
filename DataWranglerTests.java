import java.io.*;
import java.nio.Buffer;
import java.util.*;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import edu.wisc.cs.cs400.JavaFXTester;

/**
 * This is the tester class for the DataWrangler
 */
public class DataWranglerTests extends JavaFXTester{
    public DataWranglerTests(){
        super(RailroadUI.class);
    }
    @Test
    /**
     * This test tests the "loadStation" class's ability to load in the data and read the data properly. It makes sure
     * an exception is thrown for an invalid file and that the values are properly intialized into the graph
     */
    public void test1(){
        loadStationDW ls = new loadStationDW();
        railNetworkDW<StationDW, Double> networkloaded=null;

        //first we try to load data with an invalid file name
        try{
            networkloaded=ls.loadRailroad("RandomIncorrectFile.dot");
            Assertions.fail("No exception was thrown when there should've been an exception thrown!");
        }
        catch(FileNotFoundException f){
            //this means the correct exception was thrown
        }
        catch(Exception e){
            Assertions.fail("The wrong exception was thrown!!");
        }

        //now we use the correct file name
        try{
            networkloaded= ls.loadRailroad("DataWranglerTestfile.dot");
        }
        catch(Exception e){
            Assertions.fail("There should have been no exception thrown since the name of the data file is correct!");
        }

        //make sure the station that we load up is correctly initialized with the correct name and longitude and
        //latitude coordinates
        StationDW milwaukee = networkloaded.getStation("Milwaukee");
        if(!milwaukee.getName().equals("Milwaukee")|| milwaukee.getCoordinatesLat()!=43.04 ||
                milwaukee.getCoordinatesLong()!=-87.91){
            Assertions.fail("The data was not read properly because the Station does not have the correct data" +
                    "values for the name and coordinates!");
        }

        //make sure that the edges between the stations are correct in the graph
        boolean edgecorrect1=false;
        boolean edgecorrect2=false;
        boolean edgecorrect3=false;
        Set<StationDW> setOfKeys2 = networkloaded.graph.nodes.keySet();
        Iterator<StationDW> itr2 = setOfKeys2.iterator();
        while (itr2.hasNext()) {
            StationDW currstation = itr2.next();
            for (Object o : networkloaded.graph.nodes.get(currstation).edgesLeaving) {

                BaseGraph2.Node pred = (((BaseGraph2.Edge) o).predecessor);
                StationDW s = (StationDW) pred.data;

                BaseGraph2.Node successor = (((BaseGraph2.Edge) o).successor);
                StationDW s2 = (StationDW) successor.data;

                if(s.getName().equals("Milwaukee")&& s2.getName().equals("Madison") &&
                        (Double)((BaseGraph2.Edge) o).data==75.5){
                    edgecorrect1=true;
                }
                else if(s.getName().equals("GreenBay")&& s2.getName().equals("Kenosha") &&
                        (Double)((BaseGraph2.Edge) o).data==134.02){
                    edgecorrect2=true;
                }
                else if(s.getName().equals("Milwaukee")&& s2.getName().equals("Kenosha") &&
                        (Double)((BaseGraph2.Edge) o).data==34.9){
                    edgecorrect3=true;
                }
            }
        }
        if(!edgecorrect1 || !edgecorrect2 || !edgecorrect3){
            Assertions.fail("The values for the edges between the nodes weren't read in properly");
        }
    }

    @Test
    /**
     * This method tests the "makeGraph" method of the railNetwork class and checks that the graph's Nodes are populated
     * correctly and that the correct number of nodes are the graph
     */
    public void test2(){
        loadStationDW ls = new loadStationDW();
        railNetworkDW<StationDW, Double> networkloaded=null;
        try{
            networkloaded= ls.loadRailroad("DataWranglerTestfile.dot");
        }
        catch(Exception e){
            Assertions.fail("There should have been no exception thrown since the name of the data file is correct!");
        }

        //these are stations that we need to make sure are correctly intialized nodes in the graph
        StationDW MilwaukeeStation = new StationDW("Milwaukee", 43.04, -87.91);
        StationDW MadisonStation = new StationDW("Madison", 43.07, -89.40);
        StationDW GreenBayStation = new StationDW("GreenBay", 44.52, -88.02);
        StationDW KenoshaStation = new StationDW("Kenosha", 42.58, -87.82);
        StationDW RacineStation = new StationDW("Racine", 42.73, -87.78);

        boolean milwaukee=false;
        boolean madison=false;
        boolean greenBay =false;
        boolean kenosha =false;
        boolean racine = false;

        //set the variables to true if the stations exist
        Set<StationDW> setOfKeys = networkloaded.graph.nodes.keySet();
        Iterator<StationDW> itr = setOfKeys.iterator();
        while (itr.hasNext()) {
            StationDW currstation = itr.next();
            if(currstation.equals(MilwaukeeStation)){
                milwaukee=true;
            }
            else if(currstation.equals(MadisonStation)){
                madison=true;
            }
            else if(currstation.equals(GreenBayStation)){
                greenBay=true;
            }
            else if(currstation.equals(KenoshaStation)){
                kenosha=true;
            }
            else if(currstation.equals(RacineStation)){
                racine=true;
            }
        }
        //make sure all the station nodes are in the graph
        if(!milwaukee || !madison || !greenBay || !kenosha || !racine){
            Assertions.fail("The Station Nodes were not properly added to the graph!");
        }

        //also make sure the node count is correct. If the node count is too high, this means it added duplicate nodes
        // when it shouldn't have
        if(networkloaded.getNodeCount()!=14){
            Assertions.fail("The nodes were not initialized properly or there was an overcounting/undercounting of" +
                    "nodes!");
        }

    }

    @Test
    /**
     * This method tests the "makeGraph" method of the railNetwork class and checks that the graph's Edges are populated
     * correctly and that the edges have the correct values for the weights
     */
    public void test3(){

        loadStationDW ls = new loadStationDW();
        railNetworkDW<StationDW, Double> networkloaded=null;
        try{
            networkloaded= ls.loadRailroad("DataWranglerTestfile.dot");
        }
        catch(Exception e){
            Assertions.fail("There should have been no exception thrown since the name of the data file is correct!");
        }

        //create variables for different edges we are checking that exist in the graph
        Set<StationDW> setOfKeys2 = networkloaded.graph.nodes.keySet();
        Iterator<StationDW> itr2 = setOfKeys2.iterator();
        boolean racineappleton=false;
        boolean milwaukeemadison=false;
        boolean milwaukeekenosha=false;
        boolean bellevuewhitewater=false;
        boolean eauclairejanesville=false;
        boolean appletonracine=false;
        boolean sheboyganwatertown=false;
        while (itr2.hasNext()) {
            StationDW currstation  =itr2.next();
            for(Object o: networkloaded.graph.nodes.get(currstation).edgesLeaving){

                //cast the predecessor and successor of the edge to station objects
                BaseGraph2.Node pred = (((BaseGraph2.Edge) o).predecessor);
                StationDW s = (StationDW)pred.data;

                BaseGraph2.Node successor = (((BaseGraph2.Edge) o).successor);
                StationDW s2 = (StationDW)successor.data;

                //make sure the edge exists with the correct predecessor, successor, and weight value
                //if it does, set the variable for that edge to true
                if(s.getName().equals("Racine")&& s2.getName().equals("Appleton")&&
                        (Double)((BaseGraph2.Edge) o).data==110.74){
                    racineappleton=true;
                }
                if(s.getName().equals("Milwaukee")&& s2.getName().equals("Madison")&&
                        (Double)((BaseGraph2.Edge) o).data==75.5){
                    milwaukeemadison=true;
                }
                if(s.getName().equals("Milwaukee")&& s2.getName().equals("Kenosha")&&
                        (Double)((BaseGraph2.Edge) o).data==34.9){
                    milwaukeekenosha=true;
                }
                if(s.getName().equals("Bellevue")&& s2.getName().equals("Whitewater")&&
                        (Double)((BaseGraph2.Edge) o).data==118.45){
                    bellevuewhitewater=true;
                }
                if(s.getName().equals("EauClaire")&& s2.getName().equals("Janesville")&&
                        (Double)((BaseGraph2.Edge) o).data==192.19){
                    eauclairejanesville=true;
                }
                if(s.getName().equals("Appleton")&& s2.getName().equals("Racine")&&
                        (Double)((BaseGraph2.Edge) o).data==110.74){
                    appletonracine=true;
                }
                if(s.getName().equals("Sheboygan")&& s2.getName().equals("Watertown")&&
                        (Double)((BaseGraph2.Edge) o).data==91.6){
                    sheboyganwatertown=true;
                }
            }
        }

        //make sure all the variables are set to true
        if(!racineappleton||!milwaukeemadison||!milwaukeekenosha||!bellevuewhitewater||!eauclairejanesville
                ||!appletonracine||!sheboyganwatertown){
            Assertions.fail("The edges were not added to the graph properly or the weights of the edges are incorrect!");
        }

        //Make sure the total edge count is correct
        if(networkloaded.edgeCount!=16){
            Assertions.fail("The edges weren't initialized properly since there isn't the right number of edges" +
                    "in the graph!");
        }



    }

    @Test
    /**
     * This method tests the addStation, removeStation, addRail, and removeRail methods of the railNetwork class
     */
    public void test4(){
        loadStationDW ls = new loadStationDW();
        railNetworkDW<StationDW, Double> networkloaded=null;
        try{
            networkloaded= ls.loadRailroad("DataWranglerTestfile.dot");
        }
        catch(Exception e){
            Assertions.fail("There should have been no exception thrown since the name of the data file is correct!");
        }

        //test adding a new station and make sure it exists in the graph
        StationDW randomstation = new StationDW("Chicago", 35, -90);
        networkloaded.addStation(randomstation);
        if(!networkloaded.containsNode(randomstation)){
            Assertions.fail("The addStation method did not work properly!");
        }

        //test removing a staion and make sure it doesn't exist in the graph
        StationDW toRemove = new StationDW("GreenBay", 44.52, -88.02);
        networkloaded.removeStation(toRemove);
        //we want to try to get a reference to the station. However, since we removed it, the getStation() method should
        //return null if the station was removed properly
        StationDW referenceingraph = networkloaded.getStation(toRemove.getName());
        if(referenceingraph!=null){
            Assertions.fail("The removeStation did not work properly!");
        }

        //test adding a rail between two stations. First get the reference of the first station and the second station
        //and call the method
        StationDW referenceingraphmil= networkloaded.getStation("Milwaukee");
        StationDW referenceingraphracine = networkloaded.getStation("Racine");
        networkloaded.addRail(referenceingraphmil, referenceingraphracine, 38.78);

        //set a boolean variable to check if the edge exists
        //now, loop through all the edges
        boolean addededgeingraph=false;
        Set<StationDW> setOfKeys2 = networkloaded.graph.nodes.keySet();
        Iterator<StationDW> itr2 = setOfKeys2.iterator();
        while (itr2.hasNext()) {
            StationDW currstation = itr2.next();
            for (Object o : networkloaded.graph.nodes.get(currstation).edgesLeaving) {

                BaseGraph2.Node pred = (((BaseGraph2.Edge) o).predecessor);
                StationDW s = (StationDW) pred.data;

                BaseGraph2.Node successor = (((BaseGraph2.Edge) o).successor);
                StationDW s2 = (StationDW) successor.data;

                //find an edge with the predecessor, successor, and weight matching and if all of these match, set
                //the variable to true
                if(s.equals(referenceingraphmil) && s2.equals(referenceingraphracine) &&
                        (Double)((BaseGraph2.Edge) o).data==38.78){
                    addededgeingraph=true;
                }

            }
        }
        //make sure the variable is true. If not, the edge was not added
        if(!addededgeingraph){
            Assertions.fail("The addRail method did not work properly!");
        }

        //now remove the edge we just added by calling the removeRail method
        boolean removeedge=true;
        networkloaded.removeRail(referenceingraphmil, referenceingraphracine);
        Set<StationDW> setOfKeys = networkloaded.graph.nodes.keySet();
        Iterator<StationDW> itr = setOfKeys.iterator();
        //loop through all of the edges again
        while (itr.hasNext()) {
            StationDW currstation = itr.next();
            for (Object o : networkloaded.graph.nodes.get(currstation).edgesLeaving) {

                BaseGraph2.Node pred = (((BaseGraph2.Edge) o).predecessor);
                StationDW s = (StationDW) pred.data;

                BaseGraph2.Node successor = (((BaseGraph2.Edge) o).successor);
                StationDW s2 = (StationDW) successor.data;
                //check that an edge exists with the same predecessor, successor, and weight. If an edge exists,
                //we set our variable to FALSE this time because this is not what we want since we just removed it
                if(s.equals(referenceingraphmil) && s2.equals(referenceingraphracine) &&
                        (Double)((BaseGraph2.Edge) o).data==38.78){
                    addededgeingraph=false;
                }

            }
        }
        //if the variable is false, this means the edge was not removed properly
        if(!addededgeingraph){
            Assertions.fail("The removeRail method did not work properly!");
        }

    }

    @Test
    /**
     * This method tests that the "Station" objects are initialized properly with the appropriate data fields
     */
    public void test5(){

        loadStationDW ls = new loadStationDW();
        railNetworkDW<StationDW, Double> networkloaded=null;
        try{
            networkloaded= ls.loadRailroad("DataWranglerTestfile.dot");
        }
        catch(Exception e){
            Assertions.fail("There should have been no exception thrown since the name of the data file is correct!");
        }

        //find references to different stations in the graph
        StationDW milwaukee= networkloaded.getStation("Milwaukee");
        StationDW racine = networkloaded.getStation("Racine");
        StationDW sheboygan = networkloaded.getStation("Sheboygan");

        //check that the data fields of the stations are initialized properly. Check that the name, longitude, and
        //latitude coordinates are correct for the stations
        if(!milwaukee.getName().equals("Milwaukee")|| milwaukee.getCoordinatesLat()!=43.04 ||
                milwaukee.getCoordinatesLong()!=-87.91){
            Assertions.fail("The station wasn't initialized properly!");
        }

        if(!racine.getName().equals("Racine")|| racine.getCoordinatesLat()!=42.73 ||
                racine.getCoordinatesLong()!=-87.78){
            Assertions.fail("The station wasn't initialized properly!");
        }

        if(!sheboygan.getName().equals("Sheboygan")|| sheboygan.getCoordinatesLat()!=43.75 ||
                sheboygan.getCoordinatesLong()!=-87.71){
            Assertions.fail("The station wasn't initialized properly!");
        }
    }

    /**
     * This is my code review of the algorithm engineer. I am testing the functionality of the insertNode, insertEdge,
     * getSize(), and the shortest path functionality for the route.
     */
    @Test
    public void CodeReviewOfAlgorithmEngineer1(){
        try {
            //creating the graph
            RailroadAE<String, Double> graph = new RailroadAE<String, Double>();
            graph.insertNode("A");
            graph.insertNode("B");
            graph.insertNode("C");
            graph.insertNode("D");
            graph.insertNode("E");
            graph.insertNode("F");
            graph.insertNode("G");

            graph.insertEdge("A", "B", 2.0);
            graph.insertEdge("B", "A", 2.0);

            graph.insertEdge("A", "C", 6.0);
            graph.insertEdge("C", "A", 6.0);

            graph.insertEdge("B", "D", 5.0);
            graph.insertEdge("D", "B", 5.0);

            graph.insertEdge("C", "D", 8.0);
            graph.insertEdge("D", "C", 8.0);

            graph.insertEdge("D", "F", 15.0);
            graph.insertEdge("F", "D", 15.0);

            graph.insertEdge("D", "E", 8.0);
            graph.insertEdge("E", "D", 8.0);

            graph.insertEdge("E", "F", 6.0);
            graph.insertEdge("F", "E", 6.0);

            graph.insertEdge("F", "G", 6.0);
            graph.insertEdge("G", "F", 6.0);

            graph.insertEdge("E", "G", 2.0);
            graph.insertEdge("G", "E", 2.0);

            //testing the getSize() method for AE

            Assertions.assertEquals(7, graph.getSize());

            //checks size method after removal
            graph.removeNode("G");
            Assertions.assertEquals(6, graph.getSize());

            //testing the shortest path route functionality:
            List<String> shortestPath1 = (List<String>) graph.getRoute("A", "D");
            Assertions.assertEquals("[A, B, D]", shortestPath1.toString());

            List<String> shortestPath2 = (List<String>) graph.getRoute("C", "E");
            Assertions.assertEquals("[C, D, E]", shortestPath2.toString());


            List<String> shortestPath3 = (List<String>) graph.getRoute("A", "E");
            Assertions.assertEquals("[A, B, D, E]", shortestPath3.toString());

            List<String> shortestPath4 = (List<String>) graph.getRoute("D", "F");
            Assertions.assertEquals("[D, E, F]", shortestPath4.toString());

        } catch (Exception e) {
            //this means an unexpected exception was thrown
            Assertions.fail("An unexpected exception was thrown!");
        }


    }

    /**
     * I am testing the getDistance and the getRoutewithStops functionality and also making sure an exception
     * is thrown when there is no route from point A to point B
     */
    @Test
    public void CodeReviewOfAlgorithmEngineer2(){

        //creating the graph
        RailroadAE<String, Double> graph = new RailroadAE<String, Double>();
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");
        graph.insertNode("F");
        graph.insertNode("G");

        graph.insertEdge("A", "B", 2.0);
        graph.insertEdge("B", "A", 2.0);

        graph.insertEdge("A", "C", 6.0);
        graph.insertEdge("C", "A", 6.0);

        graph.insertEdge("B", "D", 5.0);
        graph.insertEdge("D", "B", 5.0);

        graph.insertEdge("C", "D", 8.0);
        graph.insertEdge("D", "C", 8.0);

        graph.insertEdge("D", "F", 15.0);
        graph.insertEdge("F", "D", 15.0);

        graph.insertEdge("D", "E", 8.0);
        graph.insertEdge("E", "D", 8.0);

        graph.insertEdge("E", "F", 6.0);
        graph.insertEdge("F", "E", 6.0);

        graph.insertEdge("F", "G", 6.0);
        graph.insertEdge("G", "F", 6.0);

        graph.insertEdge("E", "G", 2.0);
        graph.insertEdge("G", "E", 2.0);

        //testing the getDistance()
        Assertions.assertEquals(5.0, graph.getDistance("B", "D"));
        Assertions.assertEquals(10.0, graph.getDistance("D", "G"));
        Assertions.assertEquals(14.0, graph.getDistance("D", "F"));
        Assertions.assertEquals(7.0, graph.getDistance("A", "D"));

        //testing the getRoutewithStops()
        List<String> shortestpathwithstops = (List<String>) graph.getRouteWithStops("A",
                new ArrayList<String>(Arrays.asList("D","E")));
        Assertions.assertEquals("[A, B, D, E]", shortestpathwithstops.toString());

        List<String> shortestpathwithstops2 = (List<String>) graph.getRouteWithStops("B",
                new ArrayList<String>(Arrays.asList("F","G")));
        Assertions.assertEquals("[B, D, E, F, G]", shortestpathwithstops2.toString());

        List<String> shortestpathwithstops3 = (List<String>) graph.getRouteWithStops("A",
                new ArrayList<String>(Arrays.asList("C","D", "G")));
        Assertions.assertEquals("[A, C, D, E, G]", shortestpathwithstops3.toString());

        //testing route and routewithstops with unreachable node
        graph.insertNode("L");
        try {
            graph.getRoute("D", "L");
            Assertions.fail("should have thrown nosuchelementexception");
        } catch (NoSuchElementException e) {
            //this means the correct exception was thrown
        } catch (Exception e) {
            Assertions.fail("should have thrown nosuchelementexception");
        }

    }
    @Test
    public void IntegrationTest1(){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("output.txt");
            PrintStream printStream = new PrintStream(fileOutputStream);
            System.setOut(printStream);

            clickOn("#Load");
            clickOn("#EauClaire");
            clickOn("#GreenBay");
            clickOn("#Janesville");
            clickOn("#Run");
            clickOn("#Stats");
            printStream.flush();
            printStream.close();
            fileOutputStream.close();
            BufferedReader br = new BufferedReader(new FileReader("output.txt"));
            assertEquals(br.readLine(), "EauClaire -> Sheboygan -> GreenBay -> Bellevue -> Janesville");
            assertEquals(br.readLine(), "Total number of stations = 16 and railways between stations = 84");

        } catch (Exception e) {
            Assertions.fail("An unexpected exception was thrown even though no exception should've been thrown!");
        }
    }
    @Test
    public void IntegrationTest2(){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("output.txt");
            PrintStream printStream = new PrintStream(fileOutputStream);
            System.setOut(printStream);

            clickOn("#Load");
            clickOn("#Kenosha");
            clickOn("#Appleton");
            clickOn("#Madison");
            clickOn("#Run");
            clickOn("#Stats");
            BufferedReader br = new BufferedReader(new FileReader("output.txt"));
            assertEquals(br.readLine(), "Kenosha -> Appleton -> Milwaukee -> Madison");
            assertEquals(br.readLine(), "Total number of stations = 16 and railways between stations = 84");
            clickOn("#Clear");
            clickOn("#GreenBay");
            clickOn("#Sheboygan");
            clickOn("#Janesville");
            clickOn("#Run");
            assertEquals(br.readLine(), "GreenBay -> Sheboygan -> Racine -> Janesville");
            printStream.flush();
            printStream.close();
            fileOutputStream.close();

        } catch (Exception e) {
            Assertions.fail("An unexpected exception was thrown even though no exception should've been thrown!");
        }
    }

}
