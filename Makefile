run:compile
	java --module-path javafx-sdk-20.0.1/lib --add-modules javafx.controls RailroadUI.java

runTests:runAlgorithmEngineerTests runDataWranglerTests runFrontendDeveloperTests runBackendDeveloperTests 

runDataWranglerTests:compile
	javac --module-path javafx-sdk-20.0.1/lib --add-modules javafx.controls -cp .:junit5.jar:JavaFXTester.jar DataWranglerTests.java
	java --module-path javafx-sdk-20.0.1/lib --add-modules javafx.controls --add-exports javafx.graphics/com.sun.javafx.application=ALL-UNNAMED -jar junit5.jar -cp .:JavaFXTester.jar -c DataWranglerTests
	#java -jar junit5.jar -cp . --select-class=DataWranglerTests

runFrontendDeveloperTests:compile
	java --module-path javafx-sdk-20.0.1/lib --add-modules javafx.controls --add-exports javafx.graphics/com.sun.javafx.application=ALL-UNNAMED -jar junit5.jar -cp .:JavaFXTester.jar -c FrontendDeveloperTests

runBackendDeveloperTests:compile
	java --module-path javafx-sdk-20.0.1/lib --add-modules javafx.controls --add-exports javafx.graphics/com.sun.javafx.application=ALL-UNNAMED -jar junit5.jar -cp .:JavaFXTester.jar -c BackendDeveloperTests

runAlgorithmEngineerTests:compile
	java -jar junit5.jar -cp . --select-class=AlgorithmEngineerTests

compile:
	javac BaseGraph.java
	javac -cp .:junit5.jar DijkstraGraph.java
	javac GraphADT.java
	javac loadStationBD.java
	javac loadStationInterface.java
	javac railNetworkInterface.java
	javac RailRoadBackendInterface.java
	javac RailroadInterface.java
	javac railNetworkBD.java
	javac RailRoadBackend.java
	javac RailroadBD.java
	javac StationBD.java
	javac StationInterface.java
	javac BaseGraph2.java
	javac loadStationDW.java
	javac railNetworkDW.java
	javac StationDW.java
	#javac -cp .:junit5.jar DataWranglerTests.java
	javac GraphADT.java
	javac RailroadInterface.java
	javac RailroadAE.java
	javac -cp .:junit5.jar AlgorithmEngineerTests.java
	javac RailroadFrontendInterface.java
	javac RailroadFrontendFD.java
	javac --module-path javafx-sdk-20.0.1/lib --add-modules javafx.controls RailroadUI.java
	javac --module-path javafx-sdk-20.0.1/lib --add-modules javafx.controls -cp .:junit5.jar:JavaFXTester.jar FrontendDeveloperTests.java
	javac --module-path javafx-sdk-20.0.1/lib --add-modules javafx.controls -cp .:junit5.jar:JavaFXTester.jar BackendDeveloperTests.java
clean:
	rm *.class
