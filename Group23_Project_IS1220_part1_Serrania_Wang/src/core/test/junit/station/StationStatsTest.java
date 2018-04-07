package core.test.junit.station;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import core.bike.BikeFactory;
import core.bike.InvalidBikeTypeException;
import core.station.InvalidStationTypeException;
import core.station.InvalidTimeSpanException;
import core.station.OccupiedParkingSlotException;
import core.station.Station;
import core.station.StationFactory;
import core.station.StationStats;
import utils.DateParser;
import utils.Point;

/**
 * Calculates and increments the statistics for a station
 * 
 * @author animato
 *
 */
public class StationStatsTest {

	static StationFactory stationFactory = new StationFactory();
	static Station station;

	@BeforeClass
	public static void createStation() throws InvalidStationTypeException {
		station = stationFactory.createStation("STANDARD", 2, new Point(0, 0), true);
	}

	/**
	 * Test occupation rate calculation of a station. In this example, the station
	 * has two parking slots
	 */
	@Test
	public void testGetOccupationRate()
			throws InvalidTimeSpanException, InvalidBikeTypeException, OccupiedParkingSlotException {
		StationStats stats = new StationStats(station);

		// PS1: FREE from 8:00 to 9:00
		// PS2: FREE from 8:00 to 9:00
		// Occupation Rate (8:00 - 9:00): 0
		assertTrue(stats.getOccupationRate(DateParser.parse("01/01/2000T08:00:00"),
				DateParser.parse("01/01/2000T09:00:00")) == 0);

		// PS1: OUT_OF_ORDER from 7:00 to 9:00
		// PS2: FREE from 8:00 to 9:00
		// Occupation Rate (8:00 - 9:00): 0.5
		station.getParkingSlots().get(0).setWorking(false, DateParser.parse("01/01/2000T07:00:00"));
		assertTrue(stats.getOccupationRate(DateParser.parse("01/01/2000T08:00:00"),
				DateParser.parse("01/01/2000T09:00:00")) == 0.5);

		// PS1: OUT_OF_ORDER from 7:00 to 8:30, FREE from 8:30 to 9:00
		// PS2: FREE from 8:00 to 9:00
		// Occupation Rate (8:00 - 9:00): 0.25
		station.getParkingSlots().get(0).setWorking(true, DateParser.parse("01/01/2000T08:30:00"));
		assertTrue(stats.getOccupationRate(DateParser.parse("01/01/2000T08:00:00"),
				DateParser.parse("01/01/2000T09:00:00")) == 0.25);

		// PS1: OUT_OF_ORDER from 7:00 to 8:30, OCCUPIED from 8:30 to 9:00
		// PS2: FREE from 8:00 to 9:00
		// Occupation Rate (8:00 - 9:00): 0.5
		station.getParkingSlots().get(0).setBike(new BikeFactory().createBike("MECH"),
				DateParser.parse("01/01/2000T08:30:00"));
		assertTrue(stats.getOccupationRate(DateParser.parse("01/01/2000T08:00:00"),
				DateParser.parse("01/01/2000T09:00:00")) == 0.5);

		// PS1: OUT_OF_ORDER from 7:00 to 8:30, OCCUPIED from 8:30 to 9:00
		// PS2: FREE from 8:00 to 8:30, OUT_OF_ORDER from 8:30 to 9:00
		// Occupation Rate (8:00 - 9:00): 0.75
		station.getParkingSlots().get(1).setWorking(false, DateParser.parse("01/01/2000T08:30:00"));
		assertTrue(stats.getOccupationRate(DateParser.parse("01/01/2000T08:00:00"),
				DateParser.parse("01/01/2000T09:00:00")) == 0.75);

		station.getParkingSlots().get(0).emptyBike(DateParser.parse("01/01/2000T08:45:00"));
		assertTrue(stats.getOccupationRate(DateParser.parse("01/01/2000T08:00:00"),
				DateParser.parse("01/01/2000T09:00:00")) == 0.625);

	}

	/**
	 * InvalidTimeSpanException should be thrown when the given dates are wrong
	 */
	@Test
	public void whenGivenInvalidDatesThenThrowException() {
		StationStats stats = new StationStats(station);

		// Try with startDate or endDate null
		try {
			stats.getOccupationRate(DateParser.parse("01/01/2000T08:00:00"), null);
			fail("InvalidTimeSpanException wasn't thrown");
		} catch (InvalidTimeSpanException e) {
			assertTrue(true);
		}

		try {
			stats.getOccupationRate(null, DateParser.parse("01/01/2000T09:00:00"));
			fail("InvalidTimeSpanException wasn't thrown");
		} catch (InvalidTimeSpanException e) {
			assertTrue(true);
		}

		// Try with startDate > endDate
		try {
			stats.getOccupationRate(DateParser.parse("01/01/2000T10:00:00"), DateParser.parse("01/01/2000T09:00:00"));
			fail("InvalidTimeSpanException wasn't thrown");
		} catch (InvalidTimeSpanException e) {
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
