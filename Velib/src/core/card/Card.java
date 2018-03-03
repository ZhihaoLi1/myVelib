package core.card;
/**
 * Each user has a card that allows him to accumulate time credits, and give him discounts
 * @author animato
 *
 */
public abstract class Card {
	int timeCredit; 
	
	public void addTimeCredit( int timeCredit ) {
		this.timeCredit += timeCredit;
	};
	
	public int getTimeCredit() {
		return timeCredit;
	}
}
