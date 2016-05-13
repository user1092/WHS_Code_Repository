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

public class metaDataGui extends Application {

	private final int WINDOW_HEIGHT = 400;
	private final int WINDOW_WIDTH = 500;
	private final int INFO_HEIGHT = 300;
	private final int INFO_WIDTH = WINDOW_WIDTH - 10;
	private final int FONT_SIZE = 20;
	
	private Button closeButton;
	private String title;
	private String author;
	private String version;
	private String comment;
	
	public metaDataGui(PresentationEntry passedPresentation) {
		title = passedPresentation.getPresentationTitle();
		author = passedPresentation.getPresentationAuthor();
		version = passedPresentation.getPresentationVersion();
		comment = passedPresentation.getPresentationComment();
	}
	
	public static void main(String[] args) {
		launch(metaDataGui.class, args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane guiLayout = new BorderPane();
		ScrollPane metaDataLayout = new ScrollPane();
		Scene scene = new Scene(guiLayout);
		//Box that contains the ip address and port labels and text boxes
		HBox metaHBox = metaHBoxCreation();
//		VBox titlesVbox = metaVBoxCreation();
//		VBox entriesVbox = metaInfoVBoxCreation();
		// Box that contains the buttons "Connect" and "Cancel"
		HBox buttonHBox = buttonHBoxCreation(primaryStage);
		//import and set background image
		String background = ClientGui.class.getResource("resources/SlideBackground.jpg").toExternalForm();
		guiLayout.setStyle("-fx-background-image: url('" + background + "'); " +
		           "-fx-background-position: center center; " +
		           "-fx-background-repeat: stretch;");
		
		metaDataLayout.setStyle("-fx-background-color: transparent;");
//		metaDataLayout.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5);");
		

//		guiLayout.setLeft(titlesVbox);
//		guiLayout.setCenter(entriesVbox);
		metaDataLayout.setContent(metaHBox);
		metaDataLayout.setPrefHeight(INFO_HEIGHT);
		metaDataLayout.setPrefWidth(INFO_WIDTH);
		metaDataLayout.setFitToWidth(true);
		guiLayout.setBottom(buttonHBox);
		guiLayout.setTop(metaDataLayout);
		
		//main stage set up wit appropriate scene and size
		primaryStage.setScene(scene);
		primaryStage.setHeight(WINDOW_HEIGHT);
		primaryStage.setWidth(WINDOW_WIDTH);
		primaryStage.setResizable(false);
		primaryStage.setTitle("About Presentation");
		primaryStage.show();
	}
	
	private HBox metaHBoxCreation() {
		HBox metaHBox = new HBox();
		VBox titlesVbox = metaTitleVBoxCreation();
		VBox entriesVbox = metaInfoVBoxCreation();
		//metaHBox.getChildren().addAll(titlesVbox, entriesVbox);
		metaHBox.setStyle("-fx-background-color: transparent;");
		//metaHBox.getChildren().addAll(titlesVbox);
		return metaHBox;
	}

	/**
	 * Method for the creation of the HBox that contains the buttons
	 * @param primaryStage  -  The application window
	 * @return HBox  -  The box that contains the buttons
	 */
	private HBox buttonHBoxCreation(Stage primaryStage) {
		HBox buttonHBox = new HBox();
		buttonHBox.setPadding(new Insets(0, 0, 10, 135));
		buttonHBox.setSpacing(10);
		buttonHBox.setStyle("-fx-background-colour: #336699;");
		cancelButtonSetup(primaryStage);
		buttonHBox.getChildren().addAll(closeButton);
		return buttonHBox;	
	}
	
	/**
	 * Method for the creation of the HBox that contains the ip address labels and text boxes
	 * @return HBox  -  The box that contains the ip address labels and text boxes
	 */
	private VBox metaTitleVBoxCreation() {
		//HBox instantiation
		VBox labelVBox = new VBox();
		//set the position of the VBox
		labelVBox.setPadding(new Insets(10, 10, 10, 20));
		//set the spacing between objects in the box
		labelVBox.setSpacing(5);
		//set the HBox to have the background colour
//		labelVBox.setStyle("-fx-background-colour: #336699;");
		labelVBox.setStyle("-fx-background-color: transparent;");
		//labels and text boxes instantiation for the ip address
		Label titleLabel = new Label("Title");
		titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FONT_SIZE));
		
		Label authorLabel = new Label("Author");
		authorLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FONT_SIZE));
		
		Label versionLabel = new Label("Version");
		versionLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FONT_SIZE));
		
		Label commentLabel = new Label("Comment");
		commentLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FONT_SIZE));
		
		//place the objects for the ip address in the HBox
		labelVBox.getChildren().addAll(titleLabel, authorLabel, versionLabel, commentLabel);
		
		return labelVBox;	
	}
	
	/**
	 * Method for the creation of the HBox that contains the ip address labels and text boxes
	 * @return HBox  -  The box that contains the ip address labels and text boxes
	 */
	private VBox metaInfoVBoxCreation() {
		//HBox instantiation
		VBox labelVBox = new VBox();
		//set the position of the VBox
		labelVBox.setPadding(new Insets(10, 10, 10, 20));
		//set the spacing between objects in the box
		labelVBox.setSpacing(5);
		//set the HBox to have the background colour
//		labelVBox.setStyle("-fx-background-colour: #336699;");
		labelVBox.setStyle("-fx-background-color: transparent;");
		//labels and text boxes instantiation for the ip address
		Label titleLabel = new Label(title);
		titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FONT_SIZE));
		
		Label authorLabel = new Label(author);
		authorLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FONT_SIZE));
		
		Label versionLabel = new Label(version);
		versionLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FONT_SIZE));
		
		Label commentLabel = new Label(comment);
		commentLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FONT_SIZE));
		commentLabel.setWrapText(true);
		commentLabel.setPrefWidth(300);
		
		//place the objects for the ip address in the HBox
		labelVBox.getChildren().addAll(titleLabel, authorLabel, versionLabel, commentLabel);
		
		return labelVBox;	
	}
	
	
	/**
	 * Method for setting up the close button, when pressed the window closes
	 * @param primaryStage  -  The application window
	 */
	private void cancelButtonSetup(final Stage primaryStage) {
		closeButton = new Button("Close");
		closeButton.setPrefSize(200, 20);
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				primaryStage.close();
			}
		});
	}
}
