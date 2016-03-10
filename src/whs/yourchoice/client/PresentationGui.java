package whs.yourchoice.client;

import java.util.ArrayList;
import java.util.List;

import presentech.TextHandler;
import stammtisch.Objects.Images;
import stammtisch.handlers.ImageHandler;

import whs.yourchoice.graphics.PolygonGraphic;
import whs.yourchoice.graphics.ShapeGraphic;
import whs.yourchoice.presentation.ImageEntry;
import whs.yourchoice.presentation.PolygonEntry;
import whs.yourchoice.presentation.PresentationEntry;
import whs.yourchoice.presentation.ShapeEntry;
import whs.yourchoice.presentation.SlideEntry;
import whs.yourchoice.presentation.TextEntry;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class PresentationGui extends Application {
	
	// temporary number for the pagination system
	//private Integer currentSlideNumber;
	private Integer totalSlideNumber = 7;
	private VBox slide;
	private AnchorPane anchorPane;
	
	final int WINDOW_MIN_WIDTH = 640;
	final int WINDOW_MIN_HEIGHT = 480;
	final int WINDOW_WIDTH = 640;
	final int WINDOW_HEIGHT = 480;
	final int CONTROL_BAR_WIDTH = WINDOW_WIDTH;
	final int CONTROL_BAR_HEIGHT = 60;
	final int PRESENTATION_WIDTH = WINDOW_WIDTH;
	final int PRESENTATION_HEIGHT = WINDOW_HEIGHT - (CONTROL_BAR_HEIGHT/2) - CONTROL_BAR_HEIGHT;
	
    private StackPane presentationLayout;
    private BorderPane subPresentationLayout;
	private BorderPane windowLayout;
	
	private double scaleXratio = 1;
    private double scaleYRatio = 1;
    private double stageInitialWidth = 0;
    private double stageInitialHeight = 0;
	
    private ToolBar controlBar;
    
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
	
	private List<TextFlow> textList = new ArrayList<TextFlow>();
	private List<Canvas> imageList = new ArrayList<Canvas>();
	private List<Pane> shapeList = new ArrayList<Pane>();
	private List<Pane> polygonList = new ArrayList<Pane>();
	

	public PresentationGui(PresentationEntry presentation) {
		Canvas duffCanvas = new Canvas(PRESENTATION_WIDTH,  PRESENTATION_HEIGHT);
		
		SlideEntry currentSlide;
		TextEntry currentText;
		String sourceFile = null;
		ImageEntry currentImage;
		Images tempImage;
		ShapeEntry currentShape;
		PolygonEntry currentPolygon; 
		
		float[] polygonX = {0.1f, 0.2f, 0.3f};
		float[] polygonY = {0.0f, 0.2f, 0.0f};
		int numberOfSlides;
		int numberOfTexts;
		int numberOfImages;
		int numberOfShapes;
		int numberOfPolygons;
		
		numberOfSlides = presentation.slideList.size();
		for(int slide = 0; slide < numberOfSlides; slide++) {
			currentSlide = presentation.slideList.get(slide);
			
			numberOfTexts = currentSlide.textList.size();
			numberOfImages = currentSlide.imageList.size();
			numberOfShapes = currentSlide.shapeList.size();
			numberOfPolygons = currentSlide.polygonList.size();
			
			System.out.println(numberOfSlides);
			
			for(int text = 0; text < numberOfTexts; text++) {
				currentText = currentSlide.textList.get(text);
				textList.add(text, TextHandler.createText(duffCanvas, sourceFile,
						currentText.getTextContent(), currentText.getTextFont(),
						currentText.getTextFontColour(), currentText.getTextFontSize(),
						currentText.getTextXStart(), currentText.getTextYStart(),
						currentText.getTextStartTime(), currentText.getTextDuration()));
				System.out.println(text);
				System.out.println(currentText.getTextContent());
				System.out.println(currentSlide.textList.get(text));
			}
			
			for(int image = 0; image < numberOfImages; image++) {
				ImageHandler imageHandler = new ImageHandler();
				
				currentImage = currentSlide.imageList.get(image);
				
				tempImage = new Images(currentImage.getImageSourceFile(),
														currentImage.getImageStartTime(),
														currentImage.getImageDuration(),
														currentImage.getImageXStart(),
														currentImage.getImageYStart(),
														currentImage.getImageHeight(),
														currentImage.getImageWidth());
				imageList.add(imageHandler.drawCanvas(tempImage, PRESENTATION_WIDTH, PRESENTATION_HEIGHT));
				System.out.println(image);
				System.out.println(currentSlide.imageList.get(image));
			}
			
			for(int shape = 0; shape < numberOfShapes; shape++) {
				currentShape = currentSlide.shapeList.get(shape);
				
//				ShapeGraphic ShapeEntry = new ShapeGraphic(currentShape.getShapeStartTime(),
//													currentShape.getShapeDuration(),
//													currentShape.getShapeXStart(),
//													currentShape.getShapeYStart(),
//													currentShape.getShapeHeight(),
//													currentShape.getShapeWidth(),
//													currentShape.getShapeType(),
//													currentShape.getShapeFillColour(),
//													currentShape.getShapeLineColour());
				ShapeGraphic shapeEntry = new ShapeGraphic(currentShape.getShapeStartTime(),
														currentShape.getShapeDuration(),
														currentShape.getShapeXStart(),
														currentShape.getShapeYStart(),
														currentShape.getShapeHeight(),
														currentShape.getShapeWidth(),
														currentShape.getShapeType());
												
				shapeEntry.setRes(PRESENTATION_WIDTH, PRESENTATION_HEIGHT);
				shapeList.add(shapeEntry.drawShape());
				System.out.println(shape);
				System.out.println(currentSlide.shapeList.get(shape));
			}
			
			for(int polygon = 0; polygon < numberOfPolygons; polygon++) {
				currentPolygon = currentSlide.polygonList.get(polygon);
				
//				ShapeGraphic ShapeEntry = new ShapeGraphic(currentShape.getShapeStartTime(),
//													currentShape.getShapeDuration(),
//													currentShape.getShapeXStart(),
//													currentShape.getShapeYStart(),
//													currentShape.getShapeHeight(),
//													currentShape.getShapeWidth(),
//													currentShape.getShapeType(),
//													currentShape.getShapeFillColour(),
//													currentShape.getShapeLineColour());
				PolygonGraphic polygonEntry = new PolygonGraphic(currentPolygon.getPolygonStartTime(),
															currentPolygon.getPolygonDuration(),
															polygonX, polygonY,
															currentPolygon.getPolygonFillColour(),
															currentPolygon.getPolygonLineColour(),
															currentPolygon.getPolygonSourceFile());
				
				polygonEntry.setRes(PRESENTATION_WIDTH, PRESENTATION_HEIGHT);
				shapeList.add(polygonEntry.drawPolygon());
				System.out.println(polygon);
				System.out.println(currentSlide.polygonList.get(polygon));
			}
			
			
			
		}
	}
	
	private void displayPresentation() {
//		for(int text = 0; text < textList.size(); text++) {
//			System.out.println(text);
//			System.out.println(textList.get(text));
//			presentationLayout.getChildren().add(textList.get(text));
//		}
		presentationLayout.getChildren().add(textList.get(1));
		
		for(int image = 0; image < imageList.size(); image++) {
			System.out.println("disp image number: " + image);
			System.out.println(textList.get(image));
			presentationLayout.getChildren().add(imageList.get(image));
		}
		
		for(int shape = 0; shape < shapeList.size(); shape++) {
			System.out.println("disp shape number: " + shape);
			System.out.println(shapeList.get(shape));
			presentationLayout.getChildren().add(shapeList.get(shape));
		}
		
//		for(int polygon = 0; polygon < polygonList.size(); polygon++) {
//			System.out.println("disp polygon number: " + polygon);
//			System.out.println(polygonList.get(polygon));
//			presentationLayout.getChildren().add(polygonList.get(polygon));
//		}
		
		
	}
	
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

        windowLayout.getScene().widthProperty()
        .addListener(new ChangeListener<Number>() {
             @Override
             public void changed(ObservableValue<? extends Number> observable,
                       Number oldValue, Number newValue) {

                  scaleXratio = newValue.doubleValue() / stageInitialWidth;

                  presentationLayout.getTransforms().clear();
                  Scale scale = new Scale(scaleXratio, scaleYRatio, 0, 0);
                  presentationLayout.getTransforms().add(scale);
                  controlBar.setPrefHeight(CONTROL_BAR_HEIGHT);

             }
        });

        windowLayout.getScene().heightProperty()
        .addListener(new ChangeListener<Number>() {
             @Override
             public void changed(ObservableValue<? extends Number> observable,
                       Number oldValue, Number newValue) {

                  scaleYRatio = (newValue.doubleValue())
                            / (stageInitialHeight);

                  presentationLayout.getTransforms().clear();
                  Scale scale = new Scale(scaleXratio, scaleYRatio, 0, 0);
                  presentationLayout.getTransforms().add(scale);
                  controlBar.setPrefHeight(CONTROL_BAR_HEIGHT);

             }
        });
		
        displayPresentation();
        
		slideStage.setFullScreen(true);
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
	 * method for creating the play button on the control bar
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
	 * method for creating the full screen toggle button
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
	 * temporary test method for the pagination system 
	 * @param slideNumber
	 * @return
	 */
	private VBox tempPaginationTester(Integer slideNumber) {
		
		slide = new VBox();
		Label slideNumberLabel = new Label("Slide: " + (slideNumber + 1));
		slide.getChildren().addAll(slideNumberLabel);
		return slide;
	}
	
//	public void show(){
//        launch();
//    }
	
}
