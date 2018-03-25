package core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import core.bike.BikeFactory;
import core.bike.InvalidBikeTypeException;
import core.point.Point;
import core.station.InvalidStationTypeException;
import core.station.Station;
import core.station.StationFactory;
import core.station.stationSort.LeastOccupiedSort;
import core.station.stationSort.SortingStrategy;
import core.utils.DateParser;

// FIXME: Comments
public class LeastOccupiedSortTest {

	@Test
	public void testSort() {
		ArrayList<Station> stations = new ArrayList<Station>();
		SortingStrategy leastOccupiedSort = new LeastOccupiedSort();

		assertTrue(stations.equals(leastOccupiedSort.sort(stations)));

		StationFactory stationFactory = new StationFactory();
		Station station1 = null, station2 = null, station3 = null;
		try {
			station1 = stationFactory.createStation("STANDARD", 2, new Point(0, 0), true);
			station2 = stationFactory.createStation("STANDARD", 2, new Point(0, 0), true);
			station3 = stationFactory.createStation("STANDARD", 2, new Point(0, 0), true);
		} catch (InvalidStationTypeException e) {
			fail("InvalidStationTypeException was thrown");
		}

		stations.add(station1);
		stations.add(station2);
		stations.add(station3);

		System.out.println(stations.size());
		BikeFactory bikeFactory = new BikeFactory();
		try {
			stations.get(1).addBike(bikeFactory.createBike("MECH"), DateParser.parse("01/01/2000 00:00:00"));
			stations.get(0).addBike(bikeFactory.createBike("MECH"), DateParser.parse("01/01/2000 00:30:00"));
			stations.get(2).addBike(bikeFactory.createBike("MECH"), DateParser.parse("01/01/2000 01:00:00"));
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException was thrown");
		}

		ArrayList<Station> expectedStations = new ArrayList<Station>();
		expectedStations.add(station3);
		expectedStations.add(station1);
		expectedStations.add(station2);

		assertTrue(expectedStations.equals(leastOccupiedSort.sort(stations)));
	}

}
