package whs.yourchoice.presentation;
/**
 * Registered Module Entry Object
 * 
 * Copyright and Licensing Information if applicable
 */

/**
 * Registered module object, stores module information
 * usually parsed from XML object
 * @author ws659 skw501
 *
 */
public class RegisteredModuleEntry {
	protected String code;
	protected String course;
	protected String stream;
	protected int year;
	protected String title;
	protected String fileName;
	
	/**
	 * Sets module code
	 * @param code String
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * Retrieves module code
	 * @return code String
	 */
	public String getCode() {
		return this.code;
	}
	
	/**
	 * Sets module course
	 * @param course String
	 */
	public void setCourse(String course) {
		this.course = course;
	}
	
	/**
	 * Retrieves module course
	 * @return course String
	 */
	public String getCourse() {
		return this.course;
	}
	
	/**
	 * Sets module stream
	 * @param stream String
	 */
	public void setStream(String stream) {
		this.stream = stream;
	}
	
	/**
	 * Retrieves module stream
	 * @return stream String
	 */
	public String getStream() {
		return this.stream;
	}
	
	/**
	 * Sets module year
	 * @param year int
	 */
	public void setYear(int year) {
		this.year = year;
	}
	
	/**
	 * Retrieves module year
	 * @return year int
	 */
	public int getYear() {
		return this.year;
	}
	
	/**
	 * Sets module title
	 * @param title String
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Retrieves module title
	 * @return title String
	 */
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * Sets fileName
	 * @param fileName String
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * Retrieves fileName
	 * @return fileName String
	 */
	public String getFileName() {
		return this.fileName;
	}

}
