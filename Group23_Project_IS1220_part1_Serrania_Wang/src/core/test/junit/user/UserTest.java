package core.test.junit.user;

import static org.junit.Assert.*;

import org.junit.Test;

import core.card.CardVisitorFactory;
import core.card.InvalidCardTypeException;
import core.user.User;
import utils.Point;

public class UserTest {

	@Test
	public void testEquals() throws InvalidCardTypeException {
		User user1 = null;
		User user2 = null;
		
		user1 = new User("Alice", new Point(0, 0), new CardVisitorFactory().createCard("NO_CARD"));
		user2 = new User("Bob", new Point(0, 0), new CardVisitorFactory().createCard("NO_CARD"));
		
		assertEquals(user1, user1);
		assertNotEquals(user1, user2);
		
	}

}
