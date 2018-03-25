package core.station.stationSort;

import java.util.ArrayList;

import core.station.Station;

/**
 * Interface for defining sorting strategies for stations
 * 
 * @author matto
 *
 */
public interface SortingStrategy {
	public ArrayList<Station> sort(ArrayList<Station> stations);
}
