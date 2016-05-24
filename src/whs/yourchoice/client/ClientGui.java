/**
* ClientGui.java v0.4 20/05/16
*
* Copyright and Licensing Information if applicable
*/

package whs.yourchoice.client;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import whs.yourchoice.parsers.PresentationParser;
import whs.yourchoice.presentation.PresentationEntry;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
/**
* Class for creation of the Client GUI and adding functionality
*
* @author cd828 & ch1092
* @version v0.4 20/05/16
*/
public class ClientGui extends Application{
	// object for the file chooser for getting the XML file
	private FileChooser xmlFileChooser;

	private Stage primaryStage;

	private Button loginButton;
	private Button connectButton;
	private Button disconnectButton;
	// presentation related declarations
	private File xmlFile;
	private PresentationEntry presentation;
	
	// text box to show the file browse status
	private Text actionStatus;
	
	private PasswordField passwordTextField;
	
	private CheckBox adminCheckBox;
	
	private boolean autoConnect = true;
	private boolean validPassword = false;
	private boolean adminMode = false;
	private final static String numbers = "0123456789";
	private final static String lowercase = "abcdefghijklmnopqrstuvwxyz";
	private final static String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private final static String acceptablePasswordCharacters = numbers+lowercase+uppercase;

	
	// Declare a backend for the client
	private Client client;
	

	private ConfigureWindow configureWindow = new ConfigureWindow();
	
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
		
		MenuBar menuBar = createMenuBar();
		
		VBox loginVBox = loginVBoxCreation();
		
		//place the boxes inside the layout created
		guiLayout.setTop(menuBar);
		guiLayout.setBottom(buttonHBox);
		guiLayout.setRight(logHBox);
		guiLayout.setCenter(loginVBox);
		
		//main stage set up with appropriate scene and size
		primaryStage.setScene(scene);
		primaryStage.setHeight(600);
		primaryStage.setWidth(800);
		primaryStage.setTitle("YourChoice");
		primaryStage.show();
		
		autoConnect();
	}

	/**
	 * Method for the creation of the HBox that contains the buttons
	 * @return HBox  -  The box that contains the buttons 
	 */
	private HBox buttonHBoxCreation() {
		HBox buttonHBox = new HBox();
		buttonHBox.setPadding(new Insets(0, 0, 10, 235));
		buttonHBox.setSpacing(10);
		buttonHBox.setStyle("-fx-background-colour: #336699;");

		loginButtonSetup();
		
		connectButtonSetup();
		
		disconnectButtonSetup();
		
		buttonHBox.getChildren().addAll(loginButton, connectButton, disconnectButton);
		
		return buttonHBox;	
	}

	/**
	 * Method to setup the request button
	 */
	private void loginButtonSetup() {
		loginButton = new Button("Login As Guest");
		loginButton.setDisable(false);
		loginButton.setPrefSize(100, 20);
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.out.println(passwordTextField.getText());
				if (!(null == client.serverSocket)) {
					if(!client.serverSocket.isClosed()) {
						if (!validPassword) {
							if (adminCheckBox.isSelected()) {
								try {
									validPassword = client.checkPassword("Admin", passwordTextField.getText());
									adminMode = validPassword;
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								System.out.println(validPassword);
								
								System.out.println(passwordTextField.getText());
							}
							else {
								try {
									validPassword = client.checkPassword("Guest", null);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								System.out.println(validPassword);
							}
							
						}
					}
				}
				
			}
		});
	}
	
	
	
	/**
	 * Method to setup the connect button
	 */
	private void connectButtonSetup() {
		connectButton = new Button("Connect");
		connectButton.setDisable(autoConnect);
		connectButton.setPrefSize(100, 20);
		//action event that happens when the connect button is pressed
		//ip address and port from text boxes is stored and shown in the console
		connectButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					client.openSocket(configureWindow.getIpAddress(), configureWindow.getPort());
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					connectButton.setDisable(false);
					disconnectButton.setDisable(true);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
					System.out.println("Could not connect to server");
					connectButton.setDisable(false);
					disconnectButton.setDisable(true);
				}
				
				if (!(null == client.serverSocket)) {
					if(!client.serverSocket.isClosed()) {
						connectButton.setDisable(true);
						disconnectButton.setDisable(false);
					}
				}
				
			}
		});
	}

	/**
	 * Method to setup the disconnect button
	 */
	private void disconnectButtonSetup() {
		disconnectButton = new Button("Disconnect");
		disconnectButton.setDisable(!autoConnect);
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
	 * Method for the creation of the HBox that contains the log for the file explorer
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
	 * @return
	 */
	private HBox passwordHBoxCreation() {
		HBox passwordHBox = new HBox();
		passwordHBox.setPadding(new Insets(10, 10, 10, 10));
		passwordHBox.setSpacing(5);
		passwordHBox.setStyle("-fx-background-colour: #336699;");
		Label passwordLabel = new Label("Password:");
		passwordLabel.setPadding(new Insets(5, 0, 0, 0));
		passwordTextField = new PasswordField();
		addTextLimiter(passwordTextField, 20);
		passwordTextField.addEventFilter(KeyEvent.KEY_TYPED, charFilter());
		passwordTextField.setPrefWidth(100);
		passwordTextField.setDisable(true);
		passwordHBox.getChildren().addAll(passwordLabel, passwordTextField);
		return passwordHBox;
	}
	
	public static void addTextLimiter(final TextField tf, final int maxLength) {
	    tf.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (tf.getText().length() > maxLength) {
	                String s = tf.getText().substring(0, maxLength);
	                tf.setText(s);
	            }
	        }
	    });
	}
	
	public static EventHandler<KeyEvent> charFilter() {

        EventHandler<KeyEvent> aux = new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                if (!acceptablePasswordCharacters.contains(keyEvent.getCharacter())) {
                    keyEvent.consume();
                }
            }
        };
        return aux;
    }
	
	/**
	 * @return
	 */
	private HBox adminLoginHBoxCreation() {
		HBox admninLoginHBox = new HBox();
		admninLoginHBox.setPadding(new Insets(10, 10, 10, 40));
		admninLoginHBox.setSpacing(5);
		admninLoginHBox.setStyle("-fx-background-colour: #336699;");
		adminCheckBox = new CheckBox("Admin Login");
		adminCheckBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (adminCheckBox.isSelected()){
					passwordTextField.setDisable(false);
					loginButton.setText("Login As Admin");
					
				}
				else
				{
					passwordTextField.setDisable(true);
					loginButton.setText("Login As Guest");
				}
			}
		});
		admninLoginHBox.getChildren().add(adminCheckBox);
		return admninLoginHBox;
	}
	
	/**
	 * @return
	 */
	private VBox loginVBoxCreation() {
		VBox loginVBox = new VBox();
		loginVBox.setPadding(new Insets(200, 200, 200, 300));
		HBox passwordHBox = passwordHBoxCreation();
		HBox guestLoginHBox = adminLoginHBoxCreation();
		loginVBox.getChildren().addAll(passwordHBox, guestLoginHBox);
		return loginVBox;
	}
	/**
	 * Method that creates the menu bar on the top of the window
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
        	        	   configureWindow.start(new Stage());
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
					
					System.out.println(xmlFile.getParent());
					presentation = parser.parsePresention(xmlFile.getAbsolutePath(), xmlFile.getParent());
					Platform.runLater(new Runnable() {
		 	    		 public void run() {             
		 	    			try {				 	        	  
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

	/**
	 * Method to get the ID of the client
	 * 
	 * @return iD	-	The ID of the client
	 */
	protected int getID() {
		// TODO Auto-generated method stub
		return client.getID();
	}

	/**
	 * Method to handle automatic connection to the server
	 * if the autoConnect boolean is true
	 */
	private void autoConnect() {
		if (autoConnect) {
			try {
				client.openSocket(configureWindow.getIpAddress(), configureWindow.getPort());
				connectButton.setDisable(true);
				disconnectButton.setDisable(false);
				if(-1 == getID()) {
					client.closeSocket();
					connectButton.setDisable(false);
					disconnectButton.setDisable(true);
				}
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				connectButton.setDisable(false);
				disconnectButton.setDisable(true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				System.out.println("Could not connect to server");
				connectButton.setDisable(false);
				disconnectButton.setDisable(true);
			}
						
		}
	}

}
