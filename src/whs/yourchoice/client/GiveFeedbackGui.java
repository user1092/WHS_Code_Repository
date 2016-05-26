/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.client;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
* Class for creation of the give feedback window which
* allows user to enter a name comment and rating for the
* current module which is then saved in a text file
*
* @author jcl513, gw679
* @version v0.2 25/05/16
*/
public class GiveFeedbackGui {
	
	// TextField is used for name as it only requires a single line input
	private TextField nameTextField;
	// TextArea is used for comment as it may require multiple lines of input
	private TextArea commentTextArea;
	// Specified character limits for name and comment fields
	private int nameCharLimit = 60;
	private int commentCharLimit = 120;
	
	// Strings to store contents of name and comment text field/area
	private String submittedName;
	private String submittedComment;
	// Rating is used to store an integer value 1-5 depending on the user's 
	// selection
	private int submittedRating = 0;
	// String used to store a formatted line of text ready for insertion into
	// text file
	private String submittedFeedback;
	
	// feedbackError dictates whether one or more fields have not been filled
	private Boolean feedbackError = false;
	// errorLabel is used to output an error message if one or more fields have
	// not been filled
	private Label errorLabel = new Label("");
	private Stage errorWindowStage = new Stage();
	
	// 5 buttons used to display clickable star image and the corresponding
	// images/image views to go with each button
	private Button[] starButtons = new Button[5];
	private Image emptyStarImage;
	private Image fullStarImage;
	private ImageView[] emptyStarImageView = new ImageView[5];
	private ImageView[] fullStarImageView = new ImageView[5];
	// Each button has a hold value dictating whether the image should toggle 
	// or not depending on what the user has/hasn't clicked
	private Boolean[] holdStar = new Boolean[5];
	
	// Path name will need changing and adapting for integration
	private String textFilePath = null;
	// Module name should be passed to this class so it can be displayed
	private String moduleName;
	private Client client;
	private Stage stage;
	
	// Give feedback gui requires a stage to display on
	// moduleName is the name of the current module to be displayed
	public void start(Stage primaryStage, String moduleName, 
						String textFilePath, Client client) throws Exception {
		this.textFilePath = textFilePath;
		this.moduleName = moduleName;
		this.client = client;
		this.stage = primaryStage;
		
		BorderPane guiLayout = new BorderPane();
		Scene scene = new Scene(guiLayout);
				
		// Import and set background image
		String background = ClientGui.class.getResource("resources/SlideBackground.jpg").toExternalForm();
		guiLayout.setStyle("-fx-background-image: url('" + background + "'); " +
		           "-fx-background-position: center center; " +
		           "-fx-background-repeat: stretch;");

		// Create the three main boxes - title, feedback entry area and
		// 								buttons to submit or close window
		VBox titleVBox = titleVBoxCreation(moduleName);
		VBox feedbackEntryVBox = feedbackEntryVBoxCreation();
		HBox buttonsHBox = buttonsHBoxCreation();
		
		// Add boxes to guiLayout
	    guiLayout.setTop(titleVBox);
	    guiLayout.setCenter(feedbackEntryVBox);
	    guiLayout.setBottom(buttonsHBox);
		
		// Main stage set up with appropriate scene and size
		primaryStage.setScene(scene);
		primaryStage.setHeight(400);
		primaryStage.setWidth(600);
		primaryStage.setTitle("Give Module Feedback");
		primaryStage.show();
	}
	
	/**
	 * Method for the creation of a VBox that contains the title and module name
	 * @param String - The name of the current module
	 * @return VBox  -  The box that contains the two title labels
	 */
	private VBox titleVBoxCreation(String moduleName) {
		
		Label titleLabel;
		Label moduleNameLabel;
		
		titleLabel = new Label("Give Module Feedback");
		titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 36));
		moduleNameLabel = new Label(moduleName);
		moduleNameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		
		// Creates a VBox, adds the two labels and returns it
		VBox titleVBox = new VBox();
		titleVBox.setPadding(new Insets(0, 0, 0, 0));
		titleVBox.setSpacing(10);
		titleVBox.setStyle("-fx-background-colour: #336699;");
		titleVBox.setAlignment(Pos.CENTER);
		titleVBox.getChildren().addAll(titleLabel, moduleNameLabel);
		
		return titleVBox;	
	}
	
	/**
	 * Method for the creation of a VBox that contains three sub-boxes
	 * One sub-box for each piece of data entry (name, comment and rating)
	 * 
	 * @return VBox  -  The box that contains the sub-boxes
	 */
	private VBox feedbackEntryVBoxCreation() {
		
		// Creates three HBoxes to go within VBox
		HBox nameTextHBox = nameTextHBoxCreation();
		HBox commentTextHBox = commentTextHBoxCreation();
		HBox ratingHBox = ratingHBoxCreation();
		
		// Creates a VBox, adds the three boxes and returns it
		VBox feedbackEntryVBox = new VBox();
		feedbackEntryVBox.setPadding(new Insets(0, 0, 0, 0));
		feedbackEntryVBox.setSpacing(10);
		feedbackEntryVBox.setStyle("-fx-background-colour: #336699;");
		feedbackEntryVBox.setAlignment(Pos.CENTER);
		feedbackEntryVBox.getChildren().addAll(nameTextHBox, commentTextHBox, ratingHBox);
		
		return feedbackEntryVBox;
	}
	
	/**
	 * Method for the creation of a HBox that goes within feedbackEntryVBox
	 * It contains a label and an area for the user to enter a name
	 * 
	 * @return HBox  -  The box that contains the label and TextField
	 */
	private HBox nameTextHBoxCreation() {
		
		Label nameLabel;
		nameLabel = new Label("Your Name:  ");
		nameTextField = new TextField();
		nameTextField.setPrefWidth(400);
		
		// Adds a listener to the length of text entered in nameTextField
		// Truncates entry after a set limit of characters
		nameTextField.lengthProperty().addListener(new ChangeListener<Number>(){
			@Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    // Check if the new character is greater than the limit
                    if (nameTextField.getText().length() >= nameCharLimit) {
                    	// Truncate text field contents if it is too long
                    	nameTextField.setText(nameTextField.getText().substring(0, nameCharLimit));
                    }
                }
            }
		});
		
		// Creates a HBox, adds the label and text field then returns it
		HBox nameTextHBox = new HBox();
		nameTextHBox.setPadding(new Insets(0, 0, 0, 0));
		nameTextHBox.setSpacing(28);
		nameTextHBox.setStyle("-fx-background-colour: #336699;");
		nameTextHBox.setAlignment(Pos.CENTER);
		nameTextHBox.getChildren().addAll(nameLabel, nameTextField);
		
		return nameTextHBox;	
	}
	
	/**
	 * Method for the creation of a HBox that goes within feedbackEntryVBox
	 * It contains a label and an area for the user to enter a comment
	 * 
	 * @return HBox  -  The box that contains the label and TextArea
	 */
	private HBox commentTextHBoxCreation() {
		
		Label commentLabel;
		commentLabel = new Label("Your Comment: ");

		commentTextArea = new TextArea();
		commentTextArea.setPrefWidth(400);
		commentTextArea.setPrefHeight(50);
		commentTextArea.wrapTextProperty().set(true);
		
		// Adds a listener to the length of text entered in commentTextArea
		// Truncates entry after a set limit of characters
		commentTextArea.lengthProperty().addListener(new ChangeListener<Number>(){
			@Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    // Check if the new character is greater than limit
                    if (commentTextArea.getText().length() >= commentCharLimit) {
                    	// Truncate text area contents if it is too long
                    	commentTextArea.setText(commentTextArea.getText().substring(0, commentCharLimit));
                    }
                }
            }
		});
		
		// Creates a HBox, adds the label and TextArea and returns it
		HBox commentTextHBox = new HBox();
		commentTextHBox.setPadding(new Insets(0, 0, 0, 0));
		commentTextHBox.setSpacing(10);
		commentTextHBox.setStyle("-fx-background-colour: #336699;");
		commentTextHBox.setAlignment(Pos.CENTER);
		commentTextHBox.getChildren().addAll(commentLabel, commentTextArea);
		
		return commentTextHBox;	
	}
	
	/**
	 * Method for the creation of a HBox that goes within feedbackEntryVBox
	 * It contains a label and 5 buttons to allow the user to rate the module
	 * 
	 * @return HBox  -  The box that contains the label and buttons
	 */
	private HBox ratingHBoxCreation() {
		
		Label ratingLabel = new Label("Your Rating:     ");
		
		// Load empty and full star image files
		emptyStarImage = new Image(getClass().getResourceAsStream("resources/Stars/SingleStarEmpty.png"));		
		fullStarImage = new Image(getClass().getResourceAsStream("resources/Stars/SingleStarFull.png"));
		
		// Instantiate 5 buttons with image views prepared for full
		// and empty star images for each button
		// Also initialises the hold value of each button which dictates
		// whether that button's image should toggle or not
		for (int i = 0; i <=4; i++)
		{
			emptyStarImageView[i] = new ImageView(emptyStarImage);
			emptyStarImageView[i].setFitWidth(50);
			emptyStarImageView[i].setFitHeight(50);
			
			fullStarImageView[i] = new ImageView(fullStarImage);
			fullStarImageView[i].setFitWidth(50);
			fullStarImageView[i].setFitHeight(50);
			
			starButtons[i] = new Button("");
			starButtons[i].setPrefWidth(50);
			starButtons[i].setPrefHeight(50);
			starButtons[i].setGraphic(emptyStarImageView[i]);
			starButtons[i].setPadding(Insets.EMPTY);
			
			holdStar[i] = false;
		}
		
		// Method to set up action listeners for each button
		// Each button listens for when the mouse hovers over it and when
		// it is clicked
		setupButtonActionListeners();
		
		// Creates a VBox, adds the label and all buttons and returns it
		HBox ratingHBox = new HBox();
		ratingHBox.setPadding(new Insets(0, 0, 0, 0));
		ratingHBox.setSpacing(0);
		ratingHBox.setStyle("-fx-background-colour: #336699;");
		ratingHBox.setAlignment(Pos.CENTER);
		ratingHBox.getChildren().addAll(ratingLabel, starButtons[0], starButtons[1], 
									starButtons[2], starButtons[3], starButtons[4]);
		
		return ratingHBox;
	}
	
	/**
	 * Method for the creation of a HBox that goes within feedbackEntryVBox
	 * It contains a label and 5 buttons to allow the user to rate the module
	 * 
	 * @return HBox  -  The box that contains the label and buttons
	 */	
	private HBox buttonsHBoxCreation() {
		
		// Create submit and close buttons
		Button submitButton = new Button("Submit Feedback");
		submitButton.setPrefSize(150, 30);
		
		Button closeButton = new Button ("Close");
		closeButton.setPrefSize(150,30);
		
		submitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				
				// Checks that all areas have been completed
				feedbackError = checkForErrors();
				
				// If something has not been filled out a window pops up
				// to tell the user what they haven't filled
				if (feedbackError == true)
				{
					setupErrorWindow();
				}
				else
				{
					// Otherwise the name and comment contents are retrieved
					// and a function called to format them into an appropriate
					// csv string to add to the relevant text file
					submittedName = nameTextField.getText();
					submittedComment = commentTextArea.getText();

					submittedFeedback = formatSubmittedFeedback(submittedName, submittedComment, submittedRating);

					// For now the text filed is updated from here but may need to
					// be done from the server in the future
				    try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/DemoModuleComments.txt", true))){
				        bw.newLine();
				    	bw.write(submittedFeedback);
				        bw.close();
				    } catch (IOException e1) {
				        e1.printStackTrace();
				    }
					
				    // View feedback gui is reopened after feedback is submitted
				    launchViewFeedbackGui(stage, moduleName, textFilePath, client);
				}
			}
		});
		
		// Give Feedback window is closed when close is clicked
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				launchViewFeedbackGui(stage, moduleName, textFilePath, client);
			}
		});
		
		// Creates a HBox, adds the buttons and returns it
		HBox buttonsHBox = new HBox();
		buttonsHBox.setPadding(new Insets(10, 10, 10, 10));
		buttonsHBox.setSpacing(50);
		buttonsHBox.setStyle("-fx-background-colour: #336699;");
		buttonsHBox.setAlignment(Pos.CENTER);
		buttonsHBox.getChildren().addAll(submitButton, closeButton);
		
		return buttonsHBox;
	}
	
	/**
	 * Method to re-launch view feedback window in place of give feedback window
	 * 
	 * @param Stage - The stage to display the view feedback window in
	 */
	private void launchViewFeedbackGui(Stage stage, String moduleName, 
										String textFilePath, Client client) {
		ViewFeedbackGui viewFeedbackGui = new ViewFeedbackGui(moduleName, textFilePath, client);
		try {
			viewFeedbackGui.start(stage);
		} catch (Exception e1) {
			System.out.println("Unable to re-launch View Feedback Window");
			e1.printStackTrace();
		}
	}
	
	/**
	 * Method to check whether the user has completed all fields
	 * 
	 * @return Boolean - Value dictating whether the user has completed all fields or not
	 */
	private Boolean checkForErrors() {
		
		Boolean errorDetected = false;
		Boolean nameEntered = false;
		Boolean commentEntered = false;
		
		// Check for empty name and comment text field/area
		if (nameTextField.getText().trim().isEmpty())
		{
			nameEntered = false;
			errorDetected = true;
		}
		else
		{
			nameEntered = true;
		}
		if (commentTextArea.getText().trim().isEmpty())
		{
			commentEntered = false;
			errorDetected = true;
		}
		else
		{
			commentEntered = true;
		}
		if (submittedRating == 0)
		{
			errorDetected = true;
		}
		
		
		// Sets the contents of errorLabel according to which fields have not been
		// completed
		if (nameEntered == false)
		{
			if (commentEntered == false)
			{
				// A submitted rating of 0 indicates they have not entered a rating
				if (submittedRating == 0)
				{
					errorLabel.setText("Please enter a name, comment and rating");
				}
				else
				{
					errorLabel.setText("Please enter a name and a comment");
				}
			}
			else
			{
				if (submittedRating == 0)
				{
					errorLabel.setText("Please enter a name and a rating");
				}
				else
				{
					errorLabel.setText("Please enter a name");
				}
			}
		}
		else if (nameEntered == true)
		{
			if (commentEntered == false)
			{
				if (submittedRating == 0)
				{
					errorLabel.setText("Please enter a comment and a rating");
				}
				else
				{
					errorLabel.setText("Please enter a comment");
				}
			}
			else
			{
				if (submittedRating == 0)
				{
					errorLabel.setText("Please enter a rating");
				}
				else
				{
					errorDetected = false;
				}
			}
		}
		return errorDetected;
	}
	
	/**
	 * Method for the creation of an error window which appears if the user
	 * presses submit without completing all fields
	 */	
	private void setupErrorWindow(){
		
		// Create layout for error window
		BorderPane errorWindowLayout = new BorderPane();
		Scene errorWindowScene = new Scene(errorWindowLayout);
		
		// Create okay button to allow user to accept and close error window
		Button okayButton = new Button("Okay");
		okayButton.setPrefSize(70, 30);
		
		okayButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				errorWindowStage.close();
			}
		});
		
		// Add the error label detailing which fields have not been completed
		// and then display window
		errorWindowLayout.setCenter(errorLabel);
		errorWindowLayout.setBottom(okayButton);
		
		BorderPane.setAlignment(okayButton, Pos.CENTER);
		BorderPane.setMargin(okayButton, new Insets(5,5,5,5));
		
		errorWindowStage.setScene(errorWindowScene);
		errorWindowStage.setTitle("Error");
		errorWindowStage.setHeight(150);
		errorWindowStage.setWidth(300);
		errorWindowStage.show();
	}
	
	/**
	 * Method to format submitted feedback into a single string appropriately
	 * formatted for insertion into text file
	 * 
	 * @param String - the name the user has submitted
	 * @param String - the comment the user has submitted
	 * @param int - the rating the user has submitted
	 */	
	private String formatSubmittedFeedback(String name, String comment, int rating) {
		
		String formattedFeedback;
		int quotationCounter = 0;
		
		// Format name and comment to ensure they do not contain an odd number of
		// quotation marks which would break parser
		for (int i = 0; i < name.length(); i++)
		{
			if (name.charAt(i) == '"')
			{
				quotationCounter++;
			}
		}
		// If it does contain an odd number of quotation marks then append
		// one more quotation mark to make it an even number
		if (quotationCounter%2 != 0)
		{
			name = name + "\"";
		}
		
		quotationCounter = 0;
		
		for (int i = 0; i < comment.length(); i++)
		{
			if (comment.charAt(i) == '"')
			{
				quotationCounter++;
			}
		}
		if (quotationCounter%2 != 0)
		{
			comment = comment + "\"";
		}
		
		// Concatenate feedback to form a single string ready for insertion
		// into text file by surrounding name and comment with quotation marks
		// and inserting a comma between each field
		formattedFeedback = "\"" + name + "\",\"" + comment 
				+ "\"," + rating + "," + "0,"; 
		
		return formattedFeedback;
	}
	
	/**
	 * Method to set up the action listeners and logic for the 5 star buttons
	 */
	private void setupButtonActionListeners() {
		
		//******************************************************************************
		// Each button listens for when it is hovered and when it is clicked
		// If no rating is selected, hovering over a button will fill the stars
		// up until the star being hovered, as this is intuitive.
		//
		// In general, holdStar is used to dictate whether that button's image
		// should toggle or not. If it is true then that button's image should not change
		// and vice versa.
		//
		// Clicking on any star clears all stars higher than it and fills all stars
		// below it. For example clicking the second star fills stars 1 and 2 and
		// clears stars 3, 4 and 5. The rating is then set accordingly.
		//
		// Clicking the same star again clears all stars and resets rating to 0
		//******************************************************************************
		
		starButtons[0].hoverProperty().addListener(new ChangeListener<Boolean>(){
			@Override
	        public void changed(ObservableValue<? extends Boolean> observable,
	                Boolean oldValue, Boolean newValue) {
				// If the first star is not frozen, toggle it when the mouse hovers
	            if (holdStar[0] == false)
	            {
	            	if (newValue == true)
	                {
	            		starButtons[0].setGraphic(fullStarImageView[0]);
	                }
	                else
	                {
	                	starButtons[0].setGraphic(emptyStarImageView[0]);
	                }
	            }
	        }
	
		});
		
		starButtons[0].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				for (int i = 1; i <= 4; i++)
				{
					starButtons[i].setGraphic(emptyStarImageView[i]);
					holdStar[i] = false;
				}
				
				if (submittedRating == 1)
				{
					starButtons[0].setGraphic(emptyStarImageView[0]);
					holdStar[0] = false;
					submittedRating = 0;
				}
				else
				{
					starButtons[0].setGraphic(fullStarImageView[0]);
					holdStar[0] = true;
					submittedRating = 1;
				}
			}
		});
		
		starButtons[1].hoverProperty().addListener(new ChangeListener<Boolean>(){
			@Override
	        public void changed(ObservableValue<? extends Boolean> observable,
	                Boolean oldValue, Boolean newValue) {
	            if (holdStar[1] == false)
	            {
	            	if (newValue == true)
	                {
	            		if (holdStar[0] == false)
	            		{
	            			starButtons[0].setGraphic(fullStarImageView[0]);
	            		}
	            		starButtons[1].setGraphic(fullStarImageView[1]);
	                }
	                else
	                {
	                	if (holdStar[0] == false)
	                	{
	                		starButtons[0].setGraphic(emptyStarImageView[0]);
	                	}
	                	starButtons[1].setGraphic(emptyStarImageView[1]);
	                }
	            }
	        }
	
		});
		
		starButtons[1].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				for (int i = 2; i <= 4; i++)
				{
					starButtons[i].setGraphic(emptyStarImageView[i]);
					holdStar[i] = false;
				}
				
				if (submittedRating == 2)
				{
					for (int i = 0; i <= 1; i++)
					{
						starButtons[i].setGraphic(emptyStarImageView[i]);
						holdStar[i] = false;
					}
					submittedRating = 0;
				}
				else
				{
					for (int i = 0; i <= 1; i++)
					{
						starButtons[i].setGraphic(fullStarImageView[i]);
						holdStar[i] = true;
					}
					submittedRating = 2;
				}
			}
		});
		
		starButtons[2].hoverProperty().addListener(new ChangeListener<Boolean>(){
			@Override
	        public void changed(ObservableValue<? extends Boolean> observable,
	                Boolean oldValue, Boolean newValue) {
				if (holdStar[2] == false)
	            {
	            	if (newValue == true)
	                {
	            		for (int i = 0; i <= 2; i++)
	            		{
	            			if (holdStar[i] == false)
	            			{
	            				starButtons[i].setGraphic(fullStarImageView[i]);
	            			}
	            		}
	                }
	                else
	                {
	                	for (int i = 0; i <= 2; i++)
	            		{
	            			if (holdStar[i] == false)
	            			{
	            				starButtons[i].setGraphic(emptyStarImageView[i]);
	            			}
	            		}
	                }
	            }
	        }
	
		});
		
		starButtons[2].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				for (int i = 3; i <= 4; i++)
				{
					starButtons[i].setGraphic(emptyStarImageView[i]);
					holdStar[i] = false;
				}
				
				if (submittedRating == 3)
				{
					for (int i = 0; i <= 2; i++)
					{
						starButtons[i].setGraphic(emptyStarImageView[i]);
						holdStar[i] = false;
					}
					submittedRating = 0;
				}
				else
				{
					for (int i = 0; i <= 2; i++)
					{
						starButtons[i].setGraphic(fullStarImageView[i]);
						holdStar[i] = true;
					}
					submittedRating = 3;
				}
			}
		});
		
		starButtons[3].hoverProperty().addListener(new ChangeListener<Boolean>(){
			@Override
	        public void changed(ObservableValue<? extends Boolean> observable,
	                Boolean oldValue, Boolean newValue) {
				if (holdStar[3] == false)
	            {
	            	if (newValue == true)
	                {
	            		for (int i = 0; i <= 3; i++)
	            		{
	            			if (holdStar[i] == false)
	            			{
	            				starButtons[i].setGraphic(fullStarImageView[i]);
	            			}
	            		}
	                }
	                else
	                {
	                	for (int i = 0; i <= 3; i++)
	            		{
	            			if (holdStar[i] == false)
	            			{
	            				starButtons[i].setGraphic(emptyStarImageView[i]);
	            			}
	            		}
	                }
	            }
	        }
	
		});
		
		starButtons[3].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				
				starButtons[4].setGraphic(emptyStarImageView[4]);
				holdStar[4] = false;
				
				if (submittedRating == 4)
				{
					for (int i = 0; i <= 3; i++)
					{
						starButtons[i].setGraphic(emptyStarImageView[i]);
						holdStar[i] = false;
					}
					submittedRating = 0;
				}
				else
				{
					for (int i = 0; i <= 3; i++)
					{
						starButtons[i].setGraphic(fullStarImageView[i]);
						holdStar[i] = true;
					}
					submittedRating = 4;
				}
			}
		});
		
		starButtons[4].hoverProperty().addListener(new ChangeListener<Boolean>(){
			@Override
	        public void changed(ObservableValue<? extends Boolean> observable,
	                Boolean oldValue, Boolean newValue) {
				if (holdStar[4] == false)
	            {
	            	if (newValue == true)
	                {
	            		for (int i = 0; i <= 4; i++)
	            		{
	            			if (holdStar[i] == false)
	            			{
	            				starButtons[i].setGraphic(fullStarImageView[i]);
	            			}
	            		}
	                }
	                else
	                {
	                	for (int i = 0; i <= 4; i++)
	            		{
	            			if (holdStar[i] == false)
	            			{
	            				starButtons[i].setGraphic(emptyStarImageView[i]);
	            			}
	            		}
	                }
	            }
	        }
		});
		
		starButtons[4].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (submittedRating == 5)
				{
					for (int i = 0; i <= 4; i++)
					{
						starButtons[i].setGraphic(emptyStarImageView[i]);
						holdStar[i] = false;
					}
					submittedRating = 0;
				}
				else
				{
					for (int i = 0; i <= 4; i++)
					{
						starButtons[i].setGraphic(fullStarImageView[i]);
						holdStar[i] = true;
					}
					submittedRating = 5;
				}
			}
		});
	}
}
