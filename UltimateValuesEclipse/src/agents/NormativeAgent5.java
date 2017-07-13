package agents;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import repast.simphony.random.RandomHelper;
import ultimateValuesEclipse.Helper;
/*
 * Does a random action if no accept and reject seen;
 * Does a random action, in the sense, that it has equal chance to accept and reject;
 * Instead of a random action, does the action first round agents do.
 */
public class NormativeAgent5 extends Agent {
	List<Integer> seenDemands;
	List<Integer> seenRespondsAccepted;
	List<Integer> seenRespondsRejected;
	
	public NormativeAgent5(int ID, boolean isProposer) {
		super(ID,isProposer);
		seenDemands=new ArrayList<Integer>();
		seenRespondsAccepted=new ArrayList<Integer>();
		seenRespondsRejected=new ArrayList<Integer>();	
	}

	@Override
	public void update() {
		//if responder (maar ze blijven toch op dezelfde plek)
		seenDemands.add(myGame.getDemand());
		
		//if Proposer (maar ze blijven toch op dezelfde plek)
		if(myGame.isAccepted()){
			seenRespondsAccepted.add(myGame.getDemand());
		}
		else{
			seenRespondsRejected.add(myGame.getDemand());
		}
	}

	@Override
	public int myPropose(Agent responder) {
		int demand;
		if(!seenRespondsAccepted.isEmpty() &&!seenRespondsRejected.isEmpty()){
			demand = (int)
					(seenRespondsRejected.stream().mapToDouble(a -> a).min().getAsDouble() +
					seenRespondsAccepted.stream().mapToDouble(a -> a).max().getAsDouble()) /
					2;}
		else{
			demand = -10;
			double mean = 0.5618 * Helper.getParams().getInteger("pieSize");
			double sd = 0.1289 * Helper.getParams().getInteger("pieSize");
			while(demand < 0 || demand > Helper.getParams().getInteger("pieSize")){
				demand= RandomHelper.createNormal(mean, sd).nextInt();
			}
		}
		return demand;
	}

	@Override
	public boolean myRespond(int demand, Agent proposer) {
		boolean accept;
		
		
		if(seenDemands.isEmpty()){
			double acceptRate;
			double mean = 0.806;
			double sd = 0.395;
			acceptRate= RandomHelper.createNormal(mean, sd).nextDouble(); //NB: are extreme values a problem? don't thinks o
			accept= RandomHelper.createUniform(0,1).nextDouble() <acceptRate;
		}
		else{
			OptionalDouble averageSeenDemand = (OptionalDouble) seenDemands.stream().mapToDouble(a -> a).average();
			int threshold = (int) averageSeenDemand.getAsDouble();
			accept = demand <= threshold;
		}
		return accept;
	}

}
