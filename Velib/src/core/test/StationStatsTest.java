package core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import core.BikeType;
import core.bike.BikeFactory;
import core.bike.InvalidBikeTypeException;
import core.bike.MechBike;
import core.point.Point;
import core.station.StandardStation;
import core.station.Station;
import core.station.StationStats;
import core.utils.DateParser;

public class StationStatsTest {

	@Test
	public void testGetOccupationRate() {
		Station station = new StandardStation(2, new Point(0,0));
		StationStats stats = new StationStats(station);
		
		assertTrue(stats.getOccupationRate(DateParser.parse("01/01/2000 08:00:00"), DateParser.parse("01/01/2000 09:00:00")) == 0);
		
		station.getParkingSlots().get(0).setWorking(false, DateParser.parse("01/01/2000 07:00:00"));
		assertTrue(stats.getOccupationRate(DateParser.parse("01/01/2000 08:00:00"), DateParser.parse("01/01/2000 09:00:00")) == 0.5);
		
		try {
			station.getParkingSlots().get(0).setBike(new BikeFactory().createBike(BikeType.MECH), DateParser.parse("01/01/2000 08:10:00"));
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException was thrown");
		} catch (Exception e) {
			fail("Exception was thrown in testGetOccupationRate when it shouldn't");
		}
		assertTrue(stats.getOccupationRate(DateParser.parse("01/01/2000 08:00:00"), DateParser.parse("01/01/2000 09:00:00")) == 0.5);
		
		station.getParkingSlots().get(1).setWorking(false, DateParser.parse("01/01/2000 08:30:00"));
		assertTrue(stats.getOccupationRate(DateParser.parse("01/01/2000 08:00:00"), DateParser.parse("01/01/2000 09:00:00")) == 0.75);
		
		station.getParkingSlots().get(0).setWorking(true, DateParser.parse("01/01/2000 08:30:00"));
		assertTrue(stats.getOccupationRate(DateParser.parse("01/01/2000 08:00:00"), DateParser.parse("01/01/2000 09:00:00")) == 0.75);
		
		try {
			station.getParkingSlots().get(0).setBike(null, DateParser.parse("01/01/2000 08:45:00"));
		} catch (Exception e) {
			fail("Exception was thrown in testGetOccupationRate when it shouldn't");
		}
		assertTrue(stats.getOccupationRate(DateParser.parse("01/01/2000 08:00:00"), DateParser.parse("01/01/2000 09:00:00")) == 0.625);

	}
	
	@Test
	public void whenGivenNullDateThenThrowException() {
		Station station = new StandardStation(2, new Point(0,0));
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
		Station station = new StandardStation(1, new Point(0,0));
		StationStats stats = new StationStats(station);
		
		assertTrue(stats.getTotalRentals() == 0);
	}

	@Test
	public void testIncrementTotalRentals() {
		Station station = new StandardStation(1, new Point(0,0));
		StationStats stats = new StationStats(station);
		
		stats.incrementTotalRentals();
		assertTrue(stats.getTotalRentals() == 1);
		
		stats.incrementTotalRentals();		
		stats.incrementTotalRentals();
		assertTrue(stats.getTotalRentals() == 3);
	}

	@Test
	public void testGetTotalReturns() {
		Station station = new StandardStation(1, new Point(0,0));
		StationStats stats = new StationStats(station);
		
		assertTrue(stats.getTotalRentals() == 0);	
	}

	@Test
	public void testIncrementTotalReturns() {
		Station station = new StandardStation(1, new Point(0,0));
		StationStats stats = new StationStats(station);
		
		stats.incrementTotalReturns();
		assertTrue(stats.getTotalReturns() == 1);
		
		stats.incrementTotalReturns();		
		stats.incrementTotalReturns();
		assertTrue(stats.getTotalReturns() == 3);	
	}

}
