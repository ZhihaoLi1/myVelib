package core.bike;

/**
 * Implementation of an mechanical bike
 * 
 * @author matto
 */
public class MechBike implements Bike {

	private final int id;
	private final String type = "MECH";

	// Constructor

	protected MechBike() {
		id = BikeIDGenerator.getInstance().getNextIDNumber();
	}

	// Getters / Setters

	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	// Equality check methods

	@Override
	public boolean equals(Object o) {
		if (o instanceof MechBike) {
			MechBike other = (MechBike) o;
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
