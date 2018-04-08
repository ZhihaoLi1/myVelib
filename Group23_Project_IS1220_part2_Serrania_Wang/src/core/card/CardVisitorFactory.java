package core.card;

/**
 * Factory class used to create different kind of cardVisitors.<br>
 * Currently three types of card exist: NO_CARD (when the user does not have a
 * card), VLIBRE_CARD and VMAX_CARD
 * 
 * @see CardVisitor
 * @author matto
 *
 */
public class CardVisitorFactory {
	/**
	 * Creates and returns a CardVisitor corresponding to the given type
	 * 
	 * @param cardType
	 *            the type of card to create
	 * @return a CardVisitor
	 * @throws InvalidCardTypeException
	 *             if the given type is not recognized as a valid card type
	 */
	public CardVisitor createCard(String cardType) throws InvalidCardTypeException {
		if (cardType == null) {
			throw new InvalidCardTypeException(cardType);
		}
		switch (cardType.toUpperCase()) {
		case "NO_CARD":
			return new NoCardVisitor();
		case "VLIBRE_CARD":
			return new VLibreCardVisitor();
		case "VMAX_CARD":
			return new VMaxCardVisitor();
		default:
			throw new InvalidCardTypeException(cardType);
		}
	}
}
