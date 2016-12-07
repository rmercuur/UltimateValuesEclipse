package ultimateValuesEclipse;

import agents.Agent;

public class Game {
	private int ID;
	private Round round;
	private Agent proposer;
	private Agent responder;
	private int offer;
	private boolean accept;
	private int threshold;
	
	
	
	public Game(int ID, Round round, Agent proposer, Agent responder) {
		this.ID = ID;
		this.round = round;
		this.setProposer(proposer);
		this.setResponder(responder);
	}
	
	public Agent getProposer() {
		return proposer;
	}
	public void setProposer(Agent proposer) {
		this.proposer = proposer;
	}

	public Agent getResponder() {
		return responder;
	}

	public void setResponder(Agent responder) {
		this.responder = responder;
	}

	public int getOffer() {
		return offer;
	}

	public void setOffer(int offer) {
		this.offer = offer;
	}

	public boolean isAccepted() {
		return accept;
	}

	public void setAccepted(boolean accept) {
		this.accept = accept;
	}
	
	public int getOutcome(){ //defined as amount deliberator got;
		return isAccepted() ? getOffer():0; 
	}
 
	public int getOutcome(Agent asker){
		return responder.equals(asker) ? getOutcome():(Helper.getParams().getInteger("pieSize")-getOutcome());
	}
	

	public int getID() {
		return ID;
	}
	public Round getRound() {
		return round;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}
}
