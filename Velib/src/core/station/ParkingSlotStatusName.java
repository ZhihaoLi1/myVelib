package core.station;

public enum ParkingSlotStatusName {
	FREE (0),
	OCCUPIED (1),
	OUT_OF_ORDER (1);
	
	private final int occupationRate;
	
	ParkingSlotStatusName(int occupationRate) {
		this.occupationRate = occupationRate;
	}

	public int getOccupationRate() {
		return occupationRate;
	}
}
