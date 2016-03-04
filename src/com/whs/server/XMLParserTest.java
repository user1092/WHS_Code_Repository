package com.whs.server;

import static org.junit.Assert.*;

import org.junit.Test;

public class XMLParserTest 
{
	@Test
	public void ExtractPresentationInfo()
	{		
		XMLParser parser = new XMLParser();		
		PresentationClass presentation;
		presentation = parser.parsePresention("src/test_file.xml");
		
		assertEquals("Your Choice", presentation.getPresentationTitle());
		assertEquals("Woolly Hat Software", presentation.getPresentationAuthor());
		assertEquals("v1.0", presentation.getPresentationVersion());
		
		assertEquals("DDFFDD", presentation.getDefaultBackgroundColour());
		assertEquals("Arial", presentation.getDefaultFont());
		assertEquals("12", presentation.getDefaultFontSize());
		assertEquals("000000", presentation.getDefaultFontColour());
		assertEquals("FF0000", presentation.getDefaultLineColour());
		assertEquals("225533", presentation.getDefaultFillColour());
	}
	
	@Test
	public void ExtractSlideInfo()
	{
		XMLParser parser = new XMLParser();
		PresentationClass presentation = new PresentationClass();		
		presentation = parser.parsePresention("src/test_file.xml");
		
		SlideClass currentSlide;
		TextClass currentText;
		ShapeClass currentShape;
		VideoClass currentVideo;
		ImageClass currentImage;
		PolygonClass currentPolygon;
		
		currentSlide = presentation.slideList.get(0);
		
		assertEquals("0", currentSlide.getSlideID());
		assertEquals("5", currentSlide.getSlideDuration());
		assertEquals("2", currentSlide.getSlideNext());
		
		currentText = currentSlide.textList.get(0);
		
		assertEquals("0", currentText.getTextStartTime());
		assertEquals("-1", currentText.getTextDuration());
		assertEquals("0.1", currentText.getTextXStart());
		assertEquals("1", currentText.getTextYStart());
		assertEquals("Impact", currentText.getTextFont());
		assertEquals("30", currentText.getTextFontSize());
		assertEquals("000000", currentText.getTextFontColour());		
		assertEquals("I am so happy, the <b>Bold</b> text is finaly working. And so is the <i>Italic one</i>.", currentText.getTextContent());
		assertEquals(true, currentText.getTextInteractable());
		assertEquals(43, currentText.getTextTargetSlide());
		
		currentShape = currentSlide.shapeList.get(0);
		
		assertEquals("2", currentShape.getShapeStartTime());
		assertEquals("-1", currentShape.getShapeDuration());
		assertEquals("0.55", currentShape.getShapeXStart());
		assertEquals("0.6", currentShape.getShapeYStart());
		assertEquals("circle", currentShape.getShapeType());
		assertEquals("0.4", currentShape.getShapeWidth());
		assertEquals("0.3", currentShape.getShapeHeight());
		assertEquals(null, currentShape.getShapeLineColour());
		assertEquals(null, currentShape.getShapeFillColour());
		assertEquals(false, currentShape.getShapeInteractable());
		assertEquals(-10, currentShape.getShapeTargetSlide());
		
		currentText = currentSlide.textList.get(1);
		
		assertEquals("3", currentText.getTextStartTime());
		assertEquals("1", currentText.getTextDuration());
		assertEquals("0.55", currentText.getTextXStart());
		assertEquals("0.55", currentText.getTextYStart());
		assertEquals(null, currentText.getTextFont());
		assertEquals(null, currentText.getTextFontSize());
		assertEquals(null, currentText.getTextFontColour());		
		assertEquals("Click <i>here</i> to show the module choices", currentText.getTextContent());
		assertEquals(false, currentText.getTextInteractable());
		assertEquals(-10, currentText.getTextTargetSlide());
		
		currentSlide = presentation.slideList.get(1);
		
		assertEquals("1", currentSlide.getSlideID());
		assertEquals(null, currentSlide.getSlideDuration());
		assertEquals(null, currentSlide.getSlideNext());
		
		currentVideo = currentSlide.videoList.get(0);
		
		assertEquals("0", currentVideo.getVideoStartTime());
		assertEquals("-1", currentVideo.getVideoDuration());
		assertEquals("0.3", currentVideo.getVideoXStart());
		assertEquals("0.2", currentVideo.getVideoYStart());
		assertEquals("video\\Film.mp4", currentVideo.getVideoSourceFile());
		assertEquals("true", currentVideo.getVideoLoop());
		
		currentSlide = presentation.slideList.get(2);
		
		assertEquals("2", currentSlide.getSlideID());
		
		currentText = currentSlide.textList.get(0);
		
		assertEquals("0", currentText.getTextStartTime());
		assertEquals("-1", currentText.getTextDuration());
		assertEquals("0.3", currentText.getTextXStart());
		assertEquals("0.4", currentText.getTextYStart());
		assertEquals(null, currentText.getTextFont());
		assertEquals(null, currentText.getTextFontSize());
		assertEquals(null, currentText.getTextFontColour());		
		assertEquals("This Andy Marvin module was rated 5 stars!", currentText.getTextContent());	

		currentImage = currentSlide.imageList.get(0);
		
		assertEquals("0", currentImage.getImageStartTime());
		assertEquals("imgs\\companyLogo.jpg", currentImage.getImageSourceFile());
		assertEquals("-1", currentImage.getImageDuration());
		assertEquals("0.2", currentImage.getImageXStart());
		assertEquals("0.2", currentImage.getImageYStart());
		assertEquals("0.6", currentImage.getImageWidth());
		assertEquals("0.6", currentImage.getImageHeight());
		
		currentPolygon = currentSlide.polygonList.get(0);
		
		assertEquals("0", currentPolygon.getPolygonStartTime());
		assertEquals("starShape.csv", currentPolygon.getPolygonSourceFile());
		assertEquals("-1", currentPolygon.getPolygonDuration());
		assertEquals("FF5500", currentPolygon.getPolygonLineColour());
		assertEquals("007766", currentPolygon.getPolygonFillColour());
	}
}
