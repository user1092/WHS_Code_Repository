package com.whs.server;

public class PolygonClass 
{
	protected int polygonStartTime = -10;
	protected String polygonSourceFile = null;
	protected int polygonDuration = -10;
	protected String polygonLineColour = null;
	protected String polygonFillColour = null;
	
	private boolean interactable = false;
	private int targetSlide = -10;
	
	
	protected void setPolygonTargetSlide(String i)
	{
		targetSlide = Integer.parseInt(i);
	}
	
	protected int getTextTargetSlide()
	{
		return targetSlide;
	}
	
	protected void setPolygonInteractable(boolean b)
	{
		interactable = b;
	}
	
	protected boolean getPolygonInteractable()
	{
		return interactable;
	}
	
	protected void setPolygonStartTime(String contents) 
	{
		polygonStartTime = Integer.parseInt(contents);
	}

	protected int getPolygonStartTime() 
	{
		return polygonStartTime;
	}
	
	protected void setPolygonSourceFile(String contents) 
	{
		polygonSourceFile = contents;		
	}

	protected String getPolygonSourceFile() 
	{
		return polygonSourceFile;
	}
	
	protected void setPolygonDuration(String contents) 
	{
		polygonDuration = Integer.parseInt(contents);
	}

	protected int getPolygonDuration() 
	{
		return polygonDuration;
	}
	
	protected void setPolygonLineColour(String contents) 
	{
		polygonLineColour = contents;		
	}

	protected String getPolygonLineColour() 
	{
		return polygonLineColour;
	}
	
	protected void setPolygonFillColour(String contents) 
	{
		polygonFillColour = contents;		
	}

	protected String getPolygonFillColour() 
	{
		return polygonFillColour;
	}
}
