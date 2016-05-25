/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.graphics;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;

/**
 * Class that contains information relating to a shape that is to be drawn
 * and can return a group containing a shape.
 * 
 * @author ws659, rw1065
 * 
 * @version	v0.1 04/03/16
 * 
 */

public class ShapeGraphic {
	//finals for window size
	private int gui_height = 600;
	private int gui_width = 600;
	//remaining variables
	protected int startTime, duration;
	protected float xStart, yStart;
	protected String type;
	protected float width, height;
	protected String lineColourString, fillColourString;
	protected Pane group = new Pane();
	protected Color fillColour;
	protected Color lineColour;
	protected float shadingX1, shadingX2;
	protected float shadingY1, shadingY2;
	protected String shadingStringColour1, shadingStringColour2;
	protected Color shadingColour1, shadingColour2;
	protected boolean shading;
	
	
	/**
	 * Constructor containing the minimal arguments to create a valid shapeEntry.
	 * 
	 */
	public ShapeGraphic(int inStart, int inDuration, float inX, float inY, float inH, float inW) {
		this(inStart, inDuration, inX, inY, inH, inW, "circle", "000000", "FALSE");
	}
	
	/**
	 *  Constructor with optional argument for shape type.
	 */
	public ShapeGraphic(int inStart, int inDuration, float inX, float inY, float inH, float inW, String inType) {
		this(inStart, inDuration, inX, inY, inH, inW, inType, "000000", "FALSE");
	}
	
	/**
	 *  Constructor with optional arguments for shape type and fill colour.
	 */
	public ShapeGraphic(int inStart, int inDuration, float inX, float inY, float inH, float inW, String inType, String inFillColour) {
		this(inStart, inDuration, inX, inY, inH, inW, inType, inFillColour, "FALSE");
	}
	
	/**
	 *  Constructor with all optional arguments: shape type, shape colour and shape outline colour.
	 */
	public ShapeGraphic(int inStart, int inDuration, float inX, float inY, float inH, float inW, String inType, String inFillColour, String inLineColour) {
		// assign variables
		this.startTime = inStart;
		this.duration = inDuration;
		this.xStart = inX;
		this.yStart = inY;
		this.height = inH;
		this.width = inW;
		this.type = inType;
		this.lineColourString = inLineColour;
		this.fillColourString = inFillColour;
		
		// validate shape type
		validateType();
		
		//set outer line colour, default same as fill colour
		if (lineColourString.equals("FALSE")){
			lineColour = setColour(fillColourString);
		}
		else {
			lineColour = setColour(lineColourString);
		}
		fillColour = setColour(fillColourString);
	}
	
	/**
	 *  Returns start time of shapeEntry object.
	 *  @return startTime
	 */
	public int getStartTime() {
		return startTime;
	}
	
	/**
	 *  Returns the duration time of the shapeEntry object.
	 *  @return duration
	 */
	public int getDuration() {
		return duration;
	}
	
	/**
	 * Sets the resolution of the canvas
	 * @param width		Width of canvas in pixels
	 * @param height	Height of canvas in pixels
	 */
	public void setRes(int width, int height) {
		gui_width = width;
		gui_height = height;
	}
	
	
	/**
	 *  Validates user input shape type as a valid shape for shapeEntry object.
	 *  @return valid	Boolean result of shape type
	 *  @throws IllegalArgumentException
	 */
	private boolean validateType() {
		boolean valid;
		
		// does string equal a valid shape type under the PWS
		if (type.equals("circle") || type.equals("rectangle") || type.equals("roundedRectangle")) {
			valid = true;
		}
		else {
			valid = false;
			// if not throw exception
			throw new IllegalArgumentException("Invalid type, expected one of: 'circle', 'rectangle' or 'roundedRectangle'");
		}
		
		return valid;
	}
	
	/**
	 *	Validates that the user input for the colour string matches a 6-digit hexadecimal format
	 *	@param strColour	User string input representing a colour
	 *	@throws IllegalArgumentException
	 */
	private void checkColourString(String strColour) {
		
		// check to see if string contains hex digits
		try {
			Integer.parseInt(strColour, 16);
		}
		catch(NumberFormatException nfe) {
			throw new IllegalArgumentException("Invalid colour type, expected 6-digit hex value.");
		}
		
		// check to see if string is 6 digits long
		if (strColour.length() != 6) {
			throw new IllegalArgumentException("Invalid colour type, expected 6-digit hex value.");
		}
	}
	
	/**
	 *	Converts the user colour input string into a Color type object
	 *	@param strColour	user string input representing colour
	 *	@return c	Color object
	 */
	private Color setColour(String strColour) {
		Color c;
		
		checkColourString(strColour);
		
		c = Color.web(strColour);
		
		return c;
	}

	/**
	 * Converts a float representing a percentage of each axis into a
	 * float pixel amount.
	 * @param axis	A string representation of an axis
	 * @param percentage	A float representation of percentage.
	 * @return pixel	An float pixel position.
	 */
	private float getPixel(String axis, float percentage) {
		float pixel;
		
		if (axis.equals("Y")) {
			pixel = (gui_height * percentage);
		}
		else if (axis.equals("X")) {
			pixel = (gui_width * percentage);
		}
		else {
			pixel = 0;
			throw new IllegalArgumentException("Invalid axis type, expected on of: 'X' or 'Y'");
		}
		return pixel;
	}
	
	/**
	 * A method that parses shape type and calls a method to draw that shape
	 * @return this.group	Group containing shape
	 */
	public Pane drawShape() {
		//convert to percentages to pixels
		float x = getPixel("X", xStart);
		float y = getPixel("Y", yStart);
		float h = getPixel("Y", height);
		float w = getPixel("X", width);
		
		//select and draw shape
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

	/**
	 * A method that draws a rectangle and adds it to the group
	 * @param rounded	Sets rounded edges
	 * @param x	x pixel posistion of shape
	 * @param y	y pixel posistion of shape
	 * @param h 	height of shape
	 * @param w		width of shape
	 */
	private void drawRectangle(boolean rounded, float x, float y, float h, float w) {
		Rectangle r = new Rectangle();
		
		//set coordinates and height and width
		r.setX(x);
		r.setY(y);
		r.setWidth(w);
		r.setHeight(h);
		
		//set optional rounded edges
		if (rounded) {
			r.setArcHeight(20);
			r.setArcWidth(20);
		}
		
		// set line and fill colours
		if (shading == true) {
			r.setFill(drawShading());
		}
		else {
			r.setFill(fillColour);
		}
		r.setStroke(lineColour);
		
		//add rectangle to group
		group.getChildren().add(r);
	}
	
	/**
	 * A method that draws a circle and adds it to the group
	 * @param x	x pixel position of shape
	 * @param y	y pixel position of shape
	 */
	private void drawCircle(float x, float y) {
		Circle c = new Circle();
		
		//adjust centre point from top left point
		x = x + ((width*gui_width)/2);
		y = y + ((height*gui_height)/2);
		
		//set coordinates and radius
		c.setCenterX(x);
		c.setCenterY(y);
		c.setRadius((width*gui_width)/2);
		
		//set line and fill colours
		if (shading == true) {
			c.setFill(drawShading());
		}
		else {
			c.setFill(fillColour);
		}
		c.setStroke(lineColour);
	
		//add circle to group
		group.getChildren().add(c);
	}
	
	/**
	 * A method that sets shading properties
	 * @param x1	float variable representing a percentage on the x axis
	 * @param x2	float variable representing a percentage on the x axis
	 * @param y1	float variable representing a percentage on the y axis
	 * @param y2	float variable representing a percentage on the y axis
	 * @param colour1	String representation of colour
	 * @param colour2	String representation of colour
	 */
	public void setShading(float x1, float x2, float y1, float y2, String colour1, String colour2) {
		// set shading parameters
		this.shadingX1 = x1;
		this.shadingX2 = x2;
		this.shadingY1 = y1;
		this.shadingY2 = y2;
		this.shadingStringColour1 = colour1;
		this.shadingStringColour2 = colour2;
		this.shading = true;
		
		// convert and set colour parameters
		this.shadingColour1 = setColour(shadingStringColour1);
		this.shadingColour2 = setColour(shadingStringColour2);
	}
	
	/**
	 * A method that constructs a gradient fill
	 * @return	gradient	Gradient fill for shape.
	 */
	private LinearGradient drawShading() {
		// set all x y coordinates
		float x1 = getPixel("X", shadingX1);
		float y1 = getPixel("Y", shadingY1);
		float x2 = getPixel("X", shadingX2);
		float y2 = getPixel("Y", shadingY2);
		
		// calculate stops
		Stop[] stops = new Stop[] { new Stop(0, shadingColour1), new Stop(1, shadingColour2)};
		
		// create and return all stops
		LinearGradient gradient = new LinearGradient(x1, y1, x2, y2, false, CycleMethod.NO_CYCLE, stops);
		return gradient;
	}
}
