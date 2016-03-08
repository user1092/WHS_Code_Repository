package com.whs.client;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class PresentationGui extends Application {
		
	// temporary number for the pagination system
	//private Integer currentSlideNumber;
	private Integer totalSlideNumber = 7;
	private VBox slide;
	private AnchorPane anchorPane;
	private final double CONTROL_BAR_HEIGHT = 60;
	
	public static void main(String[] args) {
		launch(PresentationGui.class, args);
	}
	
	@Override
	public void start(Stage slideStage) throws Exception {		
		BorderPane slideLayout = new BorderPane();
		Scene scene = new Scene(slideLayout);
		
		
		
		//import and set background image
		String background = getClass().getResource("resources/SlideBackground.jpg").toExternalForm();
		slideLayout.setStyle("-fx-background-image: url('" + background + "'); " +
		           "-fx-background-position: center center; " +
		           "-fx-background-repeat: stretch;");
		
		
		/* Test code for pagination not currently used
		VBox slide = tempPaginationTester(tempSlideNumber);
		Canvas[] newCanvasArray = new Canvas[tempSlideNumber];
		newCanvasArray[tempSlideNumber] = createNewCanvas(tempSlideNumber);
		AnchorPane.setTopAnchor(slidePagination, 10.0);
        AnchorPane.setRightAnchor(slidePagination, 10.0);
        AnchorPane.setBottomAnchor(slidePagination, 10.0);
        AnchorPane.setLeftAnchor(slidePagination, 10.0);
		anchorPane.getChildren().addAll(slidePagination);
		*/
		
		// call method for creating the control bar
		ToolBar controlBar = createControlBar(slideStage);
		controlBar.setMaxHeight(CONTROL_BAR_HEIGHT);
		controlBar.setMinHeight(CONTROL_BAR_HEIGHT);
		controlBar.setPrefHeight(CONTROL_BAR_HEIGHT);
		// place the control bar at the bottom of the window
		slideLayout.setCenter(anchorPane);
		slideLayout.setBottom(controlBar);
		
		
		//main stage set up with appropriate scene and size
		slideStage.setScene(scene);
		slideStage.setWidth(800);
		slideStage.setHeight(600);
		slideStage.setFullScreen(true);
		slideStage.setTitle("Presentation Slide");
		slideStage.show();
		
	}
	
	/**
	 * Method for creating the control tool bar at the bottom of each slide
	 * @param slideStage  -  window contains the control bar on
	 * @return ToolBar  -  returns the tool bar which was created
	 */
	private ToolBar createControlBar(Stage slideStage) {
		// instantiation of the control bar
		ToolBar controlBar = new ToolBar();
		// instantiation of new slider calling the createVolumeSlider() method
		Slider volumeSlider = createVolumeSlider();
		
		// instantiation of transition toggle button
		ToggleButton transitionButton = createTransitionButton();
		// instantiation of mute toggle button
		ToggleButton muteButton = createMuteButton();
		// instantiation of play button
		ToggleButton playButton = createPlayButton();		
		// instantiation of full screen button
		ToggleButton fullScreenButton = createFullScreenButton(slideStage);
		
		anchorPane = new AnchorPane();
		// pagination for the slides in the presentation
		Pagination slidePagination = new Pagination(totalSlideNumber);
		slidePagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
		slidePagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
	        @Override
	        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		        if (oldValue.intValue() == 0) {
		        	slide = tempPaginationTester(1);
		        }
		        else {
		        	slide = tempPaginationTester(newValue.intValue());
		        }
	        	anchorPane.getChildren().addAll(slide);
	        }
		});
	
		/* Temporary test code for the pagination system
		 * not currently used
		slidePagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer slideNumber) {
                return tempPaginationTester(slideNumber);
            }
        });	
		*/
		
		// instantiation of the separator on the control bar
		Separator separator = new Separator();
		// adding all the items on the control bar
		controlBar.getItems().addAll(slidePagination, separator, playButton, transitionButton, volumeSlider, 
													muteButton, fullScreenButton);
		return controlBar;
	}
	
	/**
	 * Method for creating the master volume slider with all its attributes
	 * @return volumeSlider  -  master volume slider which is placed on the control bar
	 */
	private Slider createVolumeSlider() {
		// instantiation of volume slider
		Slider volumeSlider = new Slider();
		// setting the attributes of the slider
		volumeSlider.setMin(0);
		volumeSlider.setMax(100);
		volumeSlider.setValue(50);
		volumeSlider.setShowTickLabels(true);
		volumeSlider.setShowTickMarks(true);
		volumeSlider.setMajorTickUnit(50);
		volumeSlider.setMinorTickCount(5);
		volumeSlider.setBlockIncrement(10);
		// listener to change the volume of the media when the slider is adjusted
		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    System.out.println(new_val.doubleValue());
            }
        });
		return volumeSlider;
	}

	/**
	 * Method for creating the automatic/manual slide transition button
	 * @return transitionButton  -  toggle button for switching the type of slide transition
	 */
	private ToggleButton createTransitionButton() {
		ToggleButton transitionButton = new ToggleButton("Automatic");
		transitionButton.setMaxSize(80, 30);
		transitionButton.setPrefSize(80, 30);
		transitionButton.setMinSize(80, 30);
		transitionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {
            	if (transitionButton.isSelected() == true) {
            		transitionButton.setText("Manual");
            	}
            	else { 
            		transitionButton.setText("Automatic");
            	}
            }
        });
		return transitionButton;
	}

	/**
	 * Method for creating the mute button on the control bar
	 * @return muteButton  -  toggle button for muting/unmuting the media
	 */
	private ToggleButton createMuteButton() {
		// image for mute button
		Image muteImage = new Image(getClass().getResourceAsStream("resources/mute.png"));
		ImageView muteView = new ImageView(muteImage);
		Image unmutedImage = new Image(getClass().getResourceAsStream("resources/unmute.png"));
		ImageView unmutedView = new ImageView(unmutedImage);
		// instantiation of mute button
		ToggleButton muteButton = new ToggleButton();
		muteButton.setGraphic(unmutedView);
		muteButton.setMaxSize(50, 30);
		muteButton.setPrefSize(50, 30);
		muteButton.setMinSize(50, 30);
		muteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (muteButton.isSelected() == true) {
					muteButton.setGraphic(muteView);
				}
				else {
					muteButton.setGraphic(unmutedView);
				}
			}
		});
		return muteButton;
	}
	
	/**
	 * method for creating the play button on the control bar
	 * @return playButton  -  toggle button for the pause/play of media
	 */
	private ToggleButton createPlayButton() {
		// image for the play button
		Image playImage = new Image(getClass().getResourceAsStream("resources/play.png"));
		ImageView playView = new ImageView(playImage);
		// image for the pause button
		Image pauseImage = new Image(getClass().getResourceAsStream("resources/pause.png"));
		ImageView pauseView = new ImageView(pauseImage);
		// instantiation of play button
		ToggleButton playButton = new ToggleButton();
		// set the image on the button
		playButton.setGraphic(playView);
		playButton.setMaxSize(40, 30);
		playButton.setPrefSize(40, 30);
		playButton.setMinSize(40, 30);
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (playButton.isSelected() == true) {
					playButton.setGraphic(pauseView);
				}
				else {
					playButton.setGraphic(playView);
				}
			}
		});
		return playButton;
	}
	
	/**
	 * method for creating the full screen toggle button
	 * @param slideStage  -  window that will switch from full screen to normal size
	 * @return fullScreenButton  -  the toggle button that enters and exits full screen
	 */
	private ToggleButton createFullScreenButton(Stage slideStage) {
		// image for the full screen button
		Image exitImage = new Image(getClass().getResource("resources/exit.png").toExternalForm());
		ImageView exitView = new ImageView(exitImage);
		// instantiation of full screen button
		ToggleButton fullScreenButton = new ToggleButton();
		// set the image on the button
		fullScreenButton.setGraphic(exitView);
		fullScreenButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (fullScreenButton.isSelected() == true) {
					slideStage.setFullScreen(false);
				}
				else {
					slideStage.setFullScreen(true);
				}
			}
		});
		return fullScreenButton;
	}

	/**
	 * temporary test method for the pagination system 
	 * @param slideNumber
	 * @return
	 */
	private VBox tempPaginationTester(Integer slideNumber) {
		
		slide = new VBox();
		Label slideNumberLabel = new Label("Slide: " + (slideNumber + 1));
		
		//Canvas newCanvas = new Canvas();
		//GraphicsContext gcSlideNumber = newCanvas.getGraphicsContext2D();
		//gcSlideNumber.fillText("Slide: " + slideNumber, 1, 1);
		slide.getChildren().addAll(slideNumberLabel);
		return slide;
	}
}
