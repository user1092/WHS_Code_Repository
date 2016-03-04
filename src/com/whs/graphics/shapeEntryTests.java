package com.whs.graphics;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.whs.graphics.shapeEntry;

public class shapeEntryTests{
	int startTime, duration = 0;
	float xStart, yStart = 0.1f;
	String type = "circle";
	float width, height = 0.2f;
	shapeEntry circle;
	
	@Before
	public void setup() {
		circle = new shapeEntry(startTime, duration, xStart, yStart, height, width);
	}
	
	@Test
	public void correctParameters() {
		assertEquals(startTime, circle.startTime);
		assertEquals(duration, circle.duration);
		assertEquals(type, circle.type);
		assertEquals(height, circle.height, 0);
	}	
}