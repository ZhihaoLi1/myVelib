package core.bike;

import core.BikeType;
import core.station.IDGenerator;

public class MechBike implements Bike {
	
	private final int id; 
	private final BikeType type = BikeType.MECH; 

	public MechBike(){
		id = IDGenerator.getInstance().getNextIDNumber();
	}

	public int getId() {
		return id;
	}

	public BikeType getType() {
		return type;
	}
}
