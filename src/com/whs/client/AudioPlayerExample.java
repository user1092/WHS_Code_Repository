package com.whs.client;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.whs.server.AudioStreamer;

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
 * @author user1092 guest501
 * 
 * NOT FOR RELEASE!
 *
 */
public class AudioPlayerExample extends Application {	
		
	
	
	/*
	 * Start of Audio specific declarations
	 */
	
	// Location of where VLC is installed
	private final String VLC_LIBRARY_LOCATION = "C:/temp/vlc-2.1.0";
	// Set VLC video output to a dummy
	private final String[] VLC_ARGS = {"--vout", "dummy"};
	
	private MediaPlayerFactory mediaPlayerFactory;
	
	private AudioPlayer audioPlayer;
	
	//private String audioDesired = "C:\\temp\\jetairplane16sec.aac";
	private String iP = "127.0.0.1";
	private int rtpPort = 5555;
	
	/*
	 * End of Audio specific declarations
	 */
			
	public AudioPlayerExample() {
		// Find and load VLC libraries
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), VLC_LIBRARY_LOCATION);
		System.setProperty("jna.library.path", VLC_LIBRARY_LOCATION);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
		mediaPlayerFactory = new MediaPlayerFactory(VLC_ARGS);		
		//headlessPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
		
		audioPlayer = new AudioPlayer(mediaPlayerFactory);
		
		
	}
	
	public static void main(String[] args) {
		launch(AudioPlayerExample.class, args);
	}
	
	@Override
	public void start(Stage slideStage) throws Exception {
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
		slideStage.setTitle("Audio Player Example");
		slideStage.show();
		
	}
	
	/**
	 * Method for creating the control tool bar at the bottom of each slide
	 * @param id  -  number of the item to be entered on the tool bar
	 * @return ToolBar  -  returns the tool bar which was created
	 */
	private ToolBar createControlBar() {
		
		ToolBar controlBar = new ToolBar();
		// play button on tool bar
		Button playButton = new Button("Play");
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				audioPlayer.playAudio();
			}
		});
		
		// set the image on the button
		//playButton.setGraphic();
		// pause button on tool bar
		Button pauseButton = new Button("Pause");
		pauseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				audioPlayer.pauseAudio();
			}
		});
		//pauseButton.setGraphic(pauseView);
		
		// exit button on tool bar
		Button stopButton = new Button("Stop");
		stopButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				audioPlayer.stopAudio();
			}
		});
		//exitButton.setGraphic(exitView);
		
		Button muteButton = new Button("Mute");
		muteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				audioPlayer.toggleMuteAudio();
			}
		});
		
		Button loopButton = new Button("Loop");
		loopButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				audioPlayer.toggleLoopAudio();
			}
		});
		
		Button streamButton = new Button("Play Stream");
		streamButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				audioPlayer.playStreamedAudio();
			}
		});
		
		Button loadButton = new Button("Load Local File");
		loadButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				audioPlayer.loadLocalAudio("jetairplane16sec.wav");
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
		
		controlBar.getItems().addAll(playButton, pauseButton, stopButton, volumeSlider, muteButton, loopButton,  streamButton, loadButton);
		
		return controlBar;
	}
	
	@Override
    public final void stop() throws Exception {
        audioPlayer.releasePlayer(mediaPlayerFactory);
    }
	
}
