package core.rentals;

import java.time.LocalDateTime;

import core.bike.Bike;
import core.card.CardVisitor;
import core.card.InvalidBikeTypeException;
import core.card.InvalidDatesException;

/**
 * Contains information of a rental
 * Bike, date of rent, and user who rented it. 
 * 
 * @author animato
 *
 */
public class BikeRental implements Rental {
	
	private Bike bike;
	private LocalDateTime rentDate;
	private LocalDateTime returnDate;
	
	public BikeRental(Bike bike, LocalDateTime rentDate) {
		this.bike = bike;
		this.rentDate = rentDate;
		this.returnDate = null;
	}

	// Passes information to visitor to calculate final cost. 
	@Override
	public double accept(CardVisitor visitor) throws InvalidBikeTypeException, InvalidDatesException {
		return visitor.visit(this);
	}

	public Bike getBike() {
		return bike;
	}

	public void setBike(Bike bike) {
		this.bike = bike;
	}

	public LocalDateTime getRentDate() {
		return rentDate;
	}

	public void setRentDate(LocalDateTime rentDate) {
		this.rentDate = rentDate;
	}
	
	public LocalDateTime getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDateTime returnDate) {
		this.returnDate = returnDate;
	}
	
	@Override
	public String toString() {
		return "BikeRental: [Bike: " + bike + ", rentDate: " + rentDate + ", returnDate: " + returnDate + "]";
	}
}