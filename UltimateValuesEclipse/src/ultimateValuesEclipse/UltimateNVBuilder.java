package ultimateValuesEclipse;

import agents.*;

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
		//Norm Agents
		for (int i=0; i < Helper.getParams().getInteger("normAgentCount"); i++){
			NormativeAgent1 newAgent=new NormativeAgent1(i);
			context.add(newAgent);
			agents.add(newAgent);
		}
		//Value Based Agents
		for (int i=Helper.getParams().getInteger("normAgentCount"); i < params.getInteger("agentCount"); i++){
			SocialAgentThresholdDivide newAgent=new SocialAgentThresholdDivide(i);
			context.add(newAgent);
			agents.add(newAgent);
		}
	}

	@Override
	public void addEnvironment() {
		// TODO Auto-generated method stub

	}

}
