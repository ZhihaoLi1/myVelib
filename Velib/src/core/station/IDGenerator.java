package core.station;

public class IDGenerator {
	private static IDGenerator instance = null;
	private int counter = 0; 
	
	
	
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
