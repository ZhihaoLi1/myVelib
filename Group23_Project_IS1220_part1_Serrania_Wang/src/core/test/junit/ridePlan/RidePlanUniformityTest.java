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
 * Test ride plan that should preserver uniformity
 * 
 * Source station is the station that is within a 10% distance of source - closest source station and has the most bikes 
 * Dest station is the station that is within a 10% distance of dest - closest dest station and has the least bikes
 * 
 * In this manner, it ensures that over time, the number of bikes in the stations are kept around the average.
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
	public static void initialize() {
		try {
			emptierSourceStation = stationFactory.createStation("PLUS", 10, new Point(0, 0.1), true);
			emptierDestStation = stationFactory.createStation("PLUS", 10, new Point(9, 9.8), true);
			fullerSourceStation = stationFactory.createStation("PLUS", 10, new Point(0, 0.11), true);
			fullerDestStation = stationFactory.createStation("PLUS", 10, new Point(9, 9.7), true);
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

		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException was thrown");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	/**
	 * Choose the right stations
	 */
	public void avoidPlusStationsWhenPlanningRide() {
		RidePlan bobRidePlan = null;
		try {
			bobRidePlan = n.createRidePlan(source, destination, bob, "PRESERVE_UNIFORMITY", "MECH");
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException was thrown");
		} catch (InvalidRidePlanPolicyException e) {
			fail("InvalidRidePlanPolicyException was thrown");
		} catch (NoValidStationFoundException e) {
			fail("NoValidStationFoundException was thrown");
		}
		RidePlan avoidPlusRidePlan = new RidePlan(source, destination, fullerSourceStation, emptierDestStation,
				"PRESERVE_UNIFORMITY", "MECH", n);
		assertTrue(bobRidePlan.equals(avoidPlusRidePlan));
	}
}
