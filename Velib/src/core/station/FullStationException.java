package core.station;

// FIXME: Javadoc, override getMessage()
public class FullStationException extends Exception {
	private final Station station;

	public FullStationException(Station station) {
		this.station = station;
	}

	public Station getStation() {
		return station;
	}
}
