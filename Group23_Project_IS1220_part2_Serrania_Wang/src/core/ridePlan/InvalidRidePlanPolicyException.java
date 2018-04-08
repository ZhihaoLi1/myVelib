package core.ridePlan;

public class InvalidRidePlanPolicyException extends Exception {
	private final String policy;

	public InvalidRidePlanPolicyException(String policy) {
		this.policy = policy;
	}

	public String getPolicy() {
		return policy;
	}

	@Override
	public String getMessage() {
		return "The given ride plan policy is unknown: " + policy + ".";
	}
}
