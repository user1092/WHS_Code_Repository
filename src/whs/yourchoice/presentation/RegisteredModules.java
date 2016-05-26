/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.presentation;

import java.util.LinkedList;
import java.util.List;

/**
 * Class that stores, searches and removes modules in a list
 * 
 * @author ws659, skq501, ch1092
 * 
 * @version 0.2 26/05/16
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
	
	/**
	 * Method to return size of list
	 * @return int moduleList size
	 */
	public int getListSize(){
		return moduleList.size();
	}
	
	/**
	 * Returns list of modules
	 * @return List moduleList
	 */
	public List<String> getAllModules() {
		List<String> moduleNames = new LinkedList<String>();
		for (int i=0; i<moduleList.size(); i++) {
			moduleNames.add(moduleList.get(i).getTitle());
		}
		
		return moduleNames;
	}
	
	/**
	 * Method to get filename from a a title
	 * 
	 * @param title		-	The title of the module selected
	 * @return filename	-	The filename associated with the title
	 */
	public String getFilenameFromTitle(String title) {
		
		for (int i=0; i<moduleList.size(); i++) {
			if (title.equals(moduleList.get(i).getTitle())) {
				return moduleList.get(i).getFileName();
			}
		}
		
		return null;
	}
	
	/**
	 * Returns list of streams
	 * @return List streamList
	 */
	public List<String> getAllStreams() {
		List<String> streamList = new LinkedList<String>();
		String stream;
		Boolean found = false;
		
		for (int i=0; i<moduleList.size(); i++) {
			stream = moduleList.get(i).getStream();
			if (streamList.size() < 1) {
				streamList.add(stream);
			}
			for (int j=0; j<streamList.size(); j++) {
				if (stream.equals(streamList.get(j))) {
					found = true;
				}
			}
			if (found == false) {
				streamList.add(stream);
			}
			found = false;
		}
		return streamList;
	}
	
	/**
	 * Returns list of academic year
	 * @return List yearList
	 */
	public List<String> getAllYears() {
		List<String> yearList = new LinkedList<String>();
		String year;
		Boolean found = false;
		
		for (int i=0; i<moduleList.size(); i++) {
			year = Integer.toString(moduleList.get(i).getYear());
			if (yearList.size() < 1) {
				yearList.add(year);
			}
			for (int j=0; j<yearList.size(); j++) {
				if (year.equals(yearList.get(j))) {
					found = true;
				}
			}
			if (found == false) {
				yearList.add(year);
			}
			found = false;
		}
		return yearList;
	}
	
	/**
	 * Returns list of academic courses
	 * @return List courseList
	 */
	public List<String> getAllCourses() {
		List<String> courseList = new LinkedList<String>();
		String course;
		Boolean found = false;
		
		for (int i=0; i<moduleList.size(); i++) {
			course = moduleList.get(i).getCourse();
			if (courseList.size() < 1) {
				courseList.add(course);
			}
			for (int j=0; j<courseList.size(); j++) {
				if (course.equals(courseList.get(j))) {
					found = true;
				}
			}
			if (found == false) {
				courseList.add(course);
			}
			found = false;
		}
		return courseList;
	}
	
	/**
	 * Returns stream by matching course
	 * @param course String
	 * @return results List<String> Stream matching course search
	 */
	public List<String> searchStreamByCourse(String course) {
		List<String> results = new LinkedList<String>();
		
		for (int i=0; i<moduleList.size(); i++) {
			if (course.equals(moduleList.get(i).getCourse())) {
				results.add(moduleList.get(i).getStream());
			}
		}
		
		results = removeDuplicates(results);
		return results;
	}
	
	/**
	 * Returns modules by matching course
	 * @param course String
	 * @return results List<String> Modules matching course search
	 */
	public List<String> searchYearsByStream(String stream) {
		List<String> results = new LinkedList<String>();
		
		for (int i=0; i<moduleList.size(); i++) {
			if (stream.equals(moduleList.get(i).getStream())) {
				results.add(Integer.toString(moduleList.get(i).getYear()));
			}
		}
		results = removeDuplicates(results);
		return results;
	}
	
	/**
	 * Returns modules by matching course
	 * @param course String
	 * @return results List<String> Modules matching course search
	 */
	public List<String> searchModulesByYear(String year) {
		List<String> results = new LinkedList<String>();
		int iYear = Integer.parseInt(year);
		
		for (int i=0; i<moduleList.size(); i++) {
			if (iYear == moduleList.get(i).getYear()) {
				results.add(moduleList.get(i).getTitle());
			}
		}
		results = removeDuplicates(results);
		return results;
	}
	
	/**
	 * returns all relevant modules when all combo boxes selected
	 * @param String course
	 * @param String stream
	 * @param String year
	 * @return List<String> results
	 */
	public List<String> searchResultModules(String course, String stream, String year) {
		List<String> results = new LinkedList<String>();
		RegisteredModuleEntry tempModule;
		int iYear = Integer.parseInt(year);
		
		for (int i=0; i<moduleList.size(); i++) {
			tempModule = moduleList.get(i);
			if (iYear == tempModule.getYear() 
					&& stream.equals(tempModule.getStream())
					&& course.equals(tempModule.getCourse())) {
				results.add(moduleList.get(i).getTitle());
			}
		}
		return results;
	}
	
	/**
	 * Method that removes duplicate entries in a List<Sting>
	 * @param List<String> input
	 * @return List<String> output
	 */
	private List<String> removeDuplicates(List<String> list) {
		List<String> newList = new LinkedList<String>();
		String searchTerm;
		Boolean found = false;
		
		for (int i=0; i<list.size(); i++) {
			searchTerm = list.get(i);
			if (newList.size() < 1) {
				newList.add(searchTerm);
			}
			for (int j=0; j<newList.size(); j++) {
				if (newList.get(j).equals(searchTerm)) {
					found = true;
					break;
				}
			}
			if (found == false) {
				newList.add(searchTerm);
			}
			else {
				found = false;
			}
		}
		return newList;
	}
}
