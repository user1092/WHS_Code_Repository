package com.whs.server;

public class PolygonClass 
{
	protected String polygonStartTime = null;
	protected String polygonSourceFile = null;
	protected String polygonDuration = null;
	protected String polygonLineColour = null;
	protected String polygonFillColour = null;
	
	
	protected void setPolygonStartTime(String contents) 
	{
		polygonStartTime = contents;		
	}

	protected String getPolygonStartTime() 
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
		polygonDuration = contents;		
	}

	protected String getPolygonDuration() 
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
