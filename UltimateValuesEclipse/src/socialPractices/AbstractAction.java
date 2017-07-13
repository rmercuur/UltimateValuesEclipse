package socialPractices;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.util.Pair;

import values.Value;

public class AbstractAction {
	String name;
	List<Class<? extends Value>> relevantValues;
	private List<Action> actionsThatCountAs;
	Map<Map.Entry<Value,Action>,Integer> actionValueToSatisfaction;
	Map<Action,Integer> actionCounter;
	
	public AbstractAction(String name,
			List<Class<? extends Value>> relevantValues,
			List<Action> actionsThatCountAs){
		this.name= name;
		this.relevantValues = relevantValues;
		this.setActionsThatCountAs(actionsThatCountAs);
		this.actionValueToSatisfaction=new HashMap<Map.Entry<Value,Action>,Integer>();
		this.actionCounter=new HashMap<Action,Integer>();
	}

	public List<Action> getActionsThatCountAs() {
		return actionsThatCountAs;
	}

	private void setActionsThatCountAs(List<Action> actionsThatCountAs) {
		this.actionsThatCountAs = actionsThatCountAs;
	}
}
