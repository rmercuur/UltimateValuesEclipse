package agents;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.function.Predicate;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameter;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import ultimateValuesEclipse.Game;

public abstract class Agent {
	private int ID;
	private Grid<Object> myGrid;
	protected Game myGame;
	private boolean proposer;
	
	public Agent(int ID, boolean isProposer){
		this.proposer = isProposer;
		this.ID = ID;
	}
	//Modeled as Demand
	@ScheduledMethod(start = 1, interval = 1, priority = 3)
	public void propose(){
		if(myGame.getProposer().equals(this)){
			int demand = myPropose(myGame.getResponder());
			myGame.setDemand(demand);
		}
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 2)
	public void respond(){
		if(myGame.getResponder().equals(this)){
			boolean accept =myRespond(myGame.getDemand(), myGame.getProposer());
			myGame.setAccepted(accept);
		}
	}
	
	@ScheduledMethod(start =1, interval=1, priority=1) //can you schedule abstract methods
	public abstract void update();
	
	public abstract int myPropose(Agent responder);
	public abstract boolean myRespond(int demand, Agent proposer);
	
	public void setGame(Game myGame) {
		this.myGame = myGame;
	}
	public int getID() {
		return ID;
	}
	
	public double getLastProfit(){
    	return myGame.getOutcome(this);
    }
	public double getLastAction(){
		return this.equals(myGame.getProposer()) ? 
				myGame.getDemand() : myGame.isAccepted() ? 
						1337:-1337;
	}
	public double getLastResponse(){
		return this.equals(myGame.getProposer()) ? 
				myGame.isAccepted() ? 
						1337:-1337 :myGame.getDemand();
	}
	public boolean isResponder() {
		return !proposer;
	}
	public boolean isProposer(){
		return proposer;
	}
}