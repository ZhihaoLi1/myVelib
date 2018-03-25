package core.station.stationSort;

public class InvalidSortingPolicyException extends Exception {
	private final String policy;

	public InvalidSortingPolicyException(String policy) {
		this.policy = policy;
	}

	public String getPolicy() {
		return policy;
	}

	@Override
	public String getMessage() {
		return "The given sorting policy is unknown: " + policy + ".";
	}
}
