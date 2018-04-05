package core.test.junit;

import static org.junit.Assert.*;

import org.junit.Test;

import core.bike.BikeIDGenerator;
import core.station.ParkingSlotIDGenerator;
import core.station.StationIDGenerator;
import core.user.UserIDGenerator;

/*
 * Test Id generation 
 * ensure the IDgenerator classes allows only creation of a unique object
 */
public class IDGeneratorTest {

	/**
	 * Ensure that only a single instance of the generator is created and used
	 * gen1 increment should effect gen2 
	 */
	@Test
	public void testStationIDGenerator() {
		StationIDGenerator gen1 = StationIDGenerator.getInstance();
		StationIDGenerator gen2 = StationIDGenerator.getInstance();

		for (int i = 1; i < 5; i++) {
			assertEquals(gen1.getNextIDNumber(), i * 2 - 1);
			assertEquals(gen2.getNextIDNumber(), i * 2);
		}
	}
	
	/**
	 * Ensure that only a single instance of the generator is created and used
	 * gen1 increment should effect gen2 
	 */
	@Test
	public void testBikeIDGenerator() {
		BikeIDGenerator gen1 = BikeIDGenerator.getInstance();
		BikeIDGenerator gen2 = BikeIDGenerator.getInstance();

		for (int i = 1; i < 5; i++) {
			assertEquals(gen1.getNextIDNumber(), i * 2 - 1);
			assertEquals(gen2.getNextIDNumber(), i * 2);
		}
	}
	
	/**
	 * Ensure that only a single instance of the generator is created and used
	 * gen1 increment should effect gen2 
	 */
	@Test
	public void testParkingSlotIDGenerator() {
		ParkingSlotIDGenerator gen1 = ParkingSlotIDGenerator.getInstance();
		ParkingSlotIDGenerator gen2 = ParkingSlotIDGenerator.getInstance();

		for (int i = 1; i < 5; i++) {
			assertEquals(gen1.getNextIDNumber(), i * 2 - 1);
			assertEquals(gen2.getNextIDNumber(), i * 2);
		}
	}
	
	/**
	 * Ensure that only a single instance of the generator is created and used
	 * gen1 increment should effect gen2 
	 */
	@Test
	public void testUserIDGenerator() {
		UserIDGenerator gen1 = UserIDGenerator.getInstance();
		UserIDGenerator gen2 = UserIDGenerator.getInstance();

		for (int i = 1; i < 5; i++) {
			assertEquals(gen1.getNextIDNumber(), i * 2 - 1);
			assertEquals(gen2.getNextIDNumber(), i * 2);
		}
	}
}
