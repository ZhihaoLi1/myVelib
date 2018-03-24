package core.station;

public class OccupiedParkingSlotException extends Exception {
	private final ParkingSlot parkingSlot;

	public OccupiedParkingSlotException(ParkingSlot parkingSlot) {
		this.parkingSlot = parkingSlot;
	}

	public ParkingSlot getParkingSlot() {
		return parkingSlot;
	}
}
