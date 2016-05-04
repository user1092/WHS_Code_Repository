package whs.yourchoice.client;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
* Class for creation of the module comments window for displaying
* comments stored in a .txt file
*
* @author user513, user679
* @version v0.1 21/04/16
*/
public class ViewFeedbackGui extends Application {
	
	// Path names will need changing and adapting for integration
	private String starsFilePath = "file:D:\\SWEng\\JavaProject2\\YourChoice\\src\\whs\\yourchoice\\client\\resources\\Stars\\";
	private String textFilePath = "D:\\SWEng\\JavaProject2\\YourChoice\\CommentsTest.txt";
	
	// Table and list of type Comment to fill the table with
	private TableView<Comment> table = new TableView<Comment>();
	private final ObservableList<Comment> data = FXCollections.observableArrayList();
	
	// Average module rating according to text file contents
	private float avgRating;
	
	// Scanner and list used to parse text file and store each element as a string
	private Scanner scanner;
	private List<String> commentsList = new ArrayList<String>();

	
	public static void main(String[] args) {
		launch(ViewFeedbackGui.class, args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane guiLayout = new BorderPane();
		Scene scene = new Scene(guiLayout);
				
		// Read text file containing comments in csv format
		parseTextFile();

		// Import and set background image
		String background = ClientGui.class.getResource("resources/SlideBackground.jpg").toExternalForm();
		guiLayout.setStyle("-fx-background-image: url('" + background + "'); " +
		           "-fx-background-position: center center; " +
		           "-fx-background-repeat: stretch;");

		// Call methods to create boxes for display
		VBox titleVBox = titleVBoxCreation(primaryStage);
		HBox bottomHBox = bottomHBoxCreation(primaryStage);
		VBox tableVBox = tableVBoxCreation();
		
		// Add boxes to guiLayout
	    guiLayout.setTop(titleVBox);
	    guiLayout.setCenter(tableVBox);
		guiLayout.setBottom(bottomHBox);
		guiLayout.setPadding(new Insets(20,20,20,20));
		
		// Main stage set up with appropriate scene and size
		primaryStage.setScene(scene);
		primaryStage.setHeight(500);
		primaryStage.setWidth(800);
		primaryStage.setTitle("Module Feedback");
		primaryStage.show();
	}
	
	/**
	 * Function that reads a text file and parses it to extract names, comments and ratings
	 * @throws FileNotFoundException
	 */
	public void parseTextFile() throws FileNotFoundException {
		
		int newLinesNeeded = 0;
		int limit = 85;
		int location = 0;
		String tempString = "";
		
		// These variables need to be floats to allow floating point division
		// when calculating average
		float ratingTotal = 0;
		float ratingCounter = 0;
		
		
		try {
			scanner = new Scanner(new File(textFilePath));
		}
		catch (FileNotFoundException fe) {
			throw new FileNotFoundException(textFilePath + " not found");
		}
		scanner.useDelimiter(",");
		
		// Parse the text file and append each string to a list
		while (scanner.hasNext()) {
			commentsList.add(scanner.next());
		}
		scanner.close();
		
		for (int i=0; i<commentsList.size(); i++)
		{
			// Remove any line returns from each list item to ensure line returns
			// in the text file do not affect text put into table
			tempString = commentsList.get(i);
			tempString = tempString.replace(System.getProperty("line.separator"),"");
			commentsList.set(i,tempString);
			
			// If i is 1 more than a multiple of 3, the current item will be a comment
			if (i%3 == 1)
			{
				// If the comment is longer than the character limit new lines 
				// need to be inserted
				// This is essentially manual text wrapping
				if (tempString.length() > limit)
				{
					// Determine how many new lines will need to be inserted
					newLinesNeeded = tempString.length()/limit;

					// Cycle through and add as many new lines as required
					for (int j = 0; j < newLinesNeeded;j++)
					{
						// Location is initially set to the point at which the character limit
						// is reached
						location = limit*(j+1);

						// Location is the decremented until a space is found, 
						// to avoid inserting a new line in the middle of a word
						while (tempString.charAt(location)  != ' ')
						{
							location--;
						}
						// A new line is then inserted after the space found
						tempString = tempString.substring(0, location + 1) + "\n" 
									+ tempString.substring(location + 1, tempString.length());	
					}
					
					// The reformatted string then replaces the original in the list
					commentsList.set(i,tempString);
				}
			}
			
			// If i is 2 more than a multiple of 3, the current item is a rating and 
			// so a new instance of the comment class can be created
			else if (i%3 == 2)
			{
				data.add(new Comment(commentsList.get(i-2), commentsList.get(i-1), 
													commentsList.get(i)));
				
				// Parses the current rating as an integer for averaging purposes
				ratingTotal += Integer.parseInt(tempString);
				ratingCounter++;
			}
		}
		
		// Calculates average rating
		avgRating = ratingTotal/ratingCounter;
			
	}
	
	/**
	 * Method for the creation of a HBox that contains the create comment and back buttons
	 * As well as a VBox containing the average rating for the current module
	 * @param primaryStage  -  The application window
	 * @return HBox  -  The box that contains the VBox and buttons
	 */
	private HBox bottomHBoxCreation(Stage primaryStage) {
		
		// Call method to create VBox to go within HBox
		VBox ratingVBox = ratingVBoxCreation(primaryStage);
		
		// Create HBox
		HBox bottomHBox = new HBox();
		bottomHBox.setPadding(new Insets(10, 10, 10, 10));
		bottomHBox.setSpacing(80);
		bottomHBox.setStyle("-fx-background-colour: #336699;");
		
		// Create comment and back buttons
		Button commentButton = new Button("Create Comment");
		commentButton.setPrefSize(150, 30);
		Button returnButton = new Button ("Back");
		returnButton.setPrefSize(150,30);
		
		// Add everything to HBox and return it
		bottomHBox.setAlignment(Pos.CENTER);
		bottomHBox.getChildren().addAll(ratingVBox, commentButton, returnButton);
		return bottomHBox;	
	}
	
	/**
	 * Method for the creation of a VBox that contains the title and module name
	 * @param primaryStage  -  The application window
	 * @return VBox  -  The box that contains the two labels
	 */
	private VBox titleVBoxCreation(Stage primaryStage) {
		
		Label titleLabel;
		Label moduleNameLabel;
		
		titleLabel = new Label("Module Comments");
		titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 36));
		moduleNameLabel = new Label("Module Name Here");
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
	 * Method for the creation of a VBox that contains the average rating image and label
	 * @param primaryStage  -  The application window
	 * @return VBox  -  The box that contains the two labels
	 */
	private VBox ratingVBoxCreation(Stage primaryStage){
		
		Label avgRatingLabel = new Label("Average Rating: ");
		avgRatingLabel.setFont(Font.font("Verdana", 12));
		
		// Calls a method to select the appropriate image file based on average rating
		ImageView ratingImage = selectStarsImage();
		
		// Creates VBox, adds label and image and returns it
		VBox ratingVBox = new VBox();
		ratingVBox.setPadding(new Insets(0, 0, 0, 0));
		ratingVBox.setSpacing(10);
		ratingVBox.setStyle("-fx-background-colour: #336699;");
		ratingVBox.setAlignment(Pos.CENTER);
		ratingVBox.getChildren().addAll(avgRatingLabel, ratingImage);
		
		return ratingVBox;
	}
	
	/**
	 * Method for the creation of a VBox that contains the table of information
	 * @param primaryStage  -  The application window
	 * @return VBox  -  The box that contains the two labels
	 */
	private VBox tableVBoxCreation(){
		
		// Sets up three columns to contain the information in the text file 
       TableColumn<Comment,String> nameCol = new TableColumn<Comment,String>("Name");
       nameCol.setMinWidth(110);
       nameCol.setMaxWidth(110);
       nameCol.setCellValueFactory(new PropertyValueFactory<Comment, String>("name"));
       
       TableColumn<Comment,String> commentCol = new TableColumn<Comment,String>("Comment");
       commentCol.setMinWidth(500);
       commentCol.setMaxWidth(500);
       commentCol.setCellValueFactory(new PropertyValueFactory<Comment, String>("comment"));
       
       TableColumn<Comment,String> ratingCol = new TableColumn<Comment,String>("Rating");
       ratingCol.setMinWidth(100);
       ratingCol.setMaxWidth(100);
       ratingCol.setCellValueFactory(new PropertyValueFactory<Comment, String>("rating"));
       
       // Fills table with contents of data (which is a list of Comments)
       table.setEditable(false);
       table.setItems(data);
       
       table.getColumns().add(0, nameCol);
       table.getColumns().add(1, commentCol);
       table.getColumns().add(2, ratingCol);

       // Creates VBox and adds table to it
       VBox tableVBox = new VBox();
       tableVBox.setSpacing(5);
       tableVBox.setPadding(new Insets(10, 0, 0, 10));
       tableVBox.getChildren().addAll(table);
       
       return tableVBox;
	}
	
	/**
	 * Method for determining and loading appropriate stars image based on 
	 * average rating
	 * @return ImageView  -  Image view of the appropriate stars image file
	 */
	private ImageView selectStarsImage() {
		
		int avgRatingPicNo = 0;
		Image avgRatingImg;
		ImageView avgRatingImgView;
		
		// Selects appropriate image file based on average rating
		// (0.png is 0 stars, 1.png is 0.5 stars, 2.png is 1 star etc.)
		for (int i = 0; i <= 5; i++)
		{
			if (avgRating >= i - 0.25 && avgRating < i + 0.25)
			{
				avgRatingPicNo = 0 + (i*2);
			}
			else if (avgRating >= i + 0.25 && avgRating < i + 0.75)
			{
				avgRatingPicNo = 1 + (i*2);
			}		
		}
		
		// Opens image file and sets size of image view
		avgRatingImg = new Image(starsFilePath + avgRatingPicNo + ".png");
		avgRatingImgView = new ImageView();
		avgRatingImgView.setImage(avgRatingImg);
		avgRatingImgView.setFitWidth(150);
		avgRatingImgView.setFitHeight(30);
		
		return avgRatingImgView;
		
	}
	
	// Comment class is used to store name, comment and rating of each individual comment
	public static class Comment {
		 
        private final SimpleStringProperty name;
        private final SimpleStringProperty comment;
        private final SimpleStringProperty rating;
 
        private Comment(String uName, String uComment, String uRating) {
            this.name = new SimpleStringProperty(uName);
            this.comment = new SimpleStringProperty(uComment);
            this.rating = new SimpleStringProperty(uRating);
        }
 
        public String getName() {
            return name.get();
        }
 
        public void setName(String uName) {
        	name.set(uName);
        }
 
        public String getComment() {
            return comment.get();
        }
 
        public void setComment(String uName) {
        	comment.set(uName);
        }
 
        public String getRating() {
            return rating.get();
        }
 
        public void setRating(String uName) {
        	rating.set(uName);
        }
    }
}
