package core.test.junit.station;

import static org.junit.Assert.*;

import org.junit.Test;

import core.station.InvalidStationTypeException;
import core.station.StandardStation;
import core.station.Station;
import core.station.StationFactory;
import utils.Point;

public class StationTest {

	@Test
	public void testEqualsObject() {
		StationFactory stationFactory = new StationFactory();
		Station s1 = null;
		Station s2 = null;
		Station s3 = null;
		try {
			s1 = stationFactory.createStation("STANDARD", 0, new Point(0, 0), true);
			s2 = stationFactory.createStation("STANDARD", 0, new Point(0, 2), true);
			s3 = stationFactory.createStation("PLUS", 0, new Point(3, 1), true);
		} catch (InvalidStationTypeException e) {
			fail("InvalidStationTypeException thrown");
		}
		
		assertTrue(s1.equals(s1));
		assertTrue(!s1.equals(s2));
		assertTrue(!s1.equals(s3));
		assertTrue(s3.equals(s3));
	}

}
