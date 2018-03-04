package core.bike;

import core.station.IDGenerator;

public class ElecBike implements Bike {
	
	private final int id; 
	
	public ElecBike(){
		id = IDGenerator.getInstance().getNextIDNumber();
	}

}
