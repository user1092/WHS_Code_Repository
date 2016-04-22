package whs.yourchoice.parsers;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import whs.yourchoice.presentation.RegisteredModuleEntry;
import whs.yourchoice.presentation.RegisteredModules;


public class RegisteredModulesParserTests 
{
	/**
	* ExtractPresentationInfo Test. Parses the XML file and asserts if the both the "default" and "documentInfo" tags were parsed and stored successfully.
	*/
	@Test
	public void ExtractPresentationInfo()
	{		
		Path path = Paths.get("/Users/williamsharrard/Documents/SWEng_workspace/SWE/src/registered_modules.xml");
		String filePath = path.toString();
		RegisteredModulesParser parser = new RegisteredModulesParser();		
		RegisteredModules modules = parser.parseModules(filePath);
		RegisteredModuleEntry module = modules.searchModuleCode("ELE000034");
		
		assertEquals("ELE000034", module.getCode());
		System.out.println(module.getCode());
		assertEquals("Electronics", module.getCourse());
		System.out.println(module.getCourse());
		assertEquals("Electronics and Computer Engineering", module.getStream());
		System.out.println(module.getStream());
		assertEquals(2016, module.getYear());
		System.out.println(module.getYear());
		assertEquals("Checking Title", module.getTitle());
		System.out.println(module.getTitle());
		assertEquals("ele000034.zip", module.getFileName());
		System.out.println(module.getFileName());
		
		module = modules.searchModuleCode("PHL000A68");
		
		assertEquals("PHL000A68", module.getCode());
		System.out.println(module.getCode());
		assertEquals("Philosophy", module.getCourse());
		System.out.println(module.getCourse());
		assertEquals("Philosophy", module.getStream());
		System.out.println(module.getStream());
		assertEquals(2015, module.getYear());
		System.out.println(module.getYear());
		assertEquals("Bhuddist Philosophy", module.getTitle());
		System.out.println(module.getTitle());
		assertEquals("bud_phil.zip", module.getFileName());
		System.out.println(module.getFileName());
		
	}
	
}

