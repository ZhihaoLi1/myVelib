package core.card;

import core.rentals.BikeRental;

/**
 * Exception thrown when one of the dates of a bikeRental is invalid for price
 * calculation.
 * 
 * @author matto
 *
 */
public class InvalidDatesException extends Exception {

	private final BikeRental bikeRental;

	public InvalidDatesException(BikeRental bikeRental) {
		this.bikeRental = bikeRental;
	}

	public BikeRental getBikeRental() {
		return bikeRental;
	}

	@Override
	public String getMessage() {
		return "The price of this rental cannot be calculated: " + bikeRental + " (invalid dates)";
	}

}
