package core.test.junit.station;

import static org.junit.Assert.*;

import org.junit.Test;

import core.bike.BikeFactory;
import core.bike.InvalidBikeTypeException;
import core.station.OccupiedParkingSlotException;
import core.station.ParkingSlot;
import utils.DateParser;

public class ParkingSlotTest {

	@Test
	public void testGetOccupationTime() throws InvalidBikeTypeException, OccupiedParkingSlotException {
		ParkingSlot ps = new ParkingSlot();
		
		assertTrue(ps.getOccupationTime(DateParser.parse("01/01/2000T09:00:00"), DateParser.parse("01/01/2000T10:00:00")) == 0);
		ps.setBike(new BikeFactory().createBike("MECH"), DateParser.parse("01/01/2000T09:30:00"));
		assertTrue(ps.getOccupationTime(DateParser.parse("01/01/2000T09:00:00"), DateParser.parse("01/01/2000T10:00:00")) == 1800);
	}
	
	/**
	 * When the given dates do not allow computation of the occupation time, throw IllegalArgumentException
	 */
	@Test
	public void whenGivenTimesAreWrongThrowException() throws InvalidBikeTypeException, OccupiedParkingSlotException {
		ParkingSlot ps = new ParkingSlot();
		ps.setBike(new BikeFactory().createBike("MECH"), DateParser.parse("01/01/2000T09:30:00"));

		try {
			ps.getOccupationTime(DateParser.parse("01/01/2000T09:00:00"), DateParser.parse("01/01/2000T08:00:00"));
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			ps.getOccupationTime(null, DateParser.parse("01/01/2000T08:00:00"));
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			ps.getOccupationTime(DateParser.parse("01/01/2000T09:00:00"), null);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testEquals() {
		ParkingSlot ps1 = new ParkingSlot();
		ParkingSlot ps2 = new ParkingSlot();
		
		assertEquals(ps1, ps1);
		assertNotEquals(ps1, ps2);

	}

}
