package core.station;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import core.bike.Bike;

public class ParkingSlot {
	private final int id;
	private Boolean working = true;
	private Bike bike;
	private ParkingSlotStatus currentStatus;
	private ArrayList<ParkingSlotStatus> statusHistory;
	private boolean hasChanged = false;

	public ParkingSlot(){
		this.id = IDGenerator.getInstance().getNextIDNumber();
		this.statusHistory = new ArrayList<ParkingSlotStatus>();
		this.setStatus(ParkingSlotStatusName.FREE, LocalDateTime.MIN);
	}


	public Boolean isWorking() {
		return working;
	}

	public void setWorking(Boolean working, LocalDateTime date) {
		if(working != this.working) {
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

	public void setBike(Bike bike, LocalDateTime date) throws Exception {
		if (this.bike != null && bike != null) {
			throw new Exception("Parking slot already contains bike"); // FIXME: throw new OccupiedParkingSlotException
		}
		this.bike = bike;
		this.hasChanged = true;
		changeStatus(date);
	}

	public int getId() {
		return id;
	}
	
	public ParkingSlotStatus getCurrentStatus() {
		return currentStatus;
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
	
	public void setStatus(ParkingSlotStatusName newStatusName, LocalDateTime date) {
		if (this.currentStatus != null) {
			this.currentStatus.setEndDate(date);
		}
		
		this.currentStatus = new ParkingSlotStatus(newStatusName, date);
		this.statusHistory.add(this.currentStatus);
	}
	
	public double getOccupationRate(LocalDateTime startDate, LocalDateTime endDate) throws NullPointerException {
		double totalOccupiedTime = 0;
		for (ParkingSlotStatus status: statusHistory) {
			totalOccupiedTime += calculateEffectiveTimeInSpan(status, startDate, endDate) * status.getStatusName().getOccupationRate();
		}
		
		return totalOccupiedTime;
	}
	
	private double calculateEffectiveTimeInSpan(ParkingSlotStatus status, LocalDateTime startDate, LocalDateTime endDate) throws NullPointerException {
		LocalDateTime effectiveStartDate;
		LocalDateTime effectiveEndDate;

		if (status == null) {
			throw new NullPointerException("Invalid status");
		}
		
		if (startDate == null || endDate == null) {
			throw new NullPointerException("Wrong date given");
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
