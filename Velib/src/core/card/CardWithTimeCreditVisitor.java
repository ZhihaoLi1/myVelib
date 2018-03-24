package core.card;

import core.rentals.BikeRental;

/**
 * Represents a card on which a time credit can be accumulated.
 * 
 * @author matto
 *
 */
public abstract class CardWithTimeCreditVisitor implements CardVisitor {
	private int timeCredit;

	public CardWithTimeCreditVisitor() {
		timeCredit = 0;
	}

	/**
	 * Implementation of the calculation of the price of a rental
	 * 
	 * @param rental
	 *            - the BikeRental whose price is being calculated
	 * @return (double) the price of the rental
	 * @throws InvalidBikeTypeException
	 *             if an unidentified type of bike (or null) is given
	 * @throws InvalidDatesException
	 *             if invalid rent of return dates are given
	 */
	public abstract double visit(BikeRental rental) throws InvalidBikeException, InvalidDatesException;

	// Getters / Setters
	public void addTimeCredit(int timeCredit) {
		if (timeCredit >= 0) {
			this.timeCredit += timeCredit;
		} else {
			throw new IllegalArgumentException("The given time credit to add is negative: " + timeCredit);
		}
	};

	/**
	 * Tries to remove timeCredit time from the card.
	 * @param timeCredit
	 */
	public void removeTimeCredit(int timeCredit) {
		if (timeCredit >= 0) {
			if (this.timeCredit < timeCredit) {
				throw new IllegalArgumentException("The given time credit to remove: " + timeCredit + "is higher than this card's time credit: " + this.timeCredit);
			} else {
				this.timeCredit -= timeCredit;
			}
		} else {
			throw new IllegalArgumentException("The given time credit to remove is negative: " + timeCredit);
		}
	};

	public int getTimeCredit() {
		return timeCredit;
	}
}
