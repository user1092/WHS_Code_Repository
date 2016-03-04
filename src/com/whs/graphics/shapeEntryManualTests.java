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
	private float xStart = 0.1f;
	private float yStart = 0.1f;
	//private String type = "circle";
	private float width = 0.2f;
	private float height = 0.2f;
	public Group shapes;
	
	shapeEntry shapeEntry = new shapeEntry(startTime, duration, xStart, yStart, height, width);
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		//BorderPane guiLayout = new BorderPane();
		shapes = shapeEntry.drawShape();
		Scene scene = new Scene(shapes, 600, 800);
		//Scene scene = new Scene(guiLayout);
		
		//main stage set up with appropriate scene and size
		primaryStage.setHeight(600);
		primaryStage.setWidth(800);
		primaryStage.setTitle("Client GUI");
		
		//Rectangle rect = new Rectangle(200,20);
		//shapes.getChildren().add(rect);
		
		
		primaryStage.setScene(scene);
		
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(shapeEntryManualTests.class, args);
		
	}
	
}