package core.test.junit.station;

import static org.junit.Assert.*;

import org.junit.Test;

import core.bike.Bike;
import core.bike.BikeFactory;
import core.bike.ElecBike;
import core.bike.InvalidBikeTypeException;
import core.bike.MechBike;
import core.station.InvalidStationTypeException;
import core.station.PlusStation;
import core.station.StandardStation;
import core.station.Station;
import core.station.StationFactory;
import utils.Point;

public class StationFactoryTest {

	StationFactory stationFactory = new StationFactory();

	/**
	 * Test that the type of the created station corresponds to the given type.
	 */
	@Test
	public void testCreateBike() throws InvalidStationTypeException {		
		Station mb = stationFactory.createStation("STANDARD", 10, new Point(0, 0), true);
		assertTrue(mb instanceof StandardStation);
		
		Station eb = stationFactory.createStation("PLUS", 10, new Point(0, 0), true);
		assertTrue(eb instanceof PlusStation);
	}
	
	/**
	 * Test that when a wrong bike type is given, an InvalidBikeTypeException is thrown.
	 */
	@Test
	public void whenWrongBikeTypeGivenThenThrowException() {
		try {
			Station mb = stationFactory.createStation(null, 10, new Point(0, 0), true);
			fail("InvalidBikeTypeException should have been thrown");
		} catch (InvalidStationTypeException e) {
			assertTrue(true);
		}
		
		try {
			Station mb = stationFactory.createStation("NOPE", 10, new Point(0, 0), true);
			fail("InvalidBikeTypeException should have been thrown");
		} catch (InvalidStationTypeException e) {
			assertTrue(true);
		}
	}

}
