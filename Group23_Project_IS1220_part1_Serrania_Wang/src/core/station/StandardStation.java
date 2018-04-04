package core.station;

import utils.Point;

/**
 * Concrete station which behaves exactly as the abstract station would. No time
 * credit is given on return. <br>
 * 
 * Extends Station
 * 
 * @see Station
 * @author matto
 *
 */
public class StandardStation extends Station {

	/**
	 * Create a station with the given number of parking slots and coordinates. By
	 * default, online status is true.
	 * 
	 * @param numberOfParkingSlots
	 * @param coordinates
	 */
	protected StandardStation(int numberOfParkingSlots, Point coordinates) {
		super(numberOfParkingSlots, coordinates, true, 0);
	}

	/**
	 * Create a station with the given number of parking slots, coordinates. Default
	 * bonus time credit on return for standard station is 0
	 * 
	 * @param numberOfParkingSlots
	 * @param coordinates
	 * @param online
	 */
	protected StandardStation(int numberOfParkingSlots, Point coordinates, Boolean online) {
		super(numberOfParkingSlots, coordinates, online, 0);
	}
}
