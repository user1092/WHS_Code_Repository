/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.presentation;

import java.util.Comparator;

import whs.yourchoice.presentation.SlideEntry;

/**
* Class for organising a list that composes of multiple SlideEntry objects
*
* @author cd828 & ch1092
* @version v0.1 26/05/16
*/
public class SlideIDComparator implements Comparator<SlideEntry> {
	public int compare(SlideEntry s1, SlideEntry s2) {
		Integer slide1 = s1.getSlideID();
		Integer slide2 = s2.getSlideID();
		return slide1.compareTo(slide2);
	}
}
