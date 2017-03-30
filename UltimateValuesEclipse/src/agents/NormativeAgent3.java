package agents;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import repast.simphony.random.RandomHelper;
import ultimateValuesEclipse.Helper;
/*
 * Does a random action if no accept and reject seen;
 */
public class NormativeAgent3 extends Agent {
	List<Integer> seenDemands;
	List<Integer> seenRespondsAccepted;
	List<Integer> seenRespondsRejected;
	
	public NormativeAgent3(int ID, boolean isProposer) {
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
			demand= RandomHelper.createUniform(0,Helper.getParams().getInteger("pieSize")).nextInt();
		}
		return demand;
	}

	@Override
	public boolean myRespond(int demand, Agent proposer) {
		double threshold =0;
		if(seenDemands.isEmpty()){
			threshold= RandomHelper.createUniform(0,Helper.getParams().getInteger("pieSize")).nextInt();
		}
		else{
			OptionalDouble averageSeenDemand = (OptionalDouble) seenDemands.stream().mapToDouble(a -> a).average();
			threshold = (int) averageSeenDemand.getAsDouble();
		}
		return demand <= threshold; //> 0.5 signifies that more than half has been accepts, so accept;
	}

}
