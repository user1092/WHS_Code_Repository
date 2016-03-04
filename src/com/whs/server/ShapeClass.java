package com.whs.server;

public class ShapeClass 
{
	protected int shapeStartTime = -10;
	protected int shapeDuration = -10;
	protected float shapeXStart = -10;
	protected float shapeYStart = -10;
	protected String shapeType = null;
	protected float shapeWidth = -10;
	protected float shapeHeight = -10;
	protected String shapeLineColour = null;
	protected String shapeFillColour = null;
	
	protected String shapeShadeX1 = null;
	protected String shapeShadeY1 = null;
	protected String shapeShadeColour1 = null;
	protected String shapeShadeX2 = null;
	protected String shapeShadeY2 = null;
	protected String shapeShadeColour2 = null;
	
	private boolean interactable = false;
	private int targetSlide = -10;
	
	protected void setShapeTargetSlide(String i)
	{
		targetSlide = Integer.parseInt(i);
	}
	
	protected int getShapeTargetSlide()
	{
		return targetSlide;
	}
	
	
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
		shapeStartTime = Integer.parseInt(contents);
	}

	protected int getShapeStartTime() 
	{
		return shapeStartTime;
	}
	
	protected void setShapeDuration(String contents) 
	{
		shapeDuration = Integer.parseInt(contents);
	}

	protected int getShapeDuration() 
	{
		return shapeDuration;
	}
	
	protected void setShapeXStart(String contents) 
	{
		shapeXStart = Float.parseFloat(contents);
	}

	protected float getShapeXStart() 
	{
		return shapeXStart;
	}
	
	protected void setShapeYStart(String contents) 
	{
		shapeYStart = Float.parseFloat(contents);
	}

	protected float getShapeYStart() 
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
		shapeWidth = Float.parseFloat(contents);
	}

	protected float getShapeWidth() 
	{
		return shapeWidth;
	}
	
	protected void setShapeHeight(String contents) 
	{
		shapeHeight = Float.parseFloat(contents);
	}

	protected float getShapeHeight() 
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
