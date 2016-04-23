package whs.yourchoice.parsers;

/**
* Presentation Parser
* Latest Update: 10/03/16
*
* Copyright and Licensing Information if applicable
*/

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import whs.yourchoice.presentation.AudioEntry;
import whs.yourchoice.presentation.ImageEntry;
import whs.yourchoice.presentation.PolygonEntry;
import whs.yourchoice.presentation.PresentationEntry;
import whs.yourchoice.presentation.ShapeEntry;
import whs.yourchoice.presentation.SlideEntry;
import whs.yourchoice.presentation.TextEntry;
import whs.yourchoice.presentation.VideoEntry;

/**
* This class is responsible for parsing a given XML file and populating a PresentationEntry class with its contents
*
* @author ajff500, sqk501 and ch1092
* @version v0.1 18/04/16
*/

public class PresentationParser 
{	
	/** Object of PresentationEntry type. It holds all the parsed XML data.*/
	
	PresentationEntry presentation = new PresentationEntry();

	
	/**
	* parsePresention method. Sets up the necessary parsing elements. It detects the root node of the XML file (Presentation in our case),
	* and calls the continueParsingForChildren method, giving it the children of the root node.
	* @param filePath The path of the XML file to be parsed.
	* @param presentationPath 
	* @return presentation The completely populated presentation.
	*/
	public PresentationEntry parsePresention(String filePath, String presentationPath) 
	{
		try 
		{
			File file = new File(filePath);

			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			Document doc = dBuilder.parse(file);
			
			presentation.setPath(presentationPath);
			
			doc.getDocumentElement().normalize();

			System.out.println("Root element : " + doc.getDocumentElement().getNodeName());
			
			continueParsingForChildren(doc.getChildNodes());			
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
		
		return presentation;
	}
	
	/**
	* continueParsingForChildren method. Cycles through each currentNodes and checks their contents. If a specific currentNode has child nodes,
	* the method is then called again and it will cycle now through the children of the previous currentNode. And so on... 
	* @param currentNodeList The node currently being parsed.
	*/
	protected void continueParsingForChildren(NodeList currentNodeList) 
	{
		for (int count = 0; count < currentNodeList.getLength(); count++) 
		{
			Node currentNodeElement = currentNodeList.item(count);
			
			if (currentNodeElement.getNodeType() == Node.ELEMENT_NODE) 
			{
				CheckPresentationContent(currentNodeElement);
				CheckSlideContent(currentNodeElement);

				if (currentNodeElement.hasChildNodes()) 
				{
					continueParsingForChildren(currentNodeElement.getChildNodes());
				}
			}			
		}
	}
	
	/**
	* CheckPresentationContent method. Checks the node given for any content belonging to either the "Default" tag or "DocumentInfo" tag.
	* If the node is indeed from any of those tags, the contents are parsed and saved in the PresentationEntry class. 
	* @param temporaryNode The node currently being checked for.
	*/
	protected void CheckPresentationContent(Node temporaryNode)
	{
		String temporaryNodeName = temporaryNode.getNodeName();
		String temporaryNodeTextContent = temporaryNode.getTextContent();
		
		switch(temporaryNodeName)
		{
			case "Title" :
				if (temporaryNode.getParentNode().getNodeName() == "documentInfo")
				{
					presentation.setPresentationTitle(temporaryNodeTextContent);					
				}			
			break;
				
			case "Author" :
				if (temporaryNode.getParentNode().getNodeName() == "documentInfo")
				{
					presentation.setPresentationAuthor(temporaryNodeTextContent);					
				}			
			break;
				
			case "Version" :
				if (temporaryNode.getParentNode().getNodeName() == "documentInfo")
				{
					presentation.setPresentationVersion(temporaryNodeTextContent);					
				}			
			break;
			
			case "Comment" :
				if (temporaryNode.getParentNode().getNodeName() == "documentInfo")
				{
					presentation.setPresentationComment(temporaryNodeTextContent);					
				}			
			break;
			
			case "backgroundColour" :
				if (temporaryNode.getParentNode().getNodeName() == "defaults")
				{
					presentation.setDefaultBackgroundColour(temporaryNodeTextContent);					
				}			
			break;
			
			case "font" :
				if (temporaryNode.getParentNode().getNodeName() == "defaults")
				{
					presentation.setDefaultFont(temporaryNodeTextContent);					
				}			
			break;
			
			case "fontsize" :
				if (temporaryNode.getParentNode().getNodeName() == "defaults")
				{
					presentation.setDefaultFontSize(temporaryNodeTextContent);					
				}			
			break;
			
			case "fontColour" :
				if (temporaryNode.getParentNode().getNodeName() == "defaults")
				{
					presentation.setDefaultFontColour(temporaryNodeTextContent);					
				}			
			break;
			
			case "lineColour" :
				if (temporaryNode.getParentNode().getNodeName() == "defaults")
				{
					presentation.setDefaultLineColour(temporaryNodeTextContent);					
				}			
			break;
			
			case "fillColour" :
				if (temporaryNode.getParentNode().getNodeName() == "defaults")
				{
					presentation.setDefaultFillColour(temporaryNodeTextContent);					
				}			
			break;
			
			default:
				
				// Some sort of error message here.
				
			break;		
		}
	}
	
	/**
	* CheckSlideContent method. Checks the node given for any content belonging to either the "slide" tag.
	* If the node is indeed from the slide tag, the contents are parsed and saved in the PresentationEntry class. 
	* @param temporaryNode The node currently being checked for.
	*/
	protected void CheckSlideContent(Node temporaryNode) 
	{
		String temporaryNodeName = temporaryNode.getNodeName();
		NamedNodeMap temporaryNodeAttributesList = temporaryNode.getAttributes();
		
		switch(temporaryNodeName)
		{
			case "slide" :
				SlideEntry currentSlide = presentation.CreateNewSlide();
				
				// Get the current slide attributes and save them in the currentSlide instance.
				for (int i = 0; i < temporaryNodeAttributesList.getLength(); i++) 
				{
					Node reallyTempNode = temporaryNodeAttributesList.item(i);
						
					if (reallyTempNode.getNodeName() == "slideID")
					{
						currentSlide.setSlideID(reallyTempNode.getNodeValue());
					}
					
					if (reallyTempNode.getNodeName() == "duration")
					{
						currentSlide.setSlideDuration(reallyTempNode.getNodeValue());
					}
					
					if (reallyTempNode.getNodeName() == "nextSlide")
					{
						currentSlide.setSlideNext(reallyTempNode.getNodeValue());
					}
				}
				
				// Cycle through the slide's child nodes
				for (int count = 0; count < temporaryNode.getChildNodes().getLength(); count++) 
				{
					Node temporaryChildNodeElement = temporaryNode.getChildNodes().item(count);
					String temporaryChildNodeElementName = temporaryChildNodeElement.getNodeName();
					
					// Check which node type the child belongs to, and parse it.
					switch(temporaryChildNodeElementName)
					{
						case "backgroundColour" :
							
							currentSlide.setSlideBackgroundColour(temporaryChildNodeElement.getTextContent());
							
						break;
						
						case "text" :
							
							ParseText(currentSlide, temporaryChildNodeElement, false, "-10");
							
						break;
						
						case "shape" :
							
							ParseShape(currentSlide, temporaryChildNodeElement, false, "-10");
							
						break;
						
						case "polygon" :
							
							ParsePolygon(currentSlide, temporaryChildNodeElement, false, "-10");
							
						break;
						
						case "image" :
							
							ParseImage(currentSlide, temporaryChildNodeElement, false, "-10");
							
						break;
						
						case "video" :
							
							ParseVideo(currentSlide, temporaryChildNodeElement, false, "-10");
							
						break;
						
						case "audio" :
							
							ParseAudio(currentSlide, temporaryChildNodeElement);
							
						break;
						
						// If the child node is of the type interactable, we have to repeat the process one level deeper.
						case "interactable" :
							
							String targetSlide = "";
							
							NodeList interactableNodeList = temporaryChildNodeElement.getChildNodes();
							
							// item 1 because we can only have exactly 1 child of an interactable node.
							Node interactableNodeElement = interactableNodeList.item(1);
							
							String interactableNodeElementName = interactableNodeElement.getNodeName();
							
							// item 0 because we can only have exactly 1 attribute of an interactable node.
							Node interactableNodeAttributeElement = temporaryChildNodeElement.getAttributes().item(0);											
							
							if (interactableNodeAttributeElement.getNodeName() == "targetSlide")
							{
								targetSlide = interactableNodeAttributeElement.getNodeValue();
							}
							
							switch(interactableNodeElementName)
							{
								case "text" :
									
									ParseText(currentSlide, interactableNodeElement, true, targetSlide);
									
								break;
								
								case "shape" :
									
									ParseShape(currentSlide, interactableNodeElement, true, targetSlide);
									
								break;
								
								case "polygon" :
									
									ParsePolygon(currentSlide, interactableNodeElement, true, targetSlide);
									
								break;
								
								case "image" :
									
									ParseImage(currentSlide, interactableNodeElement, true, targetSlide);
									
								break;
								
								case "video" :
									
									ParseVideo(currentSlide, interactableNodeElement, true, targetSlide);
									
								break;
								
								case "audio" :
									
									ParseAudio(currentSlide, interactableNodeElement);
									
								break;
							}
								
						break;
					}	
				}
			break;
			
			default:
				
				// Some sort of error message here.
				
			break;
		}
	}
	
	/**
	* ParseText method. Parses the contents of the given text node and saves them in an instance of a TextEntry class.
	* @param currentSlide The slide to which this text node belongs to.
	* @param temporaryNode The actual text node that needs to be parsed.
	* @param interactable A flag telling us if this text node is interactable or not.
	* @param targetSlide The target slide of this text. (if it is not interactable, this parameter is -10).
	*/
	protected void ParseText (SlideEntry currentSlide, Node temporaryNode, boolean interactable, String targetSlide)
	{
		NamedNodeMap temporaryNodeAttributesList = temporaryNode.getAttributes();
		
		TextEntry temporaryText = currentSlide.CreateNewText();
		
		temporaryText.setTextInteractable(interactable);
		temporaryText.setTextTargetSlide(targetSlide);
		
		// The complete text content of this node, including bold and italic tags.
		String completeTextWithTags = "";
		
		for (int i = 0; i < temporaryNodeAttributesList.getLength(); i++) 
		{
			Node temporaryNodeAttributeElement = temporaryNodeAttributesList.item(i);
				
			if (temporaryNodeAttributeElement.getNodeName() == "starttime")
			{
				temporaryText.setTextStartTime(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "duration")
			{
				temporaryText.setTextDuration(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "xstart")
			{
				temporaryText.setTextXStart(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "ystart")
			{
				temporaryText.setTextYStart(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "font")
			{
				temporaryText.setTextFont(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "fontsize")
			{
				temporaryText.setTextFontSize(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "fontColour")
			{
				temporaryText.setTextFontColour(temporaryNodeAttributeElement.getNodeValue());							
			}
		}
		
		if(temporaryText.getTextFontColour() == null)
		{
			temporaryText.setTextFontColour(presentation.getDefaultFontColour());				
		}
		if(temporaryText.getTextFont() == null)
		{
			temporaryText.setTextFont(presentation.getDefaultFont());
		}
		if(temporaryText.getTextFontSize() == -10)
		{
			temporaryText.setTextFontSize(Integer.toString(presentation.getDefaultFontSize()));
		}
		
		if (temporaryNode.hasChildNodes())
		{
	        NodeList temporaryChildList = temporaryNode.getChildNodes();
	        for (int i = 0; i < temporaryChildList.getLength(); i++) 
	        {
	            Node temporaryChildElement = temporaryChildList.item(i);
	            
	            // Normal Text
	            if(temporaryChildElement.getNodeType() == Node.TEXT_NODE) 
	            {
	            	String normalText = temporaryChildElement.getTextContent();
	            	completeTextWithTags = completeTextWithTags.concat(normalText);
	            }   
	            	
	            // Bold Text
	            if (temporaryChildElement != null && "b".equals(temporaryChildElement.getNodeName())) 
	            {
		            NodeList checkForBoldAndItalic = temporaryChildElement.getChildNodes();
		            
		            if ("i".equals(checkForBoldAndItalic.item(0).getNodeName()))
		            {
		            	String boldText = temporaryChildElement.getTextContent();
		            	completeTextWithTags = completeTextWithTags.concat("<b><i>");
		            	completeTextWithTags = completeTextWithTags.concat(boldText);
		            	completeTextWithTags = completeTextWithTags.concat("</i></b>");
		            }
		            else
		            {
		            	String boldText = temporaryChildElement.getTextContent();
		            	completeTextWithTags = completeTextWithTags.concat("<b>");
		            	completeTextWithTags = completeTextWithTags.concat(boldText);
		            	completeTextWithTags = completeTextWithTags.concat("</b>");           	
		            }
	            }
	            
	            // Italic Text
	            if (temporaryChildElement != null && "i".equals(temporaryChildElement.getNodeName())) 
	            {
		            NodeList checkForBoldAndItalic = temporaryChildElement.getChildNodes();
		            
		            if ("b".equals(checkForBoldAndItalic.item(0).getNodeName()))
		            {
		            	String italicText = temporaryChildElement.getTextContent();
		            	completeTextWithTags = completeTextWithTags.concat("<i><b>");
		            	completeTextWithTags = completeTextWithTags.concat(italicText);
		            	completeTextWithTags = completeTextWithTags.concat("</b></i>");
		            }
		            else
		            {
		            	String italicText = temporaryChildElement.getTextContent();
		            	completeTextWithTags = completeTextWithTags.concat("<i>");
		            	completeTextWithTags = completeTextWithTags.concat(italicText);
		            	completeTextWithTags = completeTextWithTags.concat("</i>");          	
		            }
	            }
	        }		
		}
		
		temporaryText.setTextContent(completeTextWithTags);
	}
	
	/**
	* ParseShape method. Parses the contents of the given shape node and saves them in an instance of a ShapeEntry class.
	* @param currentSlide The slide to which this shape node belongs to.
	* @param temporaryNode The actual shape node that needs to be parsed.
	* @param interactable A flag telling us if this shape node is interactable or not.
	* @param targetSlide The target slide of this shape. (if it is not interactable, this parameter is -10).
	*/
	protected void ParseShape (SlideEntry currentSlide, Node temporaryNode, boolean interactable, String targetSlide)
	{
		NamedNodeMap temporaryNodeAttributesList = temporaryNode.getAttributes();
		
		ShapeEntry temporaryShape = currentSlide.CreateNewShape();
		
		temporaryShape.setShapeInteractable(interactable);
		temporaryShape.setShapeTargetSlide(targetSlide);
		
		for (int i = 0; i < temporaryNodeAttributesList.getLength(); i++) 
		{
			Node temporaryNodeAttributeElement = temporaryNodeAttributesList.item(i);
				
			if (temporaryNodeAttributeElement.getNodeName() == "starttime")
			{
				temporaryShape.setShapeStartTime(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "duration")
			{
				temporaryShape.setShapeDuration(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "xstart")
			{
				temporaryShape.setShapeXStart(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "ystart")
			{
				temporaryShape.setShapeYStart(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "type")
			{
				temporaryShape.setShapeType(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "width")
			{
				temporaryShape.setShapeWidth(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "height")
			{
				temporaryShape.setShapeHeight(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "lineColour")
			{
				temporaryShape.setShapeLineColour(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "fillColour")
			{
				temporaryShape.setShapeFillColour(temporaryNodeAttributeElement.getNodeValue());
			}
		}
		
		if(temporaryShape.getShapeLineColour() == null)
		{
			temporaryShape.setShapeLineColour(presentation.getDefaultLineColour());				
		}
		if(temporaryShape.getShapeFillColour() == null)
		{
			temporaryShape.setShapeFillColour(presentation.getDefaultFillColour());				
		}
		
        NodeList temporaryChildList = temporaryNode.getChildNodes();
        Node temporaryChildElement = temporaryChildList.item(1);       
        NamedNodeMap temporaryChildAttributesNodeList = temporaryChildElement.getAttributes();
        
        for (int i = 0; i < temporaryChildAttributesNodeList.getLength(); i++) 
		{
			Node temporaryChildAttributeElement = temporaryChildAttributesNodeList.item(i);
				
			if (temporaryChildAttributeElement.getNodeName() == "x1")
			{
				temporaryShape.setShapeShadeX1(temporaryChildAttributeElement.getNodeValue());
			}
			
			if (temporaryChildAttributeElement.getNodeName() == "y1")
			{
				temporaryShape.setShapeShadeY1(temporaryChildAttributeElement.getNodeValue());
			}
			
			if (temporaryChildAttributeElement.getNodeName() == "colour1")
			{
				temporaryShape.setShapeShadeColour1(temporaryChildAttributeElement.getNodeValue());
			}
			
			if (temporaryChildAttributeElement.getNodeName() == "x2")
			{
				temporaryShape.setShapeShadeX2(temporaryChildAttributeElement.getNodeValue());
			}
			
			if (temporaryChildAttributeElement.getNodeName() == "y2")
			{
				temporaryShape.setShapeShadeY2(temporaryChildAttributeElement.getNodeValue());
			}
			
			if (temporaryChildAttributeElement.getNodeName() == "colour2")
			{
				temporaryShape.setShapeShadeColour2(temporaryChildAttributeElement.getNodeValue());
			}
		}
    }
	
	/**
	* ParsePolygon method. Parses the contents of the given polygon node and saves them in an instance of a PolygonEntry class.
	* @param currentSlide The slide to which this polygon node belongs to.
	* @param temporaryNode The actual polygon node that needs to be parsed.
	* @param interactable A flag telling us if this polygon node is interactable or not.
	* @param targetSlide The target slide of this polygon. (if it is not interactable, this parameter is -10).
	*/
	protected void ParsePolygon (SlideEntry currentSlide, Node temporaryNode, boolean interactable, String targetSlide)
	{
		NamedNodeMap temporaryNodeAttributesList = temporaryNode.getAttributes();
		
		PolygonEntry temporaryPolygon = currentSlide.CreateNewPolygon();
		
		temporaryPolygon.setPolygonInteractable(interactable);
		temporaryPolygon.setPolygonTargetSlide(targetSlide);
		
		for (int i = 0; i < temporaryNodeAttributesList.getLength(); i++) 
		{
			Node temporaryNodeAttributeElement = temporaryNodeAttributesList.item(i);
				
			if (temporaryNodeAttributeElement.getNodeName() == "starttime")
			{
				temporaryPolygon.setPolygonStartTime(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "sourceFile")
			{
				temporaryPolygon.setPolygonSourceFile(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "duration")
			{
				temporaryPolygon.setPolygonDuration(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "lineColour")
			{
				temporaryPolygon.setPolygonLineColour(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "fillColour")
			{
				temporaryPolygon.setPolygonFillColour(temporaryNodeAttributeElement.getNodeValue());
			}
		}
		
		if(temporaryPolygon.getPolygonLineColour() == null)
		{
			temporaryPolygon.setPolygonLineColour(presentation.getDefaultLineColour());				
		}
		if(temporaryPolygon.getPolygonFillColour() == null)
		{
			temporaryPolygon.setPolygonFillColour(presentation.getDefaultFillColour());				
		}
		
		NodeList temporaryChildList = temporaryNode.getChildNodes();
        Node temporaryChildElement = temporaryChildList.item(1);       
        NamedNodeMap temporaryChildAttributesNodeList = temporaryChildElement.getAttributes();
        
        for (int i = 0; i < temporaryChildAttributesNodeList.getLength(); i++) 
		{
			Node temporaryChildAttributeElement = temporaryChildAttributesNodeList.item(i);
				
			if (temporaryChildAttributeElement.getNodeName() == "x1")
			{
				temporaryPolygon.setPolygonShadeX1(temporaryChildAttributeElement.getNodeValue());
			}
			
			if (temporaryChildAttributeElement.getNodeName() == "y1")
			{
				temporaryPolygon.setPolygonShadeY1(temporaryChildAttributeElement.getNodeValue());
			}
			
			if (temporaryChildAttributeElement.getNodeName() == "colour1")
			{
				temporaryPolygon.setPolygonShadeColour1(temporaryChildAttributeElement.getNodeValue());
			}
			
			if (temporaryChildAttributeElement.getNodeName() == "x2")
			{
				temporaryPolygon.setPolygonShadeX2(temporaryChildAttributeElement.getNodeValue());
			}
			
			if (temporaryChildAttributeElement.getNodeName() == "y2")
			{
				temporaryPolygon.setPolygonShadeY2(temporaryChildAttributeElement.getNodeValue());
			}
			
			if (temporaryChildAttributeElement.getNodeName() == "colour2")
			{
				temporaryPolygon.setPolygonShadeColour2(temporaryChildAttributeElement.getNodeValue());
			}
		}
	}
	
	/**
	* ParseImage method. Parses the contents of the given image node and saves them in an instance of an ImageEntry class.
	* @param currentSlide The slide to which this image node belongs to.
	* @param temporaryNode The actual image node that needs to be parsed.
	* @param interactable A flag telling us if this image node is interactable or not.
	* @param targetSlide The target slide of this image. (if it is not interactable, this parameter is -10).
	*/
	protected void ParseImage (SlideEntry currentSlide, Node temporaryNode, boolean interactable, String targetSlide)
	{
		NamedNodeMap temporaryNodeAttributesList = temporaryNode.getAttributes();
		
		ImageEntry temporaryImage = currentSlide.CreateNewImage();
		
		temporaryImage.setImageInteractable(interactable);
		temporaryImage.setImageTargetSlide(targetSlide);
		
		for (int i = 0; i < temporaryNodeAttributesList.getLength(); i++) 
		{
			Node temporaryNodeAttributeElement = temporaryNodeAttributesList.item(i);
				
			if (temporaryNodeAttributeElement.getNodeName() == "starttime")
			{
				temporaryImage.setImageStartTime(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "sourceFile")
			{
				temporaryImage.setImageSourceFile(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "duration")
			{
				temporaryImage.setImageDuration(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "xstart")
			{
				temporaryImage.setImageXStart(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "ystart")
			{
				temporaryImage.setImageYStart(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "width")
			{
				temporaryImage.setImageWidth(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "height")
			{
				temporaryImage.setImageHeight(temporaryNodeAttributeElement.getNodeValue());
			}
		}
	}
	
	/**
	* ParseVideo method. Parses the contents of the given video node and saves them in an instance of a VideoEntry class.
	* @param currentSlide The slide to which this video node belongs to.
	* @param temporaryNode The actual video node that needs to be parsed.
	* @param interactable A flag telling us if this video node is interactable or not.
	* @param targetSlide The target slide of this video. (if it is not interactable, this parameter is -10).
	*/
	protected void ParseVideo (SlideEntry currentSlide, Node temporaryNode, boolean interactable, String targetSlide)
	{
		NamedNodeMap temporaryNodeAttributesList = temporaryNode.getAttributes();
		
		VideoEntry temporaryVideo = currentSlide.CreateNewVideo();
		
		temporaryVideo.setVideoInteractable(interactable);
		temporaryVideo.setVideoTargetSlide(targetSlide);
		
		for (int i = 0; i < temporaryNodeAttributesList.getLength(); i++) 
		{
			Node temporaryNodeAttributeElement = temporaryNodeAttributesList.item(i);
				
			if (temporaryNodeAttributeElement.getNodeName() == "starttime")
			{
				temporaryVideo.setVideoStartTime(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "duration")
			{
				temporaryVideo.setVideoDuration(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "xstart")
			{
				temporaryVideo.setVideoXStart(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "ystart")
			{
				temporaryVideo.setVideoYStart(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "sourceFile")
			{
				temporaryVideo.setVideoSourceFile(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "loop")
			{
				temporaryVideo.setVideoLoop(temporaryNodeAttributeElement.getNodeValue());
			}
		}	
	}
	
	/**
	* ParseAudio method. Parses the contents of the given audio node and saves them in an instance of a AudioEntry class.
	* @param currentSlide The slide to which this audio node belongs to.
	* @param temporaryNode The actual audio node that needs to be parsed.
	*/
	protected void ParseAudio (SlideEntry currentSlide, Node temporaryNode)
	{
		NamedNodeMap temporaryNodeAttributesList = temporaryNode.getAttributes();
		
		AudioEntry temporaryAudio = currentSlide.CreateNewAudio();
		
		for (int i = 0; i < temporaryNodeAttributesList.getLength(); i++) 
		{
			Node temporaryNodeAttributeElement = temporaryNodeAttributesList.item(i);
				
			if (temporaryNodeAttributeElement.getNodeName() == "starttime")
			{
				temporaryAudio.setAudioStartTime(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "duration")
			{
				temporaryAudio.setAudioDuration(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "sourceFile")
			{
				temporaryAudio.setAudioSourceFile(temporaryNodeAttributeElement.getNodeValue());
			}
			
			if (temporaryNodeAttributeElement.getNodeName() == "loop")
			{
				temporaryAudio.setAudioLoop(temporaryNodeAttributeElement.getNodeValue());
			}
		}
	}
}