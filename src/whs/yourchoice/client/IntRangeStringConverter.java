/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.client;

import javafx.util.StringConverter;

/** 
 * Class for setting the text fields to only accept integers 
 * and have a range of 0-255 for the ip address and 0-65535 for the port
 * 
 * @author cd828
 * @version v0.1 25/03/16
 */
public class IntRangeStringConverter extends StringConverter<Integer> {

    private final int min;
    private final int max;
    private final int length;
    
    /**
     * Method for instantiating the max min values and length of the text fields
     * @param min  -  Minimum value of text field
     * @param max  -  Maximum value of text field
     * @param length  -  Length of the text field
     */
    public IntRangeStringConverter(int min, int max, int length) {
        this.min = min;
        this.max = max;
        this.length = length;
    }
    
    /**
     * Method for setting the length of the text field
     * @param object  -  the number of the length of the text field
     * @return string  -  
     */
    @Override
    public String toString(Integer object) {
        return String.format("%0"+length+"d", object);
    }

    /**
     * Method for setting the maximum and minimum value of the text field
     * @param string
     * @return integer  -  the integer value of the max and min
     */
    @Override
    public Integer fromString(String string) {
        int integer = Integer.parseInt(string);
        if (integer > max || integer < min) {
            throw new IllegalArgumentException();
        }
        return integer;
    }
}
