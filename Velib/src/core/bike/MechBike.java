package core.bike;

import core.station.IDGenerator;

/**
 * Implementation of an mechanical bike
 * 
 * @author matto
 */
public class MechBike implements Bike {

	private final int id;
	private final String type = "MECH";

	protected MechBike() {
		id = IDGenerator.getInstance().getNextIDNumber();
	}

	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return "Bike [id: " + id + ", type: " + type + "]";
	}
}
