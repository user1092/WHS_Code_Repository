/**
 * Licensing information
 */
package whs.yourchoice.client;

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
import javafx.stage.Stage;
import javafx.util.StringConverter;
/**
* Class for creation of the configure window for inputting 
* the ip address and port of the server
*
* @author cd828 & ch1092
* @version v0.2 20/05/16
*/
public class ConfigureWindow extends Application {

	private String ipAddress = "127.0.0.1";
	private int port = 1138;
	// text field used for inputting the ip address and port of the server
	private TextField ipAddressTextField_1, ipAddressTextField_2, ipAddressTextField_3, 
							ipAddressTextField_4, portTextField;
	// object for formatting the text fields to only accept integers
	private StringConverter<Integer> integerFormatter;
	
	private Button saveButton;
	private Button closeButton;
		
	public ConfigureWindow () {
	}
	
	public static void main(String[] args) {
		launch(ConfigureWindow.class, args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane guiLayout = new BorderPane();
		Scene scene = new Scene(guiLayout);
		//Box that contains the ip address and port labels and text boxes
		VBox ipAndPortVbox = ipAndPortVBoxCreation();
		// Box that contains the buttons "Connect" and "Cancel"
		HBox buttonHBox = buttonHBoxCreation(primaryStage);
		//import and set background image
		String background = ClientGui.class.getResource("resources/SlideBackground.jpg").toExternalForm();
		guiLayout.setStyle("-fx-background-image: url('" + background + "'); " +
		           "-fx-background-position: center center; " +
		           "-fx-background-repeat: stretch;");

		guiLayout.setCenter(ipAndPortVbox);
		guiLayout.setBottom(buttonHBox);
		
		//main stage set up wit appropriate scene and size
		primaryStage.setScene(scene);
		primaryStage.setHeight(250);
		primaryStage.setWidth(500);
		primaryStage.setTitle("Configure Server");
		primaryStage.show();
	}
	
	/**
	 * Method for the creation of the HBox that contains the ip address labels and text boxes
	 * @return HBox  -  The box that contains the ip address labels and text boxes
	 */
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
		integerFormatter = new IntRangeStringConverter(0, 255, 3);
		//labels and text boxes instantiation for the ip address
		Label ipAddressLabel = new Label("IP Address");
		ipAddressTextField_1 = new TextField();
		ipAddressTextField_1.setPrefWidth(40);
		ipAddressTextField_1.setTextFormatter(new TextFormatter<>(integerFormatter, 127));
		Label dot_1 = new Label(".");
		ipAddressTextField_2 = new TextField();
		ipAddressTextField_2.setPrefWidth(40);
		ipAddressTextField_2.setTextFormatter(new TextFormatter<>(integerFormatter, 0));
		Label dot_2 = new Label(".");
		ipAddressTextField_3 = new TextField();
		ipAddressTextField_3.setPrefWidth(40);
		ipAddressTextField_3.setTextFormatter(new TextFormatter<>(integerFormatter, 0));
		Label dot_3 = new Label(".");
		ipAddressTextField_4 = new TextField();
		ipAddressTextField_4.setPrefWidth(40);
		ipAddressTextField_4.setTextFormatter(new TextFormatter<>(integerFormatter, 1));
		//place the objects for the ip address in the HBox
		ipHBox.getChildren().addAll(ipAddressLabel, ipAddressTextField_1, dot_1, ipAddressTextField_2,
				dot_2, ipAddressTextField_3, dot_3, ipAddressTextField_4);
		return ipHBox;	
	}

	/**  
	 * Method for the creation of the HBox that contains the port label and text box
	 * @return HBox  -  The box that contains the port label and text box
	 */
	private HBox portHBoxCreation() {
		HBox portHBox = new HBox();
		portHBox.setPadding(new Insets(10, 10, 10, 20));
		portHBox.setSpacing(5);
		portHBox.setStyle("-fx-background-colour: #336699;");
		integerFormatter = new IntRangeStringConverter(0, 65535, 5);
		Label portLabel = new Label("Port");
		portTextField = new TextField();
		portTextField.setPrefWidth(50);
		portTextField.setTextFormatter(new TextFormatter<>(integerFormatter, 1138));
		portHBox.getChildren().addAll(portLabel, portTextField);
		return portHBox;
	}
	
	/**
	 * VBox that contains the two HBoxes, one for the ip address and one for the port
	 * @return ipAndPortVbox  --  The box that contains the ip address and port HBoxes
	 */
	private VBox ipAndPortVBoxCreation() {
	    VBox ipAndPortVbox = new VBox();
	    ipAndPortVbox.setPadding(new Insets(40, 0, 0, 90));
	    ipAndPortVbox.setSpacing(8);
	    
	    HBox ipHBox = ipHBoxCreation();
		HBox portHBox = portHBoxCreation();
	   
	    ipAndPortVbox.getChildren().addAll(ipHBox, portHBox);
	    return ipAndPortVbox;
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
		saveButtonSetup(primaryStage);
		closeButtonSetup(primaryStage);
		buttonHBox.getChildren().addAll(saveButton, closeButton);
		return buttonHBox;	
	}
	
	/**
	 * Method for setting up the cancel button, when pressed the window closes
	 * @param primaryStage  -  The application window
	 */
	private void saveButtonSetup(final Stage primaryStage) {
		saveButton = new Button("Save");
		saveButton.setPrefSize(100, 20);
		saveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				Integer ipAddressText_1 = Integer.parseInt(ipAddressTextField_1.getText());
				Integer ipAddressText_2 = Integer.parseInt(ipAddressTextField_2.getText());
				Integer ipAddressText_3 = Integer.parseInt(ipAddressTextField_3.getText());
				Integer ipAddressText_4 = Integer.parseInt(ipAddressTextField_4.getText());
				
				ipAddress = (ipAddressText_1 + "." + ipAddressText_2 + "." + ipAddressText_3
						+ "." + ipAddressText_4);

				port = Integer.parseInt(portTextField.getText());
				
			}
		});
	}
	
	/**
	 * Method for setting up the cancel button, when pressed the window closes
	 * @param primaryStage  -  The application window
	 */
	private void closeButtonSetup(final Stage primaryStage) {
		closeButton = new Button("Close");
		closeButton.setPrefSize(100, 20);
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				primaryStage.close();
			}
		});
	}
	
	/**
	 * Method for returning the ipAddress stored
	 * 
	 * @return ipAddress - the server's ip Address
	 */
	public String getIpAddress() {
		return ipAddress;
	}
	
	/**
	 * Method for returning the port number stored
	 * 
	 * @return port - the server's port number
	 */
	public int getPort() {
		return port;
		
	}
}
