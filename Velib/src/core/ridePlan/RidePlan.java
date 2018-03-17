package core.ridePlan;

import core.BikeType;
import core.PolicyName;
import core.point.Point;
import core.station.Station;

/*
 * A copy of the results of a planned ride by the network
 * 
 */
public class RidePlan {
	private Point source;
	private Point destination;
	private Station sourceStation;
	private Station destinationStation;
	private PolicyName policy;
	private BikeType bikeType;
	
	public RidePlan(Point source, Point destination, Station sourceStation, Station destinationStation, PolicyName policy, BikeType bikeType) {
		super();
		this.source = source;
		this.destination = destination;
		this.sourceStation = sourceStation;
		this.destinationStation = destinationStation;
		this.policy = policy;
		this.bikeType = bikeType;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof RidePlan) {
			RidePlan rp = (RidePlan) o;
			if(this.source.equals(rp.getSource())
			&& this.destination.equals(rp.getDestination())
			&& this.sourceStation.equals(rp.getSourceStation())
			&& this.destinationStation.equals(rp.getDestinationStation())
			&& this.policy.equals(rp.policy)) {
				return true;
			}
		}
		return false;
	}
	
	public Point getSource() {
		return source;
	}
	public void setSource(Point source) {
		this.source = source;
	}
	public Point getDestination() {
		return destination;
	}
	public void setDestination(Point destination) {
		this.destination = destination;
	}
	public Station getSourceStation() {
		return sourceStation;
	}
	public void setSourceStation(Station sourceStation) {
		this.sourceStation = sourceStation;
	}
	public Station getDestinationStation() {
		return destinationStation;
	}
	public void setDestinationStation(Station destinationStation) {
		this.destinationStation = destinationStation;
	}

	public PolicyName getPolicy() {
		return policy;
	}

	public void setPolicy(PolicyName policy) {
		this.policy = policy;
	}

	public BikeType getBikeType() {
		return bikeType;
	}

	public void setBikeType(BikeType bikeType) {
		this.bikeType = bikeType;
	}
}