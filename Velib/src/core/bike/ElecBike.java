package core.bike;

/**
 * Implementation of an electrical bike
 * 
 * @author matto
 */
public class ElecBike implements Bike {

	private final int id;
	private final String type = "ELEC";

	protected ElecBike() {
		id = BikeIDGenerator.getInstance().getNextIDNumber();
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

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Bike [id: " + id + ", type: " + type + "]";
	}
}
