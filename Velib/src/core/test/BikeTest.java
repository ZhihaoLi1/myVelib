package core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import core.bike.Bike;
import core.bike.BikeFactory;
import core.bike.BikeType;
import core.bike.InvalidBikeTypeException;

public class BikeTest {

	/**
	 * Test equality between bikes
	 * Different id = different bikes. Same Id, same bike
	 */
	@Test
	public void testEquals() {
		BikeFactory bikeFactory = new BikeFactory();

		try {
			Bike eb = bikeFactory.createBike(BikeType.ELEC);
			Bike eb2 = bikeFactory.createBike(BikeType.ELEC);

			assertTrue(eb.equals(eb));
			assertTrue(!eb.equals(eb2));
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException thrown");
		}

		try {
			Bike mb = bikeFactory.createBike(BikeType.MECH);
			Bike mb2 = bikeFactory.createBike(BikeType.MECH);

			assertTrue(mb.equals(mb));
			assertTrue(!mb.equals(mb2));
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException thrown");
		}
	}

	/**
	 * Test equality of hashcodes between bikes
	 * Different id = different bikes. Same Id, same bike
	 */
	@Test
	public void testHashCodes() {
		BikeFactory bikeFactory = new BikeFactory();

		try {
			Bike eb = bikeFactory.createBike(BikeType.ELEC);
			Bike eb2 = bikeFactory.createBike(BikeType.ELEC);

			assertTrue(eb.hashCode() != eb2.hashCode());
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException thrown");
		}

		try {
			Bike mb = bikeFactory.createBike(BikeType.MECH);
			Bike mb2 = bikeFactory.createBike(BikeType.MECH);

			assertTrue(mb.hashCode() != mb2.hashCode());
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException thrown");
		}
	}
}
