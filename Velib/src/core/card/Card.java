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
	
	public void addTimeCredit( int timeCredit ) {
		if (timeCredit >  0) {
			this.timeCredit += timeCredit;
		} else {
			System.out.println("Please add positive time credit. To remove time credit use removeTimeCreditMethod");
		}
	};

	public void removeTimeCredit( int timeCredit ) {
		if (timeCredit >  0) {
			if (this.timeCredit < timeCredit) {
				System.out.println("Not enought time credit left. Remaining time credit = " + this.timeCredit);
			} else {
				this.timeCredit -= timeCredit;
			}
		} else {
			System.out.println("Please remove positive time credit. To add time credit use addTimeCreditMethod");
		}
	};

	public int getTimeCredit() {
		return timeCredit;
	}
}
