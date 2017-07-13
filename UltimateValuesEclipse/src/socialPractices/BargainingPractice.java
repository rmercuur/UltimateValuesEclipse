package socialPractices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ultimateValuesEclipse.Helper;
import values.Fairness;
import values.Value;
import values.Wealth;

/*
 * What do all the agents share in the bargaining practice
 */
public class BargainingPractice extends SocialPractice {
	
	
	@Override
	public void makePlanPattern(){
		List<Action> demands=new ArrayList<Action>();
		List<Action> fairDemands=new ArrayList<Action>();
		List<Action> highDemands=new ArrayList<Action>();
		List<Action> responses=new ArrayList<Action>();
		List<Action> accept=new ArrayList<Action>();
		List<Action> reject=new ArrayList<Action>();
		
		for(int i=1; i<=9;i++){
			Action newAction =new Action(i*((int)Helper.getParams().getInteger("pieSize")/10));
			demands.add(newAction);
			if(i<6) fairDemands.add(newAction);
			if(i>5) highDemands.add(newAction);
		}
		accept.add(new Action(true));
		reject.add(new Action(false));
		
		
		List<Class<? extends Value>> relevantValues=Arrays.asList(Fairness.class,Wealth.class);
		
		Graph<AbstractAction> myPlanPattern =new Graph<AbstractAction>();
		AbstractAction startA=new AbstractAction("start",
				null,
				null); //start is just placeholder
		Vertex<AbstractAction> startV=new Vertex<AbstractAction>("start",startA);
		myPlanPattern.addVertex(startV);

		AbstractAction fairDemandA=new AbstractAction("fairDemand",
				relevantValues,
				fairDemands);
		Vertex<AbstractAction> fairDemandV=new Vertex<AbstractAction>("fairDemand",fairDemandA);
		myPlanPattern.addVertex(fairDemandV);
		
		myPlanPattern.addEdge(startV, fairDemandV, 0);

		AbstractAction highDemandA=new AbstractAction("highDemand",
				relevantValues,
				highDemands);
		Vertex<AbstractAction> highDemandV=new Vertex<AbstractAction>("highDemand",highDemandA);
		myPlanPattern.addVertex(highDemandV);
		
		myPlanPattern.addEdge(startV, fairDemandV, 0);
		
		AbstractAction acceptFairA=new AbstractAction("acceptFair",
				relevantValues,
				accept);
		Vertex<AbstractAction> acceptFairV=new Vertex<AbstractAction>("acceptFair",acceptFairA);
		myPlanPattern.addVertex(acceptFairV);
		myPlanPattern.addEdge(fairDemandV, acceptFairV, 0);
		
		AbstractAction rejectFairA=new AbstractAction("acceptFair",
				relevantValues,
				reject);
		Vertex<AbstractAction> rejectFairV=new Vertex<AbstractAction>("rejectFair",rejectFairA);
		myPlanPattern.addVertex(rejectFairV);
		myPlanPattern.addEdge(fairDemandV, rejectFairV, 0);
		
		AbstractAction acceptHighA=new AbstractAction("acceptHigh",
				relevantValues,
				accept);
		Vertex<AbstractAction> acceptHighV=new Vertex<AbstractAction>("acceptHigh",acceptHighA);
		myPlanPattern.addVertex(acceptHighV);
		myPlanPattern.addEdge(highDemandV, acceptHighV, 0);
		
		AbstractAction rejectHighA=new AbstractAction("rejectHigh",
				relevantValues,
				reject);
		Vertex<AbstractAction> rejectHighV=new Vertex<AbstractAction>("rejectHigh",rejectHighA);
		myPlanPattern.addVertex(rejectHighV);
		myPlanPattern.addEdge(highDemandV, rejectHighV, 0);
	
		AbstractAction endA=new AbstractAction("end",
				null,
				null);
		Vertex<AbstractAction> endV=new Vertex<AbstractAction>("end",endA);
		myPlanPattern.addVertex(endV);
		myPlanPattern.addEdge(acceptFairV, endV, 0);
		myPlanPattern.addEdge(rejectFairV, endV, 0);
		myPlanPattern.addEdge(acceptHighV, endV, 0);
		myPlanPattern.addEdge(rejectHighV, endV, 0);
		
		myPlanPattern.setRootVertex(startV);
	}
}
