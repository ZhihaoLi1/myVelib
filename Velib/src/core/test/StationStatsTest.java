package core.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import core.bike.BikeFactory;
import core.bike.InvalidBikeTypeException;
import core.point.Point;
import core.station.InvalidStationTypeException;
import core.station.Station;
import core.station.StationFactory;
import core.station.StationStats;
import core.utils.DateParser;

/**
 * Calculates and increments the statistics for a station
 * @author animato
 *
 */
public class StationStatsTest {

	static StationFactory stationFactory = new StationFactory();
	static Station station;

	@BeforeClass
	public static void createStation() {
		try {
			station = stationFactory.createStation("STANDARD", 2, new Point(0, 0), true);
		} catch (InvalidStationTypeException e) {
			fail("InvalidStationTypeException was thrown");
		}
	}

	@Test
	public void testGetOccupationRate() {
		StationStats stats = new StationStats(station);

		assertTrue(stats.getOccupationRate(DateParser.parse("01/01/2000 08:00:00"),
				DateParser.parse("01/01/2000 09:00:00")) == 0);

		station.getParkingSlots().get(0).setWorking(false, DateParser.parse("01/01/2000 07:00:00"));
		assertTrue(stats.getOccupationRate(DateParser.parse("01/01/2000 08:00:00"),
				DateParser.parse("01/01/2000 09:00:00")) == 0.5);

		try {
			station.getParkingSlots().get(0).setBike(new BikeFactory().createBike("MECH"),
					DateParser.parse("01/01/2000 08:10:00"));
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException was thrown");
		} catch (Exception e) {
			fail("Exception was thrown in testGetOccupationRate when it shouldn't");
		}
		assertTrue(stats.getOccupationRate(DateParser.parse("01/01/2000 08:00:00"),
				DateParser.parse("01/01/2000 09:00:00")) == 0.5);

		station.getParkingSlots().get(1).setWorking(false, DateParser.parse("01/01/2000 08:30:00"));
		assertTrue(stats.getOccupationRate(DateParser.parse("01/01/2000 08:00:00"),
				DateParser.parse("01/01/2000 09:00:00")) == 0.75);

		station.getParkingSlots().get(0).setWorking(true, DateParser.parse("01/01/2000 08:30:00"));
		assertTrue(stats.getOccupationRate(DateParser.parse("01/01/2000 08:00:00"),
				DateParser.parse("01/01/2000 09:00:00")) == 0.75);

		try {
			station.getParkingSlots().get(0).setBike(null, DateParser.parse("01/01/2000 08:45:00"));
		} catch (Exception e) {
			fail("Exception was thrown in testGetOccupationRate when it shouldn't");
		}
		assertTrue(stats.getOccupationRate(DateParser.parse("01/01/2000 08:00:00"),
				DateParser.parse("01/01/2000 09:00:00")) == 0.625);

	}

	@Test
	public void whenGivenNullDateThenThrowException() {
		StationStats stats = new StationStats(station);

		try {
			stats.getOccupationRate(DateParser.parse("01/01/2000 08:00:00"), null);
			fail("NullPointerException wasn't thrown");
		} catch (NullPointerException e) {
			assertTrue(true);
		}
		try {
			stats.getOccupationRate(DateParser.parse(null), DateParser.parse("01/01/2000 09:00:00"));
			fail("NullPointerException wasn't thrown");
		} catch (NullPointerException e) {
			assertTrue(true);
		}

	}

	@Test
	public void testGetTotalRentals() {
		StationStats stats = new StationStats(station);

		assertTrue(stats.getTotalRentals() == 0);
	}

	@Test
	public void testIncrementTotalRentals() {
		StationStats stats = new StationStats(station);

		stats.incrementTotalRentals();
		assertTrue(stats.getTotalRentals() == 1);

		stats.incrementTotalRentals();
		stats.incrementTotalRentals();
		assertTrue(stats.getTotalRentals() == 3);
	}

	@Test
	public void testGetTotalReturns() {
		StationStats stats = new StationStats(station);

		assertTrue(stats.getTotalRentals() == 0);
	}

	@Test
	public void testIncrementTotalReturns() {
		StationStats stats = new StationStats(station);

		stats.incrementTotalReturns();
		assertTrue(stats.getTotalReturns() == 1);

		stats.incrementTotalReturns();
		stats.incrementTotalReturns();
		assertTrue(stats.getTotalReturns() == 3);
	}

}
