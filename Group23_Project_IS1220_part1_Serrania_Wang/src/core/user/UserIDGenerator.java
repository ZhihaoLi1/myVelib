package core.user;

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
	
	public void reset() {
		counter = 0;
	}
}
