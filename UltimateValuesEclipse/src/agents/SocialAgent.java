package agents;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.random.RandomHelper;
import values.SelfEnhancement;
import values.SelfTranscendence;
import values.Value;

public class SocialAgent extends Agent {
    List<Value> values;
    Value selfEnhancement;
    Value selfTranscendence;

    public SocialAgent(int ID, boolean isProposer) {
		super(ID,isProposer);
        selfEnhancement =new SelfEnhancement(RandomHelper.nextDoubleFromTo(0.5, 1.5));
        selfTranscendence =new SelfTranscendence(RandomHelper.nextDoubleFromTo(0.5, 1.5));
        values =new ArrayList<Value>();
        values.add(selfEnhancement);
        values.add(selfTranscendence);
    }
    
    @Override
    public int myPropose(Agent responder) {
        return (int) ((selfEnhancement.getStrength()/selfTranscendence.getStrength()) * 50);
    }

    @Override
    public boolean myRespond(int offer, Agent proposer) {
        return offer > ((selfEnhancement.getStrength()/selfTranscendence.getStrength()) * 50);
    }
    
    public double getEStrength(){
    	return selfEnhancement.getStrength();
    }
    public double getTStrength(){
    	return selfTranscendence.getStrength();
    }
    public double getETStrength(){
    	return getEStrength()/getTStrength();
    }

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
}
