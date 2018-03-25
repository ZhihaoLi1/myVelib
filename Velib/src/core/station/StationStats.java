package core.station;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import core.utils.DateParser;
import core.utils.Point;

/**
 * Collects stats about a given station.<br>
 * Also in charge of computing the station's occupation rate.
 * 
 * @author matto
 */
public class StationStats {
	private Station station;
	private int totalRentals = 0;
	private int totalReturns = 0;

	/**
	 * Creates a stats object for a given station
	 * 
	 * @param station
	 *            - The station these stats are about
	 */
	public StationStats(Station station) {
		this.station = station;
	}

	/**
	 * Calculates the occupation rate of a station for a given time period. It is
	 * defined as the mean of occupation rates of all parking slots in the station.
	 * 
	 * @param startDate
	 * @param endDate
	 * @return the occupation rate of the station
	 * @throws IllegalArgumentException
	 *             if the startDate or endDate is null, or if startDate == endDate
	 */
	public double getOccupationRate(LocalDateTime startDate, LocalDateTime endDate) throws IllegalArgumentException {
		if (startDate == endDate) {
			// TODO: Custom exception?
			throw new IllegalArgumentException("Cannot calculate occupation rate over an empty timespan");
		}
		double totalTimeOccupied = 0;
		for (ParkingSlot parkingSlot : station.getParkingSlots()) {
			totalTimeOccupied += parkingSlot.getOccupationRate(startDate, endDate);
		}

		return totalTimeOccupied / (station.getParkingSlots().size() * startDate.until(endDate, ChronoUnit.SECONDS));
	}

	// Getters / Setters
	public int getTotalRentals() {
		return this.totalRentals;
	}

	public void incrementTotalRentals() {
		this.totalRentals += 1;
	}

	public int getTotalReturns() {
		return this.totalReturns;
	}

	public void incrementTotalReturns() {
		this.totalReturns += 1;
	}

	@Override
	public String toString() {
		return "total rentals: " + this.totalRentals + ", total returns: " + this.totalReturns;
	}
}
