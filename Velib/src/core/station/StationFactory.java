package core.station;

import core.point.Point;

/**
 * Station factory which creates different types of stations depending of the
 * given type
 * 
 * @author matto
 *
 */
public class StationFactory {

	/**
	 * Creates and returns a Station corresponding to the given type
	 * 
	 * @param stationType
	 *            - the type of station to create
	 * @param numberOfParkingSlots
	 *            - the number of parking slots the station should have
	 * @param coordinates
	 *            - the coordinates of the station
	 * @param online
	 *            - the initial online status of the station
	 * @return a Station
	 * @throws InvalidStationTypeException
	 *             if the given type is not recognized as a valid station type
	 */
	public Station createStation(StationType stationType, int numberOfParkingSlots, Point coordinates, Boolean online)
			throws InvalidStationTypeException {
		if (stationType == null) {
			throw new InvalidStationTypeException(stationType);
		}

		switch (stationType) {
		case STANDARD:
			return new StandardStation(numberOfParkingSlots, coordinates, online);
		case PLUS:
			return new PlusStation(numberOfParkingSlots, coordinates, online);
		default:
			throw new InvalidStationTypeException(stationType);
		}
	}
}
