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

public class RidePlanPlusStationsTest {
	
	// create a network
	static Network n = new Network();
	// Create source, destination
	Point source = new Point(0,0);
	Point destination = new Point(10,10);
	// Create User
	User bob = new User("bob", new Point(0,0), new NoCardVisitor());
	
	// Create source stations 
	static Station sourceStation = new PlusStation(10, new Point(0,0.1));
	// Create plus dest stations 
	static Station plusDestStation = new PlusStation(10, new Point(9,9.8));
	// Create standard dest stations 
	static Station standardDestStation = new StandardStation(10, new Point(9, 9.5));

    @BeforeClass
    public static void initialize() {
		try {
			// Plus and Standard destinations are the same distance away
			n.addStation(sourceStation);
			n.addStation(plusDestStation);
			n.addStation(standardDestStation);
			
			// add one bike to all stations : They are all Mechanical. 
			sourceStation.addBike(new MechBike());
			standardDestStation.addBike(new MechBike());
			plusDestStation.addBike(new MechBike());			
			
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
		RidePlan avoidPlusRidePlan = new RidePlan(source, destination, sourceStation, standardDestStation, PolicyName.AVOID_PLUS, BikeType.MECH);
		assertTrue(bobRidePlan.equals(avoidPlusRidePlan));
	}

	@Test
	/**
	 * Choose the right station
	 */
	public void preferPlusStationsWhenPlanningRide() {
		RidePlan bobRidePlan = n.planRide(source, destination, bob, PolicyName.PREFER_PLUS, BikeType.MECH);
		RidePlan preferPlusRidePlan = new RidePlan(source, destination, sourceStation, plusDestStation, PolicyName.PREFER_PLUS, BikeType.MECH);
		assertTrue(bobRidePlan.equals(preferPlusRidePlan));
	}

}
