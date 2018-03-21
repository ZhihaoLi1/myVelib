package core.test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.BeforeClass;
import org.junit.Test;

import core.BikeType;
import core.Network;
import core.PolicyName;
import core.User;
import core.bike.MechBike;
import core.card.NoCardVisitor;
import core.point.Point;
import core.ridePlan.RidePlan;
import core.station.StandardStation;
import core.station.Station;

public class RidePlanShortestOrFastestPathTest {

	// create a network
	static Network n = new Network();
	// Create source, destination
	Point source = new Point(0,0);
	Point destination = new Point(10,10);
	// Create User
	User bob = new User("bob", new Point(0,0), new NoCardVisitor());
	
	// Create source stations 
	static Station sourceStationS = new StandardStation(10, new Point(2,2));
	// Create dest stations 
	static Station destStationS = new StandardStation(10, new Point(8, 8));

	// Create plus source stations 
	static Station sourceStationF = new StandardStation(10, new Point(0,2));
	// Create standard dest stations 
	static Station destStationF = new StandardStation(10, new Point(8,10));

    @BeforeClass
    public static void initialize() {
		try {

			n.addStation(sourceStationS);
			n.addStation(destStationS);
			n.addStation(sourceStationF);
			n.addStation(destStationF);
			
			// add one bike to source and destination stations : They are all Mechanical. 
			sourceStationS.addBike(new MechBike(), LocalDateTime.now());
			destStationS.addBike(new MechBike(), LocalDateTime.now());
			sourceStationF.addBike(new MechBike(), LocalDateTime.now());
			destStationF.addBike(new MechBike(), LocalDateTime.now());
			
		} catch (Exception e) {
			e.printStackTrace();
		}

    }

	@Test
	/**
	 * Choose a the source station as source station and dest station as dest station not the other way round
	 */
	public void chooseCorrectStationsWhenPlanningShortestRide() {		
		RidePlan s = n.planRide(source, destination, bob, PolicyName.SHORTEST, BikeType.MECH);
		RidePlan sRidePlan = new RidePlan(source, destination, sourceStationS, destStationS, PolicyName.SHORTEST, BikeType.MECH, null);
		assertTrue(s.equals(sRidePlan));
	}
	
	@Test
	public void chooseCorrectStationsWhenPlanningFastestRide() {		
		RidePlan s = n.planRide(source, destination, bob, PolicyName.FASTEST, BikeType.MECH);
		RidePlan sRidePlan = new RidePlan(source, destination, sourceStationF, destStationF, PolicyName.FASTEST, BikeType.MECH, null);
		assertTrue(s.equals(sRidePlan));
	}

}