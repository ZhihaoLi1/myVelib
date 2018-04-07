package core.station.stationSort;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

import core.station.InvalidTimeSpanException;
import core.station.Station;

/**
 * Sorting strategy of stations based on occupation rate, in ascending order.
 * 
 * @author matto
 *
 */
public class LeastOccupiedSort implements SortingStrategy {
	/**
	 * Sort given stations according to their occupation rate over a given time
	 * span, in ascending order
	 * 
	 * @param stations
	 *            a list of stations to sort
	 * @param startDate
	 *            the start of the time span the calculation is done over
	 * @param endDate
	 *            the end of the time span the calculation is done over
	 * @return the sorted list of stations
	 */
	public ArrayList<Station> sort(ArrayList<Station> stations, LocalDateTime startDate, LocalDateTime endDate) throws InvalidTimeSpanException {
		ArrayList<Station> sortedStations = new ArrayList<Station>(stations);

		// Selection sort of the ArrayList
		double minOccRate;
		double curOccRate;
		int minOccRateIndex;
		
		for (int i = 0; i < stations.size(); i++) {
			minOccRate = stations.get(i).getStats().getOccupationRate(startDate, endDate);
			minOccRateIndex = i;
			for (int j = i; j < stations.size(); j++) {
				curOccRate = stations.get(j).getStats().getOccupationRate(startDate, endDate);
				if (curOccRate < minOccRate) {
					minOccRate = curOccRate;
					minOccRateIndex = j;
				}
			}

			Collections.swap(sortedStations, minOccRateIndex, i);
		}
		return sortedStations;
	}
}
