// --== CS400 File Header Information ==--
// Name: Aarav Mohan
// Email: amohan33@wisc.edu
// Group and Team: CQ Blue
// Group TA: Rahul Choudhary
// Lecturer: Florian Heimerl
// Notes to Grader: N/A

import static org.junit.jupiter.api.Assertions.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;

import edu.wisc.cs.cs400.JavaFXTester;
/**
 * A class to test backend methods
 * @author Aarav Mohan
 *
 */
public class BackendDeveloperTests extends JavaFXTester {
   public BackendDeveloperTests() {
        // you must specify the Application being tested by passing its class   
        // to the parent class through the constructor, like this:              
        super(RailroadUI.class);
    }

    @Test
  /**
   * Testing getRoute with no stops
   */
  public void test1MyCode() {
    //Testing getRoute 
    RailroadInterface<StationInterface, Double> testRailRoad =
        new RailroadBD<StationInterface, Double>();
    loadStationInterface testloader = new loadStationBD();
    RailRoadBackend testBackend = new RailRoadBackend(testloader, testRailRoad);
    StationBD testStation1 = new StationBD("Station 1", 1.2, 2.2);
    StationBD testStation2 = new StationBD("Station 2", 1.2, 2.2);
    ArrayList<StationInterface> testList = new ArrayList<StationInterface>();
    testList.add(testStation1);
    testList.add(testStation2);
    List<StationInterface> results = testBackend.getRoute(testList);
    
    assertEquals(true, results.get(0).getName().equals(testStation1.getName()));
    assertEquals(true, results.get(1).getName().equals(testStation2.getName()));
    assertEquals(true, results.get(0).getCoordinatesLat() == testStation1.getCoordinatesLat());
    assertEquals(true, results.get(1).getCoordinatesLong() == testStation2.getCoordinatesLong());
  }
  
  @Test
  /**
   * Testing getStats
   */
  public void testMyCode2() {
    //Testing getStats
    RailroadInterface<StationInterface, Double> testRailRoad =
        new RailroadBD<StationInterface, Double>();
    loadStationInterface testloader = new loadStationBD();
    RailRoadBackend testBackend = new RailRoadBackend(testloader, testRailRoad);
    StationBD testStation1 = new StationBD("Station 1", 1.2, 2.2);
    StationBD testStation2 = new StationBD("Station 2", 1.2, 2.2);
    testRailRoad.insertNode(testStation1);
    testRailRoad.insertNode(testStation2);
    testRailRoad.insertEdge(testStation1, testStation2, 1.0);
    testRailRoad.insertEdge(testStation2, testStation1, 1.0);
    assertEquals("Total number of stations = 2 and railways between stations = 2", testBackend.getStats());
    StationBD testStation3 = new StationBD("Station 3", 1.2, 2.2);
    StationBD testStation4 = new StationBD("Station 4", 1.2, 2.2);
    testRailRoad.insertNode(testStation3);
    testRailRoad.insertNode(testStation4);
    testRailRoad.insertEdge(testStation3, testStation4, 1.0);
    testRailRoad.insertEdge(testStation4, testStation3, 1.0);
    assertEquals("Total number of stations = 4 and railways between stations = 4", testBackend.getStats());
  }
  
  @Test
  /**
   * Testing get edges in route
   */
  public void testMyCode3() {
    //Testing get edges in route
    RailroadInterface<StationInterface, Double> testRailRoad =
        new RailroadBD<StationInterface, Double>();
    loadStationInterface testloader = new loadStationBD();
    RailRoadBackend testBackend = new RailRoadBackend(testloader, testRailRoad);
    StationBD testStation1 = new StationBD("Station 1", 1.2, 2.2);
    StationBD testStation2 = new StationBD("Station 2", 1.2, 2.2);
    ArrayList<StationInterface> testList = new ArrayList<StationInterface>();
    testList.add(testStation1);
    testList.add(testStation2);
    ArrayList<Double> results = (ArrayList<Double>) testBackend.getEdgesInRoute(testList);
    assertEquals(2, results.get(0));
    StationBD testStation3 = new StationBD("Station 3", 1.2, 2.2);
    StationBD testStation4 = new StationBD("Station 4", 1.2, 2.2);
    testList.add(testStation3);
    testList.add(testStation4);
    ArrayList<Double> results1 = (ArrayList<Double>) testBackend.getEdgesInRoute(testList);
    assertEquals(2, results1.get(1));
  }
  
  @Test
  /**
   * Testing getRoute with multiple stops
   */
  public void testMyCode4() {
    //Testing getRoute with multiple stops
    RailroadInterface<StationInterface, Double> testRailRoad =
        new RailroadBD<StationInterface, Double>();
    loadStationInterface testloader = new loadStationBD();
    RailRoadBackend testBackend = new RailRoadBackend(testloader, testRailRoad);
    StationBD testStation1 = new StationBD("Station 1", 1.2, 2.2);
    StationBD testStation2 = new StationBD("Station 2", 1.2, 2.2);
    StationBD testStation3 = new StationBD("Station 3", 1.2, 2.2);
    ArrayList<StationInterface> testList = new ArrayList<StationInterface>();
    testList.add(testStation1);
    testList.add(testStation2);
    testList.add(testStation3);
    List<StationInterface> results = testBackend.getRoute(testList);
    assertEquals(true, results.get(0).getName().equals(testStation1.getName()));
    assertEquals(true, results.get(1).getName().equals(testStation2.getName()));
  }
  
  @Test
  /**
   * Testing loadData
   */
  public void testMyCode5() {
    //Ensuring loadData is being called properly
    RailroadInterface<StationInterface, Double> testRailRoad =
        new RailroadBD<StationInterface, Double>();
    loadStationInterface testloader = new loadStationBD();
    RailRoadBackend testBackend = new RailRoadBackend(testloader, testRailRoad);
    Exception exception = assertThrows(FileNotFoundException.class, () -> {
      testBackend.loadData("hi");
    });
    
    assertEquals("E",
        exception.getMessage());
  }
    @Test
  /**
   * Testing that buttons run without exception from Frontend Developer Class
   */
  public void testCodeReviewOfFrontendDeveloper1() {
    try {
      clickOn("#Load");
      clickOn("#Stats");
      clickOn("#GreenBay");
      clickOn("#Whitewater");
      clickOn("#Run");
      clickOn("#Clear");
      clickOn("#Clear");
      clickOn("#Run");
      clickOn("#Kenosha");
      clickOn("#Janesville");
      clickOn("#Run");
    }catch(Exception e) {
	System.out.println(e.getMessage());
      fail("Exception was thrown when it should be running normally");
      }
  }
     @Test
  /**
   * Testing that only 2 buttons are loaded from Frontend Developer Class
   * before load Data button is used
   */
     public void testCodeReviewOfFrontendDeveloper2() {
	 Button loadButton = lookup("#Load").query();
	Button statsButton = lookup("#Stats").query();
	Button runButton = lookup("#Run").query();
	Button stats = lookup("#Stats").query();
	Button run = lookup("#Run").query();
	Button clear = lookup("#Clear").query();
	Button load = lookup("#Load").query();
	if (!stats.isDisabled() || !run.isDisabled() || !clear.isDisabled() ) {
	    fail("Stats, run and clear should only be enabled after load");
	}
	clickOn("#Load");
	if (!loadButton.isDisable()) {
	    fail("Load button should not be abled after used once");
	}
	if (clear.isDisabled() || stats.isDisabled() || run.isDisabled()) {
	    fail("Buttons should have enabled after load was run");
	}
	

     }

@Test
  /**
   * Testing the loadData from DW and BD correctly load the file and that it is properly displayed on the GUI
   */
     public void testIntegration1() {
     
	 clickOn("#Load");
	  Button loadButton = lookup("#Load").query();
	  if (!loadButton.isDisable()) {
	    fail("Load button should not be abled after used once");
	}
	  try{
	      clickOn("#GreenBay");
	      clickOn("#Whitewater");
	      clickOn( "#Janesville");
	      clickOn("#Racine");
	      clickOn("#Milwaukee");
	      clickOn("GreenBay");
	      clickOn("Oshkosh");
	  }catch(Exception e){
	      fail("None of these clicks should throw an exception");
	  }
	  try{
	  clickOn("Iowa");
	  fail("This should throw an exception as it is not in the dataset");
	  }catch(Exception e){
	      
	  }
     }

    @Test
    /**
     *Testing that DW, BD and AE methods are properly integrated
    */
    public void testIntegration2() {
	clickOn("#Load");
	try{
	clickOn("#Milwaukee");
	clickOn("#Madison");
	clickOn("#Run");
	}catch(Exception e){
	    fail("The path should be created without any exceptions");
	}
	}
}


