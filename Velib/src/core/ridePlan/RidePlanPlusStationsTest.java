package core.ridePlan;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import core.Network;
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
	
	// Create plus source stations 
	static Station plusSourceStation = new PlusStation(10, new Point(0,0.1));
	// Create standard source stations 
	static Station standardSourceStation = new StandardStation(10, new Point(0.1,0));
	// Create plus dest stations 
	static Station plusDestStation = new PlusStation(10, new Point(9.5,9));
	// Create standard dest stations 
	static Station standardDestStation = new StandardStation(10, new Point(9, 9.5));

    @BeforeClass
    public static void initialize() {
		try {
			// Plus and Standard destinations are the same distance away
			n.addStation(plusSourceStation);
			n.addStation(standardSourceStation);
			n.addStation(plusDestStation);
			n.addStation(standardDestStation);
			
			// add one bike to all stations : They are all Mechanical. 
			plusSourceStation.addBike(new MechBike());
			standardSourceStation.addBike(new MechBike());			
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
		RidePlan bobRidePlan = n.planRide(source, destination, bob, "avoidPlus", "Mech");
		RidePlan avoidPlusRidePlan = new RidePlan(source, destination, standardSourceStation, standardDestStation, "avoidPlus", "Mech");
		assertTrue(bobRidePlan.equals(avoidPlusRidePlan));
	}

	@Test
	/**
	 * Choose the right station
	 */
	public void preferPlusStationsWhenPlanningRide() {
		RidePlan bobRidePlan = n.planRide(source, destination, bob, "preferPlus", "Mech");
		RidePlan preferPlusRidePlan = new RidePlan(source, destination, plusSourceStation, plusDestStation, "preferPlus", "Mech");
		assertTrue(bobRidePlan.equals(preferPlusRidePlan));
	}

}
