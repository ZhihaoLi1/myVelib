package core.user;

/**
 * Exception thrown when a user tries to return a bike even though he has no
 * bike rental
 * 
 * @author matto
 *
 */
public class BikeRentalNotFoundException extends Exception {
	private final int userId;

	public BikeRentalNotFoundException(int userId) {
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}

	@Override
	public String getMessage() {
		return "User " + userId + " has no ongoing bike rental.";
	}
}
