import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class RailroadUI extends Application {

  private RailroadFrontendFD frontend;
  List<Line> lines = new LinkedList<Line>();
  Hashtable<String, StationInterface> stations;
  List<Double> edges;
  Hashtable<String, Button> stationButtons = new Hashtable<>();
  BorderPane root;
  Pane centerPane;


  /**
   * Creates the base scene and children
   */
  @Override
  public void start(Stage stage) throws Exception {
    this.frontend = new RailroadFrontendFD();
    this.centerPane = new Pane();
    this.root = new BorderPane();
    Scene scene = new Scene(root, 900, 600);
    
    
    Button statsButton = new Button("Print Stats");
    statsButton.setOnAction(e -> frontend.displayStats());
    statsButton.setDisable(true);

    Button exitButton = new Button("Exit");
    exitButton.setOnAction(e -> Platform.exit());

    Button clearButton = new Button("Reset");
    clearButton.setOnAction(e -> {
      frontend.clear();
      unhighlightAll();
    });
    clearButton.setDisable(true);

    Button runButton = new Button("Find Route");
    runButton.setOnAction(e -> {
      try {
	  highlightRoute();
	  printRoute();
      } catch (Exception exception) {

      }
    });
    runButton.setDisable(true);
    
    Button loadDataButton = new Button("Load Data");
    loadDataButton.setOnAction(e -> {
      frontend.loadData();
      displayData();
      loadDataButton.setDisable(true);
      loadDataButton.setText("Loaded");
      statsButton.setDisable(false);
      runButton.setDisable(false);
      clearButton.setDisable(false);
    });

    VBox buttonBox = new VBox(loadDataButton, runButton, clearButton, statsButton, exitButton);
    buttonBox.setAlignment(Pos.CENTER_LEFT);

    root.setLeft(buttonBox);
    root.setCenter(centerPane);

    stage.setScene(scene);
    stage.setTitle("Railroad Router");
    stage.show();
    
    loadDataButton.setId("Load");
    runButton.setId("Run");
    statsButton.setId("Stats");
    clearButton.setId("Clear");
    exitButton.setId("Exit");	

    //create label for program
    Label mapTitle = new Label("Wisconsin Railroad Router");
    //set 2 pixels from top corner
    mapTitle.setTranslateX(2); 
    mapTitle.setTranslateY(2);
    //change to large bolded font
    Font font = Font.font("Arial", FontWeight.BOLD, 24);
    mapTitle.setFont(font);
    //underline
    mapTitle.setUnderline(true);
    
    mapTitle.setId("Title");


    //create the lines that outline a rough shape of wisconsin
    Line top = new Line();
    top.setStartX(0);
    top.setStartY(2);
    top.setEndX(centerPane.getWidth());
    top.setEndY(2);

    Line right = new Line();
    right.setStartX(centerPane.getWidth() - 2);
    right.setStartY(2);
    right.setEndX(centerPane.getWidth() - 2);
    right.setEndY(centerPane.getHeight() - 2);

    Line bot = new Line();
    bot.setStartX(centerPane.getWidth() / 3);
    bot.setStartY(centerPane.getHeight() - 2);
    bot.setEndX(centerPane.getWidth() - 2);
    bot.setEndY(centerPane.getHeight() - 2);

    Line left = new Line();
    left.setStartX(0);
    left.setStartY(2);
    left.setEndX(0);
    left.setEndY(centerPane.getHeight() / 2);

    Line diagonal = new Line();
    diagonal.setStartX(0);
    diagonal.setStartY(centerPane.getHeight() / 2);
    diagonal.setEndX(centerPane.getWidth() / 3);
    diagonal.setEndY(centerPane.getHeight() - 2);

    //display the lines and title previously created
    centerPane.getChildren().addAll(mapTitle, top, right, bot, left, diagonal);
    
  }

  /**
   * This method creates all the buttons representing stations and the lines between them
   * representing railroads after getting the set of stations from the backend
   */
  private void displayData() {
    // get the data that has been loaded in
    this.stations = frontend.getNodes();

    final double padding = 50.0;

    double minLongitude = Double.MAX_VALUE, maxLongitude = Double.MIN_VALUE;
    double minLatitude = Double.MAX_VALUE, maxLatitude = Double.MIN_VALUE;

    //find min and max of x and y coordinates for scaling purposes
    for (StationInterface station : this.stations.values()) {
      minLongitude = Math.min(minLongitude, station.getCoordinatesLong() + centerPane.getWidth());
      maxLongitude = Math.max(maxLongitude, station.getCoordinatesLong() + centerPane.getWidth());
      minLatitude =
          Math.min(minLatitude, (-1 * station.getCoordinatesLat()) + centerPane.getHeight());
      maxLatitude =
          Math.max(maxLatitude, (-1 * station.getCoordinatesLat()) + centerPane.getHeight());
    }

    //create scale factors
    double scaleX = (centerPane.getWidth() - 2 * padding) / (maxLongitude - minLongitude);
    double scaleY = (centerPane.getHeight() - 2 * padding) / (maxLatitude - minLatitude);

    //create buttons at the scaled coordinates representing each station in the data set
    for (StationInterface station : this.stations.values()) {

      double scaledX =
          (station.getCoordinatesLong() + centerPane.getWidth() - minLongitude) * scaleX;
      double scaledY =
          ((-1 * station.getCoordinatesLat()) + centerPane.getHeight() - minLatitude) * scaleY;

      Button button = new Button((station.getName().toString()));
      button.setOnAction(e -> {
        frontend.addStop(station);
      });
      button.setLayoutX(scaledX + padding);
      button.setLayoutY(scaledY + padding);
      button.setId(station.getName());

      this.stationButtons.put(station.getName(), button);
    }


    Button[] buttonsArray = stationButtons.values().toArray(new Button[0]);
    
    //create a line between each button/station to represent the railroad connection
    for (int i = 0; i < buttonsArray.length; i++) {
      for (int j = i + 1; j < buttonsArray.length; j++) {
        Button button1 = buttonsArray[i];
        Button button2 = buttonsArray[j];

        double x1 = button1.getLayoutX();
        double y1 = button1.getLayoutY();

        double x2 = button2.getLayoutX();
        double y2 = button2.getLayoutY();

        Line line = new Line(x1, y1, x2, y2);
        line.setStroke(Color.BLUE);
        this.lines.add(line);
        //display the lines 
        centerPane.getChildren().add(line);
      }
    }

    //display the station buttons, comes after displaying lines so that the buttons are overlaid 
    //on top
    for (Entry<String, Button> button : stationButtons.entrySet()) {
      centerPane.getChildren().add(button.getValue());
    }

  }

  /**
   * Sets all lines/railways to original color (unhighlights route if there is one) and clears the 
   * path field in the frontend
   */
  private void unhighlightAll() {
    for (Line line : this.lines) {
      line.setStroke(Color.BLUE);
    }
  }

  /**
   * Calls upon the findLine() and findReverse() methods to highlight both lines that exist between
   * each successive pair of nodes within the shortest route
   */
  private void highlightRoute() {
    ArrayList<StationInterface> route = frontend.findRoute();

    for (int i = 0; i < route.size() - 1; i++) {
      Line toHighlight = findLine(route.get(i), route.get(i + 1));
      Line reverseHighlight = findReverse(route.get(i), route.get(i + 1));
      if (toHighlight != null) {
        toHighlight.setStroke(Color.RED);
      }
      if (reverseHighlight != null) {
        reverseHighlight.setStroke(Color.RED);
      }

    }
  }

  /**
   * Prints each station along the shortest route with arrows in between to clarify the JavaFX
   * depiction
   */
  private void printRoute() {
    ArrayList<StationInterface> route = frontend.findRoute();
    String output = (route.get(0).getName());
    for (int i = 1; i < route.size(); i++) {
      output += " -> " + route.get(i).getName();
    }
    System.out.println(output);
  }

  /**
   * This method finds and returns the line between the predecessor parameter and the successor
   * parameter
   * 
   * @param predecessor
   * @param successor
   * @return Line from predecessor to successor
   */
  private Line findLine(StationInterface predecessor, StationInterface successor) {

    // create identical line to the one we are looking for
    double x1 = stationButtons.get(predecessor.getName()).getLayoutX();
    double y1 = stationButtons.get(predecessor.getName()).getLayoutY();

    double x2 = stationButtons.get(successor.getName()).getLayoutX();
    double y2 = stationButtons.get(successor.getName()).getLayoutY();

    Line toFind = new Line(x1, y1, x2, y2);
    toFind.setStroke(Color.BLUE);

    // iterate through existing line collection until find one that matches the twin made above
    for (Line line : this.lines) {
      if (toFind.toString().equals(line.toString())) {
        return line;
      }
    }

    return null;
  }

  /**
   * Lines are created in both directions in this graph. This method finds the line going the
   * opposite direction as the findLine() method so that both can be highlighted to ensure proper
   * functionality of the highlightRoute() method
   * 
   * @param predecessor
   * @param successor
   * @return Line from successor to predecessor
   */
  private Line findReverse(StationInterface predecessor, StationInterface successor) {
    // create reverse line
    double x1 = stationButtons.get(successor.getName()).getLayoutX();
    double y1 = stationButtons.get(successor.getName()).getLayoutY();

    double x2 = stationButtons.get(predecessor.getName()).getLayoutX();
    double y2 = stationButtons.get(predecessor.getName()).getLayoutY();

    Line reverse = new Line(x1, y1, x2, y2);
    reverse.setStroke(Color.BLUE);

    // iterate through existing line collection until identical line to reverse line is found
    for (Line line : this.lines) {
      if (reverse.toString().equals(line.toString())) {
        return line;
      }
    }
    return null;
  }
    
  public static void main(String[] args) {
    Application.launch(args);
  }
}
