package core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import core.Network;

// FIXME: Comments
public class NetworkTest {

	@Test
	public void createDefaultNetworkThenNumberOfBikesDistributedCorrectly() {
		ArrayList<Integer> numberOfParkingSlotsPerStation = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			numberOfParkingSlotsPerStation.add(10);
		}
		Network n = new Network("testNetwork", 10, numberOfParkingSlotsPerStation, 10, 0.75, 0.5, 0.5);

		// total number of stations should be 10
		assertEquals(n.getStations().size(), 10);
	}

}
