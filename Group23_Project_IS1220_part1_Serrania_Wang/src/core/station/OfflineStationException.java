package core.station;

/**
 * Exception thrown when operations are performed on an offline station
 * 
 * @author matto
 *
 */
public class OfflineStationException extends Exception {
	public final int stationId;

	public OfflineStationException(int stationId) {
		this.stationId = stationId;
	}

	public int getStationId() {
		return stationId;
	}

	public String getMessage() {
		return "The station " + stationId + "is offline. No operations can be done on it";
	}
}
