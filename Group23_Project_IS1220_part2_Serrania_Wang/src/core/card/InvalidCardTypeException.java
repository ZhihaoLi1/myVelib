package core.card;

/**
 * Exception thrown when a given card type is not recognized by the system.
 * 
 * @author matto
 *
 */

public class InvalidCardTypeException extends Exception {
	private final String cardType;

	public InvalidCardTypeException(String cardType) {
		this.cardType = cardType;
	}

	public String getCardType() {
		return cardType;
	}

	@Override
	public String getMessage() {
		return "The given card type is unknown: " + cardType + ".";
	}
}
