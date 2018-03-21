package core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import core.MostUsedSort;
import core.SortingStrategy;
import core.point.Point;
import core.station.StandardStation;
import core.station.Station;

public class MostUsedSortTest {

	@Test
	public void testSort() {
		ArrayList<Station> stations = new ArrayList<Station>();
		SortingStrategy mostUsedSort = new MostUsedSort();
		
		assertTrue(stations.equals(mostUsedSort.sort(stations)));
		
		Station station1 = new StandardStation(5, new Point(0, 0));
		Station station2 = new StandardStation(6, new Point(0, 0));
		Station station3 = new StandardStation(7, new Point(0, 0));
		stations.add(station1);
		stations.add(station2);
		stations.add(station3);
		
		System.out.println(stations.size());
		stations.get(1).getStats().incrementTotalReturns();
		
		stations.get(0).getStats().incrementTotalRentals();
		stations.get(0).getStats().incrementTotalReturns();

		stations.get(2).getStats().incrementTotalRentals();
		stations.get(2).getStats().incrementTotalRentals();
		stations.get(2).getStats().incrementTotalReturns();
		
		ArrayList<Station> expectedStations = new ArrayList<Station>();
		expectedStations.add(station3);
		expectedStations.add(station1);
		expectedStations.add(station2);
		
		assertTrue(expectedStations.equals(mostUsedSort.sort(stations)));
	}

}
