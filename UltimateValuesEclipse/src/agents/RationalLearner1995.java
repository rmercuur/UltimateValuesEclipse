package agents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import repast.simphony.random.RandomHelper;
import ultimateValuesEclipse.Helper;

public class RationalLearner1995 extends Agent {
	HashMap<Integer, Double> proposeQualities;
	HashMap<Integer, Double> respondQualities;
	
	public RationalLearner1995(int ID){
		super(ID);
		proposeQualities=new HashMap<Integer, Double>();
		respondQualities=new HashMap<Integer, Double>();
		double pieSize = Helper.getParams().getInteger("pieSize");
		double initialStrength = Helper.getParams().getDouble("initialStrength");//s(1) = sumQualites/AvarageReward; AverageReward in UG = 50;
		int averageRewardInUg = 50;
		int nrOfActionsInUg = 9;
		double initialQualitySum = (initialStrength * averageRewardInUg);
		double initialQuality = initialQualitySum/nrOfActionsInUg; //initialQuality = sumQuality/10 = (s(1) * AvarageRewards)/10
		List<Double> initialPropensityListProposer = Arrays.asList(0.0,0.0,0.04,0.07,0.50,0.28,0.06,0.04,0.02);
		List<Double> initialPropensityListResponder = Arrays.asList(0.0,0.0,0.19,0.0,0.08,0.23,0.0,0.0,0.50);
		if(Helper.getParams().getBoolean("equalInitialQuality")){
			for(int i=1; i <=9; i++){
				proposeQualities.put(i*((int)pieSize/10), initialQuality); //so we can choose 0, and 100 as well
				respondQualities.put(i*((int)pieSize/10), initialQuality);
			}
		}else{
			for(int i=1; i<=nrOfActionsInUg; i++){
				proposeQualities.put(i*10, initialQualitySum*initialPropensityListProposer.get(i-1));
				respondQualities.put(i*10, initialQualitySum*initialPropensityListResponder.get(i-1));
			}
		}
	}

	@Override
	public int myPropose(Agent responder) {
		return Helper.weightedPick(proposeQualities);
	}

	@Override
	public boolean myRespond(int offer, Agent proposer) {
		int threshold = Helper.weightedPick(respondQualities);
		myGame.setThreshold(threshold);
		return offer > threshold;
	}

	@Override
	public void update() {
		if(myGame.getProposer().equals(this)){
		//System.out.println("Offer to update:" + myGame.getOffer());
		double oldQuality = proposeQualities.get(myGame.getOffer());
		double newQuality = oldQuality+myGame.getOutcome(this);
		proposeQualities.replace(myGame.getOffer(), newQuality);
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
