package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Utility class used to get LocalDateTime objects out of a string
 * representation of it.
 * 
 * @author matto
 */
public class DateParser {
	/**
	 * Takes a string following the "dd/MM/uuuu HH:mm:ss" format and converts it to
	 * a LocalDateTime object.
	 * 
	 * @param string
	 *            the string representing the date.
	 * @return a LocalDateTime object represented by the string given as input.
	 * @throws DateTimeParseException
	 *             if the string doesn't match the format used.
	 * @throws NullPointerException
	 *             if the string is null
	 */
	public static LocalDateTime parse(String string) throws DateTimeParseException, NullPointerException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm:ss", Locale.ENGLISH);
		return LocalDateTime.parse(string, formatter);
	}
}
