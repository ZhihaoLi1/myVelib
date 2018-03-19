package core.station;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import core.point.Point;
import core.utils.DateParser;

/**
 * Collects stats about a given station.
 * 
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
	 * @param station - The station these stats are about
	 */
	public StationStats(Station station) {
		this.station = station;
	}
	
	/**
	 * Calculates the occupation rate of a station for a given time period
	 * @param startDate 
	 * @param endDate
	 * @return the occupation rate of the station
	 * @throws NullPointerException if the startDate or endDate is null
	 */
	public double getOccupationRate(LocalDateTime startDate, LocalDateTime endDate) throws NullPointerException {
		double totalTimeOccupied = 0;
		for (ParkingSlot parkingSlot: station.getParkingSlots()) {
			totalTimeOccupied += parkingSlot.getOccupationRate(startDate, endDate);
		}
		
		return totalTimeOccupied / (station.getParkingSlots().size() * startDate.until(endDate, ChronoUnit.SECONDS));
	}

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
