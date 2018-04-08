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
import core.ridePlan.FastestPlan;
import core.ridePlan.InvalidRidePlanPolicyException;
import core.ridePlan.NoValidStationFoundException;
import core.ridePlan.PreferPlusPlan;
import core.ridePlan.RidePlan;
import core.ridePlan.ShortestPlan;
import core.station.InvalidStationTypeException;
import core.station.Station;
import core.station.StationFactory;
import core.user.User;
import utils.Point;

/**
 * Choose the correct source and destination station for shortest and fastest
 * ride plan By setting up a network where the different source and destination
 * stations have a bias towards the fastest or the shortest plan, we can ensure
 * that the choice is made adequately
 * 
 * destStationS and sourceStationS have the shortest distance between them, but
 * the sourceStationF and sourceStationF are further but faster as the stations
 * are closer to the source/destination of user.
 * 
 * @author animato
 *
 */
public class RidePlanShortestOrFastestPathTest {

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

	static Station sourceStationS, destStationS, sourceStationF, destStationF;

	@BeforeClass
	public static void initialize()
			throws InvalidStationTypeException, InvalidCardTypeException, InvalidBikeTypeException {
		sourceStationS = stationFactory.createStation("STANDARD", 10, new Point(3, 3), true);
		destStationS = stationFactory.createStation("STANDARD", 10, new Point(7, 7), true);
		sourceStationF = stationFactory.createStation("STANDARD", 10, new Point(0, 2), true);
		destStationF = stationFactory.createStation("STANDARD", 10, new Point(9, 10), true);

		bob = new User("bob", new Point(0, 0), cardVisitorFactory.createCard("NO_CARD"));

		n.addStation(sourceStationS);
		n.addStation(destStationS);
		n.addStation(sourceStationF);
		n.addStation(destStationF);

		// add one bike to source and destination stations : They are all Mechanical.
		sourceStationS.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
		destStationS.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
		sourceStationF.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
		destStationF.addBike(bikeFactory.createBike("MECH"), LocalDateTime.now());
	}

	/**
	 * Choose a the source station as source station and dest station as dest
	 * station not the other way round
	 */
	@Test
	public void chooseCorrectStationsWhenPlanningShortestRide()
			throws InvalidBikeTypeException, InvalidRidePlanPolicyException, NoValidStationFoundException {
		RidePlan bobRidePlan = n.createRidePlan(source, destination, bob, "SHORTEST", "MECH");
		RidePlan sRidePlan = new RidePlan(source, destination, sourceStationS, destStationS, "SHORTEST", "MECH", n);
		assertTrue(bobRidePlan.equals(sRidePlan));
	}

	@Test
	public void chooseCorrectStationsWhenPlanningFastestRide()
			throws InvalidBikeTypeException, InvalidRidePlanPolicyException, NoValidStationFoundException {
		RidePlan bobRidePlan = n.createRidePlan(source, destination, bob, "FASTEST", "MECH");
		RidePlan sRidePlan = new RidePlan(source, destination, sourceStationF, destStationF, "FASTEST", "MECH", n);
		assertTrue(bobRidePlan.equals(sRidePlan));
	}

	/**
	 * Verify that giving a bike type that is not present to a ShortestPlan throws
	 * an NoValidStationFoundException
	 */
	@Test
	public void whenBikeTypeIsNotPresentInShortestThenThrowException() {
		try {
			// There are no elec bikes in this system, so this should throw the exception
			(new ShortestPlan()).planRide(source, destination, bob, "ELEC", n);
			fail("NoValidStationFoundException should have been thrown");
		} catch (NoValidStationFoundException e) {
			assertTrue(true);
		}

		try {
			(new ShortestPlan()).planRide(source, destination, bob, "NOPE", n);
			fail("NoValidStationFoundException should have been thrown");
		} catch (NoValidStationFoundException e) {
			assertTrue(true);
		}
	}

	/**
	 * Verify that giving a bike type that is not present to a FastestPlan throws an
	 * InvalidBikeTypeException (because for this specific plan, we need the type of
	 * bike to know the bike's speed)
	 */
	@Test
	public void whenBikeTypeIsNotPresentInAvoidPlusStationsThenThrowException()
			throws NoValidStationFoundException, InvalidBikeTypeException {
		try {
			// There are no elec bikes in this system, so this should throw the
			// NoValidStationFoundException exception
			(new FastestPlan()).planRide(source, destination, bob, "ELEC", n);
			fail("NoValidStationFoundException should have been thrown");
		} catch (NoValidStationFoundException e) {
			assertTrue(true);
		}

		try {
			// The nope bike type does not exist, so the InvalidBikeTypeException should be
			// thrown here because we cannot compute the bike's speed
			(new FastestPlan()).planRide(source, destination, bob, "NOPE", n);
			fail("InvalidBikeTypeException should have been thrown");
		} catch (InvalidBikeTypeException e) {
			assertTrue(true);
		}

	}
}
