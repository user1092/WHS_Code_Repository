/**
 * polygonManualTestEntry.java	v0.1 04/03/16
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.graphics;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class that contains manual tests relating shapeEntry due to the fact
 * that javafx and junit testing are not compatible.
 * 
 * All shapes are drawn from top left corner
 * 
 * @author ws659, rw1065
 * 
 * @version	v0.1 04/03/16
 * 
 */
public class PolygonGraphicManualTests extends Application {
	

	/**
	 * Test for plain polygon 
	 * 	width: 50%
	 * 	height: 50%
	 * 	x = 25%
	 * 	y = 25%
	 */
	public void plainPolygonTest() {
		Stage primaryStage = new Stage();
		int startTime = 0;
		int duration = 0;
		float[] x = {0f, 0.f, 0.75f};
		float[] y = {0f, 0.75f, 0.75f};
		String fillColour = "0055aa";
		String lineColour = "222222";
		Group polygon;
		
		//set up shape entry
		PolygonGraphic polygonEntry = new PolygonGraphic(startTime, duration, x, y, fillColour, lineColour, "/csv/poly1.csv");
		
		//set up shape and scene
		polygon = polygonEntry.drawPolygon();
		Scene scene = new Scene(polygon, 600, 600);
				
		//main stage set up with appropriate scene and size
		primaryStage.setTitle("shadingPolygonTest");
						
		primaryStage.setScene(scene);
				
		primaryStage.show();
	}
	
	
	/**
	 * Test for shaded polygon
	 */
	public void shadedPolygonTest() {
		Stage primaryStage = new Stage();
		int startTime = 0;
		int duration = 0;
		float[] x = {0.25f, 0.5f, 0.15f};
		float[] y = {0.25f, 0.5f, 0.35f};
		Group polygon;
		
		//set up shape entry
		PolygonGraphic polygonEntry = new PolygonGraphic(startTime, duration, x, y, "/csv/poly1.csv");
		
		//set up shape and scene
		polygonEntry.setShading(0.2f, 0.5f, 0.175f, 0.175f, "aa0000", "00aa00");
		polygon = polygonEntry.drawPolygon();
		Scene scene = new Scene(polygon, 600, 600);
				
		//main stage set up with appropriate scene and size
		primaryStage.setTitle("shadingPolygonTest");
						
		primaryStage.setScene(scene);
				
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(PolygonGraphicManualTests.class, args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		plainPolygonTest();
		shadedPolygonTest();
	}
	
}