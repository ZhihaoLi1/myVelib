package core.card;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author matto
 *
 */
public class VMaxCardTest {
	
	@Test(expected = NegativeTimeCreditGivenException.class)
	public final void whenGivingNegativeTimeCreditThenThrowException() throws NegativeTimeCreditGivenException {
		Card vMaxCard = new VMaxCard();
		vMaxCard.addTimeCredit(-5);
	}
	
	@Test(expected = NegativeTimeCreditGivenException.class)
	public final void whenGivingNegativeTimeCredit2ThenThrowException() throws NegativeTimeCreditGivenException {
		Card vMaxCard = new VMaxCard();
		try {
			vMaxCard.removeTimeCredit(-5);
		} catch (NegativeTimeCreditLeftException e) {
			fail("Exception NegativeTimeCreditLeftException was thrown when it shouldn't have.");
		}
	}
	
	@Test(expected = NegativeTimeCreditLeftException.class)
	public final void whenLeavingNegativeTimeCreditThenThrowException() throws NegativeTimeCreditLeftException {
		Card vMaxCard = new VMaxCard();
		try {
			vMaxCard.removeTimeCredit(5);
		} catch (NegativeTimeCreditGivenException e) {
			fail("Exception NegativeTimeCreditGivenException was thrown when it shouldn't have.");
		}
	}

	@Test
	public void testAddTimeCredit() {
		VMaxCard vMaxCard = new VMaxCard();
		try {
			vMaxCard.addTimeCredit(10);
		} catch (NegativeTimeCreditGivenException e) {
			fail("Exception NegativeTimeCreditGivenException was thrown when it shouldn't have.");
		}
		assertEquals(vMaxCard.getTimeCredit(), 10);
	}

	@Test
	public void testRemoveTimeCredit() {
		VMaxCard vMaxCard = new VMaxCard();
		try {
			vMaxCard.addTimeCredit(10);
			vMaxCard.removeTimeCredit(5);
		} catch (NegativeTimeCreditGivenException e) {
			fail("Exception NegativeTimeCreditGivenException was thrown when it shouldn't have.");
		} catch (NegativeTimeCreditLeftException e) {
			fail("Exception NegativeTimeCreditLeftException was thrown when it shouldn't have.");
		}
		assertEquals(vMaxCard.getTimeCredit(), 5);
	}

	@Test
	public void testGetTimeCredit() {
		VMaxCard vMaxCard = new VMaxCard();
		assertEquals(vMaxCard.getTimeCredit(), 0);
	}

}
