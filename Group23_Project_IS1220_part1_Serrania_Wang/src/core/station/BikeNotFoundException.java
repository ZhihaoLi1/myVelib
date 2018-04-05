package core.station;

public class BikeNotFoundException extends Exception {
	private final int stationId;
	private final String bikeType;

	public BikeNotFoundException(int stationId, String bikeType) {
		this.stationId = stationId;
		this.bikeType = bikeType;
	}

	public int getStationId() {
		return stationId;
	}
	
	public String getBikeType() {
		return bikeType;
	}
	
	@Override
	public String getMessage() {
		return "No bike of type " + bikeType + " was found in station " + stationId;
	}
}
