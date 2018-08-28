package agents;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import socialPractices.*;
import ultimateValuesEclipse.Helper;
import values.Fairness;
import values.Value;
import values.Wealth;

public class SocialPracticeAgent extends Agent {
	BargainingPractice myBargainingPractice;
	Vertex<AbstractAction> lastAbstractAction;
	Vertex<AbstractAction> currentAbstractAction;
	boolean valueOrNorms;
	
	List<Value> values;
    Value wealth;
    Value fairness;

	@SuppressWarnings("unchecked")
	public SocialPracticeAgent(int ID, boolean isProposer) {
		super(ID, isProposer);
		updateValueOrNorms();
		
		//initateValues
	    double valueDifference = -2;
        while(valueDifference < -0.67 || valueDifference > 2.0){
        	valueDifference = RandomHelper.createNormal(Helper.getParams().getDouble("valueDifferenceMean"),
        			Helper.getParams().getDouble("valueDifferenceSD")).nextDouble();
        } //this makes a right-tailed truncated normal distribution.
        wealth =new Wealth(1+(valueDifference/2));
        fairness=new Fairness(1-(valueDifference/2));
        values =new ArrayList<Value>();
        values.add(wealth);
        values.add(fairness);
        
        
		//iniatie Practice
		myBargainingPractice=new BargainingPractice();
		currentAbstractAction=myBargainingPractice.getMyPlanPattern().getRootVertex();
		List<AbstractAction> abstractActions = myBargainingPractice.getAbstractActions();
		for(AbstractAction aa:abstractActions){
			//if this action is not start or finish, calculate best
			if(aa.getActionsThatCountAs() != null) calculateBestAction(aa);			
		}
		
        
	}

	private void calculateBestAction(AbstractAction aa) {
		if(aa.getActionsThatCountAs().get(0).isInteger()){	
			TreeMap<Double, Action> actionToUtility = new TreeMap<Double, Action>();
			for(Action a:aa.getActionsThatCountAs()){
					double utility = wealth.thresholdDivideUtility(a.getMyActionAsInteger()); 
							utility+=fairness.thresholdDivideUtility(a.getMyActionAsInteger());
					actionToUtility.put(utility, a); 
				}
			aa.setBestAction(actionToUtility.lastEntry().getValue());
		}
		else{
			aa.setBestAction(aa.getActionsThatCountAs().get(0)); //If we are responding there is only one option;
		}
	}


	
	private void updateValueOrNorms() {
		valueOrNorms = 
				RandomHelper.nextDoubleFromTo(0, 1) <0.5;
	}

	@ScheduledMethod(start =1, interval=1, priority=4)
	public void updateInBeginning(){
		lastAbstractAction= currentAbstractAction;
		currentAbstractAction = updatePlanPatternPosition();
		//updateAbstractActionEdge();
		//updateActionCounter();
	}


	@ScheduledMethod(start =1, interval=1, priority=2.5)
	public void updateAfterDemand(){
		lastAbstractAction= currentAbstractAction;
		currentAbstractAction = updatePlanPatternPosition();
		updateAbstractActionEdge();
		updateActionCounter();
	}
	
	//Goes last;
	@Override
	public void update() {
		lastAbstractAction= currentAbstractAction;
		currentAbstractAction = updatePlanPatternPosition();
		updateAbstractActionEdge();
		updateActionCounter();
		updateValueOrNorms(); //At the end of every round now
	}
	
	private void updateActionCounter() {
			List<Action> possibleActions = currentAbstractAction.getData().getActionsThatCountAs();
			if(myGame.getStage() ==2){
				Action matchingAction;
				matchingAction= //finds the action seen in environment in the abstractaction-data
							possibleActions.stream()
							.filter(v -> myGame.getDemand()==v.getMyActionAsInteger())
							.findFirst().get();
				currentAbstractAction.getData().actionUp(matchingAction);//update in AA;
			}
			//don't do anything for stage 3, only 1 action possible
	}

	private void updateAbstractActionEdge() {
		lastAbstractAction.findEdge(currentAbstractAction).up();
	}
	
	//Misschien zou deze methode in de BargainigPRactice moeten zitten;
	private Vertex<AbstractAction> updatePlanPatternPosition() {
		// Possible AA's
		Vertex<AbstractAction> newVertex =currentAbstractAction;
		List<Edge<AbstractAction>> possibleEdges=
				currentAbstractAction.getOutgoingEdges();
		List<Vertex<AbstractAction>> possibleVertices=
				possibleEdges.stream().map(x -> x.getTo()).
				collect(Collectors.toList());
		
		// Get element from environment
		if(myGame.getStage() ==1){
			newVertex = myBargainingPractice.getMyPlanPattern().getRootVertex();
		}
		if(myGame.getStage() ==2){
			Optional<Vertex<AbstractAction>> matchingVertex;
			if(myGame.getDemand() <= Helper.getParams().getInteger("pieSize")/2){
				matchingVertex=
						possibleVertices.stream()
						.filter(v -> v.getData().getName().equals("fairDemand"))
						.findFirst();
			}else{
				matchingVertex=
						possibleVertices.stream()
						.filter(v -> v.getData().getName().equals("highDemand"))
						.findFirst();
			}
			newVertex = matchingVertex.get();
		}
		if(myGame.getStage()==3){
			boolean lastResponse =myGame.isAccepted();
			newVertex= possibleVertices.stream().filter(v -> 
						v.getData().getActionsThatCountAs()
						.get(0).getMyActionAsBoolean() ==lastResponse)
						.findFirst().get(); //you know these AA's only have one action so you can do get(0)
		}
		if(Helper.getParams().getBoolean("reportDecision")){
			if(this.getID()==13){
				System.out.println("Agent " + this.getID()+ 
						" goes from: "+ currentAbstractAction.getName()+
						" to: "+newVertex.getName());
			}
		}	
		return newVertex;
	}

	@Override
	public int myPropose(Agent responder) {
		Action chosenAction = chooseAction();
		return chosenAction.getMyActionAsInteger();
	}

	private Action chooseAction() {
		//choose AbstractAction
		AbstractAction chosenAbstractAction =null;
		double chosenAAscore =Double.MIN_VALUE;
		List<Edge<AbstractAction>> possibleEdges=
				currentAbstractAction.getOutgoingEdges();
		List<AbstractAction> possibleAbstractActions=
				possibleEdges.stream().map(x -> x.getTo().getData()).
				collect(Collectors.toList());
		if(valueOrNorms){
			//Qualatitive on Values
			for(AbstractAction aa:possibleAbstractActions){
				double AAscore = 0;
				List promotedValues = aa.getPromotedValues();
				List demotedValues = aa.getDemotedValues();
				for(Value v:values){
					if(promotedValues.contains(v.getClass())) AAscore+=v.getStrength();
					if(demotedValues.contains(v.getClass())) AAscore-=v.getStrength();
				}
				if(AAscore > chosenAAscore){
					chosenAbstractAction = aa;
					chosenAAscore = AAscore;
				}
			}
			//To Report
			if(Helper.getParams().getBoolean("reportDecision")){
				if(this.getID()==13){
					System.out.println("Agent " + this.getID()+ 
							" chose: "+ chosenAbstractAction.getName()+
							" because it promotes: "+ chosenAbstractAction.getPromotedValues() +
							" but demotes "+ chosenAbstractAction.getDemotedValues() );
				}
			}	
		}else{
			   final Comparator<Edge<AbstractAction>> comp = 
			    		(p1, p2) -> Double.compare(p1.getCost(), p2.getCost());	
			    chosenAbstractAction=
			    		possibleEdges.stream().max(comp).get().getTo().getData();
			    
			    if(Helper.getParams().getBoolean("reportDecision")){
					if(this.getID()==13){
						System.out.println("Agent " + this.getID()+ 
								" chose: "+ chosenAbstractAction.getName()+
								" because it has seen that action the most");
					}
				}
		}
		
		//choose Implementation
		Action chosenAction;
		if(valueOrNorms){
			chosenAction = chosenAbstractAction.getBestAction();
			 if(Helper.getParams().getBoolean("reportDecision")){
					if(this.getID()==13){
						System.out.println("Agent " + this.getID()+ 
								" implements that action with: "+ chosenAction.getMyAction()+
								" because that balances it values the best");
					}
				}
		}else{
			chosenAction=chosenAbstractAction.getExpectedAction();
			  if(Helper.getParams().getBoolean("reportDecision")){
					if(this.getID()==13){
						System.out.println("Agent " + this.getID()+ 
								" implements that action with: "+ chosenAction.getMyAction()+
								" because it has seen that action the most");
					}
				}
		}
		return chosenAction;
	}

	@Override
	public boolean myRespond(int demand, Agent proposer) {
		Action chosenAction = chooseAction();
		return chosenAction.getMyActionAsBoolean();
	}

}
