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
import core.card.NoCardVisitor;
import core.ridePlan.InvalidRidePlanPolicyException;
import core.ridePlan.NoValidStationFoundException;
import core.ridePlan.RidePlan;
import core.station.InvalidStationTypeException;
import core.station.Station;
import core.station.StationFactory;
import core.user.User;
import utils.Point;

/**
 * Test rules that all ride plans should obey
 * 	- source station should at least have one bike of the correct type
 *  - destination station should have at least one free station that is working
 *  
 * @author animato
 *
 */
public class RidePlanStrategyTest {

	// create a network
	static Network n = new Network();
	// Create source, destination
	Point source = new Point(0, 0);
	Point destination = new Point(10, 10);
	// Create User
	static User bob;
	static CardVisitorFactory cardVisitorFactory = new CardVisitorFactory();

	static StationFactory stationFactory = new StationFactory();
	static BikeFactory bikeFactory = new BikeFactory();
	// Create standard source station
	static Station sourceStation;
	// Create standard dest station
	static Station destStation;
	// Create empty nearest to starting point
	static Station emptySourceStation;
	// Create elec station nearest to starting point
	static Station elecSourceStation;
	// Create full destination station
	static Station fullDestStation;

	@BeforeClass
	public static void initialize() {
		try {
			sourceStation = stationFactory.createStation("STANDARD", 10, new Point(0, 0.1), true);
			destStation = stationFactory.createStation("STANDARD", 10, new Point(9, 9.5), true);
			emptySourceStation = stationFactory.createStation("STANDARD", 10, new Point(0, 0.02), true);
			elecSourceStation = stationFactory.createStation("STANDARD", 10, new Point(0, 0.01), true);
			fullDestStation = stationFactory.createStation("STANDARD", 10, new Point(9, 9.9), true);
		} catch (InvalidStationTypeException e) {
			fail("InvalidStationTypeException was thrown");
		}
		
		try {
			bob = new User("bob", new Point(0, 0), cardVisitorFactory.createCard("NO_CARD"));
		} catch (InvalidCardTypeException e) {
			fail("InvalidCardTypeException was thrown");
		}

		try {
			// Plus and Standard destinations are the same distance away
			n.addStation(sourceStation);
			n.addStation(destStation);
			n.addStation(emptySourceStation);
			n.addStation(fullDestStation);
			n.addStation(elecSourceStation);

			// add one bike to source and destination stations : They are all Mechanical.
			sourceStation.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
			destStation.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());

			// fill full station
			while (fullDestStation.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now())) {
			}
			// elecSourceStation only has elec bikes
			elecSourceStation.addBike(bikeFactory.createBike("ELEC"), LocalDateTime.now());
			// empty source station doesn't have any bikes
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException was thrown");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	/**
	 * Choose a source station with at least a bike Choose a dest station that is
	 * not full
	 */
	public void ChooseNotEmptySourceAndNotFullDestWhenPlanningRide() {
		RidePlan ap = null;
		try {
			ap = n.createRidePlan(source, destination, bob, "AVOID_PLUS", "MECH");
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException was thrown");
		} catch (InvalidRidePlanPolicyException e) {
			fail("InvalidRidePlanPolicyException was thrown");
		} catch (NoValidStationFoundException e) {
			fail("NoValidStationFoundException was thrown");
		}
		RidePlan apRidePlan = new RidePlan(source, destination, sourceStation, destStation, "AVOID_PLUS",
				"MECH", n);
		assertTrue(ap.equals(apRidePlan));

		RidePlan pp = null;
		try {
			pp = n.createRidePlan(source, destination, bob, "PREFER_PLUS", "MECH");
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException was thrown");
		} catch (InvalidRidePlanPolicyException e) {
			fail("InvalidRidePlanPolicyException was thrown");
		} catch (NoValidStationFoundException e) {
			fail("NoValidStationFoundException was thrown");
		}		
		RidePlan ppRidePlan = new RidePlan(source, destination, sourceStation, destStation, "PREFER_PLUS",
				"MECH", n);
		assertTrue(pp.equals(ppRidePlan));

		RidePlan s = null;
		try {
			s = n.createRidePlan(source, destination, bob, "SHORTEST", "MECH");
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException was thrown");
		} catch (InvalidRidePlanPolicyException e) {
			fail("InvalidRidePlanPolicyException was thrown");
		} catch (NoValidStationFoundException e) {
			fail("NoValidStationFoundException was thrown");
		}

		RidePlan sRidePlan = new RidePlan(source, destination, sourceStation, destStation, "SHORTEST",
				"MECH", n);
		assertTrue(s.equals(sRidePlan));

		RidePlan f = null;
		try {
			f = n.createRidePlan(source, destination, bob, "FASTEST", "MECH");
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException was thrown");
		} catch (InvalidRidePlanPolicyException e) {
			fail("InvalidRidePlanPolicyException was thrown");
		} catch (NoValidStationFoundException e) {
			fail("NoValidStationFoundException was thrown");
		}		
		RidePlan fRidePlan = new RidePlan(source, destination, sourceStation, destStation, "FASTEST",
				"MECH", n);
		assertTrue(f.equals(fRidePlan));

		RidePlan u = null;
		try {
			u = n.createRidePlan(source, destination, bob, "PRESERVE_UNIFORMITY", "MECH");
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException was thrown");
		} catch (InvalidRidePlanPolicyException e) {
			fail("InvalidRidePlanPolicyException was thrown");
		} catch (NoValidStationFoundException e) {
			fail("NoValidStationFoundException was thrown");
		}		
		RidePlan uRidePlan = new RidePlan(source, destination, sourceStation, destStation,
				"PRESERVE_UNIFORMITY", "MECH", n);
		assertTrue(u.equals(uRidePlan));

	}
}
