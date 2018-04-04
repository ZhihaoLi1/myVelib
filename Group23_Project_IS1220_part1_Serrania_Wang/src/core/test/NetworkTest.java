package core.test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Test;

import core.Network;
import core.card.InvalidCardTypeException;
import core.station.PlusStation;
import core.station.StandardStation;
import core.station.Station;
import core.user.User;
import utils.DateParser;
import utils.Point;

/**
 * Test creation of network and methods of Network 
 * 
 * @author animato
 *
 */
public class NetworkTest {

	@Test
	public void createDefaultNetworkThenNumberOfBikesDistributedCorrectly() {
		int numberOfParkingSlotsPerStation = 10;

		Network n = new Network("testNetwork", 10, numberOfParkingSlotsPerStation, 10, 0.80, 0.5, 0.5, LocalDateTime.now());

		// total number of stations should be 10
		assertEquals(n.getStations().size(), 10);
		
		// FIXME total number of bikes should be 80 
		
		// FIXME elec bike 40 and mech bike 40
	}
	
	@Test
	public void rentAndReturnBikeTest() {
		Network n = new Network("testRental", LocalDateTime.now());
		try {
			User alice = new User("alice");
			n.addUser(alice);
			String message = "";
			for (Station s: n.getStations().values()) {
				if (s.getOnline() && s.hasCorrectBikeType("MECH")) {
					message = n.rentBike(alice.getId(), s.getId(), "MECH", DateParser.parse("01/01/2000 09:00:00"));
					break;
				}
			}
			System.out.println(alice.getBikeRental());
			assertNotEquals(alice.getBikeRental(), null);
			assertEquals(alice.getBikeRental().getBike().getType(), "MECH");
			
			// returning the bike
			for (Station s: n.getStations().values()) {
				if (s.getOnline() && !s.isFull()) {
					message = n.returnBike(alice.getId(), s.getId(), DateParser.parse("01/01/2000 10:00:00"), 60);
					break;
				}
			}
			
			System.out.println(alice.getStats().toString());
			assertEquals(alice.getBikeRental(), null);
			assertEquals(alice.getStats().getTotalTimeSpent(), 60);

		} catch (InvalidCardTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	@Test
	public void addStationToNetwork() {
		Network n = new Network();
		
		n.addStation("STANDARD");
		n.addStation("PLUS"); 
	
		assertEquals(n.getStations().values().size(), 2);
		assertTrue(new ArrayList<Station>(n.getStations().values()).get(0) instanceof StandardStation);
		assertTrue(new ArrayList<Station>(n.getStations().values()).get(1) instanceof PlusStation);

		n.addStation("STANDARD", 2, 2, 10, true);
		Station s = new ArrayList<Station>(n.getStations().values()).get(2);
		assertTrue(s instanceof StandardStation);
		assertEquals(s.getCoordinates(), new Point(2,2));
		assertEquals(s.getOnline(), true);
	}

}
