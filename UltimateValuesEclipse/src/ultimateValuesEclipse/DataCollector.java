package ultimateValuesEclipse;

import java.util.ArrayList;

import agents.Agent;
import repast.simphony.random.RandomHelper;

public class DataCollector {
	AbstractBuilder main;
	Agent oneAgent;
	
	public DataCollector(AbstractBuilder abstractBuilder) {
		main = abstractBuilder;
		oneAgent = main.agents.get(RandomHelper.nextIntFromTo(0, main.agents.size()-1));
	}
	
	public double getLastRoundAvarageResult(){
		return main.rounds.get(main.rounds.size()-1).getAvarageResult();
	}
	
	public double getLastRoundAvarageDemand(){
		return main.rounds.get(main.rounds.size()-1).getAvarageDemand();
	}
	public double getLastRoundAvarageAcceptPercantage(){
		return main.rounds.get(main.rounds.size()-1).getPercentageAccepted();
	}
}
