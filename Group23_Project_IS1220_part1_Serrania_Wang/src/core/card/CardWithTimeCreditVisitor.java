package core.card;

import core.rentals.BikeRental;

/**
 * Represents a card on which a time credit can be accumulated. <br>
 * Implements CardVisitor.
 * 
 * @see CardVisitor
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
	 *            the BikeRental whose price is being calculated
	 * @return (double) the price of the rental
	 * 
	 * @throws InvalidBikeException
	 *             if an unidentified type of bike (or null) is given
	 * @throws InvalidDatesException
	 *             if invalid rent of return dates are given
	 */
	@Override
	public abstract double visit(BikeRental rental) throws InvalidBikeException, InvalidDatesException;

	// Getters / Setters
	@Override
	public int addTimeCredit(int timeCredit) throws IllegalArgumentException {
		if (timeCredit >= 0) {
			this.timeCredit += timeCredit;
			return timeCredit;
		} else {
			throw new IllegalArgumentException("The given time credit to add is negative: " + timeCredit);
		}
	};

	/**
	 * Removes time credit from the card.
	 * 
	 * @param timeCredit
	 *            in minutes
	 * @return the time credit that was effectively removed           
	 * @throws IllegalArgumentException
	 *             when the time credit given is negative or when the time credit on
	 *             the card is not enough to match the time credit to remove
	 */
	public int removeTimeCredit(int timeCredit) throws IllegalArgumentException {
		if (timeCredit >= 0) {
			if (this.timeCredit < timeCredit) {
				throw new IllegalArgumentException("The given time credit to remove: " + timeCredit
						+ "is higher than this card's time credit: " + this.timeCredit);
			} else {
				this.timeCredit -= timeCredit;
				return timeCredit;
			}
		} else {
			throw new IllegalArgumentException("The given time credit to remove is negative: " + timeCredit);
		}
	};

	public int getTimeCredit() {
		return timeCredit;
	}
}
