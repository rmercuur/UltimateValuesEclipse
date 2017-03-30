package agents;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.TreeMap;

import repast.simphony.random.RandomHelper;
import ultimateValuesEclipse.Helper;
import values.Fairness;
import values.Value;
import values.Wealth;

public class ValueNormAgent extends Agent {
	List<Value> values;
    List<Integer> seenDemands;
	List<Integer> seenResponds;
	
    Value wealth;
    Value fairness;
    int myBestDemand;
    int myThreshold;
    
    boolean valueOrNorms;
    
	public ValueNormAgent(int ID) {
		super(ID);
		
		//Values
	    double valueDifference = -2;
        while(valueDifference < -0.67 || valueDifference > 2.0){
        	valueDifference = RandomHelper.createNormal(Helper.getParams().getDouble("valueDifferenceMean"),
        			Helper.getParams().getDouble("valueDifferenceSD")).nextDouble();
        } //this makes a right-tailed truncated normal distribution.
        wealth =new Wealth(1+(valueDifference/2));
        fairness=new Fairness(1-(valueDifference/2));
        values =new ArrayList<Value>();
        values.add(wealth);
        values.add(fairness);
        myBestDemand = calculateMyBestDemand();
        myThreshold = calculateMyThreshold();
        
        //Norms
    	seenDemands=new ArrayList<Integer>();
		seenResponds=new ArrayList<Integer>();
		
		//Value-Norm-Agent
		 valueOrNorms = true; //true = Value, norm = False;
	}
	
	public ValueNormAgent(int ID, double valueDifference) {
		super(ID);
		
		//Value
        wealth =new Wealth(1+(valueDifference/2));
        fairness=new Fairness(1-(valueDifference/2));
        values =new ArrayList<Value>();
        values.add(wealth);
        values.add(fairness);
        myBestDemand = calculateMyBestDemand();
        myThreshold = calculateMyThreshold();
        
        //Norms
    	seenDemands=new ArrayList<Integer>();
		seenResponds=new ArrayList<Integer>();
		
		//Value-Norm-Agent
		 valueOrNorms = true; //true = Value, norm = False;
	}
	

	public int calculateMyBestDemand(){
		TreeMap<Double, Integer> demandToUtility = new TreeMap<Double, Integer>();
		
		for(int demand = 0; demand < (Helper.getParams().getInteger("pieSize") +1); demand++){
	//		System.out.println("For:" + demand);
			
			double utility = wealth.thresholdDivideUtility(demand); 
					utility+=fairness.thresholdDivideUtility(demand);
			demandToUtility.put(utility, demand); 
		}
		return (int) demandToUtility.lastEntry().getValue(); 
	}
	
	public int calculateMyThreshold(){
		List<Integer> acceptableDemands =new ArrayList<Integer>();
		for(int demand = 0; demand < (Helper.getParams().getInteger("pieSize") +1); demand++){
			int offer = Helper.getParams().getInteger("pieSize") -demand;
			double acceptUtility = wealth.thresholdDivideUtility(offer) ;
			acceptUtility += fairness.thresholdDivideUtility(offer);
			double rejectUtility = wealth.thresholdDivideUtility(1) ; //NB: zodat die gelijk aan R is...
			rejectUtility += fairness.thresholdDivideUtility((Helper.getParams().getInteger("pieSize") /2)); //For fairness purposses its as if it was an even split;
			if(acceptUtility > rejectUtility) acceptableDemands.add(demand);
		}
		
		return acceptableDemands.stream().mapToInt(i -> i).max().getAsInt(); //maximum demand that is still acceptable!
	}
	
	
	@Override
	public void update() {
		//Normative Update
		seenDemands.add(myGame.getDemand());
		seenResponds.add(myGame.isAccepted() ? 1:0);
	}

	@Override
	public int myPropose(Agent responder) {
		int demand;
		if(valueOrNorms){
			demand= myBestDemand;
		}else{
			if(seenDemands.isEmpty()){
				demand= RandomHelper.createUniform(0,Helper.getParams().getInteger("pieSize")).nextInt();
			}
			else{
				OptionalDouble average = (OptionalDouble) seenDemands.stream().mapToDouble(a -> a).average();
				demand = (int) average.getAsDouble();
			}
		}
		return demand;
	}

	@Override
	public boolean myRespond(int demand, Agent proposer) {
		boolean respond;
		if(valueOrNorms){
			if(demand<=myThreshold) respond = true; //threshold is still acceptable
			else respond =false;
		}else{
			if(seenResponds.isEmpty()){
				respond= RandomHelper.createUniform(0,1).nextDouble() > 0.5; //random
			}
			else{
				OptionalDouble average = (OptionalDouble) seenResponds.stream().mapToDouble(a -> a).average();
				respond = average.getAsDouble() >0.5; //> 0.5 signifies that more than half has been accepts, so accept;
			}
		}
		return respond;
	}

}
