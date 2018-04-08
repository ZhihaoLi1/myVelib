package core.bike;

/**
 * ID Generator for bike ids <br>
 * Implements the singleton pattern
 * 
 * @author animato
 *
 */
public class BikeIDGenerator {
	private static BikeIDGenerator instance = null;
	private int counter = 0;

	/**
	 * Gets the current instance of IDGenerator, or creates it if it does not exist
	 * 
	 * @return the IDGenerator
	 */
	public static BikeIDGenerator getInstance() {
		if (instance == null) {
			instance = new BikeIDGenerator();
		}
		return instance;
	}

	public int getNextIDNumber() {
		counter++;
		return counter;
	}
}
