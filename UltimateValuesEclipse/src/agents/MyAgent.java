package agents;

import repast.simphony.engine.schedule.ScheduledMethod;

public class MyAgent extends Agent {
	public MyAgent(int ID, boolean isProposer) {
		super(ID,isProposer);
	}

	@Override
	public int myPropose(Agent responder) {
		return 99;
	}

	@Override
	public boolean myRespond(int demand, Agent proposer) {
		return demand >0;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
}
