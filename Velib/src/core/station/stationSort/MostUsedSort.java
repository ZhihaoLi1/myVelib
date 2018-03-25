package core.station.stationSort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import core.station.Station;

/**
 * Sorting strategy of stations based on usage of stations, in descending order.
 * 
 * @author matto
 *
 */
public class MostUsedSort implements SortingStrategy {
	/**
	 * Sort given stations according to their number of rentals + returns, in
	 * descending order
	 * 
	 * @param stations
	 *            - a list of stations to sort
	 * @return ArrayList<Station> - the sorted list of stations
	 */
	public ArrayList<Station> sort(ArrayList<Station> stations, LocalDateTime startDate, LocalDateTime endDate) {
		ArrayList<Station> sortedStations = new ArrayList<Station>(stations);

		// Selection sort of the ArrayList
		int maxNumOfUses;
		int curNumOfUses;
		int maxNumOfUsesIndex;
		for (int i = 0; i < stations.size(); i++) {
			maxNumOfUses = stations.get(i).getStats().getTotalRentals() + stations.get(i).getStats().getTotalReturns();
			maxNumOfUsesIndex = i;
			for (int j = i; j < stations.size(); j++) {
				curNumOfUses = stations.get(j).getStats().getTotalRentals()
						+ stations.get(j).getStats().getTotalReturns();
				if (curNumOfUses > maxNumOfUses) {
					maxNumOfUses = curNumOfUses;
					maxNumOfUsesIndex = j;
				}
			}

			Collections.swap(sortedStations, maxNumOfUsesIndex, i);
		}
		return sortedStations;
	}
}
