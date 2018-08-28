package socialPractices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ultimateValuesEclipse.Helper;
import values.Fairness;
import values.Value;
import values.Wealth;

/*
 * What do all the agents share in the bargaining practice
 */
public class BargainingPractice extends SocialPractice {
	
	
	@Override
	public Graph<AbstractAction> makePlanPattern(Graph<AbstractAction> myPlanPattern){
		//Possible Actions
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
		
		
		Map<Class<? extends Value>, Boolean> valueOrientationFairness
				=new HashMap<Class<? extends Value>, Boolean>();
		valueOrientationFairness.put(Fairness.class, true);
		valueOrientationFairness.put(Wealth.class, false);
		Map<Class<? extends Value>, Boolean> valueOrientationWealth
				=new HashMap<Class<? extends Value>, Boolean>();
		valueOrientationWealth.put(Fairness.class, false);
		valueOrientationWealth.put(Wealth.class, true);
		
		Map<Class<? extends Value>, Boolean> valueOrientationBothPos
			=new HashMap<Class<? extends Value>, Boolean>();
		valueOrientationBothPos.put(Fairness.class, true);
		valueOrientationBothPos.put(Wealth.class, true);

		Map<Class<? extends Value>, Boolean> valueOrientationBothNeg
			=new HashMap<Class<? extends Value>, Boolean>();
		valueOrientationBothNeg.put(Fairness.class, false);
		valueOrientationBothNeg.put(Wealth.class, false);

		myPlanPattern =new Graph<AbstractAction>();
		AbstractAction startA=new AbstractAction("start",
				null,
				null); //start is just placeholder
		Vertex<AbstractAction> startV=new Vertex<AbstractAction>("start",startA);
		myPlanPattern.addVertex(startV);

		AbstractAction fairDemandA=new AbstractAction("fairDemand",
				valueOrientationFairness,
				fairDemands);
		Vertex<AbstractAction> fairDemandV=new Vertex<AbstractAction>("fairDemand",fairDemandA);
		myPlanPattern.addVertex(fairDemandV);
		
		myPlanPattern.addEdge(startV, fairDemandV, 0);

		AbstractAction highDemandA=new AbstractAction("highDemand",
				valueOrientationWealth,
				highDemands);
		Vertex<AbstractAction> highDemandV=new Vertex<AbstractAction>("highDemand",highDemandA);
		myPlanPattern.addVertex(highDemandV);
		
		myPlanPattern.addEdge(startV, highDemandV, 0);
		
		AbstractAction acceptFairA=new AbstractAction("acceptFair",
				valueOrientationBothPos,
				accept);
		Vertex<AbstractAction> acceptFairV=new Vertex<AbstractAction>("acceptFair",acceptFairA);
		myPlanPattern.addVertex(acceptFairV);
		myPlanPattern.addEdge(fairDemandV, acceptFairV, 0);
		
		AbstractAction rejectFairA=new AbstractAction("rejectFair",
				valueOrientationBothNeg,
				reject);
		Vertex<AbstractAction> rejectFairV=new Vertex<AbstractAction>("rejectFair",rejectFairA);
		myPlanPattern.addVertex(rejectFairV);
		myPlanPattern.addEdge(fairDemandV, rejectFairV, 0);
		
		AbstractAction acceptHighA=new AbstractAction("acceptHigh",
				valueOrientationWealth,
				accept);
		Vertex<AbstractAction> acceptHighV=new Vertex<AbstractAction>("acceptHigh",acceptHighA);
		myPlanPattern.addVertex(acceptHighV);
		myPlanPattern.addEdge(highDemandV, acceptHighV, 0);
		
		AbstractAction rejectHighA=new AbstractAction("rejectHigh",
				valueOrientationFairness,
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
		
		return myPlanPattern;
	}
}
