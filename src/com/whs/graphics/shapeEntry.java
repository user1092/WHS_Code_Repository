/**
 * 
 */

package com.whs.graphics;

import javafx.scene.Group;
import javafx.scene.shape.*;

/**
 * @author ws659, rw1065
 *
 * 
 */

public class shapeEntry {
	//finals for window size
	private static final int GUI_HEIGHT = 600;
	private static final int GUI_WIDTH = 600;
	
	protected int startTime, duration;
	protected float xStart, yStart;
	protected String type;
	protected float width, height;
	protected String lineColour, fillColour;
	protected Group group = new Group();
	
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
		
		//default type is circle
		this.type = "circle";
	}
	
	public shapeEntry(int inStart, int inDuration, float inX, float inY, float inH, float inW, String type) {
		this.startTime = inStart;
		this.duration = inDuration;
		this.xStart = inX;
		this.yStart = inY;
		this.height = inH;
		this.width = inW;
		this.type = type;
		validateType();
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
		
		if (axis.equals("Y")) {
			pixel = ((GUI_HEIGHT * yStart)/2);
		}
		else if (axis.equals("X")) {
			pixel = ((GUI_WIDTH * xStart)/2);
		}
		else if (axis.equals("H")) {
			pixel = ((GUI_WIDTH * height)/2);
		}
		else if (axis.equals("W")) {
			pixel = ((GUI_HEIGHT * width)/2);
		}
		else {
			pixel = 0;
			throw new IllegalArgumentException("Invalid axis type, expected on of: 'H', 'W', 'X' or 'Y'");
		}
		return pixel;
	}
	
	public Group drawShape() {
		float x = getPixel("X");
		float y = getPixel("Y");
		float h = getPixel("H");
		float w = getPixel("W");
		
		switch (type) {
		case "circle":
			drawCircle(x, y);
			break;
		case "rectangle":
			drawRectangle(false, x, y, h, w);
			break;
		case "roundedRectangle":
			drawRectangle(true, x, y, h, w);
			break;
		default:
			throw new IllegalArgumentException("Invalid type, expected one of: 'circle', 'rectangle' or 'roundedRectangle'");
		}
		
		return this.group;
	}

	private void drawRectangle(boolean rounded, float x, float y, float h, float w) {
		Rectangle r = new Rectangle();
		r.setX(x);
		r.setY(y);
		r.setWidth(w);
		r.setHeight(h);
		if (rounded) {
			r.setArcHeight(20);
			r.setArcWidth(20);
		}
		group.getChildren().add(r);
	}
	
	/*
	 * 
	 */
	private void drawCircle(float x, float y) {
		Circle c = new Circle();
		c.setCenterX(x);
		c.setCenterY(y);
		
		c.setRadius(width*GUI_WIDTH);
		group.getChildren().add(c);
	}
	
	
}