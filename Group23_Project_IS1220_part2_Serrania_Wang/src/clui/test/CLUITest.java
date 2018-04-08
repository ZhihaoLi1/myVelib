package clui.test;

import static org.junit.Assert.*;

import org.junit.Test;

import clui.CLUIThread;
import clui.IncorrectArgumentException;

/**
 * Tests methods in CLUI for sanitizing input
 * @author animato
 *
 */
public class CLUITest {

	@Test(expected = IncorrectArgumentException.class)
	public void whenWrongNumberIsGivenThenThrowException() throws IncorrectArgumentException {
		CLUIThread.verifyArgumentLength(4, 6);
	}
	
	@Test
	public void verifyArgumentLengthTest() throws IncorrectArgumentException {
		CLUIThread.verifyArgumentLength(6, 6);
	}
	
	@Test
	public void santisizeInputTest() {
		String input = "runtest \\;insert command date =12/12/53T12:12:12";
		String[] sanitizedInput = CLUIThread.sanitizeInput(input);
		String[] expectedSanitizedInput = {"runtest", "insert", "command", "date", "12/12/53T12:12:12"};
		assertArrayEquals(sanitizedInput, expectedSanitizedInput);
	}
}
