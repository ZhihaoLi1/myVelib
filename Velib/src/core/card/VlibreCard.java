package core.card;

public class VlibreCard implements Card {
	private int timeCredit; 
	
	public VlibreCard(){
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
