package core.test.junit.station;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import core.Network;
import core.bike.Bike;
import core.bike.BikeFactory;
import core.bike.ElecBike;
import core.bike.InvalidBikeTypeException;
import core.bike.MechBike;
import core.card.CardVisitorFactory;
import core.card.InvalidCardTypeException;
import core.station.BikeNotFoundException;
import core.station.FullStationException;
import core.station.InvalidStationTypeException;
import core.station.OfflineStationException;
import core.station.Station;
import core.station.StationFactory;
import core.user.User;
import utils.DateParser;
import utils.Point;

/**
 * Test rent operation from a station
 * @author animato
 *
 */
public class StationRentTest {

	// Create User

	StationFactory stationFactory = new StationFactory();
	BikeFactory bikeFactory = new BikeFactory();
	CardVisitorFactory cardVisitorFactory = new CardVisitorFactory();

	// Stations
	Station s, emptyS;

	@Before
	public void fillStationAndNetwork() throws InvalidStationTypeException, InvalidBikeTypeException {
		s = stationFactory.createStation("PLUS", 10, new Point(0, 0.1), true);
		emptyS = stationFactory.createStation("PLUS", 10, new Point(0, 0.2), true);
		
		// add bikes to stations
		s.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
		s.addBike(bikeFactory.createBike("ELEC"), LocalDateTime.now());
	}

	@Test
	public void stationHasOneLessBikeOfTheDesiredKindAfterRent() throws BikeNotFoundException, OfflineStationException {
		int mechBikes = s.getNumberOfBikes("MECH");
		int elecBikes = s.getNumberOfBikes("ELEC");
		
		Bike b = s.rentBike("MECH", DateParser.parse("01/01/2000T09:00:00"));
		
		int remainingMechBikes = s.getNumberOfBikes("MECH");
		int remainingElecBikes = s.getNumberOfBikes("ELEC");
		
		assertTrue(b instanceof MechBike);
		assertEquals(mechBikes, remainingMechBikes + 1);
		assertEquals(elecBikes, remainingElecBikes);
		
		b = s.rentBike("ELEC", DateParser.parse("01/01/2000T10:05:00"));
		
		remainingMechBikes = s.getNumberOfBikes("MECH");
		remainingElecBikes = s.getNumberOfBikes("ELEC");
		
		assertTrue(b instanceof ElecBike);
		assertEquals(mechBikes, remainingMechBikes + 1);
		assertEquals(elecBikes, remainingElecBikes + 1);
	}
	
	@Test
	public void whenStationIsOfflineThenThrowException() throws BikeNotFoundException {
		s.setOnline(false);
		try {
			Bike b = s.rentBike("ELEC", DateParser.parse("01/01/2000T10:10:00"));
			fail("OfflineStationException should have been thrown");
		} catch (OfflineStationException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void whenStationDoesNotHaveBikeThenThrowException() throws OfflineStationException {
		try {
			Bike b = emptyS.rentBike("ELEC", DateParser.parse("01/01/2000T10:10:00"));
			fail("BikeNotFoundException should have been thrown");
		} catch (BikeNotFoundException e) {
			assertTrue(true);
		}
	}
}
