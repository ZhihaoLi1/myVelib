package core;

// FIXME: Javadoc
public class UserStats {
	// Statistics of user
	private int totalRides;
	private int totalTimeCredits;
	private double totalCharges;
	private int totalTimeSpent; // in minutes

	UserStats() {
		totalRides = 0;
		totalTimeCredits = 0;
		totalCharges = 0;
		totalTimeSpent = 0;
	}

	public int getTotalRides() {
		return totalRides;
	}

	public void incrementTotalRides() {
		this.totalRides += 1;
	}

	public int getTotalTimeCredits() {
		return totalTimeCredits;
	}

	public void addTotalTimeCredits(int timeCredits) {
		this.totalTimeCredits += timeCredits;
	}

	public double getTotalCharges() {
		return totalCharges;
	}

	public void addTotalCharges(double charges) {
		this.totalCharges += charges;
	}

	public int getTotalTimeSpent() {
		return totalTimeSpent;
	}

	public void setTotalTimeSpent(int totalTimeSpent) {
		this.totalTimeSpent = totalTimeSpent;
	}

}
