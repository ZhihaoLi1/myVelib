package core.test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
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
		int numberOfParkingSlotsPerStation = 10;

		Network n = new Network("testNetwork", 10, numberOfParkingSlotsPerStation, 10, 0.80, 0.5, 0.5, LocalDateTime.now());

		// total number of stations should be 10
		assertEquals(n.getStations().size(), 10);
		
		// FIXME total number of bikes should be 80 
		
		// FIXME elec bike 40 and mech bike 40
	}

}
