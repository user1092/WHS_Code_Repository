package whs.yourchoice.presentation;
/**
 * Registered Modules Object
 * 
 * Copyright and Licensing Information if applicable
 */

import java.util.LinkedList;
import java.util.List;

/**
 * Class that stores, searches and removes modules in a list
 * 
 * @author ws659 skq501
 * 
 * @version 0.1 20/04/16
 *
 */
public class RegisteredModules {
	private List<RegisteredModuleEntry> moduleList = new LinkedList<RegisteredModuleEntry>();
	
	/**
	 * Adds a module to module list
	 * @param module Module to be added to list
	 */
	public void addModule(RegisteredModuleEntry module) {
		moduleList.add(module);
	}
	
	/**
	 * Searches and returns a module by module code
	 * @param code String module code name
	 * @return module Returns module found, if none is found returns null
	 */
	public RegisteredModuleEntry searchModuleCode(String code) {
		RegisteredModuleEntry returnModule = null;
		
		//search each module individually until module found
		//if no module is found returns a null object
		for (int i=0; i<moduleList.size(); i++) {
			if(moduleList.get(i).getCode().equals(code)) {
				returnModule = moduleList.get(i);
				break;
			}
		}
		return returnModule;
	}
}
