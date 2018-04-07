package core.test.junit.card;

import static org.junit.Assert.*;

import org.junit.Test;

import core.card.CardVisitor;
import core.card.CardVisitorFactory;
import core.card.InvalidCardTypeException;
import core.card.NoCardVisitor;
import core.card.VLibreCardVisitor;
import core.card.VMaxCardVisitor;

public class CardVisitorFactoryTest {
	CardVisitorFactory cardVisitorFactory = new CardVisitorFactory();

	/**
	 * Test that the type of the created card corresponds to the given type.
	 */
	@Test
	public void testCreateCard() throws InvalidCardTypeException {
		
		CardVisitor nc = cardVisitorFactory.createCard("NO_CARD");
		assertTrue(nc instanceof NoCardVisitor);

		CardVisitor vlc = cardVisitorFactory.createCard("VLIBRE_CARD");
		assertTrue(vlc instanceof VLibreCardVisitor);
		
		CardVisitor vmc = cardVisitorFactory.createCard("VMAX_CARD");
		assertTrue(vmc instanceof VMaxCardVisitor);
	}
	
	/**
	 * Test that when a wrong card type is given, an InvalidCardTypeException is thrown.
	 */
	@Test
	public void whenWrongBikeTypeGivenThenThrowException() {
		CardVisitorFactory cardVisitorFactory = new CardVisitorFactory();
		
		try {
			CardVisitor nc = cardVisitorFactory.createCard(null);
			fail("InvalidCardTypeException should have been thrown");
		} catch (InvalidCardTypeException e) {
			assertTrue(true);
		}
		
		try {
			CardVisitor nc = cardVisitorFactory.createCard("NOPE");
			fail("InvalidCardTypeException should have been thrown");
		} catch (InvalidCardTypeException e) {
			assertTrue(true);
		}
		
	}

}
