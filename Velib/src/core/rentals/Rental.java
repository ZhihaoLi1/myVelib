package core.rentals;

import core.card.CardVisitor;
import core.card.InvalidBikeTypeException;
import core.card.InvalidDatesException;

public interface Rental {
	public double accept(CardVisitor visitor) throws InvalidBikeTypeException, InvalidDatesException;
}
