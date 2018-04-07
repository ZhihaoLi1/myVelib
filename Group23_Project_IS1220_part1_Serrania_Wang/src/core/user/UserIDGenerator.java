package core.user;

/**
 * ID Generator for user ids <br>
 * Implements the singleton pattern
 * 
 * @author matto
 *
 */
public class UserIDGenerator {
	private static UserIDGenerator instance = null;
	private int counter = 0;

	/**
	 * Gets the current instance of IDGenerator, or creates it if it does not exist
	 * 
	 * @return the IDGenerator
	 */
	public static UserIDGenerator getInstance() {
		if (instance == null) {
			instance = new UserIDGenerator();
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
