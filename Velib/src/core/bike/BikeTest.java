package core.bike;

import static org.junit.Assert.*;

import org.junit.Test;

public class BikeTest {

	/**
	 * Different id = different bikes. 
	 * Same Id, same bike
	 */
	@Test
	public void testEquals() {
		ElecBike eb = new ElecBike();
		assertTrue(eb.equals(eb));
		assertTrue(!eb.equals(new ElecBike()));
		
		MechBike mb = new MechBike();
		assertTrue(mb.equals(mb));
		assertTrue(!mb.equals(new MechBike()));
	}


	/**
	 * Different id = different bikes. 
	 * Same Id, same bike
	 */
	@Test
	public void testHashCodes() {
		ElecBike eb = new ElecBike();
		assertTrue(eb.hashCode() != new ElecBike().hashCode());
		
		MechBike mb = new MechBike();
		assertTrue(mb.hashCode() != new MechBike().hashCode());
	}
}
