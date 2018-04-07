package core.station;

import java.time.LocalDateTime;

public class InvalidTimeSpanException extends Exception {
	private final LocalDateTime startDate;
	private final LocalDateTime endDate;
	
	public InvalidTimeSpanException(LocalDateTime startDate, LocalDateTime endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}
	
	public LocalDateTime getEndDate() {
		return endDate;
	}
	
	@Override
	public String getMessage() {
		return "Cannot compute the occupation rate between: " + startDate + " and " + endDate + "(invalid timespan).";
	}
}
