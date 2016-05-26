/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.presentation;


/**
* This class is responsible for storing a shape object contained in the XML file.
* A different instance of this class is created for each shape object in the XML file.
*
* @author ajff500 and sqk501
* @version v0.1 18/04/16
*/

public class ShapeEntry 
{
	private int shapeStartTime = -10;
	private int shapeDuration = -10;
	private float shapeXStart = -10;
	private float shapeYStart = -10;
	private String shapeType = null;
	private float shapeWidth = -10;
	private float shapeHeight = -10;
	private String shapeLineColour = null;
	private String shapeFillColour = null;
	
	private float shapeShadeX1 = -10;
	private float shapeShadeY1 = -10;
	private String shapeShadeColour1 = null;
	private float shapeShadeX2 = -10;
	private float shapeShadeY2 = -10;
	private String shapeShadeColour2 = null;
	
	private int targetSlide = -10;
	
	
	/**
	* setShapeTargetSlide method. Sets the target slide of the shape.
	* @param contents The target slide of the shape.
	*/
	public void setShapeTargetSlide(String i)
	{
		targetSlide = Integer.parseInt(i);
	}
	
	/**
	* getShapeTargetSlide method. Gets the target slide of the shape.
	* @return targetSlide Returns the target slide of the shape.
	*/
	public int getShapeTargetSlide()
	{
		return targetSlide;
	}
	
	
	/**
	* setShapeStartTime method. Sets the start time of the shape.
	* @param contents The start time of the shape.
	*/
	public void setShapeStartTime(String contents) 
	{
		shapeStartTime = Integer.parseInt(contents);
	}

	/**
	* getShapeStartTime method. Gets the start time of the shape.
	* @return shapeStartTime Returns the start time of the shape.
	*/
	public int getShapeStartTime() 
	{
		return shapeStartTime;
	}
	
	/**
	* setShapeDuration method. Sets the duration of the shape.
	* @param contents The duration of the shape.
	*/
	public void setShapeDuration(String contents) 
	{
		shapeDuration = Integer.parseInt(contents);
	}

	/**
	* getShapeDuration method. Gets the duration of the shape.
	* @return shapeDuration Returns the duration of the shape.
	*/
	public int getShapeDuration() 
	{
		return shapeDuration;
	}
	
	/**
	* setShapeXStart method. Sets the x start of the shape.
	* @param contents The x start of the shape.
	*/
	public void setShapeXStart(String contents) 
	{
		shapeXStart = Float.parseFloat(contents);
	}

	/**
	* getShapeXStart method. Gets the x start of the shape.
	* @return shapeXStart Returns the x start of the shape.
	*/
	public float getShapeXStart() 
	{
		return shapeXStart;
	}
	
	/**
	* setShapeYStart method. Sets the y start of the shape.
	* @param contents The y start of the shape.
	*/
	public void setShapeYStart(String contents) 
	{
		shapeYStart = Float.parseFloat(contents);
	}

	/**
	* getShapeYStart method. Gets the y start of the shape.
	* @return shapeYStart Returns the y start of the shape.
	*/
	public float getShapeYStart() 
	{
		return shapeYStart;
	}
	
	/**
	* setShapeType method. Sets the type of the shape.
	* @param contents The type of the shape.
	*/
	public void setShapeType(String contents) 
	{
		shapeType = contents;		
	}

	/**
	* getShapeType method. Gets the type of the shape.
	* @return shapeType Returns the type of the shape.
	*/
	public String getShapeType() 
	{
		return shapeType;
	}
	
	/**
	* setShapeWidth method. Sets the width of the shape.
	* @param contents The width of the shape.
	*/
	public void setShapeWidth(String contents) 
	{
		shapeWidth = Float.parseFloat(contents);
	}

	/**
	* getShapeWidth method. Gets the width of the shape.
	* @return shapeWidth Returns the width of the shape.
	*/
	public float getShapeWidth() 
	{
		return shapeWidth;
	}
	
	/**
	* setShapeHeight method. Sets the height of the shape.
	* @param contents The height of the shape.
	*/
	public void setShapeHeight(String contents) 
	{
		shapeHeight = Float.parseFloat(contents);
	}

	/**
	* getShapeHeight method. Gets the height of the shape.
	* @return shapeHeight Returns the height of the shape.
	*/
	public float getShapeHeight() 
	{
		return shapeHeight;
	}
	
	/**
	* setShapeLineColour method. Sets the line colour of the shape.
	* @param contents The line colour of the shape.
	*/
	public void setShapeLineColour(String contents) 
	{
		shapeLineColour = contents;		
	}

	/**
	* getShapeLineColour method. Gets the line colour of the shape.
	* @return shapeLineColour Returns the line colour of the shape.
	*/
	public String getShapeLineColour() 
	{
		return shapeLineColour;
	}
	
	/**
	* setShapeFillColour method. Sets the fill colour of the shape.
	* @param contents The fill colour of the shape.
	*/
	public void setShapeFillColour(String contents) 
	{
		shapeFillColour = contents;		
	}

	/**
	* getShapeFillColour method. Gets the fill colour of the shape.
	* @return shapeFillColour Returns the fill colour of the shape.
	*/
	public String getShapeFillColour() 
	{
		return shapeFillColour;
	}
	
//****************************** SHADING ATTRIBUTES BELLOW ************************************
//*********************************************************************************************
	
	/**
	* setShapeShadeX1 method. Sets the X1 shade of the shape.
	* @param contents The X1 shade of the shape.
	*/
	public void setShapeShadeX1(String contents) 
	{
		shapeShadeX1 = Float.parseFloat(contents);	
	}

	/**
	* getShapeShadeX1 method. Gets the X1 shade of the shape.
	* @return shapeShadeX1 Returns the X1 shade of the shape.
	*/
	public float getShapeShadeX1() 
	{
		return shapeShadeX1;
	}
	
	/**
	* setShapeShadeY1 method. Sets the Y1 shade of the shape.
	* @param contents The Y1 shade of the shape.
	*/
	public void setShapeShadeY1(String contents) 
	{
		shapeShadeY1 = Float.parseFloat(contents);
	}

	/**
	* getShapeShadeY1 method. Gets the Y1 shade of the shape.
	* @return shapeShadeY1 Returns the Y1 shade of the shape.
	*/
	public float getShapeShadeY1() 
	{
		return shapeShadeY1;
	}
	
	/**
	* setShapeShadeColour1 method. Sets the colour 1 of the shape.
	* @param contents The colour 1 of the shape.
	*/
	public void setShapeShadeColour1(String contents) 
	{
		shapeShadeColour1 = contents;		
	}

	/**
	* getShapeShadeColour1 method. Gets the colour 1 of the shape.
	* @return shapeShadeColour1 Returns the colour 1 of the shape.
	*/
	public String getShapeShadeColour1() 
	{
		return shapeShadeColour1;
	}
	
	/**
	* setShapeShadeX2 method. Sets the X2 shade of the shape.
	* @param contents The X2 shade of the shape.
	*/
	public void setShapeShadeX2(String contents) 
	{
		shapeShadeX2 = Float.parseFloat(contents);
	}

	/**
	* getShapeShadeX2 method. Gets the X2 shade of the shape.
	* @return shapeShadeX2 Returns the X2 shade of the shape.
	*/
	public float getShapeShadeX2() 
	{
		return shapeShadeX2;
	}
	
	/**
	* setShapeShadeY2 method. Sets the Y2 shade of the shape.
	* @param contents The Y2 shade of the shape.
	*/
	public void setShapeShadeY2(String contents) 
	{
		shapeShadeY2 = Float.parseFloat(contents);
	}

	/**
	* getShapeShadeY2 method. Gets the Y2 shade of the shape.
	* @return shapeShadeY2 Returns the Y2 shade of the shape.
	*/
	public float getShapeShadeY2() 
	{
		return shapeShadeY2;
	}
	
	/**
	* setShapeShadeColour2 method. Sets the colour 2 of the shape.
	* @param contents The colour 2 of the shape.
	*/
	public void setShapeShadeColour2(String contents) 
	{
		shapeShadeColour2 = contents;		
	}

	/**
	* getShapeShadeColour2 method. Gets the colour 2 of the shape.
	* @return shapeShadeColour2 Returns the colour 2 of the shape.
	*/
	public String getShapeShadeColour2() 
	{
		return shapeShadeColour2;
	}
}
