package whs.yourchoice.client;

import java.util.Comparator;

import whs.yourchoice.presentation.TimingEntry;

/**
* Class for organising a list that composes of multiple TimingEntry objects
*
* @author cd828 & ch1092
* @version v0.1 28/04/16
*/
public class TimeInPresentationComparator  implements Comparator<TimingEntry> {
	public int compare(TimingEntry o1, TimingEntry o2) {
		Integer time1 = o1.getTimeInPresentation();
		Integer time2 = o2.getTimeInPresentation();
		return time1.compareTo(time2);
	}
}
