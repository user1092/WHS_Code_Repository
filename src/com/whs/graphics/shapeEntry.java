/**
 * 
 */

package com.whs.graphics;

import javafx.scene.Group;
import javafx.scene.shape.Shape;

/**
 * @author ws659
 *
 * 
 */

public class shapeEntry {
	//finals for window size
	private static final int GUI_HEIGHT = 600;
	private static final int GUI_WIDTH = 800;
	
	protected int startTime, duration;
	protected float xStart, yStart;
	protected String type;
	protected float width, height;
	protected String lineColour, fillColour;
	
	//constructor for required fields only
	/*
	 * @TODO sort out all constructors
	 */
	public shapeEntry(int inStart, int inDuration, float inX, float inY, float inH, float inW) {
		this.startTime = inStart;
		this.duration = inDuration;
		this.xStart = inX;
		this.yStart = inY;
		this.height = inH;
		this.width = inW;
		
		//fill out some defaults
		
	}
	
	private boolean validateType() {
		boolean valid;
		
		if (type.equals("circle") || type.equals("rectangle") || type.equals("roundedRectangle")) {
			valid = true;
		}
		else {
			valid = false;
		}
		
		return valid;
	}

	private int getPixel(String axis) {
		int pixel;
		
		if (axis.equals("H")) {
			pixel = (int) (GUI_HEIGHT * yStart);
		}
		else if (axis.equals("W")) {
			pixel = (int) (GUI_WIDTH * xStart);
		}
		else {
			pixel = 0;
			// maybe throw some exception here
		}
		
		return pixel;
	}
	
	public void drawShape(Group group) {
		switch (type) {
		case "circle":
			//draw a circle dum dum
			break;
		case "rectangle":
			//draw a rectangle dum dum
			break;
		case "roundedRectangle":
			//draw a rounded rectangle dum dum
			break;
		default:
			//figure something out
			break;
		}
		
	}

	
	
	
}