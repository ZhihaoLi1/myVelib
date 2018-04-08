package core.card;

import core.rentals.Rental;
import core.rentals.BikeRental;

/**
 * Represents a card with which a user can pay and accumulate time credit. <br>
 * Visitor of the visitor pattern used to calculate the price of a rental. The
 * visitable is a Rental.
 * 
 * @see Rental
 * @author matto
 *
 */
public interface CardVisitor {

	/**
	 * Calculates the price of a bike rental.
	 * 
	 * @param rental
	 *            the BikeRental whose price is being calculated
	 * @return the price of the rental (after the eventual time credit deduction)
	 * @throws InvalidBikeException
	 *             if an unidentified type of bike (or null) is given
	 * @throws InvalidDatesException
	 *             if invalid rent/return dates are given
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

	/**
	 * Gets the time credit currently on the card
	 * @return the time credit on the card
	 */
	public int getTimeCredit();
	
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
	public int removeTimeCredit(int timeCredit) throws IllegalArgumentException;
}
