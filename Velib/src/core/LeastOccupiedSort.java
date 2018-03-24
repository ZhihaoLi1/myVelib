package core;

import java.util.ArrayList;

import core.station.Station;

/**
 * Sorting strategy of stations based on occupation rate
 * 
 * @author matto
 *
 */
public class LeastOccupiedSort implements SortingStrategy {
	/**
	 * Sort given stations according to their occupation rate, in ascending order
	 * 
	 * @param stations
	 *            - a list of stations to sort
	 * @return ArrayList<Station> - the sorted list of stations
	 */
	public ArrayList<Station> sort(ArrayList<Station> stations) {
		ArrayList<Station> sortedStations = new ArrayList<Station>(stations);

		// TODO: Do the algorithm
		// Discuss with M. Ballarini about management of in-app time in the project
		return sortedStations;
	}

}
