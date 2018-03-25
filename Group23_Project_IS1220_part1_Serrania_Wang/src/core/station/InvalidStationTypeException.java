package core.station;

/**
 * Exception thrown when a given StationType is not recognized by the system.
 * @author matto
 *
 */
public class InvalidStationTypeException extends Exception {

	private final String stationType;

	public InvalidStationTypeException(String stationType) {
		this.stationType = stationType;
	}

	public String getStationType() {
		return stationType;
	}
	
	@Override
	public String getMessage() {
		return "The given station type is unknown: " + stationType + ".";
	}
}
