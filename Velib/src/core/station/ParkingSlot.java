package core.station;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import core.bike.Bike;

/**
 * Represents a parking slot of a station. <br>
 * A parking slot can contain a bike (which can be added and removed), and has a
 * status (working or out-of-order). If it's out-of-order, no operations can be
 * done on the parking slot. <br>
 * Moreover, the parking slot stores a list of its statuses over time, to
 * calculate its occupation rate.
 * 
 * @author matto
 *
 */
public class ParkingSlot {
	private final int id;
	private Boolean working = true;
	private Bike bike;
	private ParkingSlotStatus currentStatus;
	private ArrayList<ParkingSlotStatus> statusHistory;
	private boolean hasChanged = false;

	public ParkingSlot() {
		this.id = ParkingSlotIDGenerator.getInstance().getNextIDNumber();
		this.statusHistory = new ArrayList<ParkingSlotStatus>();
		this.setStatus(ParkingSlotStatusName.FREE, LocalDateTime.MIN);
	}

	@Override
	public String toString() {
		if (bike == null) return "No bike";
		return bike.getType();
	}
	public Boolean isWorking() {
		return working;
	}

	public void setWorking(Boolean working, LocalDateTime date) {
		if (working != this.working) {
			this.hasChanged = true;
		}
		this.working = working;
		changeStatus(date);
	}

	public Boolean hasBike() {
		return bike != null;
	}

	public Bike getBike() {
		return bike;
	}

	/**
	 * Adds a bike to the parking slot.
	 * 
	 * @param bike
	 *            the bike to set in the parking slot
	 * @param date
	 *            the date at which the action is performed
	 * @throws OccupiedParkingSlotException
	 *             when the station is not working or is already occupied
	 */
	public void setBike(Bike bike, LocalDateTime date) throws OccupiedParkingSlotException {
		if (this.working == false || this.bike != null) {
			throw new OccupiedParkingSlotException(this);
		}
		this.bike = bike;
		this.hasChanged = true;
		changeStatus(date);
	}

	/**
	 * Empties the parking slot.
	 * 
	 * @param date
	 *            the date at which the action is performed
	 * @throws OccupiedParkingSlotException
	 *             when the station is not working
	 */
	public void emptyBike(LocalDateTime date) throws OccupiedParkingSlotException {
		if (this.working == false) {
			throw new OccupiedParkingSlotException(this);
		}
		this.bike = null;
		this.hasChanged = true;
		changeStatus(date);
	}

	public int getId() {
		return id;
	}

	public ParkingSlotStatus getCurrentStatus() {
		return currentStatus;
	}
	
	public ArrayList<ParkingSlotStatus> getStatusHistory() {
		return statusHistory;
	}

	public ParkingSlotStatusName calculateStatusName() {
		if (!isWorking()) {
			return ParkingSlotStatusName.OUT_OF_ORDER;
		}

		if (hasBike()) {
			return ParkingSlotStatusName.OCCUPIED;
		}

		return ParkingSlotStatusName.FREE;
	}

	public void changeStatus(LocalDateTime date) {
		if (hasChanged) {
			setStatus(calculateStatusName(), date);
			hasChanged = false;
		}
	}

	/**
	 * Sets a new status. Also ends the current one (adds the endDate to it).
	 * 
	 * @param newStatusName
	 * @param date
	 *            the date at which the action is performed
	 */
	public void setStatus(ParkingSlotStatusName newStatusName, LocalDateTime date) {
		if (this.currentStatus != null) {
			this.currentStatus.setEndDate(date);
		}

		this.currentStatus = new ParkingSlotStatus(newStatusName, date);
		this.statusHistory.add(this.currentStatus);
	}

	/**
	 * Calculates the occupation time of the parking slot for a given timespan,
	 * defined by: (occupied time in timespan) / (total time in timespan)
	 * 
	 * @param startDate
	 * @param endDate
	 * @return the timespan of the status which is contained between the given start
	 *         time and end time
	 * @throws IllegalArgumentException
	 *             when one of the statuses in statusHistory, startDate or endDate
	 *             is not defined
	 */
	public double getOccupationTime(LocalDateTime startDate, LocalDateTime endDate) throws IllegalArgumentException {
		double totalOccupiedTime = 0;
		for (ParkingSlotStatus status : statusHistory) {
			totalOccupiedTime += calculateEffectiveTimeInSpan(status, startDate, endDate)
					* status.getStatusName().getOccupationRate();
		}

		return totalOccupiedTime;
	}

	/**
	 * Calculates the time (in seconds) of the given status is contained between the
	 * given start time and end time.
	 * 
	 * @param status
	 * @param startDate
	 * @param endDate
	 * @return the time (in seconds) for which the status is contained between the
	 *         given start time and end time
	 * @throws IllegalArgumentException
	 *             when the status, startDate or endDate is not defined
	 */
	private double calculateEffectiveTimeInSpan(ParkingSlotStatus status, LocalDateTime startDate,
			LocalDateTime endDate) throws IllegalArgumentException {
		LocalDateTime effectiveStartDate;
		LocalDateTime effectiveEndDate;

		if (status == null) {
			throw new IllegalArgumentException("Invalid status given");
		}

		if (startDate == null || endDate == null) {
			throw new IllegalArgumentException("Invalid date given");
		}

		if (status.getStartDate().compareTo(startDate) <= 0) {
			effectiveStartDate = startDate;
		} else {
			effectiveStartDate = status.getStartDate();
		}

		if (status.getEndDate() == null) {
			effectiveEndDate = endDate;
		} else {
			if (status.getEndDate().compareTo(endDate) <= 0) {
				effectiveEndDate = status.getEndDate();
			} else {
				effectiveEndDate = endDate;
			}
		}

		return Math.max(effectiveStartDate.until(effectiveEndDate, ChronoUnit.SECONDS), 0);
	}
}
