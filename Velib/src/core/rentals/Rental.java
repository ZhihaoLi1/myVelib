package core.rentals;

import core.card.CardVisitor;
import core.card.InvalidBikeException;
import core.card.InvalidDatesException;

// FIXME: Javadoc
public interface Rental {
	public double accept(CardVisitor visitor) throws InvalidBikeException, InvalidDatesException;
}
