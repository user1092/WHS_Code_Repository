/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.graphics;

import java.io.File;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Class that contains manual tests relating shapeEntry due to the fact
 * that javafx and junit testing are not compatible.
 * 
 * All shapes are drawn from top left corner
 * 
 * NOT FOR RELEASE!
 * 
 * @author ws659, rw1065
 * @version	v0.1 04/03/16
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
		Pane polygon;
		
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
		Pane polygon;
		String path = new File("").getAbsolutePath() + "/src/whs/yourchoice/graphics/ArrowShape.csv";
		
		//set up shape entry
		PolygonGraphic polygonEntry = new PolygonGraphic(startTime, duration, "FFFFFF", "FFFFFF", path);
		
		//set up shape and scene
		polygonEntry.setShading(0.2f, 0.5f, 0.175f, 0.175f, "aa0000", "00aa00");
		try {
			polygonEntry.parseCSV();
		}
		catch (FileNotFoundException e) {
			System.out.println(e);
		}
		//polygonEntry.parseCSV();
		
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
