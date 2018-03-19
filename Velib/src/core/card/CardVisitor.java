package core.card;

import core.rentals.BikeRental;

/**
 * Represents a card with which a user can pay and accumulate time credit.
 * Is the visitor of the visitor pattern used to calculate the price of a rental.
 * The visitable is a Rental.
 * 
 * @author matto
 *
 */
public interface CardVisitor {
	public double visit(BikeRental rental) throws InvalidBikeTypeException, InvalidDatesException;
	public void addTimeCredit(int timeCredit) throws NegativeTimeCreditGivenException;
}
