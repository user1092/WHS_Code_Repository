/**
* ClientGui.java v0.2 28/01/16
*
* Copyright and Licensing Information if applicable
*/

package whs.yourchoice.client;

import java.io.File;

import whs.yourchoice.parsers.PresentationParser;
import whs.yourchoice.presentation.PresentationEntry;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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
/**
* Class for creation of the Client GUI and adding functionality
*
* @author user828 & user1092
* @version v0.2 28/01/16
*/
public class ClientGui extends Application{
	// object for the file chooser for getting the XML file
	private FileChooser xmlFileChooser;

	private Stage primaryStage;

	private Button requestButton;
	private Button connectButton;
	private Button disconnectButton;
	
	// presentation related declarations
	private File xmlFile;
	private PresentationEntry presentation;
	
	// text box to show the file browse status
	private Text actionStatus;
	// text field used for inputting the ip address and port of the server
	private TextField ipAddressTextField_1, ipAddressTextField_2, ipAddressTextField_3, 
							ipAddressTextField_4, portTextField;
	// object for formatting the text fields to only accept integers
	private StringConverter<Integer> integerFormatter;
	
	// Declare a backend for the client
	private Client client;
	
	public ClientGui() {
		client = new Client();
	}
	
	public static void main(String[] args) {
		launch(ClientGui.class, args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane guiLayout = new BorderPane();
		Scene scene = new Scene(guiLayout);
		
		//import and set background image
		String background = ClientGui.class.getResource("resources/SlideBackground.jpg").toExternalForm();
		guiLayout.setStyle("-fx-background-image: url('" + background + "'); " +
		           "-fx-background-position: center center; " +
		           "-fx-background-repeat: stretch;");
		
		//Button box set up
		HBox buttonHBox = buttonHBoxCreation();
		//Log box set up
		HBox logHBox = logHBoxCreation();
		//Box that contains the ip address and port labels and text boxes
		VBox ipAndPortVbox = ipAndPortVBoxCreation();
		
		MenuBar menuBar = createMenuBar();
		
		//place the boxes inside the layout created
		guiLayout.setTop(menuBar);
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
	
	/** 
	 * class for setting the text fields to only accept integers 
	 * and have a range of 0-255 for the ip address and 0-65535 for the port
	 */
	private static class IntRangeStringConverter extends StringConverter<Integer> {

	    private final int min;
	    private final int max;
	    private final int length;
	    
	    /**
	     * Method for instantiating the max min values and length of the text fields
	     * @param min  -  Minimum value of text field
	     * @param max  -  Maximum value of text field
	     * @param length  -  Length of the text field
	     */
	    public IntRangeStringConverter(int min, int max, int length) {
	        this.min = min;
	        this.max = max;
	        this.length = length;
	    }
	    
	    /**
	     * Method for setting the length of the text field
	     * @param object  -  the number of the length of the text field
	     * @return string  -  
	     */
	    @Override
	    public String toString(Integer object) {
	        return String.format("%0"+length+"d", object);
	    }

	    /**
	     * Method for setting the maximum and minimum value of the text field
	     * @param string
	     * @return integer  -  the integer value of the max and min
	     */
	    @Override
	    public Integer fromString(String string) {
	        int integer = Integer.parseInt(string);
	        if (integer > max || integer < min) {
	            throw new IllegalArgumentException();
	        }
	        return integer;
	    }
	}

	/**
	 * method for the creation of the HBox that contains the buttons
	 * @return HBox  -  The box that contains the buttons 
	 */
	private HBox buttonHBoxCreation() {
		HBox buttonHBox = new HBox();
		buttonHBox.setPadding(new Insets(0, 0, 10, 235));
		buttonHBox.setSpacing(10);
		buttonHBox.setStyle("-fx-background-colour: #336699;");

		requestButtonSetup();
		
		connectButtonSetup();
		
		disconnectButtonSetup();
		
		buttonHBox.getChildren().addAll(requestButton, connectButton, disconnectButton);
		
		return buttonHBox;	
	}

	/**
	 * Method to setup the request button
	 */
	private void requestButtonSetup() {
		requestButton = new Button("Request");
		requestButton.setDisable(false);
		requestButton.setPrefSize(100, 20);
		requestButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.out.println("Requesting presentation from server");
			}
		});
	}
	
	/**
	 * Method to setup the connect button
	 */
	private void connectButtonSetup() {
		connectButton = new Button("Connect");
		connectButton.setDisable(false);
		connectButton.setPrefSize(100, 20);
		//action event that happens when the connect button is pressed
		//ip address and port from text boxes is stored and shown in the console
		connectButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Integer ipAddressText_1 = Integer.parseInt(ipAddressTextField_1.getText());
				Integer ipAddressText_2 = Integer.parseInt(ipAddressTextField_2.getText());
				Integer ipAddressText_3 = Integer.parseInt(ipAddressTextField_3.getText());
				Integer ipAddressText_4 = Integer.parseInt(ipAddressTextField_4.getText());
				int port = Integer.parseInt(portTextField.getText());
				
				String ipAddress = (ipAddressText_1 + "." + ipAddressText_2 + "." + ipAddressText_3
										+ "." + ipAddressText_4);
			
				client.openSocket(ipAddress, port);
				if(!client.serverSocket.isClosed()) {
					connectButton.setDisable(true);
					disconnectButton.setDisable(false);
				}
			}
		});
	}

	/**
	 * Method to setup the disconnect button
	 */
	private void disconnectButtonSetup() {
		disconnectButton = new Button("Disconnect");
		disconnectButton.setDisable(true);
		disconnectButton.setPrefSize(100, 20);
		disconnectButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				client.closeSocket();
				if(client.serverSocket.isClosed()) {
					connectButton.setDisable(false);
					disconnectButton.setDisable(true);
				}
			}
		});
	}

	/**
	 * method for the creation of the HBox that contains the ip address labels and text boxes
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
	 * method for the creation of the HBox that contains the log for the file explorer
	 * @return HBox  -  The box that contains the log for the file explorer
	 */
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
	
	/**  
	 * method for the creation of the HBox that contains the port label and text box
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
	 * @return VBox  --  The box that contains the ip address and port HBoxes
	 */
	private VBox ipAndPortVBoxCreation() {
	    VBox ipAndPortVbox = new VBox();
	    ipAndPortVbox.setPadding(new Insets(10));
	    ipAndPortVbox.setSpacing(8);
	    
	    HBox ipHBox = ipHBoxCreation();
		HBox portHBox = portHBoxCreation();
	   
	    ipAndPortVbox.getChildren().addAll(ipHBox, portHBox);
	    return ipAndPortVbox;
	}
	
	/**
	 * method that creates the menu bar on the top of the window
	 * @return menuBar  -  menu bar object that contains the open file and configure menus
	 */
	private MenuBar createMenuBar() {
		// instantiation of the menu bar
		MenuBar menuBar = new MenuBar();
		MenuItem configureServer = new MenuItem("Configure");
		configureServer.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
            	Platform.runLater(new Runnable() {
        	       public void run() {             
        	           try {
        	        	   new ConfigureWindow().start(new Stage());
        	           } 
        	           catch (Exception e) {
        	        	   // TODO Auto-generated catch block
        	        	   e.printStackTrace();
        	           }
        	       }
        	    });
            }
		});
		// File->OpenFile... submenu 
        MenuItem openFile = new MenuItem("Open File...");
        openFile.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
            	xmlFileChooser = new FileChooser();
				// Restrict choice of files to xml
				FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("XML files (*.xml, *.XML)", "*.xml", "*.XML");
				xmlFileChooser.getExtensionFilters().add(extensionFilter);
				
				xmlFile = xmlFileChooser.showOpenDialog(primaryStage);
				if (xmlFile != null) {
					actionStatus.setText("File selected: " + xmlFile.getName());
					PresentationParser parser = new PresentationParser();		
					
					presentation = parser.parsePresention(xmlFile.getPath());
					PresentationGui presentationGui;
					//presentationGui = new PresentationGui(presentation);
					Platform.runLater(new Runnable() {
				 	       public void run() {             
				 	           try {
				 	        	   //PresentationGui presentationGui = new  PresentationGui(presentation);
				 	        	   //presentationGui.show();
				 	        	   new PresentationGui(presentation).start(new Stage());
				 	           } 
				 	           catch (Exception e) {
				 	        	   // TODO Auto-generated catch block
				 	        	   e.printStackTrace();
				 	           }
				 	       }
				 	    });
				}
				else {
					actionStatus.setText("File selection cancelled.");
				}
			}
        }); 
        Menu optionsMenu = new Menu("Options");
        optionsMenu.getItems().addAll(configureServer);
		Menu fileMenu = new Menu("File");
		fileMenu.getItems().addAll(openFile);
		menuBar.getMenus().addAll(fileMenu, optionsMenu);
		return menuBar;
	}

	protected Object getID() {
		// TODO Auto-generated method stub
		return client.getID();
	}

}
