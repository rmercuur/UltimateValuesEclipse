package agents;

import java.util.List;
import java.util.stream.Collectors;

import socialPractices.*;

public class SocialPracticeAgent extends Agent {
	BargainingPractice myBargainingPractice;
	Vertex<AbstractAction> lastAbstractAction;
	Vertex<AbstractAction> currentAbstractAction;
	boolean valueOrNorms;
	
	
	public SocialPracticeAgent(int ID, boolean isProposer) {
		super(ID, isProposer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		lastAbstractAction= currentAbstractAction;
		currentAbstractAction = updatePlanPatternPosition();
		updateAbstractActionEdge();
		updateActionCounter();
	}

	private void updateActionCounter() {
		
	}

	private void updateAbstractActionEdge() {
		// TODO Auto-generated method stub
		
	}

	private Vertex<AbstractAction> updatePlanPatternPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int myPropose(Agent responder) {
		// choose AbstractAction
		AbstractAction chosenAbstractAction;
		List<Edge<AbstractAction>> possibleEdges=
				currentAbstractAction.getOutgoingEdges();
		List<Vertex<AbstractAction>> possibleAbstractActions=
				possibleEdges.stream().map(x -> x.getTo()).
				collect(Collectors.toList());
		if(valueOrNorms){
			//Either qualitative.. or precise
			chosenAbstractAction =null;
		}else{
			//Get Max Edge and relating AA;
			chosenAbstractAction=null;
		}
		
		//choose Implementation
		Action chosenAction;
		List<Action> possibleActions =chosenAbstractAction.getActionsThatCountAs();
		if(valueOrNorms){
			chosenAction = null;
			//getUtilityFromTable, put it in reaminder function, get max
		}else{
			//getMaxFromActionCounterTable
			chosenAction = null;
		}
		
		return chosenAction.getMyActionAsInteger();
	}

	@Override
	public boolean myRespond(int demand, Agent proposer) {
		// TODO Auto-generated method stub
		return false;
	}

}
