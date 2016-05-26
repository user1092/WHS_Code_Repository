/**
* Licensing information
* 
* Copyright Woolly Hat Software
*/

package whs.yourchoice.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;

import whs.yourchoice.parsers.PresentationParser;
import whs.yourchoice.presentation.PresentationEntry;
import whs.yourchoice.utilities.ZipUtilities;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
* Class for creation of the Client GUI and adding functionality
*
* @author cd828, ch1092, ws659
* @version v0.6 26/05/16
*/
public class ClientGui extends Application{

	private Stage primaryStage;
	private StackPane contentLayout;
	private BorderPane loginLayout;
	private GridPane modulePane;

	private Button loginButton;
	private Button connectButton;
	private Button disconnectButton;
	private Button requestModuleButton;
	
	private ComboBox<String> moduleCombo;
	private ComboBox<String> streamCombo;
	private ComboBox<String> yearCombo;
	private ComboBox<String> courseCombo;
	private String selModule;
	private String selStream;
	private String selYear;
	private String selCourse;
	ObservableList<String> obsStreams;
	ObservableList<String> obsYear;
	ObservableList<String> obsModules;
	
	// Presentation related declarations
	private PresentationEntry presentation;
	
	// text box to show the file browse status
	private Text actionStatus;
	
	private PasswordField passwordTextField;
	
	private CheckBox adminCheckBox;
	
	private ProgressBar progressBar;
	
	private boolean autoConnect = true;
	private boolean validPassword = false;
	private boolean adminMode = false;
	
	// Character restrictions on admin password
	private final static String NUMBERS = "0123456789";
	private final static String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
	private final static String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private final static String ACCEPTABLE_PASSWORD_CHARACTERS = NUMBERS+LOWERCASE+UPPERCASE;
	
	// ZIP related
	private String tempPresentationDirectory = "temp";
	
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
		contentLayout = new StackPane();
		loginLayout = new BorderPane();
		
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
		
		loginLayout.setBottom(buttonHBox);
		loginLayout.setCenter(loginVBox);
				
		//place the boxes inside the layout created
		guiLayout.setTop(menuBar);
		guiLayout.setRight(logHBox);
		guiLayout.setCenter(contentLayout);
		
		//main stage set up with appropriate scene and size
		primaryStage.setScene(scene);
		primaryStage.setHeight(600);
		primaryStage.setWidth(800);
		primaryStage.setTitle("YourChoice");
		
		progressBar = new ProgressBar(0);
		progressBar.setProgress(0);
		
		// Create a temp folder to hold presentations
		new File(tempPresentationDirectory).mkdir();
		
		autoConnect();
		
		contentLayout.getChildren().add(progressBar);
		
		// listen to the stage if it closes
        closeListener(primaryStage);
		
		primaryStage.show();
	}
	
	
	/**
	 * Method to listen for the stage closing to tidy up
	 * 
	 * @param slideStage	-	The stage to listen to
	 */
	private void closeListener(Stage slideStage) {
		// Listen for the stage closing to release all audio players
        slideStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        	public void handle(WindowEvent we) {
        		File f = new File(tempPresentationDirectory);
        		if (f.exists()) {
        			try {
						delete(f);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
        	}
        });
	}
	
	/**
	 * Method to delete a file or folder
	 * 
	 * @param f	-	The file/folder to be deleted 
	 * @throws IOException
	 */
	void delete(File f) throws IOException {
		// Windows is shit so don't delete Thumbs.db as it will be locked
		if (!(f.getName()).equals("Thumbs.db")) {
			if (f.isDirectory()) {
				for (File c : f.listFiles())
					delete(c);
			}
			if (!f.delete())
				throw new FileNotFoundException("Failed to delete file: " + f);
		}
	}
	

	/**
	 * Method for the creation of the HBox that contains the buttons
	 * 
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
	 * Creates a GridPane for module selection UI to be added
	 * @return GridPane modulePlane
	 */
	private GridPane moduleGPCreation() {
		GridPane modulePane = new GridPane();
		modulePane.setPadding(new Insets(10, 0, 10, 0));
		
		return modulePane;
	}
	
	/**
	 * Adds UI elements for module selection
	 */
	private void addModuleSelection() {
		modulePane = moduleGPCreation();
		moduleCombo = constructModuleCombo();
		Label moduleLable = new Label("Module:");
		streamCombo = constructStreamCombo();
		Label streamLable = new Label("Stream:");
		yearCombo = constructYearCombo();
		Label yearLable = new Label("Year:");
		courseCombo = constructCourseCombo();
		Label courseLable = new Label("Course:");
		
		//add course selector
		GridPane.setRowIndex(courseCombo, 1);
		GridPane.setColumnIndex(courseCombo, 2);
		GridPane.setRowIndex(courseLable, 1);
		GridPane.setColumnIndex(courseLable, 1);
		modulePane.getChildren().addAll(courseCombo, courseLable);
		
		//add stream selector
		GridPane.setRowIndex(streamCombo, 2);
		GridPane.setColumnIndex(streamCombo, 2);
		GridPane.setRowIndex(streamLable, 2);
		GridPane.setColumnIndex(streamLable, 1);
		modulePane.getChildren().addAll(streamCombo, streamLable);
		
		//add year selector
		GridPane.setRowIndex(yearCombo, 3);
		GridPane.setColumnIndex(yearCombo, 2);
		GridPane.setRowIndex(yearLable, 3);
		GridPane.setColumnIndex(yearLable, 1);
		modulePane.getChildren().addAll(yearCombo, yearLable);
		
		//add module selector
		GridPane.setRowIndex(moduleCombo, 4);
		GridPane.setColumnIndex(moduleCombo, 2);
		GridPane.setRowIndex(moduleLable, 4);
		GridPane.setColumnIndex(moduleLable, 1);
		modulePane.getChildren().addAll(moduleCombo, moduleLable);
		
		// Allow mouse clicks on items on other Panes
		modulePane.setPickOnBounds(false);
		
		contentLayout.getChildren().add(modulePane);
	}
	
	/**
	 * Creates a combobox for user to select Module
	 * @return ComboBox moduleCombo
	 */
	private ComboBox<String> constructModuleCombo(){
		ComboBox<String> moduleCombo = new ComboBox<String>();
		ObservableList<String> obsModules = FXCollections.observableArrayList(client.getRevievedModules());
		moduleCombo.setItems(obsModules);
		moduleCombo.setDisable(true);
		moduleCombo.setPrefSize(200, 30);
		//listen for user selection
		moduleCombo.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue o, String oldModule, String newModule) {
				selModule = newModule;
			}
		});
		return moduleCombo;
	}
	
	/**
	 * Creates a combobox for user to select stream
	 * @return ComboBox streamCombo
	 */
	private ComboBox<String> constructStreamCombo(){
		ComboBox<String> streamCombo = new ComboBox<String>();
		streamCombo.setDisable(true);
		streamCombo.setPrefSize(200, 30);
		
		//listen for user selection
		streamCombo.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue o, String oldStream, String newStream) {
				yearCombo.setDisable(true);
				moduleCombo.setDisable(true);
				if (!newStream.equals("")) {
					selStream = newStream;
					System.out.println(selStream);
					if (!selCourse.equals("")) {
						//get a list of matching years
						obsYear = FXCollections.observableArrayList(client.getYearsByStream(selStream));
						yearCombo.setItems(obsYear);
						yearCombo.setDisable(false);
					}
				}
			}
		});
		return streamCombo;
	}
	
	/**
	 * Creates a combobox for user to select Year
	 * @return ComboBox yearCombo
	 */
	private ComboBox<String> constructYearCombo(){
		ComboBox<String> yearCombo = new ComboBox<String>();
		yearCombo.setDisable(true);
		yearCombo.setPrefSize(200, 30);
		
		//listen for user selection
		yearCombo.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue o, String oldYear, String newYear) {
				if (!newYear.equals("")) {
					selYear = newYear;
					if (!selCourse.equals("") && !selStream.equals("")) {
						//get a list of matching modules
						obsModules = FXCollections.observableArrayList(client.getModulesByYear(selYear));
						moduleCombo.setItems(obsModules);
						moduleCombo.setDisable(false);
					}
				}
			}
		});
		return yearCombo;
	}
	
	/**
	 * Creates a combobox for user to select course
	 * @return ComboBox courseCombo
	 */
	private ComboBox<String> constructCourseCombo(){
		ComboBox<String> courseCombo = new ComboBox<String>();
		ObservableList<String> obsCourse = FXCollections.observableArrayList(client.getRevievedCourses());
		courseCombo.setItems(obsCourse);
		courseCombo.setDisable(false);
		courseCombo.setPrefSize(200, 30);
		
		//listen for user selection
		courseCombo.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue o, String oldCourse, String newCourse) {
				streamCombo.setDisable(true);
				yearCombo.setDisable(true);
				moduleCombo.setDisable(true);
				selCourse = newCourse;
				if (!newCourse.equals("")) {
					//get a list of matching streams
					obsStreams = FXCollections.observableArrayList(client.getStreamByCourse(selCourse));
					streamCombo.setItems(obsStreams);
					streamCombo.setDisable(false);
				}
			}
		});
		return courseCombo;
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
							}
							else {
								try {
									validPassword = client.checkPassword("Guest", null);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							
						}
						if (validPassword) {
							contentLayout.getChildren().clear();
							requestModuleButtonSetup();
							addModuleSelection();
							//constructModuleCombo();
						}
					}
				}
			}
		});
	}
	
	
	/**
	 * Method to create a request module button and place it on the content layout
	 */
	private void requestModuleButtonSetup() {
		Button requestModuleButton = new Button("Request Module");
		requestModuleButton.setDisable(false);
		requestModuleButton.setPrefSize(150, 20);
		contentLayout.getChildren().add(requestModuleButton);
		requestModuleButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				requestModule();
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
	 * 
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
	 * Method for the creation of the HBox that contains the password label and text field
	 * 
	 * @return HBox  -  The box that contains the label and text field
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
	
	
	/**
	 * Method to limit the length of the password
	 * 
	 * @param tf  -  The text field which the limit is applied to
	 * @param maxLength  -  The maximum length of the text field
	 */
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
	
	/**
	 * Event handler to restrict the characters entered in the password text field
	 * 
	 * @return EventHandler  -  The handler that restricts the characters
	 */
	public static EventHandler<KeyEvent> charFilter() {

        EventHandler<KeyEvent> aux = new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                if (!ACCEPTABLE_PASSWORD_CHARACTERS.contains(keyEvent.getCharacter())) {
                    keyEvent.consume();
                }
            }
        };
        return aux;
    }
	
	
	/**
	 * Method for creating the HBox that contains the admin login check box
	 * 
	 * @return HBox  -  The HBox that contains the check box
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
	 * Method that creates the VBox that contains passwordHBox and the guestLoginHBox
	 * 
	 * @return VBox  -  The VBox that containts the two other HBoxes
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
	 * 
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
            	// object for the file chooser for getting the XML file
            	FileChooser xmlFileChooser = new FileChooser();
				// Restrict choice of files to xml
				FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("XML files (*.xml, *.XML)", "*.xml", "*.XML");
				xmlFileChooser.getExtensionFilters().add(extensionFilter);
				File xmlFile;
				xmlFile = xmlFileChooser.showOpenDialog(primaryStage);
				if (xmlFile != null) {
					actionStatus.setText("File selected: " + xmlFile.getName());
					openPresentation(xmlFile);
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
	 * Method to open a presentation in the PresentationGui
	 * 
	 * @param xmlFile	-	The xml file that contains the presentation
	 */
	private void openPresentation(File xmlFile) {
		PresentationParser parser = new PresentationParser();		
		
		System.out.println(xmlFile.getParent());
		presentation = parser.parsePresention(xmlFile.getAbsolutePath(), xmlFile.getParent());
		Platform.runLater(new Runnable() {
			 public void run() {             
				try {				 	        	  
					new PresentationGui(presentation, client).start(new Stage());
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		 });
	}
	
		
	/**
	 * Method to get the ID of the client
	 * 
	 * @return iD	-	The ID of the client
	 */
	protected int getID() {
		return client.getID();
	}
	

	/**
	 * Method to handle automatic connection to the server
	 * if the autoConnect boolean is true
	 */
	private void autoConnect() {
		if (autoConnect) {
			Task<Void> autoConnectTask = new Task<Void>() {
				@Override protected Void call() throws Exception {
					try {
						client.openSocket(configureWindow.getIpAddress(), configureWindow.getPort());
						Platform.runLater(new Runnable() {
							@Override public void run() {
								connectButton.setDisable(true);
								disconnectButton.setDisable(false);
							}
						});
						if(-1 == getID()) {
							client.closeSocket();
							Platform.runLater(new Runnable() {
								@Override public void run() {
									connectButton.setDisable(false);
									disconnectButton.setDisable(true);
								}
							});
						}
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						connectButton.setDisable(false);
						disconnectButton.setDisable(true);
					} catch (IOException e) {
						System.out.println("Could not connect to server");
						Platform.runLater(new Runnable() {
							@Override public void run() {
								connectButton.setDisable(false);
								disconnectButton.setDisable(true);
							}
						});
					}
					return null;
				}
			};
			final Thread autoConnectThread = new Thread(autoConnectTask);
			autoConnectThread.setDaemon(true);
			
			progressBar.progressProperty().bind(autoConnectTask.progressProperty());
			
			autoConnectThread.start();
						
			removeProgressBar(autoConnectThread, loginLayout);
		}
	}


	/**
	 * Method to remove the progress bar once compete, 
	 * listens to a thread until it is dead then removes
	 * 
	 * @param autoConnectThread
	 */
	private void removeProgressBar(final Thread thread, final Pane paneToDisplay) {
		Task<Void> waitTask = new Task<Void>() {
			@Override protected Void call() throws Exception {
				while (thread.isAlive()) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				Platform.runLater(new Runnable() {
		    		 public void run() {
		    			progressBar.progressProperty().unbind();
		    			progressBar.setProgress(0); 
		    			try {				 	        	  
		    				contentLayout.getChildren().remove(progressBar);
		    				if (null != paneToDisplay) {
		    					contentLayout.getChildren().add(paneToDisplay);
		    				}
		     			}
		    			catch (Exception e) {
		     				// TODO Auto-generated catch block
		     				e.printStackTrace();
		     			}
		    		 }
		    	});
				return null;
			}
		};
		Thread waitThread = new Thread(waitTask);
		waitThread.setDaemon(true);
		waitThread.start();
	}

	
	/**
	 * Method to request a module to be sent from the server and then open once received
	 */
	private void requestModule() {
		
		System.out.println(selModule);
		final String filename = client.getFilenameFromTitle(selModule);
		System.out.println(filename);
		
		final String name = removeFileExtension(filename);
		final String xmlFilename = name + ".xml";
		System.out.println(xmlFilename);
		
		
		contentLayout.getChildren().add(progressBar);
		
		Task<Void> requestTask = new Task<Void>() {
			@Override protected Void call() throws Exception {
				ZipUtilities unzipper = new ZipUtilities();
				
				boolean validZip = true;
				
				if (!(null == client.serverSocket)) {
					if (validPassword) {
						File zippedPresentation = null;
						try {
							client.sendData(filename);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							zippedPresentation = (File) client.receiveData();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							unzipper.unzip(zippedPresentation, tempPresentationDirectory + "/" + name);
//							unzipper.unzip(zippedPresentation, tempPresentationDirectory);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							validZip = false;
						}
						if (validZip) {
							openPresentation(new File(tempPresentationDirectory + "/" + name + "/" + xmlFilename));
//							openPresentation(new File(tempPresentationDirectory + "/" + xmlFilename));
						}
						else {
							System.out.println("Invalid Presentation");
						}
						
					}
				}				
				
				return null;
			}
		};
		Thread requestThread = new Thread(requestTask);
		requestThread.setDaemon(true);
				
		if (!(null == filename)){
			progressBar.progressProperty().bind(requestTask.progressProperty());
			requestThread.start();
			removeProgressBar(requestThread, null);
		}
	}

	/**
	 * @param filename
	 * @return
	 */
	private String removeFileExtension(final String filename) {
		String name = null;
		int pos = filename.lastIndexOf(".");
		if (pos > 0) {
			name = filename.substring(0, pos);
		}
		return name;
	}
}
