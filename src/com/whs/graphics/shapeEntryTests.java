/**
 * shapeEntryTests.java	v0.1 04/03/16
 * 
 * Copyright Woolly Hat Software
 */
package com.whs.graphics;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.whs.graphics.shapeEntry;

/**
 * Class that contains junit tests relating shapeEntry.
 * 
 * All shapes are drawn from top left corner
 * 
 * @author ws659, rw1065
 * 
 * @version	v0.1 04/03/16
 * 
 */
public class shapeEntryTests{
	int startTime, duration = 0;
	float xStart, yStart = 0.1f;
	String type = "circle";
	float width, height = 0.2f;
	shapeEntry circle;
	
	/**
	 * setup
	 */
	@Before
	public void setup() {
		circle = new shapeEntry(startTime, duration, xStart, yStart, height, width);
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