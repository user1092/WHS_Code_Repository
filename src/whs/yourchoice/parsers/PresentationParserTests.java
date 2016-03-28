package whs.yourchoice.parsers;

/**
* Presentation Parser Tests
* Latest Update: 10/03/16
*
* Copyright and Licensing Information if applicable
*/
import static org.junit.Assert.assertEquals;

/**
* This class is responsible for testing the PresentationParser Class.
*
* @author Antonio Figueiredo and Sabrina Quinn
* @version v1.0 10/03/16
*/

import org.junit.Test;

import whs.yourchoice.presentation.AudioEntry;
import whs.yourchoice.presentation.ImageEntry;
import whs.yourchoice.presentation.PolygonEntry;
import whs.yourchoice.presentation.PresentationEntry;
import whs.yourchoice.presentation.ShapeEntry;
import whs.yourchoice.presentation.SlideEntry;
import whs.yourchoice.presentation.TextEntry;
import whs.yourchoice.presentation.VideoEntry;

public class PresentationParserTests 
{
	/**
	* ExtractPresentationInfo Test. Parses the XML file and asserts if the both the "default" and "documentInfo" tags were parsed and stored successfully.
	*/
	@Test
	public void ExtractPresentationInfo()
	{		
		PresentationParser parser = new PresentationParser();		
		PresentationEntry presentation;
		presentation = parser.parsePresention("src/test_file.xml");
		
		assertEquals("Your Choice", presentation.getPresentationTitle());
		assertEquals("Woolly Hat Software", presentation.getPresentationAuthor());
		assertEquals("v1.0", presentation.getPresentationVersion());
		
		assertEquals("DDFFDD", presentation.getDefaultBackgroundColour());
		assertEquals("Arial", presentation.getDefaultFont());
		assertEquals(12, presentation.getDefaultFontSize());
		assertEquals("987654", presentation.getDefaultFontColour());
		assertEquals("777777", presentation.getDefaultLineColour());
		assertEquals("888888", presentation.getDefaultFillColour());
	}
	
	/**
	* ExtractSlideInfo Test. Parses the XML file and asserts if all slides and all their contents were parsed and stored successfully.
	*/
	@Test
	public void ExtractSlideInfo()
	{
		PresentationParser parser = new PresentationParser();
		PresentationEntry presentation = new PresentationEntry();		
		presentation = parser.parsePresention("src/test_file.xml");
		
		SlideEntry currentSlide;
		TextEntry currentText;
		ShapeEntry currentShape;
		VideoEntry currentVideo;
		ImageEntry currentImage;
		PolygonEntry currentPolygon;
		AudioEntry currentAudio;
		
		currentSlide = presentation.slideList.get(0);
		
		assertEquals(0, currentSlide.getSlideID());
		assertEquals(5, currentSlide.getSlideDuration());
		assertEquals(2, currentSlide.getSlideNext());
		
		currentText = currentSlide.textList.get(0);
		
		assertEquals(0, currentText.getTextStartTime());
		assertEquals(-1, currentText.getTextDuration());
		assertEquals(0.1f, currentText.getTextXStart(), 0.0f);
		assertEquals(1, currentText.getTextYStart(), 0.0f);
		assertEquals("Impact", currentText.getTextFont());
		assertEquals(30, currentText.getTextFontSize());
		assertEquals("123457", currentText.getTextFontColour());		
		assertEquals("I am so happy, the <b>Bold</b> text is finaly working. And so is the <i>Italic one</i>.", currentText.getTextContent());
		assertEquals(true, currentText.getTextInteractable());
		assertEquals(43, currentText.getTextTargetSlide());
		
		currentShape = currentSlide.shapeList.get(0);
		
		assertEquals(2, currentShape.getShapeStartTime());
		assertEquals(-1, currentShape.getShapeDuration());
		assertEquals(0.55f, currentShape.getShapeXStart(), 0.0f);
		assertEquals(0.6f, currentShape.getShapeYStart(), 0.0f);
		assertEquals("circle", currentShape.getShapeType());
		assertEquals(0.4f, currentShape.getShapeWidth(), 0.0f);
		assertEquals(0.3f, currentShape.getShapeHeight(), 0.0f);
		assertEquals("777777", currentShape.getShapeLineColour());
		assertEquals("555454", currentShape.getShapeFillColour());
		assertEquals(false, currentShape.getShapeInteractable());
		assertEquals(-10, currentShape.getShapeTargetSlide());
		assertEquals(0.6f, currentShape.getShapeShadeX1(), 0.0f);
		assertEquals(0.9f, currentShape.getShapeShadeY1(), 0.0f);
		assertEquals("00FF00", currentShape.getShapeShadeColour1());
		assertEquals(0.8f, currentShape.getShapeShadeX2(), 0.0f);
		assertEquals(0.65f, currentShape.getShapeShadeY2(), 0.0f);
		assertEquals("FF0000", currentShape.getShapeShadeColour2());
		
		currentText = currentSlide.textList.get(1);
		
		assertEquals(3, currentText.getTextStartTime());
		assertEquals(1, currentText.getTextDuration());
		assertEquals(0.55f, currentText.getTextXStart(), 0.0f);
		assertEquals(0.55f, currentText.getTextYStart(), 0.0f);
		assertEquals("Arial", currentText.getTextFont());
		assertEquals(12, currentText.getTextFontSize());
		assertEquals("987654", currentText.getTextFontColour());		
		assertEquals("Click <i>here</i> to show the module choices", currentText.getTextContent());
		assertEquals(false, currentText.getTextInteractable());
		assertEquals(-10, currentText.getTextTargetSlide());
		
		currentSlide = presentation.slideList.get(1);
		
		assertEquals(1, currentSlide.getSlideID());
		assertEquals(-10, currentSlide.getSlideDuration());
		assertEquals(-10, currentSlide.getSlideNext());
		
		currentVideo = currentSlide.videoList.get(0);
		
		assertEquals(0, currentVideo.getVideoStartTime());
		assertEquals(-1, currentVideo.getVideoDuration());
		assertEquals(0.3f, currentVideo.getVideoXStart(), 0.0f);
		assertEquals(0.2f, currentVideo.getVideoYStart(), 0.0f);
		assertEquals("video\\Film.mp4", currentVideo.getVideoSourceFile());
		assertEquals(true, currentVideo.getVideoLoop());
		
		currentSlide = presentation.slideList.get(2);
		
		assertEquals(2, currentSlide.getSlideID());
		assertEquals(-10, currentSlide.getSlideDuration());
		assertEquals(-10, currentSlide.getSlideNext());
		
		currentText = currentSlide.textList.get(0);
		
		assertEquals(0, currentText.getTextStartTime());
		assertEquals(-1, currentText.getTextDuration());
		assertEquals(0.3f, currentText.getTextXStart(), 0.0f);
		assertEquals(0.4f, currentText.getTextYStart(), 0.0f);
		assertEquals("Arial", currentText.getTextFont());
		assertEquals(28, currentText.getTextFontSize());
		assertEquals("987654", currentText.getTextFontColour());		
		assertEquals("This Andy Marvin module was rated 5 stars!", currentText.getTextContent());

		currentImage = currentSlide.imageList.get(0);
		
		assertEquals(0, currentImage.getImageStartTime());
		assertEquals("imgs\\companyLogo.jpg", currentImage.getImageSourceFile());
		assertEquals(-1, currentImage.getImageDuration());
		assertEquals(0.2f, currentImage.getImageXStart(), 0.0f);
		assertEquals(0.2f, currentImage.getImageYStart(), 0.0f);
		assertEquals(0.6f, currentImage.getImageWidth(), 0.0f);
		assertEquals(0.6f, currentImage.getImageHeight(), 0.0f);
		
		currentPolygon = currentSlide.polygonList.get(0);
		
		assertEquals(0, currentPolygon.getPolygonStartTime());
		assertEquals("starShape.csv", currentPolygon.getPolygonSourceFile());
		assertEquals(-1, currentPolygon.getPolygonDuration());
		assertEquals("FF5500", currentPolygon.getPolygonLineColour());
		assertEquals("010101", currentPolygon.getPolygonFillColour());
		assertEquals(0.6f, currentPolygon.getPolygonShadeX1(), 0.0f);
		assertEquals(0.9f, currentPolygon.getPolygonShadeY1(), 0.0f);
		assertEquals("00FF00", currentPolygon.getPolygonShadeColour1());
		assertEquals(0.8f, currentPolygon.getPolygonShadeX2(), 0.0f);
		assertEquals(0.65f, currentPolygon.getPolygonShadeY2(), 0.0f);
		assertEquals("FF0000", currentPolygon.getPolygonShadeColour2());
		
		currentSlide = presentation.slideList.get(3);
		
		assertEquals(3, currentSlide.getSlideID());
		assertEquals(8, currentSlide.getSlideDuration());
		assertEquals(-1, currentSlide.getSlideNext());
		
		currentAudio = currentSlide.audioList.get(0);
		
		assertEquals(2, currentAudio.getAudioStartTime());
		assertEquals(6, currentAudio.getAudioDuration());
		assertEquals("ping.wav", currentAudio.getAudioSourceFile());
		assertEquals(false, currentAudio.getAudioLoop());
		
		currentPolygon = currentSlide.polygonList.get(0);
		
		assertEquals(23, currentPolygon.getPolygonStartTime());
		assertEquals("iceShape.csv", currentPolygon.getPolygonSourceFile());
		assertEquals(2, currentPolygon.getPolygonDuration());
		assertEquals("F5F6F7", currentPolygon.getPolygonLineColour());
		assertEquals("888888", currentPolygon.getPolygonFillColour());
		assertEquals(-10, currentPolygon.getPolygonShadeX1(), 0.0f);
		assertEquals(-10, currentPolygon.getPolygonShadeY1(), 0.0f);
		assertEquals(null, currentPolygon.getPolygonShadeColour1());
		assertEquals(-10, currentPolygon.getPolygonShadeX2(), 0.0f);
		assertEquals(-10, currentPolygon.getPolygonShadeY2(), 0.0f);
		assertEquals(null, currentPolygon.getPolygonShadeColour2());
	}
}
