package core.user;

/**
 * Contains statistics of the user
 * @author animato
 *
 */
public class UserStats {
	// Statistics of user
	private int totalRides;
	private int totalTimeCredits; // in minutes
	private double totalCharges; // in euros
	private long totalTimeSpent; // in minutes

	// Constructor 
	
	public UserStats() {
		totalRides = 0;
		totalTimeCredits = 0;
		totalCharges = 0;
		totalTimeSpent = 0;
	}

	// Getters / Setters
	
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

	public long getTotalTimeSpent() {
		return totalTimeSpent;
	}

	public void addTotalTimeSpent(long l) {
		this.totalTimeSpent += l;
	}
	
	@Override 
	public String toString() {
		String stats = "Total number of rides: " + totalRides + "\n";
		stats += "Total credits accumulated: " + totalTimeCredits + " minutes\n";
		stats += "Total amount spent: " + totalCharges + " euro(s)\n";
		stats += "Total time spent of rides: " + totalTimeSpent + " minutes\n";
		return stats;
	}

}
