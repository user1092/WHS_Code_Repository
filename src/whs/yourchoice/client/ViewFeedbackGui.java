/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.client;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
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
import javafx.util.Callback;

import whs.yourchoice.parsers.CommentParser;

/**
* Class for creation of the module comments window for displaying
* comments stored in a .txt file
*
* @author jcl513, gw679, ch1092
* @version v0.6 29/05/16
*/
public class ViewFeedbackGui extends Application {
	
	// Path name will need changing and adapting for integration
	private String textFileName = null;
	private String moduleFileSaveLocation = null;
	// Module name should be passed to this class so it can be displayed
	private String moduleName = "Test Module Name";
	private Client client;
	
	// Table and list of type Comment to fill the table with
	private TableView<Feedback> table = new TableView<Feedback>();
	private ObservableList<Feedback> feedback = FXCollections.observableArrayList();
	private ObservableList<Feedback> approvedFeedback = FXCollections.observableArrayList();
	
	// Average module rating according to text file contents
	private float avgRating;
	
	// CommentParser used to parse text file and gui/stage ready to display
	// feedback gui window when required
	private CommentParser commentsParser;
	private GiveFeedbackGui feedbackGui;
	
	private String tempDirectory = "temp";
	private boolean adminMode = false;
	TableColumn<Feedback,String> approvedCol = null;
	TableColumn<Feedback,String> approveCol = null;
	TableColumn<Feedback,String> deleteCol = null;
	
	public ViewFeedbackGui(String moduleName, String textFileName, Client client) {
		this.moduleName = moduleName;
		this.textFileName = textFileName;
		this.client = client;
		moduleFileSaveLocation = tempDirectory + "/" + this.textFileName;
		System.out.println("opened view feedback");
		try {
			requestModuleFeedback();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adminMode = client.isAdminMode();
	}
	
	
	public static void main(String[] args) {
		launch(ViewFeedbackGui.class, args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		BorderPane guiLayout = new BorderPane();
		Scene scene = new Scene(guiLayout);
		
		parseComments();
		
		// Import and set background image
		String background = ClientGui.class.getResource("resources/SlideBackground.jpg").toExternalForm();
		guiLayout.setStyle("-fx-background-image: url('" + background + "'); " +
		           "-fx-background-position: center center; " +
		           "-fx-background-repeat: stretch;");
		
		// Call methods to create boxes for display
		VBox titleVBox = titleVBoxCreation();
		VBox tableVBox = tableVBoxCreation();
		HBox bottomHBox = bottomHBoxCreation(primaryStage);
		
		// Add boxes to guiLayout
	    guiLayout.setTop(titleVBox);
	    guiLayout.setCenter(tableVBox);
		guiLayout.setBottom(bottomHBox);
		guiLayout.setPadding(new Insets(20,20,20,20));
		
		// Main stage set up with appropriate scene and size
		primaryStage.setScene(scene);
		if (adminMode) {
			primaryStage.setHeight(500);
			primaryStage.setWidth(1000);
		}
		else {
			primaryStage.setHeight(500);
			primaryStage.setWidth(810);
		}
		
		primaryStage.setResizable(false);
		
		primaryStage.setTitle("Module Feedback");
		primaryStage.show();
	}


	/**
	 * Method to request the file from the server
	 * 
	 * @throws IOException
	 */
	private void requestModuleFeedback() throws IOException {
		System.out.println("textFileName sent: " + textFileName);
		client.sendData(textFileName);
		client.receiveRequestedFile(moduleFileSaveLocation);
		System.out.println("textFileName received: " + textFileName);
	}


	/**
	 * Method to parse the comments held in moduleFileSaveLocation
	 * 
	 * @throws FileNotFoundException
	 */
	private void parseComments() throws FileNotFoundException {
		System.out.println("parsing comments: " + moduleFileSaveLocation);
		// Create parser to parse text file and return a list of type comment
		commentsParser = new CommentParser();
		commentsParser.parseTextFile(moduleFileSaveLocation);
		avgRating = commentsParser.getAverageRating();
		feedback = commentsParser.getComments();
		if (!adminMode) {
			approvedFeedback = sortFeedback(feedback);
		}
		else {
			approvedFeedback = feedback;
		}
		System.out.println("parsing done: " + moduleFileSaveLocation);
	}
	
	
	/**
	 * Method to sort the feedback read so only approved comments are displayed
	 * 
	 * @param feedbackToSort
	 * @return
	 */
	private ObservableList<Feedback> sortFeedback(ObservableList<Feedback> feedbackToSort) {
		ObservableList<Feedback> sortedFeedback = FXCollections.observableArrayList();
		for (int i = 0; i < feedbackToSort.size(); i++) {
			if (feedbackToSort.get(i).getApproved().equals("true")) {
				sortedFeedback.add(feedbackToSort.get(i));
			}
		}

		return sortedFeedback;
	}


	/**
	 * Method to save the list of feedback back into the same text file to save
	 * any changes to scores etc.
	 */
	private void saveFeedback(){
		String tempString = "";
		String tempComment = "";
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(moduleFileSaveLocation, false))){
	        
			// Re-formats each feedback item for writing to the text file
			// This ensures the parser will parse it correctly upon re-opening
			for (int i = 0; i < feedback.size(); i++) {
				tempComment = feedback.get(i).getComment();
				tempComment = tempComment.replace("\n", "");
				
				tempString = "\"" 	+ feedback.get(i).getName() + "\",\"" 
									+ tempComment + "\","
									+ feedback.get(i).getRating() + ","
									+ feedback.get(i).getScore() + ","
									+ feedback.get(i).getApproved() + ",";
				
				bw.write(tempString);
				
				// Moves to a new line if there are still feedback items remaining
				if (i != feedback.size() - 1) {
					bw.newLine();
				}
			}
			
	        bw.close();
	    } catch (IOException e1) {
	    	System.out.println("Unable to open text file to write to");
	        e1.printStackTrace();
	    }
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
		bottomHBox.setSpacing(30);
		bottomHBox.setStyle("-fx-background-colour: #336699;");
		
		// Create comment, refresh and back buttons
		Button commentButton = new Button("Give Feedback");
		commentButton.setPrefSize(150, 30);
		
		commentButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				feedbackGui = new GiveFeedbackGui();
//				Stage giveFeedbackStage = new Stage();
				try {
					feedbackGui.start(new Stage(), moduleName, moduleFileSaveLocation, client);
					Task<Void> requestTask = new Task<Void>() {
						@Override protected Void call() throws Exception {
							boolean open = true;
							while (open) {
								Thread.sleep(50);
								open = feedbackGui.isOpen();
							}
							Platform.runLater(new Runnable() {
								@Override public void run() {
									try {
										refreshComments(primaryStage);
									} catch (FileNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
							return null;
						}
					};
					// Only works if X is clicked Stage.close() doesn't work
//					giveFeedbackStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//			        	public void handle(WindowEvent we) {
//			        		try {
//								refreshComments(primaryStage);
//							} catch (FileNotFoundException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							} catch (Exception e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//			        	}
//			        });
					Thread requestThread = new Thread(requestTask);
					requestThread.setDaemon(true);
					requestThread.start();
					
				} catch (Exception e1) {
					System.out.println("Unable to open give feedback window");
					e1.printStackTrace();
				}
			}
		});
		
		Button returnButton = new Button ("Save and Close");
		returnButton.setPrefSize(150,30);
		
		returnButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				saveFeedback();
				try {
					client.sendData(textFileName + ".update");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					client.sendRequestedFile(moduleFileSaveLocation);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				primaryStage.close();
			}
		});
		
		// Refresh button re-launches class
		Button refreshButton = new Button("Refresh Feedback");
		refreshButton.setPrefSize(150, 30);
		
		refreshButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					refreshComments(primaryStage);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			
		});
		
		// Add everything to HBox and return it
		bottomHBox.setAlignment(Pos.CENTER);
		bottomHBox.getChildren().addAll(ratingVBox, commentButton, refreshButton, returnButton);
		return bottomHBox;	
	}
	
	
	/**
	 * @param primaryStage
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	private void refreshComments(final Stage primaryStage)
			throws FileNotFoundException, Exception {
		parseComments();
		start(primaryStage);
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
		moduleNameLabel = new Label(moduleName + " Feedback");
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
		
		// Load up and down vote image files
		Image upVoteImage = new Image(getClass().getResourceAsStream("resources/upVote.png"));		
		Image downVoteImage = new Image(getClass().getResourceAsStream("resources/downVote.png"));
		
		// Declare and initialise arrays of image views for each row as well as
		// an integer value storing the votes that the user has placed on that row
		// rowVoteValue is used to prevent the user from voting a single feedback item
		// beyond +/- 1
		final int rowVoteValue[] = new int[approvedFeedback.size()];
		final ImageView upVoteImageView[] = new ImageView[approvedFeedback.size()];
		final ImageView downVoteImageView[] = new ImageView[approvedFeedback.size()];
		
		for (int i = 0; i < approvedFeedback.size(); i++)
		{
			rowVoteValue[i] = 0;
			
			upVoteImageView[i] = new ImageView(upVoteImage);
			upVoteImageView[i].setFitWidth(30);
			upVoteImageView[i].setFitHeight(30);
			
			downVoteImageView[i] = new ImageView(downVoteImage);
			downVoteImageView[i].setFitWidth(30);
			downVoteImageView[i].setFitHeight(30);
		}
		
		
		
		// Set up three columns to contain the information in the text file 
       TableColumn<Feedback,String> nameCol = new TableColumn<Feedback,String>("Name");
       nameCol.setMinWidth(110);
       nameCol.setMaxWidth(110);
       nameCol.setCellValueFactory(new PropertyValueFactory<Feedback, String>("name"));
       
       TableColumn<Feedback,String> commentCol = new TableColumn<Feedback,String>("Comment");
       commentCol.setMinWidth(400);
       commentCol.setMaxWidth(400);
       commentCol.setCellValueFactory(new PropertyValueFactory<Feedback, String>("comment"));
       
       TableColumn<Feedback,String> ratingCol = new TableColumn<Feedback,String>("Rating");
       ratingCol.setMinWidth(60);
       ratingCol.setMaxWidth(60);
       ratingCol.setCellValueFactory(new PropertyValueFactory<Feedback, String>("rating"));
       
       // Set up three more columns for each feedback item's score and then
       // up and down vote buttons
       TableColumn<Feedback,String> scoreCol = new TableColumn<Feedback,String>("Score");
       scoreCol.setMinWidth(60);
       scoreCol.setMaxWidth(60);
       scoreCol.setCellValueFactory(new PropertyValueFactory<Feedback, String>("score"));
       
       TableColumn<Feedback,String> upvoteCol = new TableColumn<Feedback,String>("Vote");
       upvoteCol.setMinWidth(35);
       upvoteCol.setMaxWidth(35);
       upvoteCol.setCellValueFactory(new PropertyValueFactory<Feedback,String>("DUMMY"));
       
       TableColumn<Feedback,String> downvoteCol = new TableColumn<Feedback,String>("Vote");
       downvoteCol.setMinWidth(35);
       downvoteCol.setMaxWidth(35);
       downvoteCol.setCellValueFactory(new PropertyValueFactory<Feedback,String>("DUMMY"));
       
       if (adminMode) {
    	   approvedCol = new TableColumn<Feedback,String>("Approved");
    	   approvedCol.setMinWidth(50);
    	   approvedCol.setMaxWidth(50);
    	   approvedCol.setCellValueFactory(new PropertyValueFactory<Feedback,String>("approved"));
    	   
    	   approveCol = new TableColumn<Feedback,String>("Approved");
    	   approveCol.setMinWidth(70);
    	   approveCol.setMaxWidth(70);
    	   approveCol.setCellValueFactory(new PropertyValueFactory<Feedback,String>("DUMMY"));
    	   
    	   deleteCol = new TableColumn<Feedback,String>("Delete");
    	   deleteCol.setMinWidth(70);
    	   deleteCol.setMaxWidth(70);
    	   deleteCol.setCellValueFactory(new PropertyValueFactory<Feedback,String>("DUMMY"));
       }
       
       // Create cell factories to set up and down vote columns
       Callback<TableColumn<Feedback, String>, TableCell<Feedback, String>> cellFactory1 = upVoteColumnSetup(
			rowVoteValue, upVoteImageView);
       
       Callback<TableColumn<Feedback, String>, TableCell<Feedback, String>> cellFactory2 = downVoteColumnSetup(
			rowVoteValue, downVoteImageView);
       
       if (adminMode) {
    	   Callback<TableColumn<Feedback, String>, TableCell<Feedback, String>> cellFactory3 = approveColumnSetup();
    	   approveCol.setCellFactory(cellFactory3);
    	   Callback<TableColumn<Feedback, String>, TableCell<Feedback, String>> cellFactory4 = deleteColumnSetup();
    	   deleteCol.setCellFactory(cellFactory4);
       }
       
       upvoteCol.setCellFactory(cellFactory1);
       downvoteCol.setCellFactory(cellFactory2);
       
       // Fills table with contents of "feedback" list
       table.setEditable(false);
       table.setItems(approvedFeedback);
       
       table.getColumns().clear();
       table.getColumns().add(0, nameCol);
       table.getColumns().add(1, commentCol);
       table.getColumns().add(2, ratingCol);
       table.getColumns().add(3, scoreCol);
       table.getColumns().add(4, upvoteCol);
       table.getColumns().add(5, downvoteCol);
       if (adminMode) {
    	   table.getColumns().add(6, approvedCol);
    	   table.getColumns().add(7, approveCol);
    	   table.getColumns().add(8, deleteCol);
       }
       

       // Creates VBox and adds table to it
       VBox tableVBox = new VBox();
       tableVBox.setSpacing(5);
       tableVBox.setPadding(new Insets(10, 0, 0, 10));
       tableVBox.getChildren().addAll(table);
       
       return tableVBox;
	}


	/**
	 * @return
	 */
	private Callback<TableColumn<Feedback, String>, TableCell<Feedback, String>> approveColumnSetup() {
		Callback<TableColumn<Feedback, String>, TableCell<Feedback, String>> cellFactory3 
		   = new Callback<TableColumn<Feedback, String>, TableCell<Feedback, String>>()
		   {
		       @Override
		       public TableCell<Feedback,String> call( final TableColumn<Feedback, String> param )
		       {
		           final TableCell<Feedback, String> cell = new TableCell<Feedback, String>()
		           {
		               @Override
		               public void updateItem( String item, boolean empty )
		               {
		            	   final Button btn = new Button();
		            	   if (getIndex() >= 0 && getIndex() < approvedFeedback.size())
		            	   {
		            		   boolean approved = Boolean.parseBoolean(approvedFeedback.get(getIndex()).getApproved());
		            		   if (approved) {
		            			   btn.setText("Disapprove");
		            		   }
		            		   else {
		            			   btn.setText("Approve");
		            		   }
		            		   
		            		   btn.setPrefWidth(65);
		            		   btn.setPrefHeight(30);
		            		   btn.setPadding(Insets.EMPTY);
		            	   }
		            	   
		                   super.updateItem( item, empty );
		                   if ( empty )
		                   {
		                       setGraphic( null );
		                       setText( null );
		                   }
		                   else
		                   {
		                       btn.setOnAction(new EventHandler<ActionEvent>(){
								@Override
								public void handle(ActionEvent arg0) {
									boolean approved = Boolean.parseBoolean(approvedFeedback.get(getIndex()).getApproved());

									approved = !approved;
									
									if (approved) {
										btn.setText("Disapprove");
									}
									else{
										btn.setText("Approve");
									}
									
									approvedFeedback.get(getIndex()).setApproved(Boolean.toString(approved));
								}
		                    	   
		                       } );
		                       setGraphic(btn);
		                       setText(null);
		                   }
		               }
		           };
		           return cell;
		       }
		   };
		return cellFactory3;
	}
	
	
	/**
	 * @return
	 */
	private Callback<TableColumn<Feedback, String>, TableCell<Feedback, String>> deleteColumnSetup() {
		Callback<TableColumn<Feedback, String>, TableCell<Feedback, String>> cellFactory4 
		   = new Callback<TableColumn<Feedback, String>, TableCell<Feedback, String>>()
		   {
		       @Override
		       public TableCell<Feedback,String> call( final TableColumn<Feedback, String> param )
		       {
		           final TableCell<Feedback, String> cell = new TableCell<Feedback, String>()
		           {
		               @Override
		               public void updateItem( String item, boolean empty )
		               {
		            	   final Button btn = new Button();
		            	   if (getIndex() >= 0 && getIndex() < approvedFeedback.size())
		            	   {
		            		   btn.setText("Delete");
		            		   btn.setPrefWidth(60);
		            		   btn.setPrefHeight(30);
		            		   btn.setPadding(Insets.EMPTY);
		            	   }
		            	   
		                   super.updateItem( item, empty );
		                   if ( empty )
		                   {
		                       setGraphic( null );
		                       setText( null );
		                   }
		                   else
		                   {
		                       btn.setOnAction(new EventHandler<ActionEvent>(){
								@Override
								public void handle(ActionEvent arg0) {
									approvedFeedback.remove(getIndex());
								}
		                    	   
		                       } );
		                       setGraphic(btn);
		                       setText(null);
		                   }
		               }
		           };
		           return cell;
		       }
		   };
		return cellFactory4;
	}


	/**
	 * @param rowVoteValue
	 * @param downVoteImageView
	 * @return
	 */
	private Callback<TableColumn<Feedback, String>, TableCell<Feedback, String>> downVoteColumnSetup(
			final int[] rowVoteValue, final ImageView[] downVoteImageView) {
		Callback<TableColumn<Feedback, String>, TableCell<Feedback, String>> cellFactory2 
		   = new Callback<TableColumn<Feedback, String>, TableCell<Feedback, String>>()
		   {
		       @Override
		       public TableCell<Feedback,String> call( final TableColumn<Feedback, String> param )
		       {
		           final TableCell<Feedback, String> cell = new TableCell<Feedback, String>()
		           {

		               final Button btn = new Button("");

		               @Override
		               public void updateItem( String item, boolean empty )
		               {
		            	   if (getIndex() >= 0 && getIndex() < approvedFeedback.size())
		            	   {
		            		   btn.setPrefWidth(30);
		            		   btn.setPrefHeight(30);
		            		   btn.setGraphic(downVoteImageView[getIndex()]);
		            		   btn.setPadding(Insets.EMPTY);
		            	   }
		            	   
		                   super.updateItem( item, empty );
		                   if ( empty )
		                   {
		                       setGraphic( null );
		                       setText( null );
		                   }
		                   else
		                   {
		                       btn.setOnAction(new EventHandler<ActionEvent>(){

								@Override
								public void handle(ActionEvent arg0) {
									// Decrements score for corresponding feedback item if the
									// current row vote value is 1 or 0
									if (rowVoteValue[getIndex()] != -1){
										int score = Integer.parseInt(approvedFeedback.get(getIndex()).getScore());
										score -= 1;
										rowVoteValue[getIndex()] -= 1;
										approvedFeedback.get(getIndex()).setScore(Integer.toString(score));
										table.getItems().set(getIndex(), approvedFeedback.get(getIndex()));
										saveFeedback();
									}
								}
		                    	   
		                       } );
		                       setGraphic(btn);
		                       setText(null);
		                   }
		               }
		           };
		           return cell;
		       }
		   };
		return cellFactory2;
	}


	/**
	 * @param rowVoteValue
	 * @param upVoteImageView
	 * @return
	 */
	private Callback<TableColumn<Feedback, String>, TableCell<Feedback, String>> upVoteColumnSetup(
			final int[] rowVoteValue, final ImageView[] upVoteImageView) {
		Callback<TableColumn<Feedback, String>, TableCell<Feedback, String>> cellFactory1 =
		   new Callback<TableColumn<Feedback, String>, TableCell<Feedback, String>>()
		   {
			   // Overrides column cell construction to place a button in each cell
		       @Override
		       public TableCell<Feedback,String> call( final TableColumn<Feedback, String> param )
		       {
		           final TableCell<Feedback, String> cell = new TableCell<Feedback, String>()
		           {

		               final Button btn = new Button("");
		               
		               @Override
		               public void updateItem( String item, boolean empty )
		               {
		            	   // Only sets the button parameters if the button number is within
		            	   // the bound of feedback.size otherwise the button is not required
		            	   if (getIndex() >= 0 && getIndex() < approvedFeedback.size())
		            	   {
		            		   btn.setPrefWidth(30);
		            		   btn.setPrefHeight(30);
		            		   btn.setGraphic(upVoteImageView[getIndex()]);
		            		   btn.setPadding(Insets.EMPTY);
		            	   }
		            	   
		            	   
		                   super.updateItem( item, empty );
		                   if ( empty )
		                   {
		                       setGraphic( null );
		                       setText( null );
		                   }
		                   else
		                   {
		                       btn.setOnAction(new EventHandler<ActionEvent>(){

								@Override
								public void handle(ActionEvent arg0) {
									
									// Increments score for corresponding feedback item if the
									// current row vote value is 0 or -1
									if (rowVoteValue[getIndex()] != 1){
										int score = Integer.parseInt(approvedFeedback.get(getIndex()).getScore());
										score += 1;
										rowVoteValue[getIndex()] += 1;
										approvedFeedback.get(getIndex()).setScore(Integer.toString(score));
										table.getItems().set(getIndex(), approvedFeedback.get(getIndex()));
										saveFeedback();
									}
								}
		                    	   
		                       } );
		                       setGraphic(btn);
		                       setText(null);
		                   }
		               }
		               
		           };
		           return cell;
		       }
		   };
		return cellFactory1;
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
	
	// Feedback class is used to store name, comment, rating and score of each individual feedback
	public static class Feedback {
		 
        private final SimpleStringProperty name;
        private final SimpleStringProperty comment;
        private final SimpleStringProperty rating;
        private final SimpleStringProperty score;
        private final SimpleStringProperty approved;
 
        public Feedback(String uName, String uComment, String uRating, String uScore, String uApproved) {
            this.name = new SimpleStringProperty(uName);
            this.comment = new SimpleStringProperty(uComment);
            this.rating = new SimpleStringProperty(uRating);
            this.score = new SimpleStringProperty(uScore);
            this.approved = new SimpleStringProperty(uApproved);
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
        
        public String getScore() {
            return score.get();
        }
 
        public void setScore(String uScore) {
        	score.set(uScore);
        }

		public String getApproved() {
			return approved.get();
		}
		
		public void setApproved(String uApproved) {
        	approved.set(uApproved);
        }
    }
	
}
