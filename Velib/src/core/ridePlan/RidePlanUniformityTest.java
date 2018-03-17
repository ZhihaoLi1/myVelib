package core.ridePlan;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import core.BikeType;
import core.Network;
import core.PolicyName;
import core.User;
import core.bike.MechBike;
import core.card.NoCardVisitor;
import core.point.Point;
import core.station.PlusStation;
import core.station.StandardStation;
import core.station.Station;

public class RidePlanUniformityTest {
	
	// create a network
	static Network n = new Network();
	// Create source, destination
	Point source = new Point(0,0);
	Point destination = new Point(10,10);
	// Create User
	User bob = new User("bob", new Point(0,0), new NoCardVisitor());
	
	// 1 bike 
	static Station emptierSourceStation = new PlusStation(10, new Point(0,0.1));
	// 1 bike
	static Station emptierDestStation = new PlusStation(10, new Point(9,9.8));
	
	// 3 bikes
	static Station fullerSourceStation = new PlusStation(10, new Point(0,0.11));
	
	// 3 bikes
	static Station fullerDestStation = new PlusStation(10, new Point(9,9.7));

	@BeforeClass
    public static void initialize() {
		try {
			// Plus and Standard destinations are the same distance away
			n.addStation(emptierSourceStation);
			n.addStation(emptierDestStation);
			n.addStation(fullerSourceStation);
			n.addStation(fullerDestStation);
			
			// add bikes to all stations : They are all Mechanical. 
			emptierSourceStation.addBike(new MechBike());
			
			fullerSourceStation.addBike(new MechBike());
			fullerSourceStation.addBike(new MechBike());
			fullerSourceStation.addBike(new MechBike());
			
			emptierDestStation.addBike(new MechBike());
			
			fullerDestStation.addBike(new MechBike());
			fullerDestStation.addBike(new MechBike());
			fullerDestStation.addBike(new MechBike());
			
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
		RidePlan avoidPlusRidePlan = new RidePlan(source, destination, fullerSourceStation, emptierDestStation, PolicyName.PRESERVE_UNIFORMITY, BikeType.MECH);
		assertTrue(bobRidePlan.equals(avoidPlusRidePlan));
	}
}
