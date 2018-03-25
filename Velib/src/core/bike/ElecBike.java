package core.bike;

import core.station.IDGenerator;

/**
 * Implementation of an electrical bike
 * 
 * @author matto
 */
public class ElecBike implements Bike {

	private final int id;
	private final BikeType type = BikeType.ELEC;

	protected ElecBike() {
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

	public BikeType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Bike [id: " + id + ", type: " + type + "]";
	}
}
