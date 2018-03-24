package core.station;

import java.time.LocalDateTime;

/**
 * Describes the status of a parking slot over a given time period
 * 
 * @author matto
 *
 */
public class ParkingSlotStatus {
	private final ParkingSlotStatusName statusName;
	private final LocalDateTime startDate;
	private LocalDateTime endDate;

	/**
	 * Initializes the status at the given time
	 * 
	 * @param statusName
	 * @param startDate
	 */
	public ParkingSlotStatus(ParkingSlotStatusName statusName, LocalDateTime startDate) {
		this.statusName = statusName;
		this.startDate = startDate;
	}

	public ParkingSlotStatusName getStatusName() {
		return statusName;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "ParkngSlotStatus [status: " + this.statusName + ", startDate: " + this.startDate + ", endDate: "
				+ this.endDate + "]";
	}
}
