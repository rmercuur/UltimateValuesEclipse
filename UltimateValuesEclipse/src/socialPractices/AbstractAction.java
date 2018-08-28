package socialPractices;

import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.math3.util.Pair;

import repast.simphony.random.RandomHelper;
import ultimateValuesEclipse.Helper;
import values.Value;

public class AbstractAction {
	private String name;
	//List<Class<? extends Value>> relevantValues;
	Map<Class<? extends Value>,Boolean> valueOrientation;
	private List<Class<? extends Value>> promotedValues;
	private List<Class<? extends Value>> demotedValues;
	private List<Action> actionsThatCountAs;
	TreeMap<Map.Entry<Value,Action>,Integer> actionValueToSatisfaction; //this is way to split it up, other way is
	Map<Object, Double> actionCounter;
	private Action bestAction;
	
	public AbstractAction(String name,
			Map<Class<? extends Value>,Boolean> valueOrientation,
			List<Action> actionsThatCountAs){
		this.setName(name);
		this.valueOrientation = valueOrientation;
		if(valueOrientation !=null){ //start and finish are empty
			this.setPromotedValues(valueOrientation.entrySet().stream()
			.filter(x -> x.getValue()) //filters the entries on those that are true
			.map(x -> x.getKey()).collect(Collectors.toList())); 
			this.setDemotedValues(valueOrientation.entrySet().stream()
			.filter(x -> !x.getValue()) //filters the entries on those that are false
			.map(x -> x.getKey()).collect(Collectors.toList())); 
		}
		this.setActionsThatCountAs(actionsThatCountAs);
		this.actionValueToSatisfaction=new TreeMap<Map.Entry<Value,Action>,Integer>();
		this.actionCounter=new HashMap<Object, Double>();
		if(actionsThatCountAs != null){ //start and finish are empty TODO:make seperate constructor
			for(Action a:actionsThatCountAs){ //Adds every action one time, so no nullpointer exception later
				Helper.mapAdd(actionCounter, a);
			}
		}
	}
	
	public void actionUp(Action doneAction){
		Helper.mapAdd(actionCounter, doneAction);
	}
	public List<Action> getActionsThatCountAs() {
		return actionsThatCountAs;
	}
	private void setActionsThatCountAs(List<Action> actionsThatCountAs) {
		this.actionsThatCountAs = actionsThatCountAs;
	}

	public List<Class<? extends Value>> getPromotedValues() {
		return promotedValues;
	}

	private void setPromotedValues(List<Class<? extends Value>> promotedValues) {
		this.promotedValues = promotedValues;
	}

	public List<Class<? extends Value>> getDemotedValues() {
		return demotedValues;
	}

	private void setDemotedValues(List<Class<? extends Value>> demotedValues) {
		this.demotedValues = demotedValues;
	}


	public Action getBestAction() {
		return bestAction;
	}

	public void setBestAction(Action bestAction) {
		this.bestAction = bestAction;
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}
	/*
	 * Returns either:
	 * 1. A random action from the list if all actionCounters are equal (e.g. no action is chosen before)
	 * 2. The element of the list with the highest counter; 
	 */
	public Action getExpectedAction() {
		if(actionCounter.values().stream().distinct().limit(2).count() <=1){
			return actionsThatCountAs.get(RandomHelper.nextIntFromTo(0, actionsThatCountAs.size()-1));
		}else{
			return (Action) actionCounter.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
		}
	}
}
