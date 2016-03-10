/*
* (C) Stammtisch
* First version created by: Joseph Ingleby & Callum Silver
* Date of first version: 22nd February 2016
* 
* Last version by: Joseph Ingleby & Callum Silver
* Date of last update: 22nd February 2016
* Version number: 1.0
* 
* Commit date: 22nd February 2016
* Description: This class holds the information for imagefiles received from the xml document.
*/

package stammtisch.Objects;

public class Images
{
	private String sourceFile;
	private int startTime, duration;
	private double xStart, yStart, height, width;
	
	public Images(String mySourceFile, int myStartTime, int myDuration, double myXStart, double myYStart, double myHeight, double myWidth)
	{
		sourceFile = mySourceFile;
		startTime = myStartTime;
		duration = myDuration; 
		xStart = myXStart; 
		yStart = myYStart; 
		height = myHeight; 
		width = myWidth;
	}
	
	public Images() {
		super();
	}

	public String getSourceFile() {
		return sourceFile;
	}
	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}
	public int getStartTime() {
		return startTime;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	
	public void setStartTime(String startTime) 
	{
		this.startTime = Integer.parseInt(startTime);
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public void setDuration(String duration) {
		try
		{
			this.duration = Integer.parseInt(duration);
		}
		catch(Exception e)
		{
			this.duration = 0;
		}
	}
	
	public double getxStart() {
		return xStart;
	}
	
	public void setxStart(String xStart)
	{
		this.xStart = Double.parseDouble(xStart);
	}
	
	public void setxStart(double xStart) {
		this.xStart = xStart;
	}
	public double getyStart() {
		return yStart;
	}
	public void setyStart(double yStart) {
		this.yStart = yStart;
	}
	
	public void setyStart(String yStart)
	{
		this.yStart = Double.parseDouble(yStart);
	}
	
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	
	public void setHeight(String height)
	{
		this.height = Double.parseDouble(height);
	}
	
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public void setWidth(String width)
	{
		this.width = Double.parseDouble(width);
	}
}

