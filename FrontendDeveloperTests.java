import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Line;

import edu.wisc.cs.cs400.JavaFXTester;

/**
 * A couple of simple tests that demonstrate how the provided
 * JavaFXTester class and .jar file can be used to check test
 * the simple GUI implemented by the provided SampleMain class
 * @date 2023.4
 */
public class FrontendDeveloperTests extends JavaFXTester {
    // this JavaFXTester class implements the TestFX FXRobot class, documented here:
    // https://testfx.github.io/TestFX/docs/javadoc/testfx-core/javadoc/org.testfx/org/testfx/api/FxRobot.html

    public FrontendDeveloperTests() {
        // you must specify the Application being tested by passing its class
        // to the parent class through the constructor, like this: 
        super(RailroadUI.class);
    }

    /**
     * Tests one part (Label) of map display to ensure it is functioning properly
     */
    @Test
    public void test1() {
        Label label = lookup("#Title").query();
        assertEquals("Wisconsin Railroad Router", label.getText());
    }

    /**
     * Ensures load button and exit buttons are only ones available at start
     */
    @Test
    public void test2() {
      Button loadButton = lookup("#Load").query();
      Button statsButton = lookup("#Stats").query();
      Button runButton = lookup("#Run").query();
        if (loadButton.isDisable()) {
          fail("Load button should start as enabled");
      } if (!statsButton.isDisabled() || !runButton.isDisabled()) {
        fail("Run and Stats buttons should be disabled until data is loaded");
      }
      
    }

    /**
     * Ensures run and stats buttons become enabled after data is loaded
     */
    @Test
    public void test3() {
	Button loadButton = lookup("#Load").query();
	Button statsButton = lookup("#Stats").query();
	Button runButton = lookup("#Run").query();
	clickOn("#Load");
	if (!loadButton.isDisable()) {
	    fail("Load button should disable after running once");
	}
	if (statsButton.isDisabled() || runButton.isDisabled()) {
	    fail("Run and stats buttons should enable after loading data");
	}
	
    }

    /**
     * Ensures user does not receive an exception when clicking run button with
     * fewer than 2 stations selected
     */
    @Test
    public void test4() {
	clickOn("#Load");
	try {
	    clickOn("#Run");
	} catch (Exception e) {
	    fail("Exception should be caught earlier and only a " 
		 + "prompt for proper usage printed out");
	}
    }


    /**
     * Replicates a normal/intended user interaction and checks for any
     * exceptions being thrown
     */
    @Test
    public void test5() {
	try {
	    clickOn("#Load");
	    clickOn("#Madison");
	    clickOn("#Milwaukee");
	    clickOn("#Run");
	    clickOn("#Clear");
	    clickOn("#Run");
	    clickOn("#Stats");
	} catch (Exception e) {
	    fail("Error occurring during normal user interaction");
	}
    }

    /**
     * Ensures station objects are properly received from backend and initialized as buttons
     * when Load Data button is pressed.
     */
    @Test
    public void integrationTest1() {
	clickOn("#Load");
	try {
	    Button test1 = lookup("#Madison").query();
	    Button test2 = lookup("#Racine").query();
	    Button test3 = lookup("#Sheboygan").query();
	} catch (Exception e) {
	    fail("Station buttons should be loaded in and id'd when load button" +
		 " is pressed.");
	}

    }

    /**
     * This is an extended version of the intended user interaction test above
     * which includes multiple clicks of buttons which are able to throw exceptions to 
     * ensure that all exceptions are getting caught before they reach the user.
     */
    @Test
    public void integrationTest2() {
	try {
	    clickOn("#Load");
	    clickOn("#Milwaukee");
	    clickOn("#Madison");
	    clickOn("#Watertown");
	    clickOn("#Sheboygan");
	    clickOn("#Bellevue");
	    clickOn("#Run");
	    clickOn("#Clear");
	    clickOn("#Run");
	    clickOn("#Kenosha");
	    clickOn("#Run");
	    clickOn("#Stats");
	} catch (Exception e) {
	    fail("This should not throw an exception");
	}
        
	
    }

    /**
     * Ensures backend is initializing all fields properly
     */
    @Test
    public void codeReviewOfBackendDeveloperTest1() {
	loadStationDW loader = new loadStationDW();
	RailRoadBackend backend = new RailRoadBackend(loader);

	if(backend.loader == null || backend.railroad == null) {
	    fail("Backend should initialize these upon construction");
	}
	if(!backend.railroad.getNodes().isEmpty()) {
	    fail("railroad should be empty until data is loaded");
	}
	
    }

    /**
     * Ensures backend doesn't run into errors when loading our data file
     */
    @Test
    public void codeReviewOfBackendDeveloperTest2() throws Exception {
	loadStationDW loader = new loadStationDW();
	RailRoadBackend backend = new RailRoadBackend(loader);
	try {
	    backend.loadData("rail.dot");
	} catch (Exception e) {
	    fail("This was attempting to load a legitimate file");
	}
	if(backend.getNodes().isEmpty()) {
	    fail("After loading there should be nodes to return");
	}
    }

}
