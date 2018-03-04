package core.rentals;

import java.util.Date;

import core.User;
import core.bike.Bike;

/**
 * Contains information of a rental
 * Bike, date of rent, and user who rented it. 
 * 
 * @author animato
 *
 */
public class BikeRental implements Visitable{
	
	private Bike bike;
	private Date rentDate;
	private User user;
	
	public BikeRental(Bike bike, Date rentDate, User user){
		this.bike = bike;
		this.rentDate = rentDate;
		this.user = user;
	}

	// Passes information to visitor to calculate final cost. 
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public Bike getBike() {
		return bike;
	}

	public void setBike(Bike bike) {
		this.bike = bike;
	}

	public Date getRentDate() {
		return rentDate;
	}

	public void setRentDate(Date rentDate) {
		this.rentDate = rentDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}