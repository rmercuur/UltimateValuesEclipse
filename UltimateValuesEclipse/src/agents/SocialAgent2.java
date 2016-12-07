package agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import Values.Fairness;
import Values.Value;
import Values.Wealth;
import repast.simphony.random.RandomHelper;
import ultimateValuesEclipse.Helper;

/*
 * This social agent will have two values:
 * - fairness
 * - wealth
 * 
 * It has thresholds in these values and memory.
 */
public class SocialAgent2 extends Agent {
    List<Value> values;
    Value wealth;
    Value fairness;

    public SocialAgent2(int ID){
        super(ID);
        //wealth =new Wealth(RandomHelper.nextDoubleFromTo(0.5, 1.5));
       // fairness =new Fairness(RandomHelper.nextDoubleFromTo(0.5, 1.5));
        wealth = new Wealth(1);
        fairness =new Fairness(1);
        values =new ArrayList<Value>();
        values.add(wealth);
        values.add(fairness);
    }
    
    @Override
    public int myPropose(Agent responder) {
    	TreeMap<Double, Integer> offerToNeedDecrease = new TreeMap<Double, Integer>();
    	
    	for(int offer = 0; offer < (Helper.getParams().getInteger("pieSize")+1); offer++){
    		double demand = Helper.getParams().getInteger("pieSize")  - offer;
//    		System.out.println("For:" + demand);
    		double needDecrease = (wealth.getNeed() - wealth.tryAction(demand) + fairness.getNeed() - fairness.tryAction(demand));
    		offerToNeedDecrease.put(needDecrease, offer); //first needDecrease, because that's how we are going to look it up
    	}
    	
    	return offerToNeedDecrease.lastEntry().getValue();
    }

    @Override
    public boolean myRespond(int offer, Agent proposer) {
    	double acceptNeedDecrease = (wealth.getNeed() - wealth.tryAction(offer) + fairness.getNeed() - fairness.tryAction(offer));
    	double rejectNeedDecrease = wealth.getNeed() - wealth.tryAction(0) + fairness.getNeed() - fairness.tryAction(50); //For fairness purposses its as if it was an even split;
    	return acceptNeedDecrease > rejectNeedDecrease; //
    }

	@Override
	public void update() {
		wealth.updateSatisfaction(myGame.getOutcome(this)); //does this work, returns something but we dont care?
		fairness.updateSatisfaction(myGame.getOutcome(this));
	}
	
    
    public double getWStrength(){
    	return wealth.getStrength();
    }
    public double getFStrength(){
    	return fairness.getStrength();
    }
    public double getWFStrength(){
    	return getWStrength()/getFStrength();
    }
    public double getWNeed(){
    	return wealth.getNeed();
    }
    public double getFNeed(){
    	return fairness.getNeed();
    }
    
}
