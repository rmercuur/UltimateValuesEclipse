package agents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.random.RandomHelper;
import ultimateValuesEclipse.Helper;

public class RationalLearner1995 extends Agent {
	HashMap<Integer, Double> proposeQualities;
	HashMap<Integer, Double> respondQualities;
	
	public RationalLearner1995(int ID, boolean isProposer) {
		super(ID,isProposer);
		proposeQualities=new HashMap<Integer, Double>();
		respondQualities=new HashMap<Integer, Double>();
		
		//Initial Utility-Table
		//Roth and Erev ways
		double pieSize = Helper.getParams().getInteger("pieSize");
		double initialStrength = Helper.getParams().getDouble("initialStrength");//s(1) = sumQualites/AvarageReward; AverageReward in UG = 50;
		int averageRewardInUg = (int) pieSize/2;
		int nrOfActionsInUg = 9;
		double initialQualitySum = (initialStrength * averageRewardInUg);
		double initialQuality = initialQualitySum/nrOfActionsInUg; //initialQuality = sumQuality/10 = (s(1) * AvarageRewards)/10
		List<Double> initialPropensityListProposer = Arrays.asList(0.0,0.0,0.04,0.07,0.50,0.28,0.06,0.04,0.02);
		List<Double> initialPropensityListResponder = Arrays.asList(0.0,0.0,0.19,0.0,0.08,0.23,0.0,0.0,0.50);
		
		//Roth and Erev method 1
		if(Helper.getParams().getBoolean("equalInitialQuality")){
			for(int i=1; i <=9; i++){ //100
				proposeQualities.put(i*((int)pieSize/10), initialQuality); //so we can choose 0, and 100 as well
				respondQualities.put(i*((int)pieSize/10), initialQuality);
			}
		}else{ //Roth and Erev Method 2
			for(int i=1; i<=nrOfActionsInUg; i++){
				proposeQualities.put(i*10, initialQualitySum*initialPropensityListProposer.get(i-1));
				respondQualities.put(i*10, initialQualitySum*initialPropensityListResponder.get(i-1));
			}
		} 
		
	}

	@Override
	public int myPropose(Agent responder) {
		int demand;
		if(Helper.getParams().getBoolean("LHE:forceHumanAction") &&
				(int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount() ==1){
			demand = -10; 
			double mean = 0.5618 * Helper.getParams().getInteger("pieSize");
			double sd = 0.1289 * Helper.getParams().getInteger("pieSize");
			while(demand < 0 || demand > 0.9*Helper.getParams().getInteger("pieSize")){
				demand= (int)
						Math.round(
								(RandomHelper.createNormal(mean, sd).nextDouble()
										/100) //856 -> 8.56
						) // -> 9
						*100; //rounds up to nearest 100
			}
			
		}
		else{
			demand = Helper.weightedPick(proposeQualities);
			
		}
		return demand;
		
	}

	@Override
	public boolean myRespond(int demand, Agent proposer) {
		boolean response;
		int threshold;
		
		if(Helper.getParams().getBoolean("LHE:forceHumanAction") &&
				(int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount() ==1){

			double mean = 0.806;
			double sd = 0.395;
			double acceptRate= RandomHelper.createNormal(mean, sd).nextDouble(); //NB: are extreme values a problem? don't thinks o
			response= RandomHelper.createUniform(0,1).nextDouble() <acceptRate;
			
			//backward engineer threshold for model
			if(response){ //accepted so your threshold was lower than demand
				threshold = demand -100;
				if(threshold < 100) threshold =100; //100 is lowest threshold
				//threshold = Math.floorDiv(demand, 100) *100; //I think it always floors
			}else{ //rejected so your threshol is higher than your demand
				threshold = demand + 100;
				if(threshold>900) threshold =900; //edge case
				//threshold = (demand/100 + ((demand % 100 == 0) ? 0 : 1))*100; //first floors, but adds 1 in almost all cases
			}
			myGame.setThreshold(threshold);
		}
		else{
			threshold = Helper.weightedPick(respondQualities);
			myGame.setThreshold(threshold);
			response = demand < threshold;
		}
		return response;
	}

	@Override
	public void update() {
		if(myGame.getProposer().equals(this)){
		//System.out.println("Offer to update:" + myGame.getOffer());
		double oldQuality = proposeQualities.get(myGame.getDemand());
		double newQuality = oldQuality+myGame.getOutcome(this);
		proposeQualities.replace(myGame.getDemand(), newQuality);
		//System.out.println("OldQ" + oldQuality + "newQ:" +
		//		newQuality);
		
		}
		else{
			//System.out.println("Response to update:" + myGame.getThreshold());
			double newQuality = respondQualities.get(myGame.getThreshold())+
					myGame.getOutcome(this);
			//System.out.println("OldQ" + respondQualities.get(myGame.getThreshold()) + 
			//		"newQ:" + newQuality);
			respondQualities.replace(myGame.getThreshold(), newQuality);
		}
	}

}
