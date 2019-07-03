package ultimateValuesEclipse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.random.RandomHelper;
import agents.*;


//This one is used by the normal UVE model runner
public class UltimateNVBuilder extends AbstractBuilder {

	@Override
	public void setCFG() {

	}

	@Override
	public void setID() {
		context.setId("ultimateValuesEclipse");
	}

	@Override
	public void addAgents() {
		int halfLHEAgentCount = params.getInteger("LHEAgentCount")/2;
			for (int i=0; i < halfLHEAgentCount; i++){
				RationalLearner1995 newAgent=new RationalLearner1995(i,true);
				context.add(newAgent);
				agents.add(newAgent);
			}
			for (int i= halfLHEAgentCount; i < params.getInteger("LHEAgentCount"); i++){
				RationalLearner1995 newAgent=new RationalLearner1995(i,false);
				context.add(newAgent);
				agents.add(newAgent);
			}
		
		int halfNormAgentCount = params.getInteger("normAgentCount")/2;
			for (int i=0; i < halfNormAgentCount; i++){
				NormativeAgent5 newAgent=new NormativeAgent5(i,true);
				context.add(newAgent);
				agents.add(newAgent);
			}
			for (int i= halfNormAgentCount; i < params.getInteger("normAgentCount"); i++){
				NormativeAgent5 newAgent=new NormativeAgent5(i,false);
				context.add(newAgent);
				agents.add(newAgent);
			}
		//Value Based Agents (remaining agents)
		int valueAgentCount = params.getInteger("agentCount") - params.getInteger("normAgentCount") - params.getInteger("LHEAgentCount");
		int halfValueAgentCount = valueAgentCount/2;
			for (int i=0; i < halfValueAgentCount; i++){
				ValueBasedAgentDivide newAgent=new ValueBasedAgentDivide(i,true);
				context.add(newAgent);
				agents.add(newAgent);
			}
			for (int i=halfValueAgentCount; i < valueAgentCount; i++){
				ValueBasedAgentDivide newAgent=new ValueBasedAgentDivide(i,false);
				context.add(newAgent);
				agents.add(newAgent);
			}
	}
	
	@Override
	public void createGames() {
		games.clear(); //List doesn't become infinite, there are new games every time step.
		int gameCount = params.getInteger("agentCount")/2; //Does it cast to int automaticly?
		int roundID = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		Round newRound =new Round(roundID,this);
		rounds.add(newRound);
		List<Agent> proposers =new ArrayList<Agent>(agents);
		proposers = proposers.stream().filter(p -> p.isProposer()).collect(Collectors.toList());
		List<Agent> responders =new ArrayList<Agent>(agents);
		responders =responders.stream().filter(p -> p.isResponder()).collect(Collectors.toList());
		for(int i =0; i < gameCount; i++){	
			int ID = (int) roundID *100 + i;
			Agent agent1 = proposers.remove(RandomHelper.nextIntFromTo(0, proposers.size()-1));
			Agent agent2 = responders.remove(RandomHelper.nextIntFromTo(0, responders.size()-1));
			Game newGame=new Game(ID,newRound,agent1,agent2);
			agent1.setGame(newGame);
			agent2.setGame(newGame);
			newRound.addGame(newGame);
			games.add(newGame);
			context.add(newGame);
			grid.moveTo(agent1, i+1,rounds.size());
			grid.moveTo(agent2, i+1,rounds.size()+1);
			grid.moveTo(newGame, i+1, rounds.size()-1); //put at 3,6,etc. and agents beneath;
		}
	}

	@Override
	public void addEnvironment() {
		// TODO Auto-generated method stub

	}

}
