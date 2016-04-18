package whs.yourchoice.parsers;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import whs.yourchoice.presentation.RegisteredModuleEntry;

/**
* Registered Modules Parser
* Latest Update: 18/04/16
*
* Copyright and Licensing Information if applicable
*/

/**
 * A class to parse a registered_modules.xml file
 * 
 * @author Will Sharrard & Sabrina Quinn
 * 
 * @version 0.1 18/04/16
 */
public class RegisteredModulesParser {
	private RegisteredModuleEntry module;
	
	public RegisteredModuleEntry parseModules(String filePath) {
		try 
		{
			File file = new File(filePath);

			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			Document doc = dBuilder.parse(file);
			
			doc.getDocumentElement().normalize();

			System.out.println("Root element : " + doc.getDocumentElement().getNodeName());
			
			continueParsingForChildren(doc.getChildNodes());			
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
		
		return module;
	}
	
	protected void continueParsingForChildren(NodeList currentNodeList) 
	{
		for (int count = 0; count < currentNodeList.getLength(); count++) 
		{
			Node currentNodeElement = currentNodeList.item(count);
			
			if (currentNodeElement.getNodeType() == Node.ELEMENT_NODE) 
			{
				CheckPresentationContent(currentNodeElement);

				if (currentNodeElement.hasChildNodes()) 
				{
					continueParsingForChildren(currentNodeElement.getChildNodes());
				}
			}			
		}
	}
	
	protected void CheckPresentationContent(Node temporaryNode)
	{
		String temporaryNodeName = temporaryNode.getNodeName();
		String temporaryNodeTextContent = temporaryNode.getTextContent();
		
		switch(temporaryNodeName)
		{
			case "module" :
				if (temporaryNode.getParentNode().getNodeName() == "registeredModules")
				{
					module.setCode(temporaryNodeTextContent);				
				}			
			break;
				
			case "course" :
				if (temporaryNode.getParentNode().getNodeName() == "registeredModules")
				{
					module.setCourse(temporaryNodeTextContent);				
				}			
			break;
				
			case "stream" :
				if (temporaryNode.getParentNode().getNodeName() == "registeredModules")
				{
					module.setStream(temporaryNodeTextContent);					
				}			
			break;
			
			case "year" :
				if (temporaryNode.getParentNode().getNodeName() == "registeredModules")
				{
					module.setYear(Integer.parseInt(temporaryNodeTextContent));					
				}			
			break;
			
			case "title" :
				if (temporaryNode.getParentNode().getNodeName() == "registeredModules")
				{
					module.setTitle(temporaryNodeTextContent);				
				}			
			break;
			
			case "filename" :
				if (temporaryNode.getParentNode().getNodeName() == "registeredModules")
				{
					module.setFileName(temporaryNodeTextContent);					
				}			
			break;
			
			default:
				
				// Some sort of error message here.
				
			break;		
		}
	}

}
