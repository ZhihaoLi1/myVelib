package core;

import java.util.ArrayList;

import core.station.Station;

public interface SortingStrategy {
	public ArrayList<Station> sort(ArrayList<Station> stations);
}
