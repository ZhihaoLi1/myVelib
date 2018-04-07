package core.station;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import utils.DateParser;
import utils.Point;

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

	// Constructor
	
	/**
	 * Creates a stats object for a given station
	 * 
	 * @param station
	 *            - The station these stats are about
	 */
	public StationStats(Station station) {
		this.station = station;
	}
	
	// Core methods

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
	public double getOccupationRate(LocalDateTime startDate, LocalDateTime endDate) throws InvalidTimeSpanException {
		if (endDate == null || startDate == null || startDate.until(endDate, ChronoUnit.SECONDS) <= 0) {
			throw new InvalidTimeSpanException(startDate, endDate);
		}
		double totalTimeOccupied = 0;
		for (ParkingSlot parkingSlot : station.getParkingSlots()) {
			totalTimeOccupied += parkingSlot.getOccupationTime(startDate, endDate);
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
		return "Total rentals: " + this.totalRentals + "\nTotal returns: " + this.totalReturns;
	}
}
