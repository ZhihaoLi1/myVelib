package core.station;

import core.bike.Bike;
import core.point.Point;

public class StandardStation extends Station {

	public StandardStation(int numberOfParkingSlots, Point coordinates) {
		super(numberOfParkingSlots, coordinates);
	}

	public StandardStation(int numberOfParkingSlots, Point coordinates, Boolean online) {
		super(numberOfParkingSlots, coordinates, online);
	}

	@Override
	public void rentBike(Bike bike) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void returnBike(Bike bike) {
		// TODO Auto-generated method stub
		
	}

}
