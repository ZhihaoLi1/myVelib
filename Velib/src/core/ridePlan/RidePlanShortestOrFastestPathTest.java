package core.ridePlan;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import core.Network;
import core.User;
import core.bike.MechBike;
import core.card.NoCardVisitor;
import core.point.Point;
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
			sourceStationS.addBike(new MechBike());
			destStationS.addBike(new MechBike());
			sourceStationF.addBike(new MechBike());
			destStationF.addBike(new MechBike());
			
		} catch (Exception e) {
			e.printStackTrace();
		}

    }

	@Test
	/**
	 * Choose a the source station as source station and dest station as dest station not the other way round
	 */
	public void chooseCorrectStationsWhenPlanningShortestRide() {		
		RidePlan s = n.planRide(source, destination, bob, "shortest", "Mech");
		RidePlan sRidePlan = new RidePlan(source, destination, sourceStationS, destStationS, "shortest", "Mech");
		System.out.println(s.getSourceStation().getId());
		System.out.println(s.getDestinationStation().getId());
		System.out.println(sourceStationS.getId());
		System.out.println(destStationS.getId());
		assertTrue(s.equals(sRidePlan));
	}

	public void chooseCorrectStationsWhenPlanningFastestRide() {		
		RidePlan s = n.planRide(source, destination, bob, "shortest", "Mech");
		RidePlan sRidePlan = new RidePlan(source, destination, sourceStationF, destStationF, "shortest", "Mech");
		System.out.println(s.getSourceStation().getId());
		System.out.println(s.getDestinationStation().getId());
		System.out.println(sourceStationS.getId());
		System.out.println(destStationS.getId());
		assertTrue(s.equals(sRidePlan));
	}

}
