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
import core.ridePlan.PreserveUniformityPlan;
import core.ridePlan.RidePlan;
import core.station.InvalidStationTypeException;
import core.station.Station;
import core.station.StationFactory;
import core.user.User;
import utils.Point;

/**
 * Test ride plan that should preserver uniformity
 * 
 * Source station is the station that is within a 10% distance of source -
 * closest source station and has the most bikes Dest station is the station
 * that is within a 10% distance of dest - closest dest station and has the
 * least bikes
 * 
 * In this manner, it ensures that over time, the number of bikes in the
 * stations are kept around the average.
 * 
 * @author animato
 *
 */
public class RidePlanUniformityTest {

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

	// 1 bike
	static Station emptierSourceStation;
	// 1 bike
	static Station emptierDestStation;

	// 3 bikes
	static Station fullerSourceStation;

	// 3 bikes
	static Station fullerDestStation;

	@BeforeClass
	public static void initialize() throws InvalidStationTypeException, InvalidBikeTypeException, InvalidCardTypeException {
		emptierSourceStation = stationFactory.createStation("PLUS", 10, new Point(0, 0.1), true);
		emptierDestStation = stationFactory.createStation("PLUS", 10, new Point(9, 9.8), true);
		fullerSourceStation = stationFactory.createStation("PLUS", 10, new Point(0, 0.11), true);
		fullerDestStation = stationFactory.createStation("PLUS", 10, new Point(9, 9.7), true);

		bob = new User("bob", new Point(0, 0), cardVisitorFactory.createCard("NO_CARD"));

		// Plus and Standard destinations are the same distance away
		n.addStation(emptierSourceStation);
		n.addStation(emptierDestStation);
		n.addStation(fullerSourceStation);
		n.addStation(fullerDestStation);

		// add bikes to all stations : They are all Mechanical.
		emptierSourceStation.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());

		fullerSourceStation.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
		fullerSourceStation.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
		fullerSourceStation.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());

		emptierDestStation.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());

		fullerDestStation.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
		fullerDestStation.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
		fullerDestStation.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());

	}

	@Test
	/**
	 * Verify that the right stations are chosen, corresponding to the strategy
	 */
	public void avoidPlusStationsWhenPlanningRide() throws NoValidStationFoundException {
		RidePlan bobRidePlan = (new PreserveUniformityPlan()).planRide(source, destination, bob, "MECH", n);
		RidePlan avoidPlusRidePlan = new RidePlan(source, destination, fullerSourceStation, emptierDestStation,
				"PRESERVE_UNIFORMITY", "MECH", n);
		assertTrue(bobRidePlan.equals(avoidPlusRidePlan));
	}

	@Test
	/**
	 * Verify that giving a bike type that is not present throws an
	 * NoValidStationFoundException
	 */
	public void whenBikeTypeIsNotPresentThenThrowException() {
		try {
			(new PreserveUniformityPlan()).planRide(source, destination, bob, "NOPE", n);
			fail("NoValidStationFoundException should have been thrown");
		} catch (NoValidStationFoundException e) {
			assertTrue(true);
		}
	}
	
	@Test
	/**
	 * Verify that giving a null bike type, throw an
	 * IllegalArgumentException
	 */
	public void whenBikeTypeIsNotNullThenThrowException() throws NoValidStationFoundException {
		try {
			(new PreserveUniformityPlan()).planRide(source, destination, bob, null, n);
			fail("NoValidStationFoundException should have been thrown");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
}
