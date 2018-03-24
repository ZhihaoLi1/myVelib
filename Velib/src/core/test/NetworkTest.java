package core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import core.Network;

/**
 * Test creation of network 
 * 
 * @author animato
 *
 */
public class NetworkTest {

	@Test
	public void createDefaultNetworkThenNumberOfBikesDistributedCorrectly() {
		ArrayList<Integer> numberOfParkingSlotsPerStation = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			numberOfParkingSlotsPerStation.add(10);
		}
		Network n = new Network("testNetwork", 10, numberOfParkingSlotsPerStation, 10, 0.80, 0.5, 0.5);

		// total number of stations should be 10
		assertEquals(n.getStations().size(), 10);
		
		// FIXME total number of bikes should be 80 
		
		// FIXME elec bike 40 and mech bike 40
		
		
	}

}
