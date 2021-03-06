/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */
package whs.yourchoice.graphics;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import whs.yourchoice.graphics.ShapeGraphic;

/**
 * Class that contains junit tests relating shapeEntry.
 * 
 * All shapes are drawn from top left corner
 * 
 * NOT FOR RELEASE!
 * 
 * @author ws659, rw1065
 * @version	v0.1 04/03/16
 */
public class ShapeGraphicTests{
	int startTime, duration = 0;
	float xStart, yStart = 0.1f;
	String type = "circle";
	float width, height = 0.2f;
	ShapeGraphic circle;
	
	/**
	 * setup
	 */
	@Before
	public void setup() {
		circle = new ShapeGraphic(startTime, duration, xStart, yStart, height, width);
	}
	
	/**
	 * Assert that values are stored withing the shapeEntry object
	 */
	@Test
	public void correctParameters() {
		assertEquals(startTime, circle.startTime);
		assertEquals(duration, circle.duration);
		assertEquals(type, circle.type);
		assertEquals(height, circle.height, 0);
	}
	
	/**
	 * Assert that values are stored withing the shapeEntry object
	 */
	@Test
	public void correctShadingParameters() {
		circle.setShading(0.02f, 0.03f, 0.72f, 0.81f, "00aaff", "ddeeaa");
		assertEquals(0.02f, circle.shadingX1, 0.001);
		assertEquals(true, circle.shading);
	}
}
