/**
 * 
 */

package com.whs.graphics;

import javafx.scene.Group;
import javafx.scene.shape.*;

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
			throw new IllegalArgumentException("Invalid type, expected one of: 'circle', 'rectangle' or 'roundedRectangle'");
		}
		
		return valid;
	}

	private float getPixel(String axis) {
		float pixel;
		
		if (axis.equals("H")) {
			pixel = (GUI_HEIGHT * yStart);
		}
		else if (axis.equals("W")) {
			pixel = (GUI_WIDTH * xStart);
		}
		else {
			pixel = 0;
			throw new IllegalArgumentException("Invalid axs type, expected on of: 'H' or 'W'");
		}
		
		return pixel;
	}
	
	public void drawShape(Group group) {
		float x = getPixel("W");
		float y = getPixel("H");
		
		switch (type) {
		case "circle":
			drawCircle(group, x, y);
			break;
		case "rectangle":
			drawRectangle(group, false, x, y);
			break;
		case "roundedRectangle":
			drawRectangle(group, true, x, y);
			break;
		default:
			throw new IllegalArgumentException("Invalid type, expected one of: 'circle', 'rectangle' or 'roundedRectangle'");
		}
	}

	private void drawRectangle(Group group, boolean rounded, float x, float y) {
		Rectangle r = new Rectangle(x, y, width, height);
		
		if (rounded) {
			r.setArcHeight(20);
			r.setArcWidth(20);
		}
		
		group.getChildren().add(r);	
	}
	
	private void drawCircle(Group group, float x, float y) {
		Circle c = new Circle(x, y, width);
		
		group.getChildren().add(c);
	}
	
	
}