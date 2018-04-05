package core.card;

import core.bike.InvalidBikeTypeException;
import core.rentals.BikeRental;

/**
 * Represents a card with which a user can pay and accumulate time credit. Is
 * the visitor of the visitor pattern used to calculate the price of a rental.
 * The visitable is a Rental.
 * 
 * @author matto
 *
 */
public interface CardVisitor {

	/**
	 * Calculates the price of a bike rental.
	 * 
	 * @param rental
	 *            the BikeRental whose price is being calculated
	 * @return (double) the price of the rental
	 * @throws InvalidBikeException
	 *             if an unidentified type of bike (or null) is given
	 * @throws InvalidDatesException
	 *             if invalid rent of return dates are given
	 */
	public double visit(BikeRental rental) throws InvalidBikeException, InvalidDatesException;

	/**
	 * Adds time credit to the card.
	 * 
	 * @param timeCredit
	 *            in minutes
	 * @return the time credit that was effectively added (might be different from
	 *         what was given, depending on the card)
	 * @throws IllegalArgumentException
	 *             when the timeCredit given is negative
	 */
	public int addTimeCredit(int timeCredit) throws IllegalArgumentException;
	
	public int getTimeCredit();
}
