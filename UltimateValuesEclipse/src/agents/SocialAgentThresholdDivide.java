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
public class SocialAgentThresholdDivide extends Agent {
    List<Value> values;
    Value wealth;
    Value fairness;

    public SocialAgentThresholdDivide(int ID){
        super(ID);
        double valueDifference = RandomHelper.createNormal(0.45, Helper.getParams().getDouble("valueDifferenceSD")).nextDouble();
        wealth =new Wealth(1+(valueDifference/2));
        fairness=new Fairness(1-(valueDifference/2));
        values =new ArrayList<Value>();
        values.add(wealth);
        values.add(fairness);
    }
    
    @Override
    public int myPropose(Agent responder) {
    	TreeMap<Double, Integer> offerToUtility = new TreeMap<Double, Integer>();
    	
    	for(int offer = 0; offer < (Helper.getParams().getInteger("pieSize") +1); offer++){
    		double demand = Helper.getParams().getInteger("pieSize") - offer;
//    		System.out.println("For:" + demand);
    		
    		double utility = wealth.thresholdDivideUtility(demand); 
    				utility+=fairness.thresholdDivideUtility(demand);
    		offerToUtility.put(utility, offer); 
    	}
    	
    	return offerToUtility.lastEntry().getValue();
    }

    @Override
    public boolean myRespond(int offer, Agent proposer) {
    	double acceptUtility = wealth.thresholdDivideUtility(offer) ;
    			acceptUtility += fairness.thresholdDivideUtility(offer);
    	double rejectUtility = wealth.thresholdDivideUtility(0) ;
    			rejectUtility += fairness.thresholdDivideUtility(50); //For fairness purposses its as if it was an even split;
    	return acceptUtility > rejectUtility; //
    }

	@Override
	public void update() {
	//	wealth.updateSatisfaction(myGame.getOutcome(this)); //does this work, returns something but we dont care?
	//	fairness.updateSatisfaction(myGame.getOutcome(this));
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