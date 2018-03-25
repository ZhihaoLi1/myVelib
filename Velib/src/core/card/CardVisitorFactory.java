package core.card;

public class CardVisitorFactory {
	/**
	 * Creates and returns a CardVisitor corresponding to the given type
	 * 
	 * @param cardType
	 *            - the type of card to create
	 * @return a CardVisitor
	 * @throws InvalidCardTypeException
	 *             if the given type is not recognized as a valid card type
	 */
	public CardVisitor createCard(String cardType) throws InvalidCardTypeException {
		switch(cardType.toUpperCase()) {
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
