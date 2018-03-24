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

/**
 * Abstract station representation
 * 
 * @author matto
 *
 */
public abstract class Station extends Observable {
	private final int id;

	// time credit added to card to a bike is returned to this station.
	private final int bonusTimeCreditOnReturn;

	// A station has multiple parking slots
	private ArrayList<ParkingSlot> parkingSlots = new ArrayList<ParkingSlot>();

	private Point coordinates;
	private Boolean online;

	// Observers
	private Set<User> observers = new HashSet<User>();

	// Statistics
	private StationStats stats;

	/**
	 * Create a station with the given number of parking slots, coordinates and
	 * online status
	 * 
	 * @param numberOfParkingSlots
	 * @param coordinates
	 * @param online
	 * @param bonusTimeCreditOnReturn
	 */
	public Station(int numberOfParkingSlots, Point coordinates, Boolean online, int bonusTimeCreditOnReturn) {
		super();
		this.id = IDGenerator.getInstance().getNextIDNumber();
		for (int i = 0; i < numberOfParkingSlots; i++) {
			this.parkingSlots.add(new ParkingSlot());
		}
		this.coordinates = coordinates;
		this.online = online;
		this.bonusTimeCreditOnReturn = bonusTimeCreditOnReturn;
		this.stats = new StationStats(this);
	}

	/**
	 * Adds a bike to the first empty slot that it finds TODO Make thread safe ?
	 * 
	 * @param b
	 *            - the bike to add
	 * @param date
	 *            - the date at which the bike is added
	 * @return boolean - true if bike was added, false if not
	 */
	public boolean addBike(Bike b, LocalDateTime date) {
		for (int i = 0; i < parkingSlots.size(); i++) {
			try {
				// Throws Exception if bike couldn't be set
				parkingSlots.get(i).setBike(b, date);
				// If the station is full after adding the bike, notification should be sent to
				// users
				if (isFull()) {
					notifyObservers();
				}
				return true;
			} catch (Exception e) {
				continue;
			}
		}
		return false;
	}

	/**
	 * Tells if the station is full
	 * 
	 * @return boolean - true if station is full, false if not
	 */
	public Boolean isFull() {
		for (ParkingSlot ps : parkingSlots) {
			if (ps.isWorking() && !ps.hasBike()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Tells if station has a bike of the given type
	 * 
	 * @param bikeType
	 *            - the type of bike we want to check
	 * @return boolean - true if a bike of type bikeType is present, false if not
	 */
	public boolean hasCorrectBikeType(BikeType bikeType) {
		for (ParkingSlot ps : parkingSlots) {
			if (ps.hasBike() && ps.getBike().getType() == bikeType && ps.isWorking()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gives the number of bikes in the station of the given bikeType
	 * 
	 * @param bikeType
	 *            - the type of bike we want to check
	 * @return int - the number of bikes in the station of the given bikeType
	 */
	public int getNumberOfBikes(BikeType bikeType) {
		int t = 0;
		for (int i = 0; i < parkingSlots.size(); i++) {
			ParkingSlot ps = parkingSlots.get(i);
			if (ps.hasBike() && ps.getBike().getType() == bikeType && ps.isWorking()) {
				t += 1;
			}
		}
		return t;
	}

	/**
	 * Rents a bike of the given bike type at a given time
	 * 
	 * @param bikeType
	 * @return
	 * @throws Exception
	 *             when station is offline
	 */
	public Bike rentBike(BikeType bikeType, LocalDateTime date) throws Exception {
		// verify if station is online
		if (!this.online)
			// FIXME: I think we should just return null
			throw new Exception("Station is offline");
		// find appropriate bike in station;
		Bike b = null;

		for (ParkingSlot ps : this.getParkingSlots()) {
			if (ps.isWorking() && ps.hasBike() && ps.getBike().getType() == bikeType) {
				b = ps.getBike();
				// FIXME locking ps ?
				ps.setBike(null, date);
				break;
			}
		}

		return b;
	}

	/**
	 * Returns a bike at a given time The returning of the bike is different given
	 * the station type (take into consideration the timeCredit or not), so this is
	 * an abstract method
	 * 
	 * @param bikeRental
	 *            - the bike which is returned
	 * @param date
	 *            - the date at which the bike is returned FIXME Only addTimeCredit
	 *            should be abstract, and separated from this method
	 * @throws Exception
	 *             when station is full
	 */
	public void returnBike(BikeRental bikeRental, LocalDateTime date) throws FullStationException {
		if (this.isFull())
			throw new FullStationException(this);
		// loop over stations and place bike the first empty slot
		addBike(bikeRental.getBike(), LocalDateTime.now());

	};

	/**
	 * Add user to the list of observers of the station
	 * 
	 * @param user
	 *            - the observer user to add
	 */
	public void addObserver(User user) {
		if (this.observers.add(user)) {
			System.out.println("User " + user.getName() + " is observing station S" + this.id);
		} else {
			System.out.println("User is already observing this station S" + this.id);
		}
	}

	/**
	 * Remove user to the list of observers of the station
	 * 
	 * @param user
	 *            - the observer user to remove
	 */
	public void deleteObserver(User user) {
		if (this.observers.remove(user)) {
			System.out.println("User " + user.getName() + "stopped observing this station" + this.id);
		} else {
			System.out.println("User is not observing this station S" + this.id);
		}
	}

	/**
	 * Notify all observers
	 */
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

	@Override
	public boolean equals(Object o) {
		if (o instanceof Station) {
			Station s = (Station) o;
			if (s.getId() == this.id) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Calculate occupation rate over a given time period
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public double computeOccupationRate(LocalDateTime startDate, LocalDateTime endDate) {
		return stats.getOccupationRate(startDate, endDate);
	}

	/**
	 * Gives a String representation of the statistics of the occupation rate over a
	 * given time period
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public String displayOccupationRate(LocalDateTime startDate, LocalDateTime endDate) {
		return "Occupation rate between " + startDate + " and " + endDate + ": "
				+ computeOccupationRate(startDate, endDate);
	}

	/**
	 * Gives a String representation of the statistics of the station (total returns
	 * and rentals)
	 * 
	 * @return String - representing the stats
	 */
	public String displayStats() {
		return "Station [id: " + this.id + ", " + stats.toString() + "]";
	}

	// Getters / Setters
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

	public int getBonusTimeCreditOnReturn() {
		return bonusTimeCreditOnReturn;
	}

	public StationStats getStats() {
		return stats;
	}
}
