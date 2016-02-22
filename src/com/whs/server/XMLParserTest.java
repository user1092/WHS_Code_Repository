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
		assertEquals("2222AA", presentation.getDefaultFillColour());
	}
	
	@Test
	public void ExtractSlideInfo()
	{
		XMLParser parser = new XMLParser();		
		PresentationClass presentation = new PresentationClass();		
		presentation = parser.parsePresention("src/test_file.xml");
		
		SlideClass currentSlide = presentation.slideList.get(0);
		
		assertEquals("0", currentSlide.getSlideID());
		assertEquals("5", currentSlide.getSlideDuration());
		assertEquals("2", currentSlide.getSlideNext());
		
		TextClass currentText = currentSlide.textList.get(0);
		
		assertEquals("0", currentText.getTextStartTime());
		assertEquals("-1", currentText.getTextDuration());
		assertEquals("0.1", currentText.getTextXStart());
		assertEquals("1", currentText.getTextYStart());
		assertEquals("Impact", currentText.getTextFont());
		assertEquals("30", currentText.getTextFontSize());
		assertEquals("000000", currentText.getTextFontColour());		
		assertEquals("Hello, this is a text", currentText.getTextContent());
		
		ShapeClass currentShape = currentSlide.shapeList.get(0);
		
		assertEquals("2", currentShape.getShapeStartTime());
		assertEquals("-1", currentShape.getShapeDuration());
		assertEquals("0.4", currentShape.getShapeXStart());
		assertEquals("0.4", currentShape.getShapeYStart());
		assertEquals("0.4", currentShape.getShapeType());
		assertEquals("0.4", currentShape.getShapeWidth());
		assertEquals("0.4", currentShape.getShapeHeight());
		assertEquals("0.4", currentShape.getShapeLineColour());
		assertEquals("0.4", currentShape.getShapeFillColour());

	}
}
