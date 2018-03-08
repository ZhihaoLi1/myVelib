package core.card;

import static org.junit.Assert.*;

import org.junit.Test;

public class VLibreCardTest {

	@Test(expected = NegativeTimeCreditGivenException.class)
	public final void whenGivingNegativeTimeCreditThenThrowException() throws NegativeTimeCreditGivenException {
		Card vLibreCard = new VlibreCard();
		vLibreCard.addTimeCredit(-5);
	}
	
	@Test(expected = NegativeTimeCreditGivenException.class)
	public final void whenGivingNegativeTimeCredit2ThenThrowException() throws NegativeTimeCreditGivenException {
		Card vLibreCard = new VlibreCard();
		try {
			vLibreCard.removeTimeCredit(-5);
		} catch (NegativeTimeCreditLeftException e) {
			fail("Exception NegativeTimeCreditLeftException was thrown when it shouldn't have.");
		}
	}
	
	@Test(expected = NegativeTimeCreditLeftException.class)
	public final void whenLeavingNegativeTimeCreditThenThrowException() throws NegativeTimeCreditLeftException {
		Card vLibreCard = new VlibreCard();
		try {
			vLibreCard.removeTimeCredit(5);
		} catch (NegativeTimeCreditGivenException e) {
			fail("Exception NegativeTimeCreditGivenException was thrown when it shouldn't have.");
		}
	}
	
	@Test
	public void testAddTimeCredit() {
		VlibreCard vLibreCard = new VlibreCard();
		try {
			vLibreCard.addTimeCredit(10);
		} catch (NegativeTimeCreditGivenException e) {
			fail("Exception NegativeTimeCreditGivenException was thrown when it shouldn't have.");
		}
		assertEquals(vLibreCard.getTimeCredit(), 10);
	}

	@Test
	public void testRemoveTimeCredit() {
		VlibreCard vLibreCard = new VlibreCard();
		try {
			vLibreCard.addTimeCredit(10);
			vLibreCard.removeTimeCredit(5);
		} catch (NegativeTimeCreditGivenException e) {
			fail("Exception NegativeTimeCreditGivenException was thrown when it shouldn't have.");
		} catch (NegativeTimeCreditLeftException e) {
			fail("Exception NegativeTimeCreditLeftException was thrown when it shouldn't have.");
		}
		assertEquals(vLibreCard.getTimeCredit(), 5);
	}

	@Test
	public void testGetTimeCredit() {
		VlibreCard vLibreCard = new VlibreCard();
		assertEquals(vLibreCard.getTimeCredit(), 0);
	}
}
