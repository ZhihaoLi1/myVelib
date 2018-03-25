package core.test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.BeforeClass;
import org.junit.Test;

import core.Network;
import core.bike.BikeFactory;
import core.bike.InvalidBikeTypeException;
import core.card.CardVisitorFactory;
import core.card.InvalidCardTypeException;
import core.station.InvalidStationTypeException;
import core.station.Station;
import core.station.StationFactory;
import core.user.User;
import core.utils.Point;

/**
 * Test rent operation from  a station
 * @author animato
 *
 */
public class StationRentTest {

	// create a network
	static Network n = new Network();
	// Create User
	static User bob;

	static StationFactory stationFactory = new StationFactory();
	static BikeFactory bikeFactory = new BikeFactory();
	static CardVisitorFactory cardVisitorFactory = new CardVisitorFactory();

	// Stations
	static Station s;
	static Station emptyS;

	@BeforeClass
	public static void fillStationAndNetwork() {
		try {
			s = stationFactory.createStation("PLUS", 10, new Point(0, 0.1), true);
			emptyS = stationFactory.createStation("PLUS", 10, new Point(0, 0.2), true);
		} catch (InvalidStationTypeException e) {
			fail("InvalidStationTypeException was thrown");
		}
		
		try {
			bob = new User("bob", new Point(0, 0), cardVisitorFactory.createCard("NO_CARD"));
		} catch (InvalidCardTypeException e) {
			fail("InvalidCardTypeException was thrown");
		}
		
		// add bikes to stations
		try {
			s.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
			s.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException was thrown");
		} catch (Exception e) {
			fail("Exception was thrown");
		}

		// add user and station to network
		n.createUser(bob);
		n.createStation(s);
		n.createStation(emptyS);
	}

	@Test
	public void stationHasOneLessBikeOfTheDesiredKindAfterRent() throws Exception {

		int mechBikes = s.getNumberOfBikes("MECH");
		int elecBikes = s.getNumberOfBikes("ELEC");
		n.rentBike(bob.getId(), s.getId(), "MECH", LocalDateTime.now());
		int remainingMechBikes = s.getNumberOfBikes("MECH");
		int remainingElecBikes = s.getNumberOfBikes("ELEC");
		assertEquals(mechBikes, remainingMechBikes + 1);
		assertEquals(elecBikes, remainingElecBikes);
	}

}
