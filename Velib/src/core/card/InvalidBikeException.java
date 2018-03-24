package core.card;

import core.bike.Bike;

// FIXME: Javadoc
public class InvalidBikeException extends Exception {

	private final Bike bike;

	public InvalidBikeException(Bike bike) {
		this.bike = bike;
	}

	public Bike getBike() {
		return bike;
	}
}
