package core;

import core.station.ParkingSlot;

// FIXME: Javadoc
public class OngoingBikeRentalException extends Exception {
	private final ParkingSlot parkingSlot;

	public OngoingBikeRentalException(ParkingSlot parkingSlot) {
		this.parkingSlot = parkingSlot;
	}

	public ParkingSlot getParkingSlot() {
		return parkingSlot;
	}
}
