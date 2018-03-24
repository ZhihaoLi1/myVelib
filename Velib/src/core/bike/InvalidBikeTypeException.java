package core.bike;

import core.BikeType;

// FIXME: Javadoc
public class InvalidBikeTypeException extends Exception {

	private final BikeType bikeType;

	public InvalidBikeTypeException(BikeType bikeType) {
		this.bikeType = bikeType;
	}

	public BikeType getBikeType() {
		return bikeType;
	}
}
