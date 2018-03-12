package core.card;

import java.time.LocalDateTime;

public class InvalidDatesException extends Exception {

	private final LocalDateTime rentDate;
	private final LocalDateTime returnDate;
	
	public InvalidDatesException(LocalDateTime rentDate, LocalDateTime returnDate) {
		this.rentDate = rentDate;
		this.returnDate = returnDate;
	}

	public LocalDateTime getRentDate() {
		return rentDate;
	}
	
	public LocalDateTime getReturnDate() {
		return returnDate;
	}
}
