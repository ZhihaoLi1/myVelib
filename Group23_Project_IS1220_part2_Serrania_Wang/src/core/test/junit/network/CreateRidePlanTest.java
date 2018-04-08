package core.test.junit.network;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import core.Network;
import core.bike.BikeFactory;
import core.bike.InvalidBikeTypeException;
import core.card.CardVisitorFactory;
import core.card.InvalidCardTypeException;
import core.ridePlan.InvalidRidePlanPolicyException;
import core.ridePlan.NoValidStationFoundException;
import core.ridePlan.RidePlan;
import core.station.InvalidStationTypeException;
import core.station.Station;
import core.station.StationFactory;
import core.user.User;
import utils.Point;

/**
 * Test createRidePlan and planRide methods of Network
 * 
 * @author animato
 */
public class CreateRidePlanTest {

	// create a network
	Network n = new Network("EmptyNetwork", 0, 0, 4.0, 0, 0, 0, LocalDateTime.now());
	// Create source, destination
	Point source = new Point(0, 0);
	Point destination = new Point(4, 4);
	// Create User
	User bob;
	CardVisitorFactory cardVisitorFactory = new CardVisitorFactory();

	StationFactory stationFactory = new StationFactory();
	BikeFactory bikeFactory = new BikeFactory();
	// Create standard source station
	Station sourceStation;
	// Create standard dest station
	Station destStation;
	// Create empty nearest to starting point
	Station emptySourceStation;
	// Create elec station nearest to starting point
	Station elecSourceStation;
	// Create full destination station
	Station fullDestStation;

	@Before
	public void initialize() throws InvalidStationTypeException, InvalidCardTypeException, InvalidBikeTypeException {
		sourceStation = stationFactory.createStation("STANDARD", 10, new Point(0, 0.04), true);
		destStation = stationFactory.createStation("STANDARD", 10, new Point(3.6, 3.8), true);
		emptySourceStation = stationFactory.createStation("STANDARD", 10, new Point(0, 0.005), true);
		elecSourceStation = stationFactory.createStation("STANDARD", 10, new Point(0, 0.0025), true);
		fullDestStation = stationFactory.createStation("STANDARD", 10, new Point(3.6, 3.95), true);

		bob = new User("bob", new Point(0, 0), cardVisitorFactory.createCard("NO_CARD"));

		// Plus and Standard destinations are the same distance away
		n.addStation(sourceStation);
		n.addStation(destStation);
		n.addStation(emptySourceStation);
		n.addStation(fullDestStation);
		n.addStation(elecSourceStation);
		n.addUser(bob);

		// add one bike to source and destination stations : They are all Mechanical.
		sourceStation.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
		destStation.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());

		// fill full station
		while (fullDestStation.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now())) {
		}
		// elecSourceStation only has one elec bike
		elecSourceStation.addBike(bikeFactory.createBike("ELEC"), LocalDateTime.now());
		// empty source station doesn't have any bikes
	}

	@Test
	/**
	 * Verify that createRidePlan does work when given correct arguments and policy. <br>
	 * Test rules that all ride plans should obey - source station should at least
	 * have one bike of the correct type - destination station should have at least
	 * one free station that is working
	 */
	public void ChooseNotEmptySourceAndNotFullDestWhenPlanningRide()
			throws InvalidBikeTypeException, InvalidRidePlanPolicyException, NoValidStationFoundException {
		RidePlan ap = n.createRidePlan(source, destination, bob, "AVOID_PLUS", "MECH");
		RidePlan apRidePlan = new RidePlan(source, destination, sourceStation, destStation, "AVOID_PLUS", "MECH", n);
		assertTrue(ap.equals(apRidePlan));

		RidePlan pp = n.createRidePlan(source, destination, bob, "PREFER_PLUS", "MECH");
		RidePlan ppRidePlan = new RidePlan(source, destination, sourceStation, destStation, "PREFER_PLUS", "MECH", n);
		assertTrue(pp.equals(ppRidePlan));

		RidePlan s = n.createRidePlan(source, destination, bob, "SHORTEST", "MECH");
		RidePlan sRidePlan = new RidePlan(source, destination, sourceStation, destStation, "SHORTEST", "MECH", n);
		assertTrue(s.equals(sRidePlan));

		RidePlan f = n.createRidePlan(source, destination, bob, "FASTEST", "MECH");
		RidePlan fRidePlan = new RidePlan(source, destination, sourceStation, destStation, "FASTEST", "MECH", n);
		assertTrue(f.equals(fRidePlan));

		RidePlan u = n.createRidePlan(source, destination, bob, "PRESERVE_UNIFORMITY", "MECH");
		RidePlan uRidePlan = new RidePlan(source, destination, sourceStation, destStation, "PRESERVE_UNIFORMITY",
				"MECH", n);
		assertTrue(u.equals(uRidePlan));
	}

	/**
	 * When an invalid policy is given an InvalidRidePlanPolicyException should be
	 * thrown
	 */
	@Test
	public void whenInvalidPolicyIsGivenThenThrowException()
			throws NoValidStationFoundException, InvalidBikeTypeException {
		try {
			n.createRidePlan(source, destination, bob, "NOPE", "MECH");
			fail("InvalidRidePlanPolicyException should have been thrown");
		} catch (InvalidRidePlanPolicyException e) {
			assertTrue(true);
		}
	}
	
	/**
	 * When a null policy is given an InvalidRidePlanPolicyException should be
	 * thrown
	 */
	@Test
	public void whenNullPolicyIsGivenThenThrowException()
			throws NoValidStationFoundException, InvalidBikeTypeException, InvalidRidePlanPolicyException {
		try {
			n.createRidePlan(source, destination, bob, null, "MECH");
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	@Test
	/**
	 * Verify that planRide does give a ride plan when given correct arguments and
	 * policy
	 */
	public void testPlanRide() {
		bob.setRidePlan(null);
		n.planRide(source.getX(), source.getY(), destination.getX(), destination.getY(), bob.getId(), "AVOID_PLUS",
				"MECH");
		assertNotEquals(bob.getRidePlan(), null);

		bob.setRidePlan(null);
		n.planRide(source.getX(), source.getY(), destination.getX(), destination.getY(), bob.getId(), "PREFER_PLUS",
				"MECH");
		assertNotEquals(bob.getRidePlan(), null);

		bob.setRidePlan(null);
		n.planRide(source.getX(), source.getY(), destination.getX(), destination.getY(), bob.getId(), "SHORTEST",
				"MECH");
		assertNotEquals(bob.getRidePlan(), null);

		bob.setRidePlan(null);
		n.planRide(source.getX(), source.getY(), destination.getX(), destination.getY(), bob.getId(), "FASTEST",
				"MECH");
		assertNotEquals(bob.getRidePlan(), null);

		bob.setRidePlan(null);
		n.planRide(source.getX(), source.getY(), destination.getX(), destination.getY(), bob.getId(),
				"PRESERVE_UNIFORMITY", "MECH");
		assertNotEquals(bob.getRidePlan(), null);
	}

	/**
	 * When an invalid policy is given to planRide, no ride plan should be created
	 */
	@Test
	public void whenInvalidPolicyIsGivenThenDoNotCreateBikePlan() {
		bob.setRidePlan(null);
		n.planRide(source.getX(), source.getY(), destination.getX(), destination.getY(), bob.getId(), "NOPE", "MECH");
		assertEquals(bob.getRidePlan(), null);
	}
	
	/**
	 * When an null policy is given to planRide, no ride plan should be created
	 */
	@Test
	public void whenNullPolicyIsGivenThenDoNotCreateBikePlan() {
		bob.setRidePlan(null);
		n.planRide(source.getX(), source.getY(), destination.getX(), destination.getY(), bob.getId(), null, "MECH");
		assertEquals(bob.getRidePlan(), null);
	}

	/**
	 * When invalid coordinates are given to planRide, no ride plan should be
	 * created
	 */
	@Test
	public void whenInvalidCoordinatesAreGivenThenDoNotCreateBikePlan() {
		bob.setRidePlan(null);
		n.planRide(-0.1, source.getY(), destination.getX(), destination.getY(), bob.getId(), "AVOID_PLUS", "MECH");
		assertEquals(bob.getRidePlan(), null);

		bob.setRidePlan(null);
		n.planRide(source.getX(), n.getSide() + 0.1, destination.getX(), destination.getY(), bob.getId(), "AVOID_PLUS",
				"MECH");
		assertEquals(bob.getRidePlan(), null);

		bob.setRidePlan(null);
		n.planRide(source.getX(), source.getY(), -0.1, destination.getY(), bob.getId(), "AVOID_PLUS", "MECH");
		assertEquals(bob.getRidePlan(), null);

		bob.setRidePlan(null);
		n.planRide(source.getX(), source.getY(), destination.getX(), n.getSide() + 0.1, bob.getId(), "AVOID_PLUS",
				"MECH");
		assertEquals(bob.getRidePlan(), null);
	}
}
