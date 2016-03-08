/**
 * polygonEntryTests.java	v0.1 04/03/16
 * 
 * Copyright Woolly Hat Software
 */
package com.whs.graphics;

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
public class PolygonEntryTests{
	int startTime, duration = 0;
	float[] x = {0.25f, 0.5f, 0.15f};
	float[] y = {0.25f, 0.5f, 0.35f};
	PolygonEntry polygon;
	String sourceFile = "csv/polygon.csv";
	
	/**
	 * setup
	 */
	@Before
	public void setup() {
		polygon = new PolygonEntry(startTime, duration, x, y, sourceFile);
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
}
