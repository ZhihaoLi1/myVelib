package core.ridePlan;

import core.Network;
import core.bike.BikeType;
import core.point.Point;
import core.station.Station;

/**
 * A copy of the results of a planned ride by the network
 * 
 */
public class RidePlan {
	private Point source;
	private Point destination;
	private Station sourceStation;
	private Station destinationStation;
	private RidePlanPolicyName policy;
	private BikeType bikeType;
	private Network network;
	
	public RidePlan(Point source, Point destination, Station sourceStation, Station destinationStation, RidePlanPolicyName policy, BikeType bikeType, Network network) {
		super();
		this.source = source;
		this.destination = destination;
		this.sourceStation = sourceStation;
		this.destinationStation = destinationStation;
		this.policy = policy;
		this.bikeType = bikeType;
		this.network = network;
	}
	
	public int approximateTime() {
		double walkingSpeed = 4; // km/h
		double bikeSpeed = 0;
		switch (bikeType) {
		case ELEC:
			bikeSpeed = 20;
		case MECH:
			bikeSpeed = 15;
		}
		
		double totalTime = 0;
		totalTime += sourceStation.getCoordinates().distance(source) / walkingSpeed;
		totalTime += sourceStation.getCoordinates().distance(destinationStation.getCoordinates()) / bikeSpeed;
		totalTime += destinationStation.getCoordinates().distance(destination) / walkingSpeed;
		
		return (int) Math.floor(totalTime*60);
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

	public RidePlanPolicyName getPolicy() {
		return policy;
	}

	public void setPolicy(RidePlanPolicyName policy) {
		this.policy = policy;
	}

	public BikeType getBikeType() {
		return bikeType;
	}

	public void setBikeType(BikeType bikeType) {
		this.bikeType = bikeType;
	}
	
	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof RidePlan) {
			RidePlan rp = (RidePlan) o;
			if(this.source.equals(rp.getSource())
			&& this.destination.equals(rp.getDestination())
			&& this.sourceStation.equals(rp.getSourceStation())
			&& this.destinationStation.equals(rp.getDestinationStation())
			&& this.network.equals(rp.getNetwork())
			&& this.policy.equals(rp.policy)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		String rp = "Source : " + source.toString() + "\n";
		rp += "Destination : " + destination.toString() + "\n";
		rp += "Source station : " + sourceStation.getCoordinates().toString() + "\n";
		rp += "Source station Id: " + sourceStation.getId() + "\n";
		rp += "Destination station: " + destinationStation.getCoordinates().toString() + "\n";
		rp += "Destination station Id: " + destinationStation.getId() + "\n";
		rp += "Policy : " + policy.toString() + "\n";
		return rp;
	}
}