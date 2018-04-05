package core.test.junit.station;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import core.Network;
import core.bike.Bike;
import core.bike.BikeFactory;
import core.bike.InvalidBikeTypeException;
import core.card.CardVisitorFactory;
import core.card.InvalidCardTypeException;
import core.rentals.BikeRental;
import core.station.FullStationException;
import core.station.InvalidStationTypeException;
import core.station.Station;
import core.station.StationFactory;
import core.user.User;
import utils.DateParser;
import utils.Point;

public class StationReturnTest {

	// create a network
	static Network n = new Network("EmptyNetwork", 0, 0, 4, 0, 0, 0, LocalDateTime.now());
	// Create User
	static User bob, alice;

	static StationFactory stationFactory = new StationFactory();
	static BikeFactory bikeFactory = new BikeFactory();
	static CardVisitorFactory cardVisitorFactory = new CardVisitorFactory();

	// Stations
	static Station s, emptyS, fullS;

	@Before
	public void fillStationAndNetwork() {
		try {
			s = stationFactory.createStation("PLUS", 10, new Point(0, 0.1), true);
			emptyS = stationFactory.createStation("PLUS", 10, new Point(0, 0.2), true);
			fullS = stationFactory.createStation("PLUS", 0, new Point(0, 0.2), true);
		} catch (InvalidStationTypeException e) {
			fail("InvalidStationTypeException was thrown");
		}

		try {
			bob = new User("Bob", new Point(0, 0), cardVisitorFactory.createCard("NO_CARD"));
			alice = new User("Alice", new Point(5, 2), cardVisitorFactory.createCard("NO_CARD"));
		} catch (InvalidCardTypeException e) {
			fail("InvalidCardTypeException was thrown");
		}

		// add bikes to stations
		try {
			s.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
			s.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
			s.addBike(bikeFactory.createBike("ELEC"), LocalDateTime.now());
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException was thrown");
		}

		// add user and station to network
		n.addUser(bob);
		n.addStation(s);
		n.addStation(emptyS);
	}

	@Test
	public void stationHasOneMoreBikeOfTheDesiredKindAfterReturn() {

		Bike b = s.rentBike("MECH", DateParser.parse("01/01/2000T09:00:00"));
		try {
			emptyS.returnBike(new BikeRental(b, DateParser.parse("01/01/2000T09:00:00")),
					DateParser.parse("01/01/2000T10:00:00"));
		} catch (FullStationException e) {
			fail("FullStationException thrown");
		}
		int numMechBikes = emptyS.getNumberOfBikes("MECH");
		int numElecBikes = emptyS.getNumberOfBikes("ELEC");
		assertEquals(1, numMechBikes);
		assertEquals(0, numElecBikes);

		b = s.rentBike("ELEC", DateParser.parse("01/01/2000T10:00:00"));
		try {
			emptyS.returnBike(new BikeRental(b, DateParser.parse("01/01/2000T10:00:00")),
					DateParser.parse("01/01/2000T11:00:00"));
		} catch (FullStationException e) {
			fail("FullStationException thrown");
		}
		numMechBikes = emptyS.getNumberOfBikes("MECH");
		numElecBikes = emptyS.getNumberOfBikes("ELEC");
		assertEquals(1, numMechBikes);
		assertEquals(1, numElecBikes);
	}

	@Test
	public void whenStationIsFullThenThrowException() {
		Bike b = s.rentBike("ELEC", DateParser.parse("01/01/2000T10:00:00"));
		try {
			fullS.returnBike(new BikeRental(b, DateParser.parse("01/01/2000T10:00:00")),
					DateParser.parse("01/01/2000T11:00:00"));
			fail("FullStationException should have been thrown");
		} catch (FullStationException e) {
			assertTrue(true);
		}	
	}

}
