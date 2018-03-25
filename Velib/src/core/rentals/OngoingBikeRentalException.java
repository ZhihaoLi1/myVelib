package core.rentals;

import core.station.ParkingSlot;
import user.User;

/**
 * Thrown when a bike rental is added to a user which already has one.
 * @author matto
 *
 */
public class OngoingBikeRentalException extends Exception {
	private final User user;

	public OngoingBikeRentalException(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
	
	@Override
	public String getMessage() {
		return "Cannot add a new rental to user " + user.getName() + ": it already has one.";
	}
}
