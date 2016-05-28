/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.audio;

import java.io.File;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


/**
 * Example for using the AudioPlayer.java class
 * 
 * This file has been tested with the following configuration:
 * JDK 1.8 32-bit
 * VLC 2.1.0 32-bit
 * vlcj-3.8.0
 * an acc format audio file
 * 
 * NOT FOR RELEASE!
 * 
 * @author 		ch1092, skq501
 * @version		v0.3 05/03/2016
 */
public class AudioPlayerExample extends Application {	
	
	/*
	 * Start of Audio specific declarations
	 */
	
	// Location of where VLC is installed
	private final String VLC_LIBRARY_LOCATION = new File("").getAbsolutePath() + "/vlc-2.1.0";
	// Set VLC video output to a dummy, waveout used as bug with DX
	private final String[] VLC_ARGS = {"--vout", "dummy", "--aout", "waveout"};//, "-vvv"};
	
	private MediaPlayerFactory mediaPlayerFactory;
	
	private AudioPlayer audioPlayer;
	// File to play locally
	private String audioFile = "jetairplane16sec.aac";
	// Server details
	private String host = "127.0.0.1";
	private int rtpPort = 5555;
	
    private Number masterVolume = DEFAULT_START_VOLUME;
    private static final int DEFAULT_START_VOLUME = 100;
    private boolean masterMute = false;
	
	/*
	 * End of Audio specific declarations
	 */
	
	/**
	 * Constructor
	 * 
	 * Setup the audio player backend
	 */
	public AudioPlayerExample() {
		// Find and load VLC libraries
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), VLC_LIBRARY_LOCATION);
		System.setProperty("jna.library.path", VLC_LIBRARY_LOCATION);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
		// Create a media player factory
		mediaPlayerFactory = new MediaPlayerFactory(VLC_ARGS);
		
		// Create a audio player
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
	 * 
	 * @return ToolBar  -  returns the tool bar which was created
	 */
	private ToolBar createControlBar() {
		
		ToolBar controlBar = new ToolBar();
		
		// play button on tool bar
		Button playNowButton = new Button("Play Now");
		playNowButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				audioPlayer.playAudio(audioFile);
				audioPlayer.playAudio();
				// Fixes "feature" with vlc where audio is not observed if not playing
				while(-1 == audioPlayer.getAudioVolume()) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				audioPlayer.setAudioVolume(masterVolume.intValue());
				audioPlayer.muteAudio(masterMute);
			}
		});
		
		// play button on tool bar
		Button playButton = new Button("Play");
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				audioPlayer.playAudio();
				audioPlayer.playAudio();
				// Fixes "feature" with vlc where audio is not observed if not playing
				while(-1 == audioPlayer.getAudioVolume()) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				audioPlayer.setAudioVolume(masterVolume.intValue());
				audioPlayer.muteAudio(masterMute);
			}
		});
		
		// pause button on tool bar
		Button pauseButton = new Button("Pause");
		pauseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				audioPlayer.pauseAudio();
			}
		});
		
		// stop button on tool bar
		Button stopButton = new Button("Stop");
		stopButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				audioPlayer.stopAudio();
			}
		});
		
		// mute button on tool bar
		Button muteButton = new Button("Mute");
		muteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (masterMute) {
					masterMute = false;
				}
				else {
					masterMute = true;
				}
				audioPlayer.toggleMuteAudio();
			}
		});
		
		// loop button on tool bar
		Button loopButton = new Button("Loop");
		loopButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				audioPlayer.toggleLoopAudio();
			}
		});
		
		// play stream button on tool bar
		Button streamButton = new Button("Play Stream");
		streamButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				audioPlayer.playStreamedAudio(host, rtpPort);
			}
		});
		
		// load button on tool bar
		Button loadButton = new Button("Load Local File");
		loadButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				audioPlayer.loadLocalAudio(audioFile);
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
            		masterVolume = new_val;
                    audioPlayer.setAudioVolume(new_val.intValue());
            }
        });
		
		// add all buttons to controlBar
		controlBar.getItems().addAll(playNowButton, playButton, pauseButton, stopButton, volumeSlider, muteButton, loopButton,  streamButton, loadButton);
		
		return controlBar;
	}
	
	@Override
    public final void stop() throws Exception {
        audioPlayer.releasePlayer();
		mediaPlayerFactory.release();
    }
	
}
