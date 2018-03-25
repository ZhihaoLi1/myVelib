package core.bike;

import core.BikeType;

/**
 * Exception thrown when a given BikeType is not recognized by the system.
 * 
 * @author matto
 *
 */
public class InvalidBikeTypeException extends Exception {

	private final BikeType bikeType;

	public InvalidBikeTypeException(BikeType bikeType) {
		this.bikeType = bikeType;
	}

	public BikeType getBikeType() {
		return bikeType;
	}

	@Override
	public String getMessage() {
		return "The given bike type is unknown: " + bikeType + ".";
	}
}
