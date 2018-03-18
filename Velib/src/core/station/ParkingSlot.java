package core.station;

import core.bike.Bike;

public class ParkingSlot {
	private final int id;
	private Boolean working = true;
	private Bike bike;
	
	public ParkingSlot(){
		this.id = IDGenerator.getInstance().getNextIDNumber();
	}

	@Override
	public String toString() {
		if (bike == null) {
			return "parkingSlotId: " + this.id + " has no bike and is working " + this.working + "\n";
		}
		return "parkingSlotId: " + this.id + "has bike of type" + this.bike.getType().toString() + " and is working " + this.working + "\n";
	}
	public Boolean getWorking() {
		return working;
	}

	public void setWorking(Boolean working) {
		this.working = working;
	}
	
	public Boolean hasBike() {
		return bike != null;
	}
	
	public Bike getBike() {
		return bike;
	}

	public void setBike(Bike bike) {
		this.bike = bike;
	}

	public int getId() {
		return id;
	}
}
