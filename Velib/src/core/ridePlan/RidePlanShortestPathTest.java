package core.ridePlan;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import core.Network;
import core.User;
import core.bike.ElecBike;
import core.bike.MechBike;
import core.card.NoCard;
import core.point.Point;
import core.station.StandardStation;
import core.station.Station;

public class RidePlanShortestPathTest {

	// create a network
	static Network n = new Network();
	// Create source, destination
	Point source = new Point(0,0);
	Point destination = new Point(10,10);
	// Create User
	User bob = new User("bob", new Point(0,0), new NoCard());
	
	// Create plus source stations 
	static Station sourceStation = new StandardStation(10, new Point(0,0.1));
	// Create standard dest stations 
	static Station destStation = new StandardStation(10, new Point(9, 9.5));

    @BeforeClass
    public static void initialize() {
		try {

			n.addStation(sourceStation);
			n.addStation(destStation);
			
			// add one bike to source and destination stations : They are all Mechanical. 
			sourceStation.addBike(new MechBike());
			destStation.addBike(new MechBike());
			
		} catch (Exception e) {
			e.printStackTrace();
		}

    }

	@Test
	/**
	 * Choose a the source station as source station and dest station as dest station not the other way round
	 */
	public void chooseCorrectStationsWhenPlanningRide() {		
		RidePlan s = n.planRide(source, destination, bob, "shortest", "Mech");
		RidePlan sRidePlan = new RidePlan(source, destination, sourceStation, destStation, "shortest", "Mech");
		System.out.println(s.getSourceStation().getId());
		System.out.println(s.getDestinationStation().getId());
		System.out.println(sourceStation.getId());
		System.out.println(destStation.getId());
		assertTrue(s.equals(sRidePlan));
	}
}
