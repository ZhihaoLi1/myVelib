package core.ridePlan;

import core.BikeType;
import core.Network;
import core.User;
import core.point.Point;


/**
 * RidePlan interface
 * 
 * Different classes that implement RidePlanStrategy returns a ride plan given the input parameters. 
 * 
 * @author animato
 *
 */
public interface RidePlanStrategy {
	/**
	 * 
	 * @param source
	 * @param destination
	 * @param user
	 * @param bikeType
	 * @param network
	 * @return RidePlan
	 * @throws NoStationsFoundInNetworkException
	 */
	public RidePlan planRide(Point source, Point destination, User user, BikeType bikeType,
			Network n) throws Exception;
}
