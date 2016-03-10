/**
 * polygonEntry.java	v0.1 04/03/16
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.graphics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import java.util.Scanner;

/**
 * Class that contains information relating to a polygon that is to be drawn
 * and can return a group containing a polygon.
 * 
 * @author ws659, rw1065
 * 
 * @version	v0.1 04/03/16
 * 
 */

public class PolygonGraphic {
	//finals for window size
	private int gui_height = 600;
	private int gui_width = 600;
	//remaining variables
	protected int startTime, duration;
	protected float[] x, y;
	protected String lineColourString, fillColourString;
	protected Pane group = new Pane();
	protected Color fillColour;
	protected Color lineColour;
	protected float shadingX1, shadingX2;
	protected float shadingY1, shadingY2;
	protected String shadingStringColour1, shadingStringColour2;
	protected Color shadingColour1, shadingColour2;
	protected boolean shading;
	protected String sourceFile;
	protected List<Float> xList = new ArrayList<Float>();
	protected List<Float> yList = new ArrayList<Float>();
	
	/**
	 * Constructor containing the minimal arguments to create a valid shapeEntry.
	 * 
	 */
	public PolygonGraphic(int inStart, int inDuration, float[] inX, float[] inY, String inSourceFile) {
		this(inStart, inDuration, inX, inY, "000000", "FALSE", inSourceFile);
	}
	
	/**
	 *  Constructor with optional arguments for fill colour.
	 */
	public PolygonGraphic(int inStart, int inDuration, float[] inX, float[] inY, String inFillColour, String inSourceFile) {
		this(inStart, inDuration, inX, inY, inFillColour, "FALSE", inSourceFile);
	}
	
	/**
	 *  Constructor with all optional arguments: shape type, shape colour and shape outline colour.
	 */
	public PolygonGraphic(int inStart, int inDuration, float[] inX, float[] inY, String inFillColour, String inLineColour, String inSourceFile) {
		// assign variables
		this.startTime = inStart;
		this.duration = inDuration;
		this.x = inX;
		this.y = inY;
		this.lineColourString = inLineColour;
		this.fillColourString = inFillColour;
		this.sourceFile = inSourceFile;
		
		//set outer line colour, default same as fill colour
		if (lineColourString.equals("FALSE")){
			lineColour = setColour(fillColourString);
		}
		else {
			lineColour = setColour(lineColourString);
		}
		fillColour = setColour(fillColourString);
		populateList();
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
	 * Populates an array list with x y values
	 */
	private void populateList() {
		for (int i = 0; i < x.length; i++) {
			xList.add(x[i]);
			yList.add(y[i]);
		}
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
	 * A method that draws a rectangle and adds it to the group
	 * @param rounded	Sets rounded edges
	 * @param x	x pixel posistion of shape
	 * @param y	y pixel posistion of shape
	 * @param h 	height of shape
	 * @param w		width of shape
	 * @returns group	Reutrns pane with drawn shape
	 */
	public Pane drawPolygon() {
		List<Float> pixelX = new ArrayList<Float>();
		List<Float> pixelY = new ArrayList<Float>();
		
		for (int i = 0; i < xList.size(); i++) {
			pixelX.add(getPixel("X", xList.get(i)));
			pixelY.add(getPixel("Y", yList.get(i)));
		}
		Polygon p = new Polygon();
		
		for (int i = 0; i < pixelX.size(); i++) {
			p.getPoints().add((double) pixelX.get(i));
			p.getPoints().add((double) pixelY.get(i));
		}
		
		// set line and fill colours
		if (shading == true) {
			p.setFill(drawShading());
		}
		else {
			p.setFill(fillColour);
		}
		p.setStroke(lineColour);
		
		//add rectangle to group
		group.getChildren().add(p);
		
		return group;
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
	
	/**
	 * function that reads a csv file and parses it
	 * @throws FileNotFoundException
	 */
	public void parseCSV() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(sourceFile));
		scanner.useDelimiter(",");
		xList = new ArrayList<Float>();
		yList = new ArrayList<Float>();
		
		// parse the CSV file
		while (scanner.hasNext()) {
			xList.add(scanner.nextFloat());
			// if there are an odd number of points
			if (scanner.hasNextFloat()) {
				yList.add(scanner.nextFloat());
			}
			else {
				throw new IllegalArgumentException("Invalid CSV file, expected an even number of data points.");
			}
		}
		scanner.close();
	}
}
