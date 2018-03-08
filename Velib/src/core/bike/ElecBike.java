package core.bike;

import core.station.IDGenerator;

public class ElecBike implements Bike {
	
	private final int id; 
	
	public ElecBike(){
		id = IDGenerator.getInstance().getNextIDNumber();
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ElecBike) {
			ElecBike other = (ElecBike) o;
			if (id == other.id)
				return true;
		}
		return false;
	}
	
}
