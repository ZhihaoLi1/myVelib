package core.station;

/**
 * Exception thrown when a bike is added to an already occupied (not working
 * and/or really occupied).
 * 
 * @author matto
 *
 */
public class OccupiedParkingSlotException extends Exception {
	private final ParkingSlot parkingSlot;

	public OccupiedParkingSlotException(ParkingSlot parkingSlot) {
		this.parkingSlot = parkingSlot;
	}

	public ParkingSlot getParkingSlot() {
		return parkingSlot;
	}

	@Override
	public String getMessage() {
		return "The parking slot " + parkingSlot.getId() + "is already occupied or out of order.";
	}
}
