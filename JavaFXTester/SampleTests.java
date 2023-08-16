import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;

import edu.wisc.cs.cs400.JavaFXTester;

/**
 * A couple of simple tests that demonstrate how the provided
 * JavaFXTester class and .jar file can be used to check test
 * the simple GUI implemented by the provided SampleMain class
 * @date 2023.4
 */
public class SampleTests extends JavaFXTester {
    // this JavaFXTester class implements the TestFX FXRobot class, documented here:
    // https://testfx.github.io/TestFX/docs/javadoc/testfx-core/javadoc/org.testfx/org/testfx/api/FxRobot.html

    public SampleTests() {
        // you must specify the Application being tested by passing its class
        // to the parent class through the constructor, like this: 
        super(SampleMain.class);
    }

    @Test
    public void test1() {
        Label label = lookup("#MyLabel").query();

        // the FxRobot super class also contains helper method to simulate
        // user interaction... watch the mouse move while this test runs

        clickOn("#MyButton"); // simulates mouse clicking
        assertEquals("changed once",label.getText());

	clickOn("#MyButton"); // simulates mouse clicking
        type(KeyCode.ENTER); // simulates key typing
        assertEquals("changed multiple times (and will not change again)",label.getText());
    }

    @Test
    public void test2() {
        // lookup nodes in the scene graph using CSS style selectors:
        Button button = lookup("#MyButton").query();
        Label label = lookup("#MyLabel").query();
        // events must be fired on the JavaFX Application thread, 
        // which this interact method helps you accomplish:
        interact( () -> button.fireEvent(new ActionEvent()) );
        // then we can assert that the expected state has been updated
        assertEquals("changed once",label.getText());
    }

}
