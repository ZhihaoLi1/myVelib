package core.bike;

import core.station.IDGenerator;

public class MechBike implements Bike {
	
	private final int id; 
	
	public MechBike(){
		id = IDGenerator.getInstance().getNextIDNumber();
	}
}
