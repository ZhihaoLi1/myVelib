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
import core.station.Station;
import core.station.StationFactory;
import core.station.StationType;

public class RidePlanUniformityTest {
	
	// create a network
	static Network n = new Network();
	// Create source, destination
	Point source = new Point(0,0);
	Point destination = new Point(10,10);
	// Create User
	User bob = new User("bob", new Point(0,0), new NoCardVisitor());
	
	static StationFactory stationFactory = new StationFactory();
	static BikeFactory bikeFactory = new BikeFactory();
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
			emptierSourceStation = stationFactory.createStation(StationType.PLUS, 10, new Point(0,0.1), true);
			emptierDestStation = stationFactory.createStation(StationType.PLUS, 10, new Point(9,9.8), true);
			fullerSourceStation = stationFactory.createStation(StationType.PLUS, 10, new Point(0,0.11), true);
			fullerDestStation = stationFactory.createStation(StationType.PLUS, 10, new Point(9,9.7), true);
    	} catch (InvalidStationTypeException e) {
    		fail("InvalidStationTypeException was thrown");
    	}
		
		try {
			// Plus and Standard destinations are the same distance away
			n.addStation(emptierSourceStation);
			n.addStation(emptierDestStation);
			n.addStation(fullerSourceStation);
			n.addStation(fullerDestStation);
			
			// add bikes to all stations : They are all Mechanical. 
			emptierSourceStation.addBike(bikeFactory.createBike(BikeType.MECH), LocalDateTime.now());
			
			fullerSourceStation.addBike(bikeFactory.createBike(BikeType.MECH), LocalDateTime.now());
			fullerSourceStation.addBike(bikeFactory.createBike(BikeType.MECH), LocalDateTime.now());
			fullerSourceStation.addBike(bikeFactory.createBike(BikeType.MECH), LocalDateTime.now());
			
			emptierDestStation.addBike(bikeFactory.createBike(BikeType.MECH), LocalDateTime.now());
			
			fullerDestStation.addBike(bikeFactory.createBike(BikeType.MECH), LocalDateTime.now());
			fullerDestStation.addBike(bikeFactory.createBike(BikeType.MECH), LocalDateTime.now());
			fullerDestStation.addBike(bikeFactory.createBike(BikeType.MECH), LocalDateTime.now());
			
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
		RidePlan bobRidePlan = n.planRide(source, destination, bob, PolicyName.PRESERVE_UNIFORMITY, BikeType.MECH);
		RidePlan avoidPlusRidePlan = new RidePlan(source, destination, fullerSourceStation, emptierDestStation, PolicyName.PRESERVE_UNIFORMITY, BikeType.MECH, n);
		assertTrue(bobRidePlan.equals(avoidPlusRidePlan));
	}
}
