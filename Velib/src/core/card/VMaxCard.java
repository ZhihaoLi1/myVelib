package core.card;

public class VMaxCard implements Card {
	private int timeCredit; 
	
	public VMaxCard(){
		this.timeCredit = 0;
	}
	
	@Override
	public void addTimeCredit(int timeCredit) {
		this.timeCredit += timeCredit;
	}
	
	public int getTimeCredit() {
		return timeCredit;
	}

}
