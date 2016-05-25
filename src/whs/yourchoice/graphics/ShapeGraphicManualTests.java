/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.graphics;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Class that contains manual tests relating ShapeEntry due to the fact
 * that javafx and junit testing are not compatible.
 * 
 * All shapes are drawn from top left corner
 * 
 * NOT FOR RELEASE!
 * 
 * @author ws659, rw1065
 * @version	v0.1 04/03/16
 */
public class ShapeGraphicManualTests extends Application {
	
	/**
	 * Test for plain square 
	 * 	width: 50%
	 * 	height: 50%
	 * 	x = 25%
	 * 	y = 25%
	 */
	public void planeSquareTest() {
		Stage primaryStage = new Stage();
		int startTime = 0;
		int duration = 0;
		float xStart = 0.25f;
		float yStart = 0.25f;
		String type = "rectangle";
		float width = 0.5f;
		float height = 0.5f;
		Pane shapes;
		
		//set up shape entry
		ShapeGraphic ShapeEntry = new ShapeGraphic(startTime, duration, xStart, yStart, height, width, type);
		
		//set up shape and scene
		shapes = ShapeEntry.drawShape();
		Scene scene = new Scene(shapes, 600, 600);
				
		//main stage set up with appropriate scene and size
		primaryStage.setTitle("plainSquareTest");
						
		primaryStage.setScene(scene);
				
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(ShapeGraphicManualTests.class, args);
		
	}
	
	/**
	 * Test for plain circle 
	 * 	width: 50%
	 * 	x = 25%
	 * 	y = 25%
	 */
	public void planeCircleTest() {
		Stage primaryStage = new Stage();
		int startTime = 0;
		int duration = 0;
		float xStart = 0.25f;
		float yStart = 0.25f;
		String type = "circle";
		float width = 0.5f;
		float height = 0.5f;
		Pane shapes;
		
		//set up shape entry
		ShapeGraphic ShapeEntry = new ShapeGraphic(startTime, duration, xStart, yStart, height, width, type);
		
		//set up shape and scene
		shapes = ShapeEntry.drawShape();
		Scene scene = new Scene(shapes, 600, 600);
				
		//main stage set up with appropriate scene and size
		primaryStage.setTitle("planeCircleTest");
						
		primaryStage.setScene(scene);
				
		primaryStage.show();
	}
	
	/**
	 * Test for coloured rectangle with black border
	 * Off-centered
	 */
	public void colourRectangleTest() {
		Stage primaryStage = new Stage();
		int startTime = 0;
		int duration = 0;
		float xStart = 0.125f;
		float yStart = 0.625f;
		String type = "rectangle";
		String fillColour = "e15c00";
		String lineColour = "000000";
		float width = 0.5f;
		float height = 0.321f;
		Pane shapes;
		
		//set up shape entry
		ShapeGraphic ShapeEntry = new ShapeGraphic(startTime, duration, xStart, yStart, height, width, type, fillColour, lineColour);
		
		//set up shape and scene
		shapes = ShapeEntry.drawShape();
		Scene scene = new Scene(shapes, 600, 600);
				
		//main stage set up with appropriate scene and size
		primaryStage.setTitle("colourRectangleTest");
						
		primaryStage.setScene(scene);
				
		primaryStage.show();
	}
	
	/**
	 * Test for shaded circle
	 */
	public void shadingCircleTest() {
		Stage primaryStage = new Stage();
		int startTime = 0;
		int duration = 0;
		float xStart = 0.5f;
		float yStart = 0.5f;
		String type = "circle";
		float width = 0.25f;
		float height = 0.25f;
		Pane shapes;
		
		//set up shape entry
		ShapeGraphic ShapeEntry = new ShapeGraphic(startTime, duration, xStart, yStart, height, width, type);
		
		//set up shape and scene
		ShapeEntry.setShading(0.5f, 0.75f, 0.5f, 0.75f, "aa0000", "00aa00");
		shapes = ShapeEntry.drawShape();
		Scene scene = new Scene(shapes, 600, 600);
				
		//main stage set up with appropriate scene and size
		primaryStage.setTitle("shadingCircleTest");
						
		primaryStage.setScene(scene);
				
		primaryStage.show();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		planeSquareTest();
		planeCircleTest();
		colourRectangleTest();
		shadingCircleTest();		
	}
	
}
