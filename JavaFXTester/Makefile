# this example combines what we've learned about compiling and running java code
# that makes use of junit and javafx, along with the extra JavaFXTester.jar

# There is one new piece to the runTests command:
# --add-exports javafx.graphics/com.sun.javafx.application=ALL-UNNAMED
# You don't need to understand anything about this extra argument.  But for the
# curious, it allows the unnamed module with the provided JavaFXTester.jar to
# access code within the JavaFX modules to help run these tests

runTests: SampleTests.class
	java --module-path ../javafx-sdk-17.0.6/lib --add-modules javafx.controls --add-exports javafx.graphics/com.sun.javafx.application=ALL-UNNAMED -jar junit5.jar -cp .:JavaFXTester.jar -c SampleTests

SampleTests.class: SampleTests.java SampleMain.class
	javac --module-path ../javafx-sdk-17.0.6/lib --add-modules javafx.controls -cp .:junit5.jar:JavaFXTester.jar SampleTests.java

runProgram: SampleMain.class
	java --module-path ../javafx-sdk-17.0.6/lib --add-modules javafx.controls SampleMain

SampleMain.class: SampleMain.java
	javac --module-path ../javafx-sdk-17.0.6/lib --add-modules javafx.controls SampleMain.java
