package core;

import core.station.ParkingSlot;

// FIXME: Javadoc, override getMessage()
public class OngoingBikeRentalException extends Exception {
	private final User user;

	public OngoingBikeRentalException(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
