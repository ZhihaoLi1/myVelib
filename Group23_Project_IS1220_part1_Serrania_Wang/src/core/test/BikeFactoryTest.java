package core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import core.bike.Bike;
import core.bike.BikeFactory;
import core.bike.ElecBike;
import core.bike.InvalidBikeTypeException;
import core.bike.MechBike;

public class BikeFactoryTest {

	/**
	 * Test that the type of the created bike corresponds to the given type.
	 */
	@Test
	public void testCreateBike() {
		BikeFactory bikeFactory = new BikeFactory();
		
		try {
			Bike mb = bikeFactory.createBike("MECH");
			assertTrue(mb instanceof MechBike);
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException thrown");
		}
		
		try {
			Bike eb = bikeFactory.createBike("ELEC");
			assertTrue(eb instanceof ElecBike);
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException thrown");
		}
	}
	
	/**
	 * Test that when a wrong bike type is given, an InvalidBikeTypeException is thrown.
	 */
	@Test
	public void whenWrongBikeTypeGivenThenThrowException() {
		BikeFactory bikeFactory = new BikeFactory();
		
		try {
			Bike mb = bikeFactory.createBike(null);
			fail("InvalidBikeTypeException should have been thrown");
		} catch (InvalidBikeTypeException e) {
			assertTrue(true);
		}
	}

}
