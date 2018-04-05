package core.test.junit.ridePlan;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.BeforeClass;
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
 * Test ride plans concerning plus stations Create a network with standard and
 * plus stations with bikes
 * 
 * @author animato
 *
 */
public class RidePlanPlusStationsTest {

	// create a network
	static Network n = new Network("EmptyNetwork", 0, 0, 4, 0, 0, 0, LocalDateTime.now());
	// Create source, destination
	Point source = new Point(0, 0);
	Point destination = new Point(10, 10);
	// Create User
	static User bob;

	static StationFactory stationFactory = new StationFactory();
	static BikeFactory bikeFactory = new BikeFactory();
	static CardVisitorFactory cardVisitorFactory = new CardVisitorFactory();

	// Create stations
	static Station sourceStation, plusDestStation, standardDestStation;

	@BeforeClass
	public static void initialize() {
		try {
			sourceStation = stationFactory.createStation("PLUS", 10, new Point(0, 0.1), true);
			plusDestStation = stationFactory.createStation("PLUS", 10, new Point(9, 9.8), true);
			standardDestStation = stationFactory.createStation("STANDARD", 10, new Point(9, 9.5), true);
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
			n.addStation(plusDestStation);
			n.addStation(standardDestStation);

			// add one bike to all stations : They are all Mechanical.
			sourceStation.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
			standardDestStation.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
			plusDestStation.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());

		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException was thrown");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	/**
	 * Choose the right station Avoid plus stations
	 */
	public void avoidPlusStationsWhenPlanningRide() {
		RidePlan bobRidePlan = null;
		try {
			bobRidePlan = n.createRidePlan(source, destination, bob, "AVOID_PLUS", "MECH");
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException was thrown");
		} catch (InvalidRidePlanPolicyException e) {
			fail("InvalidRidePlanPolicyException was thrown");
		} catch (NoValidStationFoundException e) {
			fail("NoValidStationFoundException was thrown");
		}
		RidePlan avoidPlusRidePlan = new RidePlan(source, destination, sourceStation, standardDestStation,
				"AVOID_PLUS", "MECH", n);
		assertTrue(bobRidePlan.equals(avoidPlusRidePlan));
	}

	@Test
	/**
	 * Choose the closest station (standard station)
	 */
	public void preferPlusStationsWhenPlanningRide() {
		RidePlan bobRidePlan = null;
		try {
			bobRidePlan = n.createRidePlan(source, destination, bob, "PREFER_PLUS", "MECH");
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException was thrown");
		} catch (InvalidRidePlanPolicyException e) {
			fail("InvalidRidePlanPolicyException was thrown");
		} catch (NoValidStationFoundException e) {
			fail("NoValidStationFoundException was thrown");
		}
		RidePlan preferPlusRidePlan = new RidePlan(source, destination, sourceStation, plusDestStation,
				"PREFER_PLUS", "MECH", n);
		assertTrue(bobRidePlan.equals(preferPlusRidePlan));
	}

}
