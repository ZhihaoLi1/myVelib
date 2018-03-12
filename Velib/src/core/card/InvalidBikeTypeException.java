package core.card;

import core.bike.Bike;

public class InvalidBikeTypeException extends Exception {

	private final Bike bike;
	
	public InvalidBikeTypeException(Bike bike) {
		this.bike = bike;
	}

	public Bike getBike() {
		return bike;
	}
}
