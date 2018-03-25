package core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import core.card.CardVisitor;
import core.card.CardVisitorFactory;
import core.card.InvalidCardTypeException;
import core.card.NoCardVisitor;
import core.card.VLibreCardVisitor;
import core.card.VMaxCardVisitor;

public class CardVisitorFactoryTest {

	/**
	 * Test that the type of the created card corresponds to the given type.
	 */
	@Test
	public void testCreateCard() {
		CardVisitorFactory cardVisitorFactory = new CardVisitorFactory();
		
		try {
			CardVisitor nc = cardVisitorFactory.createCard("NO_CARD");
			assertTrue(nc instanceof NoCardVisitor);
		} catch (InvalidCardTypeException e) {
			fail("InvalidCardTypeException thrown");
		}
		
		try {
			CardVisitor vlc = cardVisitorFactory.createCard("VLIBRE_CARD");
			assertTrue(vlc instanceof VLibreCardVisitor);
		} catch (InvalidCardTypeException e) {
			fail("InvalidCardTypeException thrown");
		}
		
		try {
			CardVisitor vmc = cardVisitorFactory.createCard("VMAX_CARD");
			assertTrue(vmc instanceof VMaxCardVisitor);
		} catch (InvalidCardTypeException e) {
			fail("InvalidCardTypeException thrown");
		}
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
		
	}

}
