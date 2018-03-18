package core.station;

import core.bike.Bike;
import core.point.Point;

public class PlusStation extends Station {

	public PlusStation(int numberOfParkingSlots, Point coordinates) {
		super(numberOfParkingSlots, coordinates);
	}

	public PlusStation(int numberOfParkingSlots, Point coordinates, Boolean online) {
		super(numberOfParkingSlots, coordinates, online);
	}

	@Override
	public void returnBike(Bike bike) {
		// TODO Auto-generated method stub
		
	}

}
