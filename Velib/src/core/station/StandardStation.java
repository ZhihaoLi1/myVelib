package core.station;

import java.time.LocalDateTime;

import core.bike.Bike;
import core.point.Point;
import core.rentals.BikeRental;

/**
 * Concrete station which behaves exactly as the abstract station would. No time credit is given on return.
 * @author matto
 *
 */
public class StandardStation extends Station {

	/**
	 * Create a station with the given number of parking slots, coordinates and online status
	 * @param numberOfParkingSlots
	 * @param coordinates
	 */
	public StandardStation(int numberOfParkingSlots, Point coordinates, int bonusTimeCreditOnReturn) {
		super(numberOfParkingSlots, coordinates, bonusTimeCreditOnReturn);
	}
	
	// default bonus time credit on return for standard station is 0
	public StandardStation(int numberOfParkingSlots, Point coordinates) {
		super(numberOfParkingSlots, coordinates, 0);
	}

	/**
	 * Create a station with the given number of parking slots, coordinates. By default, online status is true
	 * @param numberOfParkingSlots
	 * @param coordinates
	 * @param online
	 */
	public StandardStation(int numberOfParkingSlots, Point coordinates, Boolean online, int bonusTimeCreditOnReturn) {
		super(numberOfParkingSlots, coordinates, online, bonusTimeCreditOnReturn);
	}


	@Override
	public void returnBike(BikeRental bikeRental, LocalDateTime date) {
		// TODO Auto-generated method stub
		
	}

}
