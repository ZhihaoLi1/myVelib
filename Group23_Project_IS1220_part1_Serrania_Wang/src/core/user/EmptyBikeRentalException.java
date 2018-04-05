package core.user;

public class EmptyBikeRentalException extends Exception {
	private final int userId;

	public EmptyBikeRentalException(int userId) {
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}
	
	@Override
	public String getMessage() {
		return "Tried to set an empty bike rental for user " + userId + ".";
	}
}
