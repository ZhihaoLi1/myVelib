package core.card;

import core.rentals.BikeRental;

// FIXME: Javadoc
public class InvalidBikeException extends Exception {

	private final BikeRental bikeRental;

	public InvalidBikeException(BikeRental bikeRental) {
		this.bikeRental = bikeRental;
	}

	public BikeRental getBikeRental() {
		return bikeRental;
	}
	
	@Override
	public String getMessage() {
		return "The price of this rental cannot be calculated: " + bikeRental + " (invalid bike)";
	}
}
