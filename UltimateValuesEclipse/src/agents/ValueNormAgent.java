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
    List<Integer> seenOffers;
	List<Integer> seenResponds;
	
    Value wealth;
    Value fairness;
    int myBestOffer;
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
        myBestOffer = calculateMyBestOffer();
        myThreshold = calculateMyThreshold();
        
        //Norms
    	seenOffers=new ArrayList<Integer>();
		seenResponds=new ArrayList<Integer>();
		
		//Value-Norm-Agent
		 valueOrNorms = true; //true = Value, norm = False;
	}

	public int calculateMyBestOffer(){
		TreeMap<Double, Integer> offerToUtility = new TreeMap<Double, Integer>();
		
		for(int offer = 0; offer < (Helper.getParams().getInteger("pieSize") +1); offer++){
			double demand = Helper.getParams().getInteger("pieSize") - offer;
	//		System.out.println("For:" + demand);
			
			double utility = wealth.thresholdDivideUtility(demand); 
					utility+=fairness.thresholdDivideUtility(demand);
			offerToUtility.put(utility, offer); 
		}
		return (int) offerToUtility.lastEntry().getValue(); 
	}
	
	public int calculateMyThreshold(){
		List<Integer> acceptableOffers =new ArrayList<Integer>();
		for(int offer = 0; offer < (Helper.getParams().getInteger("pieSize") +1); offer++){
			double acceptUtility = wealth.thresholdDivideUtility(offer) ;
			acceptUtility += fairness.thresholdDivideUtility(offer);
			double rejectUtility = wealth.thresholdDivideUtility(0) ;
			rejectUtility += fairness.thresholdDivideUtility((Helper.getParams().getInteger("pieSize") /2)); //For fairness purposses its as if it was an even split;
			if(acceptUtility > rejectUtility) acceptableOffers.add(offer);
		}
		return acceptableOffers.stream().mapToInt(i -> i).min().getAsInt(); //
	}
	
	
	@Override
	public void update() {
		//Normative Update
		seenOffers.add(myGame.getOffer());
		seenResponds.add(myGame.isAccepted() ? 1:0);
	}

	@Override
	public int myPropose(Agent responder) {
		int offer;
		if(valueOrNorms){
			offer= myBestOffer;
		}else{
			if(seenOffers.isEmpty()){
				offer= RandomHelper.createUniform(0,Helper.getParams().getInteger("pieSize")).nextInt();
			}
			else{
				OptionalDouble average = (OptionalDouble) seenOffers.stream().mapToDouble(a -> a).average();
				offer = (int) average.getAsDouble();
			}
		}
		return offer;
	}

	@Override
	public boolean myRespond(int offer, Agent proposer) {
		boolean respond;
		if(valueOrNorms){
			if(offer>myThreshold) respond = true;
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
