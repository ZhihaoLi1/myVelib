package core.test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.BeforeClass;
import org.junit.Test;

import core.Network;
import core.bike.BikeFactory;
import core.bike.BikeType;
import core.bike.InvalidBikeTypeException;
import core.card.CardVisitorFactory;
import core.card.InvalidCardTypeException;
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
			sourceStation = stationFactory.createStation(StationType.STANDARD, 10, new Point(0, 0.1), true);
			destStation = stationFactory.createStation(StationType.STANDARD, 10, new Point(9, 9.5), true);
			emptySourceStation = stationFactory.createStation(StationType.STANDARD, 10, new Point(0, 0.02), true);
			elecSourceStation = stationFactory.createStation(StationType.STANDARD, 10, new Point(0, 0.01), true);
			fullDestStation = stationFactory.createStation(StationType.STANDARD, 10, new Point(9, 9.9), true);
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
			n.createStation(sourceStation);
			n.createStation(destStation);
			n.createStation(emptySourceStation);
			n.createStation(fullDestStation);
			n.createStation(elecSourceStation);

			// add one bike to source and destination stations : They are all Mechanical.
			sourceStation.addBike(bikeFactory.createBike(BikeType.MECH), LocalDateTime.now());
			destStation.addBike(bikeFactory.createBike(BikeType.MECH), LocalDateTime.now());

			// fill full station
			while (fullDestStation.addBike(bikeFactory.createBike(BikeType.MECH), LocalDateTime.now())) {
			}
			// elecSourceStation only has elec bikes
			elecSourceStation.addBike(bikeFactory.createBike(BikeType.ELEC), LocalDateTime.now());
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
		RidePlan ap = n.createRidePlan(source, destination, bob, RidePlanPolicyName.AVOID_PLUS, BikeType.MECH);
		RidePlan apRidePlan = new RidePlan(source, destination, sourceStation, destStation, RidePlanPolicyName.AVOID_PLUS,
				BikeType.MECH, n);
		assertTrue(ap.equals(apRidePlan));

		RidePlan pp = n.createRidePlan(source, destination, bob, RidePlanPolicyName.PREFER_PLUS, BikeType.MECH);
		RidePlan ppRidePlan = new RidePlan(source, destination, sourceStation, destStation, RidePlanPolicyName.PREFER_PLUS,
				BikeType.MECH, n);
		assertTrue(pp.equals(ppRidePlan));

		RidePlan s = n.createRidePlan(source, destination, bob, RidePlanPolicyName.SHORTEST, BikeType.MECH);
		RidePlan sRidePlan = new RidePlan(source, destination, sourceStation, destStation, RidePlanPolicyName.SHORTEST,
				BikeType.MECH, n);
		assertTrue(s.equals(sRidePlan));

		RidePlan f = n.createRidePlan(source, destination, bob, RidePlanPolicyName.FASTEST, BikeType.MECH);
		RidePlan fRidePlan = new RidePlan(source, destination, sourceStation, destStation, RidePlanPolicyName.FASTEST,
				BikeType.MECH, n);
		assertTrue(f.equals(fRidePlan));

		RidePlan u = n.createRidePlan(source, destination, bob, RidePlanPolicyName.PRESERVE_UNIFORMITY, BikeType.MECH);
		RidePlan uRidePlan = new RidePlan(source, destination, sourceStation, destStation,
				RidePlanPolicyName.PRESERVE_UNIFORMITY, BikeType.MECH, n);
		assertTrue(u.equals(uRidePlan));

	}
}
