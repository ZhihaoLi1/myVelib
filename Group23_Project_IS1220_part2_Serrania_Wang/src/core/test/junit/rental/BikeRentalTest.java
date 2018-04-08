package core.test.junit.rental;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

import core.bike.BikeFactory;
import core.bike.InvalidBikeTypeException;
import core.bike.MechBike;
import core.rentals.BikeRental;
import utils.DateParser;

public class BikeRentalTest {

	/**
	 * Test that the constructor does work as planned in normal conditions
	 */
	@Test
	public void testConstructor() throws InvalidBikeTypeException {
		BikeFactory bikeFactory = new BikeFactory();
		BikeRental br = new BikeRental(bikeFactory.createBike("MECH"), DateParser.parse("01/01/2000T09:00:00"));
		assertTrue(br.getBike() instanceof MechBike);
		assertEquals(br.getRentDate(), LocalDateTime.of(2000, 1, 1, 9, 0, 0));
	}
	
	/**
	 * Test that the constructor throws an IllegalArgumentException when no bike is given.
	 * The bikeRental should by definition be created with a bike, to avoid problems when returning bikeRentals.
	 */
	@Test
	public void whenGivenBikeIsNullThenThrowException() {
		try {
			BikeRental br = new BikeRental(null, DateParser.parse("01/01/2000T09:00:00"));
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testEquals() throws InvalidBikeTypeException {
		BikeFactory bikeFactory = new BikeFactory();
		
		BikeRental br1 = new BikeRental(bikeFactory.createBike("MECH"), DateParser.parse("01/01/2000T09:00:00"));
		BikeRental br2 = new BikeRental(bikeFactory.createBike("ELEC"), DateParser.parse("01/01/2000T09:00:00"));
		BikeRental br3 = new BikeRental(br1.getBike(), DateParser.parse("01/01/2000T09:00:00"));
		
		assertEquals(br1, br1);
		assertNotEquals(br1, br2);
		assertEquals(br1, br3);
	}

}
