package core.test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.BeforeClass;
import org.junit.Test;

import core.Network;
import core.bike.BikeFactory;
import core.bike.BikeType;
import core.bike.InvalidBikeTypeException;
import core.card.NoCardVisitor;
import core.point.Point;
import core.ridePlan.RidePlanPolicyName;
import core.ridePlan.RidePlan;
import core.station.InvalidStationTypeException;
import core.station.Station;
import core.station.StationFactory;
import core.station.StationType;
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
	User bob = new User("bob", new Point(0, 0), new NoCardVisitor());

	static StationFactory stationFactory = new StationFactory();
	static BikeFactory bikeFactory = new BikeFactory();

	static Station sourceStationS, destStationS, sourceStationF, destStationF;

	@BeforeClass
	public static void initialize() {
		try {
			sourceStationS = stationFactory.createStation(StationType.STANDARD, 10, new Point(3, 3), true);
			destStationS = stationFactory.createStation(StationType.STANDARD, 10, new Point(7, 7), true);
			sourceStationF = stationFactory.createStation(StationType.STANDARD, 10, new Point(0, 2), true);
			destStationF = stationFactory.createStation(StationType.STANDARD, 10, new Point(9, 10), true);
		} catch (InvalidStationTypeException e) {
			fail("InvalidStationTypeException was thrown");
		}

		try {

			n.addStation(sourceStationS);
			n.addStation(destStationS);
			n.addStation(sourceStationF);
			n.addStation(destStationF);

			// add one bike to source and destination stations : They are all Mechanical.
			sourceStationS.addBike(bikeFactory.createBike(BikeType.MECH), LocalDateTime.now());
			destStationS.addBike(bikeFactory.createBike(BikeType.MECH), LocalDateTime.now());
			sourceStationF.addBike(bikeFactory.createBike(BikeType.MECH), LocalDateTime.now());
			destStationF.addBike(bikeFactory.createBike(BikeType.MECH), LocalDateTime.now());

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
		RidePlan s = n.createRidePlan(source, destination, bob, RidePlanPolicyName.SHORTEST, BikeType.MECH);
		RidePlan sRidePlan = new RidePlan(source, destination, sourceStationS, destStationS, RidePlanPolicyName.SHORTEST,
				BikeType.MECH, n);
		assertTrue(s.equals(sRidePlan));
	}

	@Test
	public void chooseCorrectStationsWhenPlanningFastestRide() {
		RidePlan s = n.createRidePlan(source, destination, bob, RidePlanPolicyName.FASTEST, BikeType.MECH);
		RidePlan sRidePlan = new RidePlan(source, destination, sourceStationF, destStationF, RidePlanPolicyName.FASTEST,
				BikeType.MECH, n);
		assertTrue(s.equals(sRidePlan));
	}

}
