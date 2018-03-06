package core.station;

import core.bike.Bike;

public class ParkingSlot {
	private final int id;
	private Boolean working = true;
	private Bike bike;
	
	public ParkingSlot(){
		this.id = IDGenerator.getInstance().getNextIDNumber();
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
