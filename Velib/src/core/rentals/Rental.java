package core.rentals;

import core.card.CardVisitor;
import core.card.InvalidBikeException;
import core.card.InvalidDatesException;

/**
 * Interface representing a rental in the system. Is the visitor of the visitor
 * pattern used to calculate the price of a rental. The visitor is a
 * CardVisitor.
 * 
 * @author matto
 *
 */
public interface Rental {

	/**
	 * Passes itself to the visitor so that its cost can be calculated.
	 * 
	 * @param visitor
	 *            The CardVisitor which will be used to compute costs
	 * @return the price of the course (after time credit is deducted)
	 * @throws InvalidBikeException
	 * @throws InvalidDatesException
	 */
	public double accept(CardVisitor visitor) throws InvalidBikeException, InvalidDatesException;
}
