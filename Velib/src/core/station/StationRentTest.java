package core.station;

import static org.junit.Assert.*;

import org.junit.Test;

import core.Network;
import core.User;
import core.card.NoCardVisitor;
import core.point.Point;

public class StationRentTest {
	
	// create a network
	static Network n = new Network();
	// Create User
	User bob = new User("bob", new Point(0,0), new NoCardVisitor());
	
	// Stations
	static Station s = new PlusStation(10, new Point(0,0.1));
	static Station emptyS = new PlusStation(10, new Point(0,0.2));
	
	@Test
	public void rentBikefrom() {
		fail("Not yet implemented");
	}

}
