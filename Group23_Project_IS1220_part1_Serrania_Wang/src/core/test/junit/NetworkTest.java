package core.test.junit;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

import core.Network;
import core.card.CardVisitorFactory;
import core.card.InvalidCardTypeException;
import core.station.InvalidStationTypeException;
import core.station.PlusStation;
import core.station.Station;
import core.station.StationFactory;
import core.user.User;
import utils.DateParser;
import utils.Point;

/**
 * Test creation of network and methods of Network
 * 
 * @author animato
 *
 */
public class NetworkTest {

	/**
	 * Test the internal version of addUser
	 */
	@Test
	public void addUserTest() {
		Network n = new Network("testNetwork", 10, 10, 10.0, 0.80, 0.5, 0.5, LocalDateTime.now());
		User alice = null;
		try {
			alice = new User("Alice");
		} catch (InvalidCardTypeException e) {
			fail("InvalidCardTypeException thrown");
		}

		// Verify that a user was indeed added
		n.addUser(alice);
		assertEquals(n.getUsers().size(), 1);
	}

	/**
	 * Test that an IllegalArgumentException is thrown when a null user is added
	 */
	@Test
	public void whenAddingNullUserThenThrowException() {
		Network n = new Network("testNetwork", 10, 10, 10.0, 0.80, 0.5, 0.5, LocalDateTime.now());
		try {
			n.addUser(null);
			fail("IllegalArgumentException was thrown");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test that addUser (the version used by the UI) works correctly
	 */
	@Test
	public void addUserUITest() {
		Network n = new Network("testNetwork", 10, 10, 10.0, 0.80, 0.5, 0.5, LocalDateTime.now());

		n.addUser("Bob", "NO_CARD");
		assertEquals(n.getUsers().size(), 1);

		// When a wrong card type is given, no new user should be added

		n.addUser("Bob", "Adzfzg");
		assertEquals(n.getUsers().size(), 1);
	}

	/**
	 * Test that addStation (the version used by the UI) works correctly
	 */
	@Test
	public void addStationUITest() {
		Network n = new Network("EmptyNetwork", 0, 0, 4, 0, 0, 0, LocalDateTime.now());

		n.addStation("STANDARD");
		n.addStation("PLUS");

		assertEquals(n.getStations().size(), 2);

		n.addStation("STANDARD", 2, 2, 10, true);
		assertEquals(n.getStations().size(), 3);

		// When a wrong station type is given, no new station should be added
		n.addStation("NOPE");
		assertEquals(n.getStations().size(), 3);

		// When no station type is given, no new station should be added
		n.addStation((String) null);
		assertEquals(n.getStations().size(), 3);

	}

	/**
	 * Test that an IllegalArgumentException is thrown when a null station is added
	 */
	@Test
	public void whenAddingNullStationThenThrowException() {
		Network n = new Network("testNetwork", 10, 10, 10.0, 0.80, 0.5, 0.5, LocalDateTime.now());
		try {
			n.addStation((Station) null);
			fail("IllegalArgumentException was thrown");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test that addStation works correctly
	 */
	@Test
	public void addStationTest() {
		Network n = new Network("EmptyNetwork", 0, 0, 4, 0, 0, 0, LocalDateTime.now());
		StationFactory stationFactory = new StationFactory();
		Station s = null;
		try {
			s = stationFactory.createStation("STANDARD", 10, new Point(0, 0), true);
		} catch (InvalidStationTypeException e) {
			fail("InvalidStationTypeException thrown");
		}
		n.addStation(s);

		assertEquals(n.getStations().size(), 1);
	}

	/**
	 * Test that the bike rental method exposed to the UI works correctly in normal
	 * conditions
	 */
	@Test
	public void rentBikeTest() {
		// Initialization
		Network n = new Network("testRental", 10, 10, 10.0, 0.80, 0.5, 0.5, LocalDateTime.now());
		User alice = null;
		try {
			alice = new User("Alice");
		} catch (InvalidCardTypeException e) {
			fail("InvalidCardTypeException thrown");
		}

		n.addUser(alice);
		// Find a station where a bike of type MECH can be rented (because of the way
		// the network is created, there necessarily is one)
		for (Station s : n.getStations().values()) {
			if (s.getOnline() && s.hasCorrectBikeType("MECH")) {
				n.rentBike(alice.getId(), s.getId(), "MECH", DateParser.parse("01/01/2000T09:00:00"));
				break;
			}
		}
		// Verify that Alice does have a Bike rental, and that the type of bike is
		// correct
		assertNotEquals(alice.getBikeRental(), null);
		assertEquals(alice.getBikeRental().getBike().getType(), "MECH");

	}
	
	/**
	 * Test that the bike rental method exposed to the UI works correctly in normal
	 * conditions
	 */
	@Test
	public void whenStationHasNoBikeThenNoBikeRentalIsCreated() {
		// Initialization: create stations with no parking slots (and thus no bikes)
		Network n = new Network("testRental", 10, 0, 10.0, 0.80, 0.5, 0.5, LocalDateTime.now());
		User alice = null;
		try {
			alice = new User("Alice");
		} catch (InvalidCardTypeException e) {
			fail("InvalidCardTypeException thrown");
		}

		n.addUser(alice);
		// Try to rent a bike at the first station listed in the list of stations
		n.rentBike(alice.getId(), n.getStationIds().get(0), "MECH", DateParser.parse("01/01/2000T09:00:00"));
		
		// Verify that Alice does not have a Bike rental
		assertEquals(alice.getBikeRental(), null);
	}
	
	/**
	 * Test that the bike rental method exposed to the UI works correctly in normal
	 * conditions
	 */
	@Test
	public void whenStationIsOfflineThenNoBikeRentalIsCreated() {
		// Initialization: create stations with no parking slots (and thus no bikes)
		Network n = new Network("testRental", 10, 0, 10.0, 0.80, 0.5, 0.5, LocalDateTime.now());
		User alice = null;
		try {
			alice = new User("Alice");
		} catch (InvalidCardTypeException e) {
			fail("InvalidCardTypeException thrown");
		}

		n.addUser(alice);
				// Find a station where a bike of type MECH can be rented (because of the way
				// the network is created, there necessarily is one)
				// Then set it offline, and then try to rent from this station
				for (Station s : n.getStations().values()) {
					if (s.getOnline() && s.hasCorrectBikeType("MECH")) {
						n.setOffline(s.getId());
						n.rentBike(alice.getId(), s.getId(), "MECH", DateParser.parse("01/01/2000T09:00:00"));
						break;
					}
				}
		
		// Verify that Alice does not have a Bike rental
		assertEquals(alice.getBikeRental(), null);
	}

	/**
	 * Test that the bike return method exposed to the UI works correctly
	 */
	@Test
	public void returnBikeTest() {
		// Initialization
		Network n = new Network("testRental", 10, 10, 10.0, 0.80, 0.5, 0.5, LocalDateTime.now());
		
		// Create a new user with no card
		User alice = null;
		try {
			alice = new User("alice");
		} catch (InvalidCardTypeException e) {
			fail("InvalidCardTypeException thrown");
		}
		n.addUser(alice);

		// Find a station where a bike of type MECH can be rented (because of the way
		// the network is created, there necessarily is one)
		for (Station s : n.getStations().values()) {
			if (s.getOnline() && s.hasCorrectBikeType("MECH")) {
				n.rentBike(alice.getId(), s.getId(), "MECH", DateParser.parse("01/01/2000T09:00:00"));
				break;
			}
		}

		// Verify that Alice does have a bikeRental, and that the type of bike she has is MECH
		assertNotEquals(alice.getBikeRental(), null);
		assertEquals(alice.getBikeRental().getBike().getType(), "MECH");

		// Return the bike
		for (Station s : n.getStations().values()) {
			if (s.getOnline() && !s.isFull()) {
				n.returnBike(alice.getId(), s.getId(), DateParser.parse("01/01/2000T10:00:00"));
				break;
			}
		}

		// Verify that Alice does not have a bikeRental anymore, and that the total time spent was updated
		assertEquals(alice.getBikeRental(), null);
		assertEquals(alice.getStats().getTotalTimeSpent(), 60);

		// Create a new user with a VLibreCard
		User bob = null;
		try {
			bob = new User("Bob", new CardVisitorFactory().createCard("VLIBRE_CARD"));
		} catch (InvalidCardTypeException e) {
			e.printStackTrace();
		}

		n.addUser(bob);
		bob.getCard().addTimeCredit(60);

		// Find a station where a bike of type MECH can be rented (once again, because of the way
		// the network is created, there necessarily is one)
		for (Station s : n.getStations().values()) {
			if (s.getOnline() && s.hasCorrectBikeType("MECH")) {
				n.rentBike(bob.getId(), s.getId(), "MECH", DateParser.parse("01/01/2000T10:00:00"));
				break;
			}
		}
		
		// Verify that Bob does have a bikeRental, and that the type of bike he has is MECH
		assertNotEquals(bob.getBikeRental(), null);
		assertEquals(bob.getBikeRental().getBike().getType(), "MECH");

		// Return the bike
		for (Station s : n.getStations().values()) {
			if (s.getOnline() && !s.isFull() && s instanceof PlusStation) {
				n.returnBike(bob.getId(), s.getId(), DateParser.parse("01/01/2000T12:00:00"));
				break;
			}
		}

		// Verify that Bob does not have a bikeRental anymore, and that the total time spent was updated
		// Ride of 2 hours, 65 minutes of time credit => 60 minutes of effective time,
		// so 0€ (MechBike, VLibreCardVisitor)
		assertEquals(bob.getBikeRental(), null);
		assertEquals(bob.getStats().getTotalTimeSpent(), 120);
		assertEquals(bob.getStats().getTotalTimeCredits(), 5);
		assertEquals(bob.getCard().getTimeCredit(), 5);

	}
}
