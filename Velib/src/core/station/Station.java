package core.station;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

import core.User;
import core.bike.Bike;
import core.bike.ElecBike;
import core.bike.MechBike;
import core.point.Point;

public abstract class Station extends Observable  {
	private final int id;
	private ArrayList<ParkingSlot> parkingSlots = new ArrayList<ParkingSlot>(); 
	private Point coordinates;
	private Boolean online;
	private Set<User> observers = new HashSet<User>(); 
	// Statistics
	private int totalRentals = 0;
	private int totalReturns = 0;

	
	public Station(int numberOfParkingSlots, Point coordinates, Boolean online) {
		super();
		this.id = IDGenerator.getInstance().getNextIDNumber();
		for (int i=0; i < numberOfParkingSlots; i++) {
			this.parkingSlots.add(new ParkingSlot());
		}
		this.coordinates = coordinates;
		this.online = online;
	}
	

	public Station(int numberOfParkingSlots, Point coordinates) {
		super();
		this.id = IDGenerator.getInstance().getNextIDNumber();
		for (int i=0; i < numberOfParkingSlots; i++) {
			this.parkingSlots.add(new ParkingSlot());
		}
		this.coordinates = coordinates;
		this.online = true;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Station) {
			Station s = (Station) o;
			if (s.getId() == this.id) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Adds a bike to the first empty slot that it finds
	 * TODO Make thread safe ? 
	 * @param b
	 * @return boolean 
	 * True if bike is added
	 * False if parkingSlots are full
	 */
	public boolean addBike(Bike b) {
		for (int i = 0; i<parkingSlots.size(); i++) {
			if (!parkingSlots.get(i).hasBike()) {
				parkingSlots.get(i).setBike(b);
				// if the station is full after adding the bike, notification should be sent to users
				if(isFull()) {
					notifyObservers();
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @return true if full
	 */
	public Boolean isFull() {
		for (int i = 0; i<parkingSlots.size(); i++) {
			if (!parkingSlots.get(i).hasBike()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @return true if there is elec bike
	 */
	public Boolean hasElecBike() {
		for (int i = 0; i<parkingSlots.size(); i++) {
			if (parkingSlots.get(i).hasBike() && parkingSlots.get(i).getBike() instanceof ElecBike) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @return true if there is mech bike
	 */
	public Boolean hasMechBike() {
		for (int i = 0; i<parkingSlots.size(); i++) {
			if (parkingSlots.get(i).hasBike() && parkingSlots.get(i).getBike() instanceof MechBike) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param bikeType
	 * @return
	 */
	public boolean hasCorrectBikeType(String bikeType) {
		switch(bikeType) {
		case "elec":
			return hasElecBike();
		case "mech":
			return hasMechBike();
		default:
			return false;
		}
	}

	public abstract void rentBike(Bike bike); // you don't need the type ? just need instanceof 
	public abstract void returnBike(Bike bike);
	
	public void addObserver(User user) {
		if(this.observers.add(user)) {
			System.out.println("User " + user.getName() + "is observing station S" + this.id);		
		} else {
			System.out.println("User is already observing this station S" + this.id);
		}
	}
	
	public void deleteObserver(User user) {
		if(this.observers.remove(user)) {
			System.out.println("User " + user.getName() + "stopped observing this station" + this.id);
		} else {
			System.out.println("User is not observing this station S" + this.id);
		}
	}
	
	public void notifyObservers() {
		for (User user : observers) {
			user.update(this, null);
		}
	}
		
	/**
	 * Calculate occupation rate
	 * Out of order parking slots are ignored
	 */
	public void computeOccupationRate() {
		
	}
	
	/**
	 * Display Statistics of station
	 */
	public void displayBalance() {
		
	}
	
	public int getId() {
		return id;
	}


	public ArrayList<ParkingSlot> getParkingSlots() {
		return parkingSlots;
	}


	public Point getCoordinates() {
		return coordinates;
	}


	public Boolean getOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		if (!online)
			notifyObservers();
		this.online = online;
	}

	public Set<User> getObservers() {
		return observers;
	}


	public int getTotalRentals() {
		return totalRentals;
	}


	public int getTotalReturns() {
		return totalReturns;
	}
}
