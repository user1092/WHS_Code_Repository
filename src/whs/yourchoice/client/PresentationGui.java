package whs.yourchoice.client;

import java.util.List;
import java.util.ArrayList;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import presentech.TextHandler;

import stammtisch.Objects.Images;
import stammtisch.handlers.ImageHandler;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import whs.yourchoice.audio.AudioPlayer;
import whs.yourchoice.graphics.PolygonGraphic;
import whs.yourchoice.graphics.ShapeGraphic;
import whs.yourchoice.presentation.AudioEntry;
import whs.yourchoice.presentation.ImageEntry;
import whs.yourchoice.presentation.PolygonEntry;
import whs.yourchoice.presentation.PresentationEntry;
import whs.yourchoice.presentation.ShapeEntry;
import whs.yourchoice.presentation.SlideEntry;
import whs.yourchoice.presentation.TextEntry;
import whs.yourchoice.presentation.VideoEntry;
import whs.yourchoice.video.VideoPlayer;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Pagination;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
* Class for creation of the presentation window and adding functionality
*
* @author user828 & user1092
* @version v0.6 23/03/16
*/
public class PresentationGui extends Application {
	
	// Definitions for window, presentation and control area sizes
	final int WINDOW_MIN_WIDTH = 640;
	final int WINDOW_MIN_HEIGHT = 480;
	final int WINDOW_WIDTH = 640;
	final int WINDOW_HEIGHT = 480;
	final int CONTROL_BAR_WIDTH = WINDOW_WIDTH;
	final int CONTROL_BAR_HEIGHT = 60;
	final int PRESENTATION_WIDTH = WINDOW_WIDTH;
	final int PRESENTATION_HEIGHT = WINDOW_HEIGHT - (CONTROL_BAR_HEIGHT/2) - CONTROL_BAR_HEIGHT;
	//TODO May consider implementing in the xml file
	final float VIDEO_WIDTH = 0.3f;
	final float VIDEO_HEIGHT = 0.3f;
	
	private StackPane presentationLayout;
    private BorderPane subPresentationLayout;
	private BorderPane windowLayout;
	
	// temporary number for the pagination system
	// private Integer currentSlideNumber;
	private Integer totalSlideNumber = 7;
	
	// Variables for the scaling of all the objects on the window
	private double scaleWidthRatio = 1;
    private double scaleHeightRatio = 1;
    private double stageInitialWidth = 0;
    private double stageInitialHeight = 0;
	
    
    private ToolBar controlBar;
    
    // Objects present on the control bar
	private ToggleButton transitionButton;
	private ToggleButton muteButton;
	private Image muteImage;
	private ImageView muteView;
	private Image unmutedImage;
	private ImageView unmutedView;
	private ToggleButton playButton;
	private Image playImage;
	private ImageView playView;
	private Image pauseImage;
	private ImageView pauseView;
	private ToggleButton fullScreenButton;
	
	private PresentationEntry presentation;
	
	// Location of where VLC is installed
	private final String VLC_LIBRARY_LOCATION = "M:/vlc-2.1.0";
	// Set VLC video output to a dummy, waveout used as bug with DX
	private final String[] VLC_ARGS = {"--vout", "dummy", "--aout", "waveout"};
	private MediaPlayerFactory mediaPlayerFactory;
	private List<AudioPlayer> audioPlayerList = new ArrayList<AudioPlayer>();
	//private AudioPlayer audioPlayer;
	

	/**
	 * Constructor requires a presentation to display
	 * 
	 * @param passedPresentation	-	The presentation to be played
	 */
	public PresentationGui(PresentationEntry passedPresentation) {
		presentation = passedPresentation;
		
		// Find and load VLC libraries
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), VLC_LIBRARY_LOCATION);
		System.setProperty("jna.library.path", VLC_LIBRARY_LOCATION);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
		// Create a media player factory
		mediaPlayerFactory = new MediaPlayerFactory(VLC_ARGS);
			
	}
	
	
	/**
	 * Main method to launch the GUI
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(PresentationGui.class, args);
	}
	
	@Override
	public void start(Stage slideStage) throws Exception {
		presentationLayout = new StackPane();
		subPresentationLayout = new BorderPane();
		windowLayout = new BorderPane();
		
		Scene scene = new Scene(windowLayout, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		windowLayout.setTop(subPresentationLayout);
		subPresentationLayout.setLeft(presentationLayout);
		
		slideStage.setMinHeight(WINDOW_MIN_HEIGHT);
		slideStage.setMinWidth(WINDOW_MIN_WIDTH);
		
		//import and set background image
		String background = getClass().getResource("resources/SlideBackground.jpg").toExternalForm();
		windowLayout.setStyle("-fx-background-image: url('" + background + "'); " +
		           "-fx-background-position: center center; " +
		           "-fx-background-repeat: stretch;");
		
		// call method for creating the control bar
		controlBar = createControlBar(slideStage);
		controlBar.setMaxHeight(CONTROL_BAR_HEIGHT);
		controlBar.setMinHeight(CONTROL_BAR_HEIGHT);
		controlBar.setPrefHeight(CONTROL_BAR_HEIGHT);
		
		// place the control bar at the bottom of the window
		windowLayout.setBottom(controlBar);

		// Handler to change state of full screen toggle button
		// when the user exits fullscreen using esc button
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
	        public void handle(KeyEvent ke) {
	            if (ke.getCode() == KeyCode.ESCAPE) {
	            	fullScreenButton.setSelected(true);
	            }
	        }
	    });
		
		//main stage set up with appropriate scene and size
		slideStage.setScene(scene);
		slideStage.setWidth(WINDOW_WIDTH);
		slideStage.setHeight(WINDOW_HEIGHT);
		slideStage.setTitle("Presentation Slide");
		slideStage.show();
		
		
		stageInitialWidth = scene.getWidth();
        stageInitialHeight = scene.getHeight();

        // listener for getting the width of the window
        // and scaling everything according to the change in the window size
        windowLayout.getScene().widthProperty()
        .addListener(new ChangeListener<Number>() {
             @Override
             public void changed(ObservableValue<? extends Number> observable,
                       Number oldValue, Number newValue) {

            	 scaleWidthRatio = newValue.doubleValue() / stageInitialWidth;

                  presentationLayout.getTransforms().clear();
                  Scale scale = new Scale(scaleWidthRatio, scaleHeightRatio, 0, 0);
                  presentationLayout.getTransforms().add(scale);
                  controlBar.setPrefHeight(CONTROL_BAR_HEIGHT);

             }
        });

        // listener for getting the height of the window
        // and scaling everything according to the change in the window size
        windowLayout.getScene().heightProperty()
        .addListener(new ChangeListener<Number>() {
             @Override
             public void changed(ObservableValue<? extends Number> observable,
                       Number oldValue, Number newValue) {

            	 scaleHeightRatio = (newValue.doubleValue())
                            / (stageInitialHeight);

                  presentationLayout.getTransforms().clear();
                  Scale scale = new Scale(scaleWidthRatio, scaleHeightRatio, 0, 0);
                  presentationLayout.getTransforms().add(scale);
                  controlBar.setPrefHeight(CONTROL_BAR_HEIGHT);

             }
        });
        
        // Listen for the stage closing to release all audio players
        slideStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        	public void handle(WindowEvent we) {
        		
        		System.out.println("audioPlayerList size: " + audioPlayerList.size());
        		
        		for(int audio = 0; audio <audioPlayerList.size(); audio++) {
        			audioPlayerList.get(audio).releasePlayer();
        		}
        		
        		mediaPlayerFactory.release();
        		System.out.println("Exit of audio players successful");
        	}
        });
		       
        displaySlide(0);
                
        // initialise full screen
		slideStage.setFullScreen(true);
	}
	
	/**
	 * Method for creating the control tool bar at the bottom of each slide
	 * 
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
		
		// pagination for the slides in the presentation
		Pagination slidePagination = new Pagination(totalSlideNumber);
		slidePagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
		
	
		
		// instantiation of the separator on the control bar
		Separator separator = new Separator();
		// adding all the items on the control bar
		controlBar.getItems().addAll(slidePagination, separator, playButton, transitionButton, volumeSlider, 
													muteButton, fullScreenButton);
		return controlBar;
	}
	
	/**
	 * Method for creating the master volume slider with all its attributes
	 * 
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
	 * 
	 * @return transitionButton  -  toggle button for switching the type of slide transition
	 */
	private ToggleButton createTransitionButton() {
		transitionButton = new ToggleButton("Automatic");
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
	 * 
	 * @return muteButton  -  toggle button for muting/unmuting the media
	 */
	private ToggleButton createMuteButton() {
		// image for mute button
		muteImage = new Image(getClass().getResourceAsStream("resources/mute.png"));
		muteView = new ImageView(muteImage);
		unmutedImage = new Image(getClass().getResourceAsStream("resources/unmute.png"));
		unmutedView = new ImageView(unmutedImage);
		// instantiation of mute button
		muteButton = new ToggleButton();
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
	 * Method for creating the play button on the control bar
	 * 
	 * @return playButton  -  toggle button for the pause/play of media
	 */
	private ToggleButton createPlayButton() {
		// image for the play button
		playImage = new Image(getClass().getResourceAsStream("resources/play.png"));
		playView = new ImageView(playImage);
		// image for the pause button
		pauseImage = new Image(getClass().getResourceAsStream("resources/pause.png"));
		pauseView = new ImageView(pauseImage);
		// instantiation of play button
		playButton = new ToggleButton();
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
	 * Method for creating the full screen toggle button
	 * 
	 * @param slideStage  -  window that will switch from full screen to normal size
	 * @return fullScreenButton  -  the toggle button that enters and exits full screen
	 */
	private ToggleButton createFullScreenButton(final Stage slideStage) {
		// image for the full screen button
		Image exitImage = new Image(getClass().getResource("resources/exit.png").toExternalForm());
		ImageView exitView = new ImageView(exitImage);
		// instantiation of full screen button
		fullScreenButton = new ToggleButton();
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
	 * Method for displaying all objects on a slide within the current presentation
	 * 
	 * @param slideId - The slide number in the presentation to be displayed
	 */
	private void displaySlide(int slideId) {
		// An empty canvas to represent the size of the slide area
		Canvas duffCanvas = new Canvas(PRESENTATION_WIDTH,  PRESENTATION_HEIGHT);
		
		// Retrieve the slide information that was requested
		SlideEntry currentSlide = presentation.slideList.get(slideId);
		
		displayTexts(currentSlide, duffCanvas);
				
		displayImages(currentSlide);
		
		displayShapes(currentSlide);
		
		displayPolygons(currentSlide);
		
		displayVideos(currentSlide, duffCanvas);
		
		// Get the total number of audio files to be played
		int numberOfAudios = currentSlide.audioList.size();
				
		// Display all audio
		for(int audio = 0; audio < numberOfAudios; audio++) {
			// Create a audio player
			AudioPlayer audioPlayer = new AudioPlayer(mediaPlayerFactory);
			audioPlayerList.add(audioPlayer);
			
			AudioEntry currentAudio = new AudioEntry();
			
			currentAudio = currentSlide.audioList.get(audio);
			
			audioPlayerList.get(audio).playAudio(currentAudio.getAudioSourceFile());
			audioPlayerList.get(audio).loopAudio(currentAudio.getAudioLoop());
			
			System.out.println("audio entry: " + audio + "\n");
		}
	}


	/**
	 * Method to display all text on the current slide
	 * 
	 * @param currentSlide	-	The slide of which elements are to be displayed
	 * @param duffCanvas	-	A reference canvas for getting the size of the presentation area
	 */
	private void displayTexts(SlideEntry currentSlide, Canvas duffCanvas) {
		// Get the total number of images to be displayed
		int numberOfTexts = currentSlide.textList.size();
		
		// Set sourceFile to null as not used by Wooly Hat Software 
		String sourceFile = null;
		
		// Display all text
		for(int text = 0; text < numberOfTexts; text++) {
			Pane tempPane = new Pane();
			TextEntry currentText = new TextEntry();
			
			currentText = currentSlide.textList.get(text);
			
			tempPane.getChildren().add(TextHandler.createText(duffCanvas, sourceFile,
					currentText.getTextContent(), currentText.getTextFont(),
					currentText.getTextFontColour(), currentText.getTextFontSize(),
					currentText.getTextXStart(), currentText.getTextYStart(),
					currentText.getTextStartTime(), currentText.getTextDuration()));
			
			presentationLayout.getChildren().add(tempPane);
			
			System.out.println("text entry: " + text + "\n");
			System.out.println(currentText.getTextContent() + "\n");
		}
	}


	/**
	 * Method to display all images on the current slide
	 * 
	 * @param currentSlide	-	The slide of which elements are to be displayed
	 */
	private void displayImages(SlideEntry currentSlide) {
		// Get the total number of images to be displayed
		int numberOfImages = currentSlide.imageList.size();
		
		// Display all images
		for(int image = 0; image < numberOfImages; image++) {
			ImageHandler imageHandler = new ImageHandler();
			Images tempImage = new Images();
			
			ImageEntry currentImage = currentSlide.imageList.get(image);
			
			tempImage = new Images(currentImage.getImageSourceFile(),
													currentImage.getImageStartTime(),
													currentImage.getImageDuration(),
													currentImage.getImageXStart(),
													currentImage.getImageYStart(),
													currentImage.getImageHeight(),
													currentImage.getImageWidth());
			
			presentationLayout.getChildren().add(imageHandler.drawCanvas(tempImage, PRESENTATION_WIDTH, PRESENTATION_HEIGHT));
			
			System.out.println("image entry: " + image + "\n");
		}
	}


	/**
	 * Method to display all shapes on the current slide
	 * 
	 * @param currentSlide	-	The slide of which elements are to be displayed
	 */
	private void displayShapes(SlideEntry currentSlide) {
		// Get the total number of shapes to be displayed
		int numberOfShapes = currentSlide.shapeList.size();
		
		// Display all shapes
		for(int shape = 0; shape < numberOfShapes; shape++) {
			ShapeEntry currentShape = currentSlide.shapeList.get(shape);
			
			//TODO Should be what is in the commented code but parser doesn't set defaults.
//			ShapeGraphic ShapeEntry = new ShapeGraphic(currentShape.getShapeStartTime(),
//												currentShape.getShapeDuration(),
//												currentShape.getShapeXStart(),
//												currentShape.getShapeYStart(),
//												currentShape.getShapeHeight(),
//												currentShape.getShapeWidth(),
//												currentShape.getShapeType(),
//												currentShape.getShapeFillColour(),
//												currentShape.getShapeLineColour());
			ShapeGraphic tempShape = new ShapeGraphic(currentShape.getShapeStartTime(),
													currentShape.getShapeDuration(),
													currentShape.getShapeXStart(),
													currentShape.getShapeYStart(),
													currentShape.getShapeHeight(),
													currentShape.getShapeWidth(),
													currentShape.getShapeType());
			
			// Scale the shape to the resolution of the presentation area
			tempShape.setRes(PRESENTATION_WIDTH, PRESENTATION_HEIGHT);
			
			presentationLayout.getChildren().add(tempShape.drawShape());
			
			System.out.println("shape entry: " + shape + "\n");
		}
	}


	/**
	 * Method to display all polygons on the current slide
	 * 
	 * @param currentSlide	-	The slide of which elements are to be displayed
	 */
	private void displayPolygons(SlideEntry currentSlide) {
		// Get the total number of polygons to be displayed
		int numberOfPolygons = currentSlide.polygonList.size();
		
		//TODO Currently broken uses this as default
		float[] polygonX = {0.1f, 0.2f, 0.3f};
		float[] polygonY = {0.2f, 0.4f, 0.2f};
		
		// Display all polygons
		for(int polygon = 0; polygon < numberOfPolygons; polygon++) {
			PolygonEntry currentPolygon = currentSlide.polygonList.get(polygon);
			
			//TODO modifiy below
//			ShapeGraphic ShapeEntry = new ShapeGraphic(currentShape.getShapeStartTime(),
//												currentShape.getShapeDuration(),
//												currentShape.getShapeXStart(),
//												currentShape.getShapeYStart(),
//												currentShape.getShapeHeight(),
//												currentShape.getShapeWidth(),
//												currentShape.getShapeType(),
//												currentShape.getShapeFillColour(),
//												currentShape.getShapeLineColour());
			PolygonGraphic tempPolygon = new PolygonGraphic(currentPolygon.getPolygonStartTime(),
														currentPolygon.getPolygonDuration(),
														polygonX, polygonY,
														currentPolygon.getPolygonFillColour(),
														currentPolygon.getPolygonLineColour(),
														currentPolygon.getPolygonSourceFile());
			
			tempPolygon.setRes(PRESENTATION_WIDTH, PRESENTATION_HEIGHT);
			
			presentationLayout.getChildren().add(tempPolygon.drawPolygon());
			
			System.out.println("polygon entry: " + polygon + "\n");
		}
	}


	/**
	 * Method to display all videos on the current slide
	 * 
	 * @param currentSlide	-	The slide of which elements are to be displayed
	 * @param duffCanvas	-	A reference canvas for getting the size of the presentation area
	 */
	private void displayVideos(SlideEntry currentSlide, Canvas duffCanvas) {
		// Get the total number of videos to be displayed
		int numberOfVideos = currentSlide.videoList.size();
				
		// Display all videos
		for(int video = 0; video < numberOfVideos; video++) {
			VideoEntry currentVideo = currentSlide.videoList.get(video);
			
			VideoPlayer tempVideo = new VideoPlayer();
			
			presentationLayout.getChildren().add(tempVideo.videoPlayerWindow(currentVideo.getVideoSourceFile(),
												currentVideo.getVideoYStart(),
												currentVideo.getVideoXStart(),
												VIDEO_WIDTH, VIDEO_HEIGHT, duffCanvas));
			
			System.out.println("video entry: " + video + "\n");
		}
	}
}
