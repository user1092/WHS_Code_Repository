package com.whs.client;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class PresentationGui extends Application {
		
	// temporary number for the pagination system
	private Integer slideNumber = 7;
	
	public static void main(String[] args) {
		launch(PresentationGui.class, args);
	}
	
	@Override
	public void start(Stage slideStage) throws Exception {		
		BorderPane slideLayout = new BorderPane();
		Scene scene = new Scene(slideLayout);
		
		
		//import and set background image
		String background = getClass().getResource("SlideBackground.jpg").toExternalForm();
		slideLayout.setStyle("-fx-background-image: url('" + background + "'); " +
		           "-fx-background-position: center center; " +
		           "-fx-background-repeat: stretch;");
		
		// call method for creating the control bar
		ToolBar controlBar = createControlBar();
		// place the control bar at the bottom of the window
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
	 * @return ToolBar  -  returns the tool bar which was created
	 */
	private ToolBar createControlBar() {
		// image for the play button
		Image playImage = new Image(getClass().getResourceAsStream("play.png"));
		ImageView playView = new ImageView(playImage);
		// image for the pause button
		Image pauseImage = new Image(getClass().getResourceAsStream("pause.png"));
		ImageView pauseView = new ImageView(pauseImage);
		// image for mute button
		Image muteImage = new Image(getClass().getResourceAsStream("mute.png"));
		ImageView muteView = new ImageView(muteImage);
		// image for the exit button
		Image exitImage = new Image(getClass().getResourceAsStream("exit.png"));
		ImageView exitView = new ImageView(exitImage);
		
		ToolBar controlBar = new ToolBar();
		// play button on tool bar
		Button playButton = new Button();
		// set the image on the button
		playButton.setGraphic(playView);
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.out.println("Play");
			}
		});
		// pause button on tool bar
		Button pauseButton = new Button();
		pauseButton.setGraphic(pauseView);
		pauseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.out.println("Pause");
			}
		});
		// mute button on tool bar
		Button muteButton = new Button();
		muteButton.setGraphic(muteView);
		muteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.out.println("Mute");
			}
		});
		// exit button on tool bar
		Button exitButton = new Button();
		exitButton.setGraphic(exitView);
		exitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.out.println("Exit");
			}
		});
		
		// volume slider on tool bar
		Slider volumeSlider = new Slider();
		volumeSlider.setMin(0);
		volumeSlider.setMax(100);
		volumeSlider.setValue(50);
		volumeSlider.setShowTickLabels(true);
		volumeSlider.setShowTickMarks(true);
		volumeSlider.setMajorTickUnit(50);
		volumeSlider.setMinorTickCount(5);
		volumeSlider.setBlockIncrement(10);
		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    System.out.println(new_val.doubleValue());
            }
        });
		
		// pagination for the slides in the presentation
		Pagination slidePagination = new Pagination(slideNumber);
		slidePagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
		
		Separator separator = new Separator();
		
		Region spacer = new Region();
		spacer.setMinWidth(Region.USE_PREF_SIZE);
		
		controlBar.getItems().addAll(slidePagination, separator, playButton, pauseButton, spacer, volumeSlider, 
													muteButton, exitButton);
		
		return controlBar;
	}

}
