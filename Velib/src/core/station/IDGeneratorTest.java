package core.station;

import static org.junit.Assert.*;

import org.junit.Test;

public class IDGeneratorTest {

	/**
	 * Ensure that only a single instance of the generator is created and used 
	 */
	@Test
	public void testGenerator() {
		StationIDGenerator gen1 = StationIDGenerator.getInstance();
		StationIDGenerator gen2 = StationIDGenerator.getInstance();
		
		for(int i=1;i<5;i++){
			assertEquals(gen1.getNextIDNumber(), i*2 - 1);
			assertEquals(gen2.getNextIDNumber(), i*2);
		}
	}
}
