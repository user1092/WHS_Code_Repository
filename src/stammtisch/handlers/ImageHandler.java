/*
* (C) Stammtisch
* First version created by: Joseph Ingleby & Callum Silver
* Date of first version: 2nd March 2016
* 
* Last version by: Joseph Ingleby & Callum Silver
* Date of last update: 3rd March 2016
* Version number: 2.0
* 
* Commit date: 3rd March 2016
* Description: This class holds takes an Images Object and returns a canvas containing the image in 
* the correct position.
*/
package stammtisch.handlers;

// Imports
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import stammtisch.Objects.Images;

public class ImageHandler
{
	private Images imageToHandle;
	
	//constructors
	public ImageHandler()
	{
		super();
	}
	
	//returns a canvas that contains the image
	public Canvas drawCanvas(Images imageToHandle , int canvasSizeX, int canvasSizeY) 
	{
		this.imageToHandle = imageToHandle;
		
		// set up a new canvas
		Canvas imageCanvas = new Canvas(canvasSizeX, canvasSizeY);
		GraphicsContext gContext = imageCanvas.getGraphicsContext2D();
		Image image = generateImage();
		// draw image (image, x start, y start, width, height)
		gContext.drawImage(image, getXPosition(canvasSizeX), getYPosition(canvasSizeY), getWidth(canvasSizeX), getHeight(canvasSizeY));
		
		return imageCanvas;
	}
	
	//this method gets the starting X position of the image for use in the canvas
	public double getXPosition(int sizeOfCanvasX)
	{
		return (sizeOfCanvasX*imageToHandle.getxStart());
	}

	//this method gets the starting Y position of the image for use in the canvas
	public double getYPosition(int sizeOfCanvasY)
	{
		return (sizeOfCanvasY*imageToHandle.getyStart());
	}
	
	//this method gets the width of the image for use in the canvas
	public double getWidth(int sizeOfCanvasX)
	{
		return ((sizeOfCanvasX*imageToHandle.getWidth()));
	}

	//this method gets the height of the image for use in the canvas
	public double getHeight(int sizeOfCanvasY)
	{
		return ((sizeOfCanvasY*imageToHandle.getHeight()));
	}
	
	//PATCH:  30th May 2016 added in the "file:" string to allow for non repository images.
	//this method will read the source file and create the image
	private Image generateImage() 
	{
		return new Image("file:" + imageToHandle.getSourceFile());
	}

}
