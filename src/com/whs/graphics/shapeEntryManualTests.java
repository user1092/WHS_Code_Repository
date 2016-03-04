package com.whs.graphics;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class shapeEntryManualTests extends Application {
	private int startTime = 0;
	private int duration = 0;
	private float xStart = 0f;
	private float yStart = 0f;
	private String type = "roundedRectangle";
	private float width = 0.5f;
	private float height = 0.5f;
	public Group shapes;
	
	shapeEntry shapeEntry = new shapeEntry(startTime, duration, xStart, yStart, height, width, type);
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		//BorderPane guiLayout = new BorderPane();
		shapes = shapeEntry.drawShape();
		Scene scene = new Scene(shapes, 600, 600);
		//Scene scene = new Scene(guiLayout);
		
		//main stage set up with appropriate scene and size
		primaryStage.setTitle("Client GUI");
				
		primaryStage.setScene(scene);
		
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(shapeEntryManualTests.class, args);
		
	}
	
}