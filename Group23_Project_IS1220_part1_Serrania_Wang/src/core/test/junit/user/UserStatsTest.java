package core.test.junit.user;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import core.user.UserStats;

public class UserStatsTest {
	UserStats stats = new UserStats();

	@Test
	public void testGetTotalRides() {
		assertTrue(stats.getTotalRides() == 0);
	}

	@Test
	public void testIncrementTotalRides() {
		stats.incrementTotalRides();
		assertTrue(stats.getTotalRides() == 1);

		stats.incrementTotalRides();
		stats.incrementTotalRides();
		assertTrue(stats.getTotalRides() == 3);
	}

	@Test
	public void testGetTotalCharges() {
		assertTrue(stats.getTotalCharges() == 0);
	}

	@Test
	public void testAddTotalCharges() {
		stats.addTotalCharges(10.0);
		assertTrue(stats.getTotalCharges() == 10.0);
	}
	
	@Test
	public void testGetTotalTimeCredits() {
		assertTrue(stats.getTotalTimeCredits() == 0);
	}

	@Test
	public void testAddTotalTimeCredits() {
		stats.addTotalTimeCredits(5);
		assertTrue(stats.getTotalTimeCredits() == 5);
	}
	
	@Test
	public void testGetTotalTimeSpent() {
		assertTrue(stats.getTotalTimeSpent() == 0);
	}

	@Test
	public void testAddTotalTimeSpent() {
		stats.addTotalTimeSpent(50);
		assertTrue(stats.getTotalTimeSpent() == 50);
	}
}
