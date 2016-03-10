package com.whs.server;

import java.util.ArrayList;
import java.util.List;

public class SlideClass 
{
	private int slideID = -10;
	private int slideDuration = -10;
	private int slideNext = -10;
	
	protected List<TextClass> textList = new ArrayList<TextClass>();
	protected List<ShapeClass> shapeList = new ArrayList<ShapeClass>();
	protected List<PolygonClass> polygonList = new ArrayList<PolygonClass>();
	protected List<ImageClass> imageList = new ArrayList<ImageClass>();
	protected List<VideoClass> videoList = new ArrayList<VideoClass>();
	protected List<AudioClass> audioList = new ArrayList<AudioClass>();
	

	protected void setSlideID(String contents) 
	{
		slideID = Integer.parseInt(contents);
	}
	
	protected int getSlideID()
	{
		return slideID;
	}

	protected void setSlideDuration(String contents) 
	{
		slideDuration = Integer.parseInt(contents);
	}
	
	protected int getSlideDuration()
	{
		return slideDuration;
	}

	protected void setSlideNext(String contents) 
	{
		slideNext = Integer.parseInt(contents);
	}
	
	protected int getSlideNext()
	{
		return slideNext;
	}
	
//-------------------------------------------------------------------------------------------------------

	protected TextClass CreateNewText() 
	{
		TextClass newText = new TextClass();
		
		textList.add(newText);
		
		return newText;
	}

	protected ShapeClass CreateNewShape() 
	{
		ShapeClass newShape = new ShapeClass();
		
		shapeList.add(newShape);
		
		return newShape;
	}

	protected PolygonClass CreateNewPolygon() 
	{
		PolygonClass newPolygon = new PolygonClass();
		
		polygonList.add(newPolygon);
		
		return newPolygon;
	}

	protected ImageClass CreateNewImage() 
	{
		ImageClass newImage = new ImageClass();
		
		imageList.add(newImage);
		
		return newImage;
	}

	protected VideoClass CreateNewVideo() 
	{
		VideoClass newVideo = new VideoClass();
		
		videoList.add(newVideo);
		
		return newVideo;
	}

	protected AudioClass CreateNewAudio() 
	{
		AudioClass newAudio = new AudioClass();
		
		audioList.add(newAudio);
		
		return newAudio;
	}	
}