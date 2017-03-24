package ultimateValuesEclipse;

import agents.Agent;

public class Game {
	private int ID;
	private Round round;
	private Agent proposer;
	private Agent responder;
	private int demand;
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

	public int getDemand() {
		return demand;
	}

	public void setDemand(int demand) {
		this.demand = demand;
	}

	public boolean isAccepted() {
		return accept;
	}

	public void setAccepted(boolean accept) {
		this.accept = accept;
	}
	
	public int getOutcome(){ //defined as amount proposer got;
		return isAccepted() ? getDemand():0; 
	}
 
	public int getOutcome(Agent asker){
		return isAccepted() ?
			proposer.equals(asker) ? 
					getOutcome():
				(Helper.getParams().getInteger("pieSize")-getOutcome()):
					0;
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
	
	//For Last Round Statistics
	public boolean inLastRound(){
		return round.main.rounds.get(round.main.rounds.size()-1).games.contains(this);
	}
	public int getDemandIfLastRound(){
		return inLastRound()? getDemand():Helper.getParams().getInteger("pieSize")+100;
	}
}
