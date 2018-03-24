package core.test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.BeforeClass;
import org.junit.Test;

import core.BikeType;
import core.Network;
import core.PolicyName;
import core.User;
import core.bike.BikeFactory;
import core.bike.InvalidBikeTypeException;
import core.bike.MechBike;
import core.card.NoCardVisitor;
import core.point.Point;
import core.ridePlan.RidePlan;
import core.station.InvalidStationTypeException;
import core.station.PlusStation;
import core.station.StandardStation;
import core.station.Station;
import core.station.StationFactory;
import core.station.StationType;

public class RidePlanPlusStationsTest {
	
	// create a network
	static Network n = new Network();
	// Create source, destination
	Point source = new Point(0,0);
	Point destination = new Point(10,10);
	// Create User
	User bob = new User("bob", new Point(0,0), new NoCardVisitor());
	
	static StationFactory stationFactory = new StationFactory();
	static BikeFactory bikeFactory = new BikeFactory();

	// Create stations 
	static Station sourceStation, plusDestStation, standardDestStation;

    @BeforeClass
    public static void initialize() {
    	try {
    		sourceStation = stationFactory.createStation(StationType.PLUS, 10, new Point(0,0.1), true);
    		plusDestStation = stationFactory.createStation(StationType.PLUS, 10, new Point(9,9.8), true);
    		standardDestStation = stationFactory.createStation(StationType.STANDARD, 10, new Point(9, 9.5), true);
    	} catch (InvalidStationTypeException e) {
    		fail("InvalidStationTypeException was thrown");
    	}
    	
		try {
			// Plus and Standard destinations are the same distance away
			n.addStation(sourceStation);
			n.addStation(plusDestStation);
			n.addStation(standardDestStation);
			
			// add one bike to all stations : They are all Mechanical. 
			sourceStation.addBike(bikeFactory.createBike(BikeType.MECH), LocalDateTime.now());
			standardDestStation.addBike(bikeFactory.createBike(BikeType.MECH), LocalDateTime.now());
			plusDestStation.addBike(bikeFactory.createBike(BikeType.MECH), LocalDateTime.now());			
			
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException was thrown");
		} catch (Exception e) {
			e.printStackTrace();
		}

    }

	@Test
	/**
	 * Choose the right station
	 * Avoid plus stations 
	 */
	public void avoidPlusStationsWhenPlanningRide() {
		RidePlan bobRidePlan = n.planRide(source, destination, bob, PolicyName.AVOID_PLUS, BikeType.MECH);
		RidePlan avoidPlusRidePlan = new RidePlan(source, destination, sourceStation, standardDestStation, PolicyName.AVOID_PLUS, BikeType.MECH, n);
		assertTrue(bobRidePlan.equals(avoidPlusRidePlan));
	}

	@Test
	/**
	 * Choose the right station
	 */
	public void preferPlusStationsWhenPlanningRide() {
		RidePlan bobRidePlan = n.planRide(source, destination, bob, PolicyName.PREFER_PLUS, BikeType.MECH);
		RidePlan preferPlusRidePlan = new RidePlan(source, destination, sourceStation, plusDestStation, PolicyName.PREFER_PLUS, BikeType.MECH, n);
		assertTrue(bobRidePlan.equals(preferPlusRidePlan));
	}

}
