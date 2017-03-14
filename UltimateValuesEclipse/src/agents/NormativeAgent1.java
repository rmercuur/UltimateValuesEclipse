package agents;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import repast.simphony.random.RandomHelper;
import ultimateValuesEclipse.Helper;

public class NormativeAgent1 extends Agent {
	List<Integer> seenOffers;
	List<Integer> seenResponds;
	public NormativeAgent1(int ID) {
		super(ID);
		seenOffers=new ArrayList<Integer>();
		seenResponds=new ArrayList<Integer>();
	}

	@Override
	public void update() {
		//NB: Takes into account own actions as well;
		seenOffers.add(myGame.getOffer());
		seenResponds.add(myGame.isAccepted() ? 1:0);
	}

	@Override
	public int myPropose(Agent responder) {
		int offer;
		if(seenOffers.isEmpty()){
			offer= RandomHelper.createUniform(0,Helper.getParams().getInteger("pieSize")).nextInt();
		}
		else{
			OptionalDouble average = (OptionalDouble) seenOffers.stream().mapToDouble(a -> a).average();
			offer = (int) average.getAsDouble();
		}
		return offer;
	}

	@Override
	public boolean myRespond(int offer, Agent proposer) {
		double respond;
		if(seenResponds.isEmpty()){
			respond= RandomHelper.createUniform(0,1).nextDouble();
		}
		else{
			OptionalDouble average = (OptionalDouble) seenResponds.stream().mapToDouble(a -> a).average();
			respond = average.getAsDouble();
			if(average.getAsDouble()==0.5) respond= RandomHelper.createUniform(0,1).nextDouble();//edge-case where there are equal accepts and rejects
		}
		return respond >0.5; //> 0.5 signifies that more than half has been accepts, so accept;
	}

}
