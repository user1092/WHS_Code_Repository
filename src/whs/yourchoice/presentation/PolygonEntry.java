/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.presentation;


/**
* This class is responsible for storing a polygon object contained in the XML file.
* A different instance of this class is created for each polygon object in the XML file.
*
* @author ajff500 and sqk501
* @version v0.1 18/04/16
*/

public class PolygonEntry 
{
	private int polygonStartTime = -10;
	private String polygonSourceFile = null;
	private int polygonDuration = -10;
	private String polygonLineColour = null;
	private String polygonFillColour = null;
	
	private float polygonShadeX1 = -10;
	private float polygonShadeY1 = -10;
	private String polygonShadeColour1 = null;
	private float polygonShadeX2 = -10;
	private float polygonShadeY2 = -10;
	private String polygonShadeColour2 = null;
	
	private int targetSlide = -10;
	
	
	/**
	* setPolygonTargetSlide method. Sets the target slide of the polygon.
	* @param contents The target slide of the polygon.
	*/
	public void setPolygonTargetSlide(String i)
	{
		targetSlide = Integer.parseInt(i);
	}
	
	/**
	* getTextTargetSlide method. Gets the target slide of the polygon.
	* @return targetSlide Returns the target slide of the polygon.
	*/
	public int getTextTargetSlide()
	{
		return targetSlide;
	}
	
	
	/**
	* setPolygonStartTime method. Sets the start time of the polygon.
	* @param contents The start time of the polygon.
	*/
	public void setPolygonStartTime(String contents) 
	{
		polygonStartTime = Integer.parseInt(contents);
	}

	/**
	* getPolygonStartTime method. Gets the start time of the polygon.
	* @return polygonStartTime Returns the start time of the polygon.
	*/
	public int getPolygonStartTime() 
	{
		return polygonStartTime;
	}
	
	/**
	* setPolygonSourceFile method. Sets the source file of the polygon.
	* @param contents The source file of the polygon.
	*/
	public void setPolygonSourceFile(String contents) 
	{
		polygonSourceFile = contents;		
	}

	/**
	* getPolygonSourceFile method. Gets the source file of the polygon.
	* @return polygonSourceFile Returns the source file of the polygon.
	*/
	public String getPolygonSourceFile() 
	{
		return polygonSourceFile;
	}
	
	/**
	* setPolygonDuration method. Sets the duration of the polygon.
	* @param contents The duration of the polygon.
	*/
	public void setPolygonDuration(String contents) 
	{
		polygonDuration = Integer.parseInt(contents);
	}

	/**
	* getPolygonDuration method. Gets the duration of the polygon.
	* @return polygonDuration Returns the duration of the polygon.
	*/
	public int getPolygonDuration() 
	{
		return polygonDuration;
	}
	
	/**
	* setPolygonDuration method. Sets the line colour of the polygon.
	* @param contents The line colour of the polygon.
	*/
	public void setPolygonLineColour(String contents) 
	{
		polygonLineColour = contents;		
	}

	/**
	* getPolygonLineColour method. Gets the line colour of the polygon.
	* @return polygonLineColour Returns the line colour of the polygon.
	*/
	public String getPolygonLineColour() 
	{
		return polygonLineColour;
	}
	
	/**
	* setPolygonFillColour method. Sets the fill colour of the polygon.
	* @param contents The fill colour of the polygon.
	*/
	public void setPolygonFillColour(String contents) 
	{
		polygonFillColour = contents;		
	}

	/**
	* getPolygonFillColour method. Gets the fill colour of the polygon.
	* @return polygonFillColour Returns the fill colour of the polygon.
	*/
	public String getPolygonFillColour() 
	{
		return polygonFillColour;
	}
	

//****************************** SHADING ATTRIBUTES BELLOW ************************************
//*********************************************************************************************
	
	/**
	* setPolygonShadeX1 method. Sets the X1 shade of the polygon.
	* @param contents The X1 shade of the polygon.
	*/
	public void setPolygonShadeX1(String contents) 
	{
		polygonShadeX1 = Float.parseFloat(contents);	
	}

	/**
	* getPolygonShadeX1 method. Gets the X1 shade of the polygon.
	* @return polygonShadeX1 Returns the X1 shade of the polygon.
	*/
	public float getPolygonShadeX1() 
	{
		return polygonShadeX1;
	}
	
	/**
	* setPolygonShadeY1 method. Sets the Y1 shade of the polygon.
	* @param contents The Y1 shade of the polygon.
	*/
	public void setPolygonShadeY1(String contents) 
	{
		polygonShadeY1 = Float.parseFloat(contents);
	}

	/**
	* getPolygonShadeY1 method. Gets the Y1 shade of the polygon.
	* @return polygonShadeY1 Returns the Y1 shade of the polygon.
	*/
	public float getPolygonShadeY1() 
	{
		return polygonShadeY1;
	}
	
	/**
	* setPolygonShadeColour1 method. Sets the colour 1 of the polygon.
	* @param contents The colour 1 of the polygon.
	*/
	public void setPolygonShadeColour1(String contents) 
	{
		polygonShadeColour1 = contents;		
	}

	/**
	* getPolygonShadeColour1 method. Gets the colour 1 of the polygon.
	* @return polygonShadeColour1 Returns the colour 1 of the polygon.
	*/
	public String getPolygonShadeColour1() 
	{
		return polygonShadeColour1;
	}
	
	/**
	* setPolygonShadeX2 method. Sets the X2 shade of the polygon.
	* @param contents The X2 shade of the polygon.
	*/
	public void setPolygonShadeX2(String contents) 
	{
		polygonShadeX2 = Float.parseFloat(contents);
	}

	/**
	* getPolygonShadeX2 method. Gets the X2 shade of the polygon.
	* @return polygonShadeX2 Returns the X2 shade of the polygon.
	*/
	public float getPolygonShadeX2() 
	{
		return polygonShadeX2;
	}
	
	/**
	* setPolygonShadeY2 method. Sets the Y2 shade of the polygon.
	* @param contents The Y2 shade of the polygon.
	*/
	public void setPolygonShadeY2(String contents) 
	{
		polygonShadeY2 = Float.parseFloat(contents);
	}

	/**
	* getPolygonShadeY2 method. Gets the Y2 shade of the polygon.
	* @return polygonShadeY2 Returns the Y2 shade of the polygon.
	*/
	public float getPolygonShadeY2() 
	{
		return polygonShadeY2;
	}
	
	/**
	* setPolygonShadeColour2 method. Sets the colour 2 of the polygon.
	* @param contents The colour 2 of the polygon.
	*/
	public void setPolygonShadeColour2(String contents) 
	{
		polygonShadeColour2 = contents;		
	}

	/**
	* getPolygonShadeColour2 method. Gets the colour 2 of the polygon.
	* @return polygonShadeColour2 Returns the colour 2 of the polygon.
	*/
	public String getPolygonShadeColour2() 
	{
		return polygonShadeColour2;
	}
}