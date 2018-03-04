package core.ridePlan;

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
	private String policy;
	
	public RidePlan(Point source, Point destination, Station sourceStation, Station destinationStation, String policy) {
		super();
		this.source = source;
		this.destination = destination;
		this.sourceStation = sourceStation;
		this.destinationStation = destinationStation;
		this.policy = policy;
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
	public String getPolicy() {
		return policy;
	}
	public void setPolicy(String policy) {
		this.policy = policy;
	}
}
