package core.station;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

import core.BikeType;
import core.User;
import core.bike.Bike;
import core.bike.ElecBike;
import core.bike.MechBike;
import core.point.Point;
import core.rentals.BikeRental;

public abstract class Station extends Observable  {
	private final int id;
	private ArrayList<ParkingSlot> parkingSlots = new ArrayList<ParkingSlot>(); 
	private Point coordinates;
	private Boolean online;
	private Set<User> observers = new HashSet<User>(); 
	// Statistics
	private StationStats stats;

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
	 * @param date
	 * @return boolean 
	 * True if bike is added
	 * False if parkingSlots are full
	 */
	public boolean addBike(Bike b, LocalDateTime date) throws Exception {
		for (int i = 0; i<parkingSlots.size(); i++) {
			if (!parkingSlots.get(i).hasBike()) {
				parkingSlots.get(i).setBike(b, date);
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
	 * @param bikeType
	 * @return
	 */
	public boolean hasCorrectBikeType(BikeType bikeType) {
		for (ParkingSlot ps : parkingSlots) {
			if (ps.hasBike() && ps.getBike().getType() == bikeType && ps.isWorking()) {
				return true;
			}
		}
		return false;
	}
	
	public int getNumberOfMechBikes() {
		int t = 0;
		for (int i = 0; i<parkingSlots.size(); i++) {
			ParkingSlot ps = parkingSlots.get(i);
			if (ps.hasBike() && ps.getBike() instanceof MechBike && ps.isWorking()) {
				t += 1;
			}
		}
		return t;
	}
	
	public int getNumberOfElecBikes() {
		int t = 0;
		for (int i = 0; i<parkingSlots.size(); i++) {
			ParkingSlot ps = parkingSlots.get(i);
			if (ps.hasBike() && ps.getBike() instanceof ElecBike && ps.isWorking()) {
				t += 1;
			}
		}
		return t;
	}
	
	public int getNumberOfBikes(BikeType bikeType) {
		switch(bikeType) {
		case ELEC:
			return getNumberOfElecBikes();
		case MECH:
			return getNumberOfMechBikes();
		default:
			return 0;
		}
	}

	/**
	 * 
	 * @param bikeType
	 * @return
	 * @throws Exception when station is offline
	 */
	public Bike rentBike(BikeType bikeType, LocalDateTime date) throws Exception {
		// verify if station is online 
		if (!this.online) throw new Exception("Station is offline");
		// find appropriate bike in station;
		Bike b = null;
		
		for(ParkingSlot ps: this.getParkingSlots()) {
			if (ps.getWorking() && ps.hasBike() && ps.getBike().getType() == bikeType) {
				b = ps.getBike();
				// FIXME locking ps ? 
				ps.setBike(null, date);
				break;
			}
		}
		
		return b;
	}
	
	public abstract void returnBike(Bike bike);
	
	public void addObserver(User user) {
		if(this.observers.add(user)) {
			System.out.println("User " + user.getName() + " is observing station S" + this.id);		
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
	
	@Override
	public String toString() {
		String s = "StationId: " + this.id + "\n";
		s += "ParkingSlots: " + this.parkingSlots.toString() + "\n";
		s += "Coordinates: " + this.coordinates.toString() + "\n";
		s += "Online: " + this.online;
		return s;
		
	}
		
	/**
	 * Calculate occupation rate
	 * Out of order parking slots are ignored
	 */
	public double computeOccupationRate(LocalDateTime startDate, LocalDateTime endDate) {
		return stats.getOccupationRate(startDate, endDate);
	}
	
	public String displayOccupationRate(LocalDateTime startDate, LocalDateTime endDate) {
		return "Occupation rate between " + startDate + " and " + endDate + ": " + computeOccupationRate(startDate, endDate); 
	}
	
	/**
	 * Display Statistics of station
	 */
	public String displayStats() {
		return "Station [id: " + this.id + ", " + stats.toString() +  "]";
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
	
	public static void main(String[] args) {
		Station st = new StandardStation(10, new Point(0,0));
		
		System.out.println(st.displayStats());
	}
}
