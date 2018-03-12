package core.ridePlan;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import core.Network;
import core.User;
import core.bike.ElecBike;
import core.bike.MechBike;
import core.card.NoCardVisitor;
import core.point.Point;
import core.station.PlusStation;
import core.station.StandardStation;
import core.station.Station;

public class RidePlanStrategyTest {
	
	// create a network
	static Network n = new Network();
	// Create source, destination
	Point source = new Point(0,0);
	Point destination = new Point(10,10);
	// Create User
	User bob = new User("bob", new Point(0,0), new NoCardVisitor());
	
	// Create plus source stations 
	static Station sourceStation = new StandardStation(10, new Point(0,0.1));
	// Create standard dest stations 
	static Station destStation = new StandardStation(10, new Point(9, 9.5));
	// Create empty nearest to starting point 
	static Station emptySourceStation = new StandardStation(10, new Point(9, 9.5));
	// Create elec station nearest to starting point 
	static Station elecSourceStation = new StandardStation(10, new Point(9, 9.5));
	// Create full destination station
	static Station fullDestStation = new StandardStation(10, new Point(9, 9.5));	

    @BeforeClass
    public static void initialize() {
		try {
			// Plus and Standard destinations are the same distance away
			n.addStation(sourceStation);
			n.addStation(destStation);
			n.addStation(emptySourceStation);
			n.addStation(fullDestStation);
			n.addStation(elecSourceStation);
			
			// add one bike to source and destination stations : They are all Mechanical. 
			sourceStation.addBike(new MechBike());
			destStation.addBike(new MechBike());			
			
			// fill full station
			while(fullDestStation.addBike(new MechBike())) {				
			}
			// elecSourceStation only has elec bikes
			elecSourceStation.addBike(new ElecBike());
			// empty source station doesn't have any bikes
		} catch (Exception e) {
			e.printStackTrace();
		}

    }

	@Test
	/**
	 * Choose a source station with at least a bike
	 * Choose a dest station that is not full
	 */
	public void ChooseNotEmptySourceAndNotFullDestWhenPlanningRide() {
		RidePlan ap = n.planRide(source, destination, bob, "avoidPlus", "Mech");
		RidePlan apRidePlan = new RidePlan(source, destination, sourceStation, destStation, "avoidPlus", "Mech");
		assertTrue(ap.equals(apRidePlan));
		
		RidePlan pp = n.planRide(source, destination, bob, "preferPlus", "Mech");
		RidePlan ppRidePlan = new RidePlan(source, destination, sourceStation, destStation, "preferPlus", "Mech");
		assertTrue(pp.equals(ppRidePlan));
		
		RidePlan s = n.planRide(source, destination, bob, "shortest", "Mech");
		RidePlan sRidePlan = new RidePlan(source, destination, sourceStation, destStation, "shortest", "Mech");
		assertTrue(s.equals(sRidePlan));
		
		RidePlan f = n.planRide(source, destination, bob, "fastest", "Mech");
		RidePlan fRidePlan = new RidePlan(source, destination, sourceStation, destStation, "fastest", "Mech");
		assertTrue(f.equals(fRidePlan));
		
		RidePlan u = n.planRide(source, destination, bob, "uniform", "Mech");
		RidePlan uRidePlan = new RidePlan(source, destination, sourceStation, destStation, "uniform", "Mech");
		assertTrue(u.equals(uRidePlan));
		
		
	}
}
