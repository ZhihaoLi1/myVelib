package core.card;
/**
 * Each user has a card that allows him to accumulate time credits, and give him discounts
 * @author animato
 *
 */
public abstract class Card {
	int timeCredit; 
	
	public Card() {
		timeCredit = 0;
	}
	
	public void addTimeCredit(int timeCredit) throws NegativeTimeCreditGivenException {
		if (timeCredit >  0) {
			this.timeCredit += timeCredit;
		} else {
			throw new NegativeTimeCreditGivenException(timeCredit);
		}
	};

	public void removeTimeCredit(int timeCredit) throws NegativeTimeCreditGivenException, NegativeTimeCreditLeftException {
		if (timeCredit >  0) {
			if (this.timeCredit < timeCredit) {
				throw new NegativeTimeCreditLeftException(timeCredit);
			} else {
				this.timeCredit -= timeCredit;
			}
		} else {
			throw new NegativeTimeCreditGivenException(timeCredit);
		}
	};

	public int getTimeCredit() {
		return timeCredit;
	}
}
