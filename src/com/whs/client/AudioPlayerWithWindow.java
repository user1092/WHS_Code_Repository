package com.whs.client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


/**
 * Example for using the AudioPlayer.java class
 * 
 * @author ch1092 skq501
 * 
 * NOT FOR RELEASE!
 *
 */
public class AudioPlayerWithWindow extends Application {	
		
	private AudioPlayer audioPlayer;
			
	public AudioPlayerWithWindow() {
		audioPlayer = new AudioPlayer();
		
	}
	
	public static void main(String[] args) {
		launch(AudioPlayerWithWindow.class, args);
	}
	
	@Override
	public void start(Stage slideStage) throws Exception {		
		System.out.println("Hello");
		BorderPane slideLayout = new BorderPane();
		Scene scene = new Scene(slideLayout);
				
		// call method for creating the control bar
		ToolBar controlBar = createControlBar();
		// place the control bar at the bottom of the window
		slideLayout.setBottom(controlBar);
		
		
		//main stage set up with appropriate scene and size
		slideStage.setScene(scene);
		slideStage.setHeight(600);
		slideStage.setWidth(800);
		slideStage.setTitle("Test Slide");
		slideStage.show();
		
		System.out.println("Setup");
		
		
		//audioPlayer.loadLocalAudio("jetairplane16sec.wav");
		audioPlayer.loadStreamedAudio();
	}

	
	
	/**
	 * Method for creating the control tool bar at the bottom of each slide
	 * @param id  -  number of the item to be entered on the tool bar
	 * @return ToolBar  -  returns the tool bar which was created
	 */
	private ToolBar createControlBar() {
		// image for the play button
		Image playImage = new Image(getClass().getResourceAsStream("play.png"));
		ImageView playView = new ImageView(playImage);
		// image for the pause button
		Image pauseImage = new Image(getClass().getResourceAsStream("pause.png"));
		ImageView pauseView = new ImageView(pauseImage);
		// image for the exit button
		Image exitImage = new Image(getClass().getResourceAsStream("exit.png"));
		ImageView exitView = new ImageView(exitImage);
		
		ToolBar controlBar = new ToolBar();
		// play button on tool bar
		Button playButton = new Button();
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				audioPlayer.playAudio();
			}
		});
		
		// set the image on the button
		playButton.setGraphic(playView);
		// pause button on tool bar
		Button pauseButton = new Button();
		pauseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				audioPlayer.pauseAudio();
			}
		});
		pauseButton.setGraphic(pauseView);
		
		// exit button on tool bar
		Button exitButton = new Button();
		exitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				audioPlayer.stopAudio();
			}
		});
		exitButton.setGraphic(exitView);
		
		Button muteButton = new Button();
		muteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				audioPlayer.toggleMuteAudio();
			}
		});
		
		Button loopButton = new Button();
		loopButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				audioPlayer.toggleLoopAudio();
			}
		});
		
		// volume slider on tool bar
		Slider volumeSlider = new Slider();
		volumeSlider.setMin(0);
		volumeSlider.setMax(100);
		volumeSlider.setValue(40);
		volumeSlider.setShowTickLabels(true);
		volumeSlider.setShowTickMarks(true);
		volumeSlider.setMajorTickUnit(50);
		volumeSlider.setMinorTickCount(5);
		volumeSlider.setBlockIncrement(10);
		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    System.out.println(new_val.doubleValue());
                    audioPlayer.setAudioVolume(new_val.intValue());
            }
        });
		
		Separator separator = new Separator();
		
		Region spacer = new Region();
		spacer.setMinWidth(Region.USE_PREF_SIZE);
		
		controlBar.getItems().addAll(separator, playButton, pauseButton, spacer, volumeSlider, muteButton, loopButton, exitButton);
		
		return controlBar;
	}
	
	@Override
    public final void stop() throws Exception {
        audioPlayer.releasePlayer();
    }
	
}
