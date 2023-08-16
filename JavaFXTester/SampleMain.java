
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/** A simple JavaFX Application to help demonstrate the use of the provided
 * JavaFXTester class and .jar file.  The only thing added to this code to
 * help make it easier to test, are the ids set on each control that you'd
 * like to reference from your test methods.
 * @date 2023.4
 */
public class SampleMain extends Application {
  
  // Instructions for testing.
  // 1. Add this code to a project configured to use JavaFX,
  //    and ensure that it correctly compiles and runs.
  // 2. Add SampleTest.java to this project, and add jars to fix errors:
  //    adding junit5.jar should get rid of the junit import errors,
  //    and adding JavaFXTester.jar should ge rid of the other errors.
  // 3. Run your the junit tests within SampleTests and enjoy watching
  //    as the interactions for each test play out before your very eyes.

  @Override
  public void start(Stage stage) {
    // create a button and a label for this button to change the text within
    Label label = new Label("original");
    Button button = new Button("change");
    button.setOnAction( e -> { // first two clicks each change the label's text
      if(label.getText().equals("original"))
        label.setText("changed once");
      else
        label.setText("changed multiple times (and will not change again)");
    });
    // then add these controls to the scenegraph
    HBox hbox = new HBox(label,button);
    hbox.setSpacing(8);
    Scene scene = new Scene(hbox,800,600);
    stage.setScene(scene);
    stage.show();

    // to make these controls easier to reference from our tests,
    // we are going to give each of them unique identifiers
    label.setId("MyLabel");
    button.setId("MyButton");
  }

  public static void main(String[] args) { 
    Application.launch(); // run this app to confirm your understanding of it
   }
}
