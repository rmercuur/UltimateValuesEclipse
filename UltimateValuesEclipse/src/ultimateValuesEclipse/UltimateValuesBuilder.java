package ultimateValuesEclipse;

import agents.*;

public class UltimateValuesBuilder extends AbstractBuilder {

	@Override
	public void setCFG() {

	}

	@Override
	public void setID() {
		context.setId("ultimateValuesEclipse");
	}

	@Override
	public void addAgents() {
		for (int i=0; i < params.getInteger("agentCount"); i++){
			ValueBasedAgentDivide newAgent=new ValueBasedAgentDivide(i,true);
			//RationalLearner1995 newAgent=new RationalLearner1995(i);
			context.add(newAgent);
			agents.add(newAgent);
		}
	}

	@Override
	public void addEnvironment() {
		// TODO Auto-generated method stub

	}

}
