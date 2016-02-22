package com.whs.client;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class JavaFxClientGui extends Application {
	
	private final FileChooser xmlFileChooser = new FileChooser();
	private Stage primaryStage;
	private Text actionStatus;
	private TextField ipAddressTextField_1, ipAddressTextField_2, ipAddressTextField_3, 
							ipAddressTextField_4, portTextField;
	private StringConverter<Integer> integerFormatter;
	
	public static void main(String[] args) {
		launch(JavaFxClientGui.class, args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane guiLayout = new BorderPane();
		Scene scene = new Scene(guiLayout);
		
		//import and set background image
		String background = JavaFxClientGui.class.getResource("SlideBackground.jpg").toExternalForm();
		guiLayout.setStyle("-fx-background-image: url('" + background + "'); " +
		           "-fx-background-position: center center; " +
		           "-fx-background-repeat: stretch;");
		
		//Button box set up
		HBox buttonHBox = buttonHBoxCreation();
		//Log box set up
		HBox logHBox = logHBoxCreation();
		//Box that contains the ip address and port labels and text boxes
		VBox ipAndPortVbox = ipAndPortVBoxCreation();
		
		//place the boxes inside the layout created
		guiLayout.setBottom(buttonHBox);
		guiLayout.setRight(logHBox);
		guiLayout.setLeft(ipAndPortVbox);
		
		//main stage set up with appropriate scene and size
		primaryStage.setScene(scene);
		primaryStage.setHeight(600);
		primaryStage.setWidth(800);
		primaryStage.setTitle("Client GUI");
		primaryStage.show();
	}
	
	/* method for setting the text fields to only accept integers */
	private void setIntegerTextFormat() {
		integerFormatter = new StringConverter<Integer>()
        {
           @Override
           public Integer fromString(String string)
           {
              return Integer.parseInt(string);
           }

           @Override
           public String toString(Integer object)
           {
              if (object == null)
                 return "";
              return object.toString();
           }
        };
	}
	
	/* method for the creation of the HBox that contains the buttons */
	private HBox buttonHBoxCreation() {
		HBox buttonHBox = new HBox();
		buttonHBox.setPadding(new Insets(0, 0, 10, 235));
		buttonHBox.setSpacing(10);
		buttonHBox.setStyle("-fx-background-colour: #336699;");
		Button browseButton = new Button("Browse");
		browseButton.setPrefSize(100, 20);
		browseButton.setOnAction(new BrowseButtonListener());
		Button connectButton = new Button("Connect");
		connectButton.setPrefSize(100, 20);
		connectButton.setOnAction(new ConnectButtonListener());
		Button disconnectButton = new Button("Discconect");
		disconnectButton.setPrefSize(100, 20);
		buttonHBox.getChildren().addAll(browseButton, connectButton, disconnectButton);
		return buttonHBox;	
	}
	
	/* method for the creation of the HBox that contains the buttons */
	private HBox ipHBoxCreation() {
		//HBox instantiation
		HBox ipHBox = new HBox();
		//set the position of the HBox
		ipHBox.setPadding(new Insets(10, 10, 10, 20));
		//set the spacing between objects in the box
		ipHBox.setSpacing(5);
		//set the HBox to have the background colour
		ipHBox.setStyle("-fx-background-colour: #336699;");
		//call method for setting the format of text fields
		setIntegerTextFormat();
		//labels and text boxes instantiation for the ip address
		Label ipAddressLabel = new Label("IP Address");
		ipAddressTextField_1 = new TextField();
		ipAddressTextField_1.setPrefWidth(40);
		ipAddressTextField_1.setTextFormatter(new TextFormatter<Integer>(integerFormatter));
		Label dot_1 = new Label(".");
		ipAddressTextField_2 = new TextField();
		ipAddressTextField_2.setPrefWidth(40);
		ipAddressTextField_2.setTextFormatter(new TextFormatter<Integer>(integerFormatter));
		Label dot_2 = new Label(".");
		ipAddressTextField_3 = new TextField();
		ipAddressTextField_3.setPrefWidth(40);
		ipAddressTextField_3.setTextFormatter(new TextFormatter<Integer>(integerFormatter));
		Label dot_3 = new Label(".");
		ipAddressTextField_4 = new TextField();
		ipAddressTextField_4.setPrefWidth(40);
		ipAddressTextField_4.setTextFormatter(new TextFormatter<Integer>(integerFormatter));
		//place the objects for the ip address in the HBox
		ipHBox.getChildren().addAll(ipAddressLabel, ipAddressTextField_1, dot_1, ipAddressTextField_2,
				dot_2, ipAddressTextField_3, dot_3, ipAddressTextField_4);
		return ipHBox;	
	}
	
	/* method for the creation of the HBox that contains the buttons */
	private HBox logHBoxCreation() {
		HBox logHBox = new HBox();
		logHBox.setPadding(new Insets(10, 10, 10, 10));
		logHBox.setSpacing(5);
		logHBox.setStyle("-fx-background-colour: #336699;");
		// Status message text
		actionStatus = new Text();
		actionStatus.setFont(Font.font("Courier", FontWeight.NORMAL, 14));
		actionStatus.setFill(Color.BLACK);
		logHBox.getChildren().add(actionStatus);
		return logHBox;
	}
	
	/* method for the creation of the HBox that contains the buttons */
	private HBox portHBoxCreation() {
		HBox portHBox = new HBox();
		portHBox.setPadding(new Insets(10, 10, 10, 20));
		portHBox.setSpacing(5);
		portHBox.setStyle("-fx-background-colour: #336699;");
		setIntegerTextFormat();
		Label portLabel = new Label("Port");
		portTextField = new TextField();
		portTextField.setPrefWidth(50);
		portTextField.setTextFormatter(new TextFormatter<Integer>(integerFormatter));
		portHBox.getChildren().addAll(portLabel, portTextField);
		return portHBox;
	}
	
	/* VBox that contains the two HBoxes, one for the ip address and one for the port */
	private VBox ipAndPortVBoxCreation() {
	    VBox ipAndPortVbox = new VBox();
	    ipAndPortVbox.setPadding(new Insets(10));
	    ipAndPortVbox.setSpacing(8);
	    
	    HBox ipHBox = ipHBoxCreation();
		HBox portHBox = portHBoxCreation();
	   
	    ipAndPortVbox.getChildren().addAll(ipHBox, portHBox);
	    return ipAndPortVbox;
	}
	
	/* action listener method for the browse button */
	private class BrowseButtonListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			openFileButton();
		}
	}
	
	/* action listener function for the connect button */
	private class ConnectButtonListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			getIpAddressAndPort();
		}
	}
	
	/* method for getting the ip address and the port from the text boxes */
	private void getIpAddressAndPort() {
		Integer ipAddressText_1 = Integer.parseInt(ipAddressTextField_1.getText());
		Integer ipAddressText_2 = Integer.parseInt(ipAddressTextField_2.getText());
		Integer ipAddressText_3 = Integer.parseInt(ipAddressTextField_3.getText());
		Integer ipAddressText_4 = Integer.parseInt(ipAddressTextField_4.getText());
		Integer portNum = Integer.parseInt(portTextField.getText());
		
		String ipAddress = (ipAddressText_1 + "." + ipAddressText_2 + "." + ipAddressText_3
								+ "." + ipAddressText_4);
		String port = ("" + portNum);
		System.out.println("IP Address: " + ipAddress + "");
		System.out.println("Port: " + port + "");
		
	}
	
	/* method for opening the file explorer to open an XML file */
	private void openFileButton() {
		File xmlFile = xmlFileChooser.showOpenDialog(primaryStage);
		if (xmlFile != null) {
			actionStatus.setText("File selected: " + xmlFile.getName());
		}
		else {
			actionStatus.setText("File selection cancelled.");
		}
	}	
}
