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
	public void testEquals() throws InvalidStationTypeException {
		StationFactory stationFactory = new StationFactory();
		
		Station s1 = stationFactory.createStation("STANDARD", 0, new Point(0, 0), true);
		Station s2 = stationFactory.createStation("STANDARD", 0, new Point(0, 2), true);
		Station s3 = stationFactory.createStation("PLUS", 0, new Point(3, 1), true);
		
		assertTrue(s1.equals(s1));
		assertTrue(!s1.equals(s2));
		assertTrue(!s1.equals(s3));
		assertTrue(s3.equals(s3));
	}

}
