package core.ridePlan;

import java.util.HashMap;

import core.PolicyName;
import core.station.Station;

// FIXME: Javadoc, override getMessage()
public class NoValidStationFoundException extends Exception {
	private final HashMap<Integer, Station>  stations;
	private final PolicyName policyName;

	public NoValidStationFoundException(HashMap<Integer, Station> stations, PolicyName policyName) {
		this.stations = stations;
		this.policyName = policyName;
	}

	public HashMap<Integer, Station>  getStations() {
		return stations;
	}

	public PolicyName getPolicyName() {
		return policyName;
	}
}
