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
import core.ridePlan.AvoidPlusPlan;
import core.ridePlan.InvalidRidePlanPolicyException;
import core.ridePlan.NoValidStationFoundException;
import core.ridePlan.PreferPlusPlan;
import core.ridePlan.PreserveUniformityPlan;
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
	public static void initialize() throws InvalidStationTypeException, InvalidCardTypeException, InvalidBikeTypeException {
		sourceStation = stationFactory.createStation("PLUS", 10, new Point(0, 0.1), true);
		plusDestStation = stationFactory.createStation("PLUS", 10, new Point(9, 9.8), true);
		standardDestStation = stationFactory.createStation("STANDARD", 10, new Point(9, 9.5), true);

		bob = new User("bob", new Point(0, 0), cardVisitorFactory.createCard("NO_CARD"));

		// Plus and Standard destinations are the same distance away
		n.addStation(sourceStation);
		n.addStation(plusDestStation);
		n.addStation(standardDestStation);

		// add one bike to all stations : They are all Mechanical.
		sourceStation.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
		standardDestStation.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
		plusDestStation.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());

	}

	@Test
	/**
	 * Choose the right station Avoid plus stations
	 */
	public void avoidPlusStationsWhenPlanningRide() throws NoValidStationFoundException {
		RidePlan bobRidePlan = (new AvoidPlusPlan()).planRide(source, destination, bob, "MECH", n);
		RidePlan avoidPlusRidePlan = new RidePlan(source, destination, sourceStation, standardDestStation,
				"AVOID_PLUS", "MECH", n);
		assertTrue(bobRidePlan.equals(avoidPlusRidePlan));
	}

	@Test
	/**
	 * Choose the closest station (standard station)
	 */
	public void preferPlusStationsWhenPlanningRide() throws NoValidStationFoundException {
		RidePlan bobRidePlan = (new PreferPlusPlan()).planRide(source, destination, bob, "MECH", n);
		RidePlan preferPlusRidePlan = new RidePlan(source, destination, sourceStation, plusDestStation,
				"PREFER_PLUS", "MECH", n);
		assertTrue(bobRidePlan.equals(preferPlusRidePlan));
	}
	
	@Test
	/**
	 * Verify that giving a bike type that is not present to a preferPlusPlan throws an
	 * NoValidStationFoundException
	 */
	public void whenBikeTypeIsNotPresentInPreferPlusStationsThenThrowException() {
		try {
			// There are no elec bikes in this system, so this should throw the exception
			(new PreferPlusPlan()).planRide(source, destination, bob, "ELEC", n);
			fail("NoValidStationFoundException should have been thrown");
		} catch (NoValidStationFoundException e) {
			assertTrue(true);
		}
		
		try {
			(new PreferPlusPlan()).planRide(source, destination, bob, "NOPE", n);
			fail("NoValidStationFoundException should have been thrown");
		} catch (NoValidStationFoundException e) {
			assertTrue(true);
		}
	}
	
	@Test
	/**
	 * Verify that giving a bike type that is not present to a preferPlusPlan throws an
	 * NoValidStationFoundException
	 */
	public void whenBikeTypeIsNotPresentInAvoidPlusStationsThenThrowException() {
		try {
			// There are no elec bikes in this system, so this should throw the exception
			(new AvoidPlusPlan()).planRide(source, destination, bob, "ELEC", n);
			fail("NoValidStationFoundException should have been thrown");
		} catch (NoValidStationFoundException e) {
			assertTrue(true);
		}
		
		try {
			(new AvoidPlusPlan()).planRide(source, destination, bob, "NOPE", n);
			fail("NoValidStationFoundException should have been thrown");
		} catch (NoValidStationFoundException e) {
			assertTrue(true);
		}
	}

}
