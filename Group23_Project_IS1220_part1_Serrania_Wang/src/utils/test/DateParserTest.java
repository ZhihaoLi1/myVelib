package utils.test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.junit.Test;

import utils.DateParser;

public class DateParserTest {

	@Test
	public void testParse() {
		try {
			LocalDateTime d = DateParser.parse("01/03/2045 06:07:08");
			assertEquals(d, LocalDateTime.of(2045, 3, 1, 6, 7, 8));
		} catch (DateTimeParseException e) {
			fail("DateTimeParseException was thrown");
		}
	}
	
	@Test
	public void whenInvalidDateIsGivenThenThrowException() {
		try {
			DateParser.parse(null);
			fail("DateTimeParseException should have been thrown");
		} catch (NullPointerException e) {
			assertTrue(true);
		}
		
		try {
			DateParser.parse("01-03-2045 06:07:08");
			fail("DateTimeParseException should have been thrown");
		} catch (DateTimeParseException e) {
			assertTrue(true);
		}
		
		try {
			DateParser.parse("qergrgergg");
			fail("DateTimeParseException should have been thrown");
		} catch (DateTimeParseException e) {
			assertTrue(true);
		}
		
		try {
			DateParser.parse("dZ/é3/0192 00:az:98");
			fail("DateTimeParseException should have been thrown");
		} catch (DateTimeParseException e) {
			assertTrue(true);
		}
	}

}
