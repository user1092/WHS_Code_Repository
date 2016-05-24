package whs.yourchoice.client;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

import whs.yourchoice.parsers.CommentParser;

/**
* Class for creation of the module comments window for displaying
* comments stored in a .txt file
*
* @author user513, user679
* @version v0.3 10/05/16
*/
public class ViewFeedbackGui extends Application {
	
	// Path name will need changing and adapting for integration
	private String textFilePath = "src/DemoModuleComments.txt";
	// Module name should be passed to this class so it can be displayed
	private String moduleName = "Test Module Name";
	
	// Table and list of type Comment to fill the table with
	private TableView<Feedback> table = new TableView<Feedback>();
	private ObservableList<Feedback> feedback = FXCollections.observableArrayList();
	
	// Average module rating according to text file contents
	private float avgRating;
	
	// CommentParser used to parse text file and gui/stage ready to display
	// feedback gui window when required
	private CommentParser commentsParser;
	private GiveFeedbackGui feedbackGui;
	private Stage feedbackWindowStage = new Stage();
	
	
	public ViewFeedbackGui(String moduleName, String textFilePath, Client client) {
		this.moduleName = moduleName + " Feedback";
		this.textFilePath = textFilePath;
	}
	
	
	public static void main(String[] args) {
		launch(ViewFeedbackGui.class, args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane guiLayout = new BorderPane();
		Scene scene = new Scene(guiLayout);
				
		// Create parser to parse text file and return a list of type comment
		commentsParser = new CommentParser();
		commentsParser.parseTextFile(textFilePath);
		avgRating = commentsParser.getAverageRating();
		feedback = commentsParser.getComments();

		// Import and set background image
		String background = ClientGui.class.getResource("resources/SlideBackground.jpg").toExternalForm();
		guiLayout.setStyle("-fx-background-image: url('" + background + "'); " +
		           "-fx-background-position: center center; " +
		           "-fx-background-repeat: stretch;");

		// Call methods to create boxes for display
		VBox titleVBox = titleVBoxCreation();
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
	 * Method for the creation of a HBox that contains the create comment and back buttons
	 * As well as a VBox containing the average rating for the current module
	 * @param primaryStage  -  The application window
	 * @return HBox  -  The box that contains the VBox and buttons
	 */
	private HBox bottomHBoxCreation(final Stage primaryStage) {
		
		// Call method to create VBox to go within HBox
		VBox ratingVBox = ratingVBoxCreation();
		
		// Create HBox
		HBox bottomHBox = new HBox();
		bottomHBox.setPadding(new Insets(10, 10, 10, 10));
		bottomHBox.setSpacing(80);
		bottomHBox.setStyle("-fx-background-colour: #336699;");
		
		// Create comment and back buttons
		Button commentButton = new Button("Give Feedback");
		commentButton.setPrefSize(150, 30);
		
		commentButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				feedbackGui = new GiveFeedbackGui();
				try {
					feedbackGui.start(feedbackWindowStage, moduleName);
				} catch (Exception e1) {
					System.out.println("Unable to open give feedback window");
					e1.printStackTrace();
				}
			}
		});
		
		Button returnButton = new Button ("Back");
		returnButton.setPrefSize(150,30);
		
		returnButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				primaryStage.close();
			}
		});
		
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
	private VBox titleVBoxCreation() {
		
		Label titleLabel;
		Label moduleNameLabel;
		
		titleLabel = new Label("Module Comments");
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
	 * Method for the creation of a VBox that contains the average rating image and label
	 * @param primaryStage  -  The application window
	 * @return VBox  -  The box that contains the two labels
	 */
	private VBox ratingVBoxCreation(){
		
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
       TableColumn<Feedback,String> nameCol = new TableColumn<Feedback,String>("Name");
       nameCol.setMinWidth(110);
       nameCol.setMaxWidth(110);
       nameCol.setCellValueFactory(new PropertyValueFactory<Feedback, String>("name"));
       
       TableColumn<Feedback,String> commentCol = new TableColumn<Feedback,String>("Comment");
       commentCol.setMinWidth(500);
       commentCol.setMaxWidth(500);
       commentCol.setCellValueFactory(new PropertyValueFactory<Feedback, String>("comment"));
       
       TableColumn<Feedback,String> ratingCol = new TableColumn<Feedback,String>("Rating");
       ratingCol.setMinWidth(100);
       ratingCol.setMaxWidth(100);
       ratingCol.setCellValueFactory(new PropertyValueFactory<Feedback, String>("rating"));
       
       // Fills table with contents of data (which is a list of Comments)
       table.setEditable(false);
       table.setItems(feedback);
       
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
		avgRatingImg = new Image(getClass().getResourceAsStream("resources/Stars/" + avgRatingPicNo + ".png"));
		avgRatingImgView = new ImageView(avgRatingImg);
		avgRatingImgView.setFitWidth(150);
		avgRatingImgView.setFitHeight(30);
		
		return avgRatingImgView;
		
	}
	
	// Feedback class is used to store name, comment and rating of each individual comment
	public static class Feedback {
		 
        private final SimpleStringProperty name;
        private final SimpleStringProperty comment;
        private final SimpleStringProperty rating;
 
        public Feedback(String uName, String uComment, String uRating) {
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
