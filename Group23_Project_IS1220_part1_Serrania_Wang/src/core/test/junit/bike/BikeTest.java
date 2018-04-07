package core.test.junit.bike;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import core.bike.Bike;
import core.bike.BikeFactory;
import core.bike.InvalidBikeTypeException;

public class BikeTest {
	Bike mb, mb2, eb, eb2;

	BikeFactory bikeFactory = new BikeFactory();

	@Before
	public void initialize() throws InvalidBikeTypeException {
		mb = bikeFactory.createBike("MECH");
		mb2 = bikeFactory.createBike("MECH");
		eb = bikeFactory.createBike("ELEC");
		eb2 = bikeFactory.createBike("ELEC");
	}

	/**
	 * Test equality between bikes <br>
	 * Different id = different bikes. Same Id, same bike
	 */
	@Test
	public void testEquals() {
		assertEquals(eb, eb);
		assertNotEquals(eb, eb2);

		assertEquals(mb, mb);
		assertNotEquals(mb, mb2);

		assertNotEquals(mb, eb);
	}
}
