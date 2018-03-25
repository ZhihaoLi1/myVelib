package core.station;

/**
 * Exception thrown when a bike is added to a station which is already full.
 * 
 * @author matto
 *
 */
public class FullStationException extends Exception {
	private final Station station;

	public FullStationException(Station station) {
		this.station = station;
	}

	public Station getStation() {
		return station;
	}
	
	@Override
	public String getMessage() {
		return "Cannot add a bike to station " + station.getId() + ": it is already full";
	}
}
