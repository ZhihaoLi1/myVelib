package core.test.junit.stationSort;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import core.bike.BikeFactory;
import core.bike.InvalidBikeTypeException;
import core.station.FullStationException;
import core.station.InvalidStationTypeException;
import core.station.InvalidTimeSpanException;
import core.station.Station;
import core.station.StationFactory;
import core.station.stationSort.LeastOccupiedSort;
import core.station.stationSort.SortingStrategy;
import utils.DateParser;
import utils.Point;

/**
 * Test sorting of the stations from the least occupied to the most occupied
 * @author animato
 *
 */
public class LeastOccupiedSortTest {
	ArrayList<Station> stations = new ArrayList<Station>();;
	Station station1, station2, station3;
	
	SortingStrategy leastOccupiedSort = new LeastOccupiedSort();
	
	StationFactory stationFactory = new StationFactory();
	BikeFactory bikeFactory = new BikeFactory();

	@Before
	public void initialize() throws InvalidStationTypeException, InvalidBikeTypeException {		
		station1 = stationFactory.createStation("STANDARD", 2, new Point(0, 0), true);
		station2 = stationFactory.createStation("STANDARD", 2, new Point(0, 0), true);
		station3 = stationFactory.createStation("STANDARD", 2, new Point(0, 0), true);

		stations.add(station1);
		stations.add(station2);
		stations.add(station3);

		BikeFactory bikeFactory = new BikeFactory();
		stations.get(1).addBike(bikeFactory.createBike("MECH"), DateParser.parse("01/01/2000T00:00:00"));
		stations.get(0).addBike(bikeFactory.createBike("MECH"), DateParser.parse("01/01/2000T00:30:00"));
		stations.get(2).addBike(bikeFactory.createBike("MECH"), DateParser.parse("01/01/2000T01:00:00"));
	}

	@Test
	public void testSort() throws InvalidTimeSpanException {
		// Verify that it works for an empty list of stations
		assertTrue(new ArrayList<Station>().equals(leastOccupiedSort.sort(new ArrayList<Station>(), DateParser.parse("01/01/2000T00:00:00"), DateParser.parse("01/01/2000T01:00:00"))));

		ArrayList<Station> expectedStations = new ArrayList<Station>();
		expectedStations.add(station3);
		expectedStations.add(station1);
		expectedStations.add(station2);
		
		assertTrue(expectedStations.equals(leastOccupiedSort.sort(stations, DateParser.parse("01/01/2000T00:00:00"), DateParser.parse("01/01/2000T01:00:00"))));
	}
	
	@Test
	public void whenGivenWrongTimeSpanThenThrowException() {		
		try {
			leastOccupiedSort.sort(stations, DateParser.parse("01/01/2000T01:00:00"), DateParser.parse("01/01/2000T00:00:00"));
			fail("InvalidTimeSpanException should have been thrown");
		} catch (InvalidTimeSpanException e) {
			assertTrue(true);
		}
		
		try {
			leastOccupiedSort.sort(stations, DateParser.parse("01/01/2000T00:00:00"), DateParser.parse("01/01/2000T00:00:00"));
			fail("InvalidTimeSpanException should have been thrown");
		} catch (InvalidTimeSpanException e) {
			assertTrue(true);
		}
		
		try {
			leastOccupiedSort.sort(stations, null, DateParser.parse("01/01/2000T00:00:00"));
			fail("InvalidTimeSpanException should have been thrown");
		} catch (InvalidTimeSpanException e) {
			assertTrue(true);
		}
		
		try {
			leastOccupiedSort.sort(stations, DateParser.parse("01/01/2000T00:00:00"), null);
			fail("InvalidTimeSpanException should have been thrown");
		} catch (InvalidTimeSpanException e) {
			assertTrue(true);
		}
	}

}
