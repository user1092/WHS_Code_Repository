/**
 * polygonEntryTests.java	v0.1 04/03/16
 * 
 * Copyright Woolly Hat Software
 */
package whs.yourchoice.graphics;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Class that contains junit tests relating polygonEntry.
 * 
 * All shapes are drawn from top left corner
 * 
 * @author ws659, rw1065
 * 
 * @version	v0.1 04/03/16
 * 
 */
public class PolygonGraphicTests{
	int startTime, duration = 0;
	float[] x = {0.25f, 0.5f, 0.15f};
	float[] y = {0.25f, 0.5f, 0.35f};
	PolygonGraphic polygon;
	String sourceFile = "/Users/williamsharrard/Desktop/ArrowShape.csv";
	
	/**
	 * setup
	 */
	@Before
	public void setup() {
		polygon = new PolygonGraphic(startTime, duration, x, y, sourceFile);
	}
	
	/**
	 * Assert that values are stored withing the shapeEntry object
	 */
	@Test
	public void correctParameters() {
		assertEquals(startTime, polygon.startTime);
		assertEquals(duration, polygon.duration);
		assertEquals(x, polygon.x);
	}
	
	/**
	 * Assert that values are stored withing the shapeEntry object
	 */
	@Test
	public void correctShadingParameters() {
		polygon.setShading(0.02f, 0.03f, 0.72f, 0.81f, "00aaff", "ddeeaa");
		assertEquals(0.02f, polygon.shadingX1, 0.001);
		assertEquals(true, polygon.shading);
	}
	
	/**
	 * Assert CSV values are read correctly
	 */
	@Test
	public void correctCSVParsing(){
		try {
			polygon.parseCSV();
		}
		catch (Exception e) {
			System.out.println("File cannot be found.");
		}
		
	}
	
}
