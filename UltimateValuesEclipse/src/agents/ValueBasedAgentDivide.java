package agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import repast.simphony.random.RandomHelper;
import ultimateValuesEclipse.Helper;
import values.Fairness;
import values.Value;
import values.Wealth;

/* This is the agent used in the simulation to simulate value-based agents
 *
 */
public class ValueBasedAgentDivide extends Agent {
    List<Value> values;
    Value wealth;
    Value fairness;
    
    public ValueBasedAgentDivide(int ID, boolean isProposer) {
		super(ID,isProposer); 
        
        //If you want to make truncated agent
        double leftBound =-100;
        double rightBound=100;
        
        valueDifference = -200;
        while(valueDifference < leftBound || valueDifference > rightBound){
        	valueDifference = RandomHelper.createNormal(Helper.getParams().getDouble("valueDifferenceMean"),
        			Helper.getParams().getDouble("valueDifferenceSD")).nextDouble();
        } 
        wealth =new Wealth(1+(valueDifference/2));
        fairness=new Fairness(1-(valueDifference/2));
        values =new ArrayList<Value>();
        values.add(wealth);
        values.add(fairness);
    }
    
    public ValueBasedAgentDivide(int ID, double valueDifference, boolean isProposer) {
		super(ID,isProposer);
        this.valueDifference = valueDifference;
   
        wealth =new Wealth(1+(valueDifference/2));
        fairness=new Fairness(1-(valueDifference/2));
        values =new ArrayList<Value>();
        values.add(wealth);
        values.add(fairness);
    }
    
    @Override
    public int myPropose(Agent responder) {
    	TreeMap<Double, Integer> demandToUtility = new TreeMap<Double, Integer>();
    	
    	for(int demand = 0; demand < (Helper.getParams().getInteger("pieSize") +1); demand++){
    		
    		double utility = wealth.thresholdDivideUtility(demand); 
    				utility+=fairness.thresholdDivideUtility(demand);
    		demandToUtility.put(utility, demand); 
    	}
    	
    	return demandToUtility.lastEntry().getValue();
    }

    @Override
    public boolean myRespond(int demand, Agent proposer) {
    	int offer = Helper.getParams().getInteger("pieSize") - demand;
    	double acceptUtility = wealth.thresholdDivideUtility(offer) ;
    			acceptUtility += fairness.thresholdDivideUtility(offer);
    	double rejectUtility = wealth.thresholdDivideUtility(1) ; //zodat die gelijk aan R is;
    			rejectUtility += fairness.thresholdDivideUtility((Helper.getParams().getInteger("pieSize") /2)); //For fairness purposses its as if it was an even split;
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
