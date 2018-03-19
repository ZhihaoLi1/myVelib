package core.station;

import java.time.LocalDateTime;

import core.bike.Bike;
import core.point.Point;
import core.rentals.BikeRental;

/**
 * Concrete station which behaves exactly as the abstract station would, 5 minutes of time credit are given on return.
 * @author matto
 *
 */
public class PlusStation extends Station {

	/**
	 * Create a station with the given number of parking slots, coordinates and online status
	 * @param numberOfParkingSlots
	 * @param coordinates
	 */
	public PlusStation(int numberOfParkingSlots, Point coordinates) {
		super(numberOfParkingSlots, coordinates);
	}

	/**
	 * Create a station with the given number of parking slots, coordinates. By default, online status is true
	 * @param numberOfParkingSlots
	 * @param coordinates
	 * @param online
	 */
	public PlusStation(int numberOfParkingSlots, Point coordinates, Boolean online) {
		super(numberOfParkingSlots, coordinates, online);
	}

	@Override
	public void returnBike(BikeRental bikeRental, LocalDateTime date) {
		// TODO Auto-generated method stub
		
	}

}
