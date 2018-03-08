package core.card;

public class NegativeTimeCreditGivenException extends Exception {

	private final int timeCredit;
	
	public NegativeTimeCreditGivenException(int timeCredit) {
		this.timeCredit = timeCredit;
	}

	public int getTimeCredit() {
		return timeCredit;
	}
}
