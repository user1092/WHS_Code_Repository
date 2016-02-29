package com.whs.server;

public class ShapeClass 
{
	protected String shapeStartTime = null;
	protected String shapeDuration = null;
	protected String shapeXStart = null;
	protected String shapeYStart = null;
	protected String shapeType = null;
	protected String shapeWidth = null;
	protected String shapeHeight = null;
	protected String shapeLineColour = null;
	protected String shapeFillColour = null;
	
	protected String shapeShadeX1 = null;
	protected String shapeShadeY1 = null;
	protected String shapeShadeColour1 = null;
	protected String shapeShadeX2 = null;
	protected String shapeShadeY2 = null;
	protected String shapeShadeColour2 = null;
	
	private boolean interactable = false;
	
	
	protected void setShapeInteractable(boolean b)
	{
		interactable = b;
	}
	
	protected boolean getShapeInteractable()
	{
		return interactable;
	}
	
	protected void setShapeStartTime(String contents) 
	{
		shapeStartTime = contents;		
	}

	protected String getShapeStartTime() 
	{
		return shapeStartTime;
	}
	
	protected void setShapeDuration(String contents) 
	{
		shapeDuration = contents;		
	}

	protected String getShapeDuration() 
	{
		return shapeDuration;
	}
	
	protected void setShapeXStart(String contents) 
	{
		shapeXStart = contents;		
	}

	protected String getShapeXStart() 
	{
		return shapeXStart;
	}
	
	protected void setShapeYStart(String contents) 
	{
		shapeYStart = contents;		
	}

	protected String getShapeYStart() 
	{
		return shapeYStart;
	}
	
	protected void setShapeType(String contents) 
	{
		shapeType = contents;		
	}

	protected String getShapeType() 
	{
		return shapeType;
	}
	
	protected void setShapeWidth(String contents) 
	{
		shapeWidth = contents;		
	}

	protected String getShapeWidth() 
	{
		return shapeWidth;
	}
	
	protected void setShapeHeight(String contents) 
	{
		shapeHeight = contents;		
	}

	protected String getShapeHeight() 
	{
		return shapeHeight;
	}
	
	protected void setShapeLineColour(String contents) 
	{
		shapeLineColour = contents;		
	}

	protected String getShapeLineColour() 
	{
		return shapeLineColour;
	}
	
	protected void setShapeFillColour(String contents) 
	{
		shapeFillColour = contents;		
	}

	protected String getShapeFillColour() 
	{
		return shapeFillColour;
	}
	
//****************************** SHADING ATTRIBUTES BELLOW ************************************
//*********************************************************************************************
	
	protected void setShapeShadeX1(String contents) 
	{
		shapeShadeX1 = contents;		
	}

	protected String getShapeShadeX1() 
	{
		return shapeShadeX1;
	}
	
	protected void setShapeShadeY1(String contents) 
	{
		shapeShadeY1 = contents;		
	}

	protected String getShapeShadeY1() 
	{
		return shapeShadeY1;
	}
	
	protected void setShapeShadeColour1(String contents) 
	{
		shapeShadeColour1 = contents;		
	}

	protected String getShapeShadeColour1() 
	{
		return shapeShadeColour1;
	}
	
	protected void setShapeShadeX2(String contents) 
	{
		shapeShadeX2 = contents;		
	}

	protected String getShapeShadeX2() 
	{
		return shapeShadeX2;
	}
	
	protected void setShapeShadeY2(String contents) 
	{
		shapeShadeY2 = contents;		
	}

	protected String getShapeShadeY2() 
	{
		return shapeShadeY2;
	}
	
	protected void setShapeShadeColour2(String contents) 
	{
		shapeShadeColour2 = contents;		
	}

	protected String getShapeShadeColour2() 
	{
		return shapeShadeColour2;
	}
}
