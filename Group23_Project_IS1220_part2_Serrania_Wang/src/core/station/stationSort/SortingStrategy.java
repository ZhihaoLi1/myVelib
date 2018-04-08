package core.station.stationSort;

import java.time.LocalDateTime;
import java.util.ArrayList;

import core.station.InvalidTimeSpanException;
import core.station.Station;

/**
 * Interface for defining sorting strategies for stations
 * 
 * @author matto
 *
 */
public interface SortingStrategy {
	/**
	 * Sort stations depending on attributes (determined by the concrete strategy
	 * used) for a given time span.
	 * 
	 * @param stations
	 *            a list of stations to sort
	 * @param startDate
	 *            the start of the time span the calculation is done over
	 * @param endDate
	 *            the start of the time span the calculation is done over
	 * @return the sorted list of stations
	 */
	public ArrayList<Station> sort(ArrayList<Station> stations, LocalDateTime startDate, LocalDateTime endDate) throws InvalidTimeSpanException;
}
