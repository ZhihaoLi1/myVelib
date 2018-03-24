package core.station;

import core.BikeType;

public class InvalidStationTypeException extends Exception {
	
	private final StationType stationType;
	
	public InvalidStationTypeException(StationType stationType) {
		this.stationType = stationType;
	}

	public StationType getStationType() {
		return stationType;
	}
}
