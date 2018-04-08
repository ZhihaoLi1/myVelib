package core.bike;

/**
 * Exception thrown when a given BikeType is not recognized by the system.
 * 
 * @author matto
 *
 */
public class InvalidBikeTypeException extends Exception {

	private final String bikeType;

	public InvalidBikeTypeException(String bikeType) {
		this.bikeType = bikeType;
	}

	public String getBikeType() {
		return bikeType;
	}

	@Override
	public String getMessage() {
		return "The given bike type is unknown: " + bikeType + ".";
	}
}
