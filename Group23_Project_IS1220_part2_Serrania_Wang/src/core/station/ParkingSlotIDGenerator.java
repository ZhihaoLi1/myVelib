package core.station;

/**
 * ID Generator for parking slot ids <br>
 * Implements the singleton pattern
 * 
 * @author animato
 *
 */
public class ParkingSlotIDGenerator {
	private static ParkingSlotIDGenerator instance = null;
	private int counter = 0;

	/**
	 * Gets the current instance of IDGenerator, or creates it if it does not exist
	 * 
	 * @return the IDGenerator
	 */
	public static ParkingSlotIDGenerator getInstance() {
		if (instance == null) {
			instance = new ParkingSlotIDGenerator();
		}
		return instance;
	}

	public int getNextIDNumber() {
		counter++;
		return counter;
	}
	
	/**
	 * Used when running tests, to make sure the app starts in a clean state
	 */
	public void reset() {
		counter = 0;
	}
}
