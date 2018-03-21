package core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import core.LeastOccupiedSort;
import core.SortingStrategy;
import core.bike.MechBike;
import core.point.Point;
import core.station.StandardStation;
import core.station.Station;
import core.utils.DateParser;

public class LeastOccupiedSortTest {

	@Test
	public void testSort() {
		ArrayList<Station> stations = new ArrayList<Station>();
		SortingStrategy leastOccupiedSort = new LeastOccupiedSort();
		
		assertTrue(stations.equals(leastOccupiedSort.sort(stations)));
		
		Station station1 = new StandardStation(2, new Point(0, 0));
		Station station2 = new StandardStation(2, new Point(0, 0));
		Station station3 = new StandardStation(2, new Point(0, 0));
		stations.add(station1);
		stations.add(station2);
		stations.add(station3);
		
		System.out.println(stations.size());
		stations.get(1).addBike(new MechBike(), DateParser.parse("01/01/2000 00:00:00"));
		
		stations.get(0).addBike(new MechBike(), DateParser.parse("01/01/2000 00:30:00"));

		stations.get(2).addBike(new MechBike(), DateParser.parse("01/01/2000 01:00:00"));
		
		ArrayList<Station> expectedStations = new ArrayList<Station>();
		expectedStations.add(station3);
		expectedStations.add(station1);
		expectedStations.add(station2);
		
		assertTrue(expectedStations.equals(leastOccupiedSort.sort(stations)));	
	}

}
