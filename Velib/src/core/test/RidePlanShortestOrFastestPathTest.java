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
import core.point.Point;
import core.ridePlan.InvalidRidePlanPolicyException;
import core.ridePlan.NoValidStationFoundException;
import core.ridePlan.RidePlan;
import core.station.InvalidStationTypeException;
import core.station.Station;
import core.station.StationFactory;
import user.User;

/**
 * Choose the correct source and destination station for shortest and fastest ride plan
 * By setting up a network where the different source and destination stations have a bias towards the fastest or the shortest plan, 
 * we can ensure that the choice is made adequately
 * 
 * destStationS and sourceStationS have the shortest distance between them, but the sourceStationF and sourceStationF are further
 * but faster as the stations are closer to the source/destination of user.
 * 
 * @author animato
 *
 */
public class RidePlanShortestOrFastestPathTest {

	// create a network
	static Network n = new Network();
	// Create source, destination
	Point source = new Point(0, 0);
	Point destination = new Point(10, 10);
	// Create User
	static User bob;

	static StationFactory stationFactory = new StationFactory();
	static BikeFactory bikeFactory = new BikeFactory();
	static CardVisitorFactory cardVisitorFactory = new CardVisitorFactory();

	static Station sourceStationS, destStationS, sourceStationF, destStationF;

	@BeforeClass
	public static void initialize() {
		try {
			sourceStationS = stationFactory.createStation("STANDARD", 10, new Point(3, 3), true);
			destStationS = stationFactory.createStation("STANDARD", 10, new Point(7, 7), true);
			sourceStationF = stationFactory.createStation("STANDARD", 10, new Point(0, 2), true);
			destStationF = stationFactory.createStation("STANDARD", 10, new Point(9, 10), true);
		} catch (InvalidStationTypeException e) {
			fail("InvalidStationTypeException was thrown");
		}
		
		try {
			bob = new User("bob", new Point(0, 0), cardVisitorFactory.createCard("NO_CARD"));
		} catch (InvalidCardTypeException e) {
			fail("InvalidCardTypeException was thrown");
		}

		try {

			n.createStation(sourceStationS);
			n.createStation(destStationS);
			n.createStation(sourceStationF);
			n.createStation(destStationF);

			// add one bike to source and destination stations : They are all Mechanical.
			sourceStationS.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
			destStationS.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
			sourceStationF.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
			destStationF.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());

		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException was thrown");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	/**
	 * Choose a the source station as source station and dest station as dest
	 * station not the other way round
	 */
	public void chooseCorrectStationsWhenPlanningShortestRide() {
		RidePlan bobRidePlan = null;
		try {
			bobRidePlan = n.createRidePlan(source, destination, bob, "SHORTEST", "MECH");
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException was thrown");
		} catch (InvalidRidePlanPolicyException e) {
			fail("InvalidRidePlanPolicyException was thrown");
		} catch (NoValidStationFoundException e) {
			fail("NoValidStationFoundException was thrown");
		}		
		RidePlan sRidePlan = new RidePlan(source, destination, sourceStationS, destStationS, "SHORTEST",
				"MECH", n);
		assertTrue(bobRidePlan.equals(sRidePlan));
	}

	@Test
	public void chooseCorrectStationsWhenPlanningFastestRide() {
		RidePlan bobRidePlan = null;
		try {
			bobRidePlan = n.createRidePlan(source, destination, bob, "FASTEST", "MECH");
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException was thrown");
		} catch (InvalidRidePlanPolicyException e) {
			fail("InvalidRidePlanPolicyException was thrown");
		} catch (NoValidStationFoundException e) {
			fail("NoValidStationFoundException was thrown");
		}
		RidePlan sRidePlan = new RidePlan(source, destination, sourceStationF, destStationF, "FASTEST",
				"MECH", n);
		assertTrue(bobRidePlan.equals(sRidePlan));
	}

}
