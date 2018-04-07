package core.card;

import java.time.Duration;

import core.bike.Bike;
import core.bike.ElecBike;
import core.bike.MechBike;
import core.rentals.BikeRental;

/**
 * Implementation of CardVisitor for users without any card. <br>
 * No time credit can be accumulated on this type of card. <br>
 * Allows calculation of the price of a bike rental for this type of card. <br>
 * Without a card, the price is as follows: <br>
 * - 1€/hour for mechanical bikes <br>
 * - 2€/hour for electrical bikes <br>
 * 
 * Implements CardVisitor
 * @see CardVisitor
 * @author matto
 */
public class NoCardVisitor implements CardVisitor {

	// Constructor 
	
	protected NoCardVisitor() {
		super();
	}

	// Core methods
	
	/**
	 * Calculates the price of a bike rental and updates the bike rental with it.
	 * 
	 * @param rental
	 *            the BikeRental whose price is being calculated
	 * @return the price of the ride           
	 * @throws InvalidBikeException
	 *             if an unidentified type of bike (or null) is given
	 * @throws InvalidDatesException
	 *             if invalid rent of return dates are given
	 */
	@Override
	public double visit(BikeRental rental) throws InvalidBikeException, InvalidDatesException {
		Bike bike = rental.getBike();
		if (rental.getRentDate() == null || rental.getReturnDate() == null || rental.getTimeSpent() < 0) {
			throw new InvalidDatesException(rental);
		}
		
		long nMinutes = Duration.between(rental.getRentDate(), rental.getReturnDate()).toMinutes();
		
		if (bike instanceof MechBike) {
			rental.setPrice(nMinutes / 60 + ((nMinutes % 60 == 0) ? 0 : 1));
			return rental.getPrice();
		} else if (bike instanceof ElecBike) {
			rental.setPrice(2 * (nMinutes / 60 + ((nMinutes % 60 == 0) ? 0 : 1)));
			return rental.getPrice();
		} else {
			throw new InvalidBikeException(rental);
		}
	}

	// Getters / Setters
	@Override
	public int getTimeCredit() {
		return 0;
	}

	@Override
	public int addTimeCredit(int timeCredit) {
		return 0;
	}
	
	@Override
	public int removeTimeCredit(int timeCredit) {
		return 0;
	}
	
	@Override
	public String toString() {
		return "No card";
	}
}
