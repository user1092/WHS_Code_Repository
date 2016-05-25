/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.client;

import whs.yourchoice.presentation.PresentationEntry;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Class for displaying the meta data for the current presentation
 * 
 * @author		ch1092, cd828
 * @version		v0.3 25/05/2016
 */
public class MetaDataGui extends Application {

	private final int WINDOW_HEIGHT = 400;
	private final int WINDOW_WIDTH = 500;
	private final int INFO_HEIGHT = 300;
	private final int INFO_WIDTH = WINDOW_WIDTH - 10;
	private final int FONT_SIZE = 20;
	
	private String title;
	private String author;
	private String version;
	private String comment;
	
	/**
	 * Constructor that extracts the meta data of the presentation
	 * 
	 * @param passedPresentation	-	The presentation to display the meta data 
	 */
	public MetaDataGui(PresentationEntry passedPresentation) {
		title = passedPresentation.getPresentationTitle();
		author = passedPresentation.getPresentationAuthor();
		version = passedPresentation.getPresentationVersion();
		comment = passedPresentation.getPresentationComment();
	}
	
	public static void main(String[] args) {
		launch(MetaDataGui.class, args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane guiLayout = new BorderPane();
		ScrollPane metaDataLayout = new ScrollPane();
		Scene scene = new Scene(guiLayout);
		
		// Box that contains the titles and entries for the meta data
		VBox metaVBox = metaVBoxCreation();
		// Box that contains the button "Close"
		HBox buttonHBox = buttonHBoxCreation(primaryStage);
		// Import and set background image
		String background = ClientGui.class.getResource("resources/SlideBackground.jpg").toExternalForm();
		guiLayout.setStyle("-fx-background-image: url('" + background + "'); " +
		           "-fx-background-position: center center; " +
		           "-fx-background-repeat: stretch;");
		
		// TODO DOESN'T WORK!
		metaDataLayout.setStyle("-fx-background-color: transparent;");
		
		metaDataLayout.setContent(metaVBox);
		metaDataLayout.setPrefHeight(INFO_HEIGHT);
		metaDataLayout.setPrefWidth(INFO_WIDTH);
		metaDataLayout.setFitToWidth(true);
		guiLayout.setBottom(buttonHBox);
		guiLayout.setTop(metaDataLayout);
		
		// Main stage set up with appropriate scene and size
		primaryStage.setScene(scene);
		primaryStage.setHeight(WINDOW_HEIGHT);
		primaryStage.setWidth(WINDOW_WIDTH);
		primaryStage.setResizable(false);
		primaryStage.setTitle("About Presentation");
		primaryStage.show();
	}
	
	
	/**
	 * Method to populate a VBox with the meta data 
	 * 
	 * @return metaVBox - The VBox with populated meta data
	 */
	private VBox metaVBoxCreation() {
		
		VBox metaVBox = new VBox();
		// An array for each row
		HBox[] metaHBoxes = new HBox[4];
		// All entries to be displayed
		String[] fields = {"Title", title, "Author", author, "Version", version, "Comment", comment};
		
		int rowNumber = 0;
		
		for (int fieldNumber = 0; fieldNumber < fields.length; fieldNumber = fieldNumber + 2) {
			HBox labelHBox = new HBox();
			
			//set the position of the VBox
			labelHBox.setPadding(new Insets(10, 10, 10, 20));
			//set the spacing between objects in the box
			labelHBox.setSpacing(20);
			//set the HBox to be transparent
			labelHBox.setStyle("-fx-background-color: transparent;");
			
			Label titleLabel = new Label(fields[fieldNumber]);
			titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FONT_SIZE));
			titleLabel.setPrefWidth(120);
			
			Label entryLabel = new Label(fields[fieldNumber+1]);
			entryLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FONT_SIZE));
			entryLabel.setWrapText(true);
			entryLabel.setPrefWidth(300);
			
			labelHBox.getChildren().addAll(titleLabel, entryLabel);
			
			metaHBoxes[rowNumber] = labelHBox;
			rowNumber++;
		}
		
		metaVBox.setStyle("-fx-background-color: transparent;");
		metaVBox.getChildren().addAll(metaHBoxes);
		return metaVBox;
	}

	
	/**
	 * Method for the creation of the HBox that contains the button
	 * 
	 * @param primaryStage  -  The application window
	 * @return HBox  -  The box that contains the button
	 */
	private HBox buttonHBoxCreation(Stage primaryStage) {
		HBox buttonHBox = new HBox();
		buttonHBox.setPadding(new Insets(0, 0, 10, 135));
		buttonHBox.setSpacing(10);
		buttonHBox.setStyle("-fx-background-colour: #336699;");
		Button closeButton = closeButtonSetup(primaryStage);
		buttonHBox.getChildren().addAll(closeButton);
		return buttonHBox;	
	}
		
	
	/**
	 * Method for setting up the close button, when pressed the window closes
	 * 
	 * @param primaryStage  -  The application window
	 * @return Button	-	close button  
	 */
	private Button closeButtonSetup(final Stage primaryStage) {
		Button closeButton = new Button("Close");
		closeButton.setPrefSize(200, 20);
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				primaryStage.close();
			}
		});
		return closeButton;
	}
}