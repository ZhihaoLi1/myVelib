package core.bike;

/**
 * Implementation of an electrical bike
 * 
 * @see Bike
 * @author matto
 */
public class ElecBike implements Bike {

	private final int id;
	private final String type = "ELEC";

	// Constructor 

	protected ElecBike() {
		id = BikeIDGenerator.getInstance().getNextIDNumber();
	}
	
	// Getters / Setters
	
	public String getType() {
		return type;
	}
	
	// Equality check methods

	@Override
	public boolean equals(Object o) {
		if (o instanceof ElecBike) {
			ElecBike other = (ElecBike) o;
			if (id == other.id)
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Bike [id: " + id + ", type: " + type.toLowerCase() + "]";
	}
}
