package core;

import core.station.ParkingSlot;

// FIXME: Javadoc
public class OngoingBikeRentalException extends Exception {
	private final User user;

	public OngoingBikeRentalException(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
