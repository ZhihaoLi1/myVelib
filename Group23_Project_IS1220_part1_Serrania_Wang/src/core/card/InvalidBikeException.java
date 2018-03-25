package core.card;

import core.rentals.BikeRental;

/**
 * Exception thrown when the bike of a bikeRental is invalid for price
 * calculation.
 * 
 * @author matto
 *
 */
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
		return "The price of this rental cannot be calculated: " + bikeRental + " (invalid bike).";
	}
}
