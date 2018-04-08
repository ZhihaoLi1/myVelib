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
import core.station.BikeNotFoundException;
import core.station.FullStationException;
import core.station.InvalidStationTypeException;
import core.station.OfflineStationException;
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
	public void fillStationAndNetwork()
			throws InvalidStationTypeException, InvalidBikeTypeException, InvalidCardTypeException {
		s = stationFactory.createStation("PLUS", 10, new Point(0, 0.1), true);
		emptyS = stationFactory.createStation("PLUS", 10, new Point(0, 0.2), true);
		fullS = stationFactory.createStation("PLUS", 0, new Point(0, 0.2), true);

		bob = new User("Bob", new Point(0, 0), cardVisitorFactory.createCard("NO_CARD"));
		alice = new User("Alice", new Point(5, 2), cardVisitorFactory.createCard("NO_CARD"));

		// add bikes to stations
		s.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
		s.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
		s.addBike(bikeFactory.createBike("ELEC"), LocalDateTime.now());

		// add user and station to network
		n.addUser(bob);
		n.addStation(s);
		n.addStation(emptyS);
	}

	@Test
	public void stationHasOneMoreBikeOfTheDesiredKindAfterReturn()
			throws FullStationException, OfflineStationException, BikeNotFoundException {
		Bike b = s.rentBike("MECH", DateParser.parse("01/01/2000T09:00:00"));
		emptyS.returnBike(new BikeRental(b, DateParser.parse("01/01/2000T09:00:00")),
				DateParser.parse("01/01/2000T10:00:00"));

		int numMechBikes = emptyS.getNumberOfBikes("MECH");
		int numElecBikes = emptyS.getNumberOfBikes("ELEC");
		assertEquals(1, numMechBikes);
		assertEquals(0, numElecBikes);

		b = s.rentBike("ELEC", DateParser.parse("01/01/2000T10:00:00"));
		emptyS.returnBike(new BikeRental(b, DateParser.parse("01/01/2000T10:00:00")),
				DateParser.parse("01/01/2000T11:00:00"));

		numMechBikes = emptyS.getNumberOfBikes("MECH");
		numElecBikes = emptyS.getNumberOfBikes("ELEC");
		assertEquals(1, numMechBikes);
		assertEquals(1, numElecBikes);
	}

	@Test
	public void whenStationIsFullThenThrowException() throws InvalidBikeTypeException, OfflineStationException {
		Bike b = null;

		b = bikeFactory.createBike("MECH");

		try {
			fullS.returnBike(new BikeRental(b, DateParser.parse("01/01/2000T10:00:00")),
					DateParser.parse("01/01/2000T11:00:00"));
			fail("FullStationException should have been thrown");
		} catch (FullStationException e) {
			assertTrue(true);
		}
	}

	@Test
	public void whenStationIsOfflineThenThrowException() throws InvalidBikeTypeException, FullStationException {
		Bike b = bikeFactory.createBike("MECH");

		emptyS.setOnline(false);

		try {
			emptyS.returnBike(new BikeRental(b, DateParser.parse("01/01/2000T10:00:00")),
					DateParser.parse("01/01/2000T11:00:00"));
			fail("OfflineStationException should have been thrown");
		} catch (OfflineStationException e) {
			assertTrue(true);
		}
	}
}
