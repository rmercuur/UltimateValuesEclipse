package ultimateValuesEclipse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.random.RandomHelper;
import agents.*;

public class UltimateNormBuilder extends AbstractBuilder {

	@Override
	public void setCFG() {

	}

	@Override
	public void setID() {
		context.setId("ultimateValuesEclipse");
	}

	@Override
	public void addAgents() {
		int halfAgentCount = params.getInteger("agentCount")/2;
		for (int i=0; i < halfAgentCount; i++){
			NormativeAgent5 newAgent=new NormativeAgent5(i,true);
			context.add(newAgent);
			agents.add(newAgent);
		}
		for (int i= halfAgentCount; i < params.getInteger("agentCount"); i++){
			NormativeAgent5 newAgent=new NormativeAgent5(i,false);
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
			Helper.moveToObject(grid, agent1, newGame);
			Helper.moveToObject(grid, agent2, newGame);
		}
	}
	
	@Override
	public void addEnvironment() {
		// TODO Auto-generated method stub

	}

}
