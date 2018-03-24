package core.station;

/**
 * Describes the different statuses a parking slot can have
 * 
 * @author matto
 *
 */
public enum ParkingSlotStatusName {
	FREE(0), OCCUPIED(1), OUT_OF_ORDER(1);

	// The occupation rate indicates if the given status is considered as "occupied"
	// w.r.t. the calculation of the occupation rate
	private final int occupationRate;

	ParkingSlotStatusName(int occupationRate) {
		this.occupationRate = occupationRate;
	}

	public int getOccupationRate() {
		return occupationRate;
	}
}
