package core.test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.BeforeClass;
import org.junit.Test;

import core.BikeType;
import core.Network;
import core.PolicyName;
import core.User;
import core.bike.ElecBike;
import core.bike.MechBike;
import core.card.NoCardVisitor;
import core.point.Point;
import core.ridePlan.RidePlan;
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
	static Station emptySourceStation = new StandardStation(10, new Point(0, 0.02));
	// Create elec station nearest to starting point 
	static Station elecSourceStation = new StandardStation(10, new Point(0, 0.01));
	// Create full destination station
	static Station fullDestStation = new StandardStation(10, new Point(9, 9.9));	

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
			sourceStation.addBike(new MechBike(), LocalDateTime.now());
			destStation.addBike(new MechBike(), LocalDateTime.now());			
			
			// fill full station
			while(fullDestStation.addBike(new MechBike(), LocalDateTime.now())) {				
			}
			// elecSourceStation only has elec bikes
			elecSourceStation.addBike(new ElecBike(), LocalDateTime.now());
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
		RidePlan ap = n.planRide(source, destination, bob, PolicyName.AVOID_PLUS, BikeType.MECH);
		RidePlan apRidePlan = new RidePlan(source, destination, sourceStation, destStation, PolicyName.AVOID_PLUS, BikeType.MECH, null);
		assertTrue(ap.equals(apRidePlan));
		
		RidePlan pp = n.planRide(source, destination, bob, PolicyName.PREFER_PLUS, BikeType.MECH);
		RidePlan ppRidePlan = new RidePlan(source, destination, sourceStation, destStation, PolicyName.PREFER_PLUS, BikeType.MECH, null);
		assertTrue(pp.equals(ppRidePlan));
		
		RidePlan s = n.planRide(source, destination, bob, PolicyName.SHORTEST, BikeType.MECH);
		RidePlan sRidePlan = new RidePlan(source, destination, sourceStation, destStation, PolicyName.SHORTEST, BikeType.MECH, null);
		assertTrue(s.equals(sRidePlan));
		
		RidePlan f = n.planRide(source, destination, bob, PolicyName.FASTEST, BikeType.MECH);
		RidePlan fRidePlan = new RidePlan(source, destination, sourceStation, destStation, PolicyName.FASTEST, BikeType.MECH, null);
		assertTrue(f.equals(fRidePlan));
		
		RidePlan u = n.planRide(source, destination, bob, PolicyName.PRESERVE_UNIFORMITY, BikeType.MECH);
		RidePlan uRidePlan = new RidePlan(source, destination, sourceStation, destStation, PolicyName.PRESERVE_UNIFORMITY, BikeType.MECH, null);
		assertTrue(u.equals(uRidePlan));
		
		
	}
}