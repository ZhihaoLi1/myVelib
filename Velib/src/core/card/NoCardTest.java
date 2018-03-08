package core.card;

import static org.junit.Assert.*;

import org.junit.Test;

public class NoCardTest {

	@Test
	public void testAddTimeCredit() {
		NoCard noCard = new NoCard();
		noCard.addTimeCredit(10);
		assertEquals(noCard.getTimeCredit(), 0);
		
	}

	@Test
	public void testRemoveTimeCredit() {
		NoCard noCard = new NoCard();
		noCard.removeTimeCredit(10);
		assertEquals(noCard.getTimeCredit(), 0);
	}

	@Test
	public void testGetTimeCredit() {
		NoCard noCard = new NoCard();
		assertEquals(noCard.getTimeCredit(), 0);
	}
}
