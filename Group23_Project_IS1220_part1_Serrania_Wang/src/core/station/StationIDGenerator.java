package core.station;

/**
 * ID Generator for stations
 * 
 * Follows the singleton pattern
 * 
 * @author animato
 *
 */
public class StationIDGenerator {
	private static StationIDGenerator instance = null;
	private int counter = 0;

	/**
	 * Gets the current instance of IDGenerator, or creates it if it does not exist
	 * 
	 * @return the IDGenerator
	 */
	public static StationIDGenerator getInstance() {
		if (instance == null) {
			instance = new StationIDGenerator();
		}
		return instance;
	}

	public int getNextIDNumber() {
		counter++;
		return counter;
	}
	
	public void reset() {
		counter = 0;
	}
}
