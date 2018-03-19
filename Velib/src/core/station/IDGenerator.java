package core.station;

/**
 * ID Generator for stations
 * 
 * Follows the singleton pattern
 * 
 * @author animato
 *
 */
public class IDGenerator {
	private static IDGenerator instance = null;
	private int counter = 0; 
	
	/**
	 * Gets the current instance of IDGenerator, or creates it if it does not exist
	 * @return the IDGenerator
	 */
	public static IDGenerator getInstance() {
		if (instance == null) {
			instance = new IDGenerator();
		}
		return instance;
	}
	
	public int getNextIDNumber() {
		counter ++;
		return counter;
	}
}
