package agents;

import repast.simphony.engine.schedule.ScheduledMethod;

public class MyAgent extends Agent {
	public MyAgent(int ID) {
		super(ID);
	}

	@Override
	public int myPropose(Agent responder) {
		return 99;
	}

	@Override
	public boolean myRespond(int offer, Agent proposer) {
		return offer >0;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
}
