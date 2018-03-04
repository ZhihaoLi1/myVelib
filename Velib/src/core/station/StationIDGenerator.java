package core.station;

public class StationIDGenerator {
	private static StationIDGenerator instance = null;
	private int counter = 0; 
	
	
	
	public static StationIDGenerator getInstance() {
		if (instance == null) {
			instance = new StationIDGenerator();
		}
		return instance;
	}
	
	public int getNextIDNumber() {
		counter ++;
		return counter;
	}
}
