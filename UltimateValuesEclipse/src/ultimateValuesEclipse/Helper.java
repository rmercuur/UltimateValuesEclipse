/*******************************************************************************
 * 2015, All rights reserved.
 *******************************************************************************/
package ultimateValuesEclipse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;


public class Helper {
	// Start of user code (user defined attributes for Helper)
	
	// End of user code
	
	/**
	 * The constructor.
	 */
	public Helper() {
		// Start of user code constructor for Helper)
		super();
		// End of user code
	}
	public static void moveToObject(Grid<Object> grid, Object movingObject,
			Object targetObject) {
		GridPoint pt = grid.getLocation(targetObject);
		
		Helper.moveTo(grid, movingObject, pt.getX(), pt.getY());
	}
	
	public static void moveTo(Grid<Object> grid, Object movingObject, int x, int y){
		if(Helper.getParams().getBoolean("GUI")){
			Iterator<Object> iter = grid.getObjectsAt(x,y).iterator();
			boolean hasAgent = false;
			if(iter.hasNext()) hasAgent = true;
	/*		while(iter.hasNext()){
				Object obje = iter.next();
				if(obje instanceof Agent) hasAgent = true;
			}*/
			if(hasAgent){
				moveTo(grid, movingObject, x+1,y); //Maybe getNeighbor or something?
			}else{
				grid.moveTo(movingObject, x,y);
			}
		}
	}
	
	
/*	public static void filter(
			ArrayList<SocialPractice> candidateSocialPractices,
			HashMap<SocialPractice, Double> habitStrengths, double HABIT_THRESHOLD) {
		Iterator<SocialPractice> iter = candidateSocialPractices.iterator();
		
		while(iter.hasNext()){
			SocialPractice sp = iter.next();
			if(habitStrengths.get(sp) < HABIT_THRESHOLD) iter.remove(); 
		}
		
	}*/
	
	public static int sum(Collection<Integer> values) {
		int sum = 0;
		for(Integer i:values){
			sum = sum + i;
		}
		return sum;
	}
	
	public static double sumDouble(Collection<Double> values) {
		double sum = 0;
		for(Double i:values){
			sum = sum + i;
		}
		return sum;
	}
	
	//Wrapper
	public static void mapAdd(HashMap<Object, Double> map,
			Object key){
		mapAdd(map, key, 1);
	}
	
	/*
	 * Adds 'add'-points to HashMap entry;
	 */
	public static void mapAdd(HashMap<Object, Double> map,
			Object key, double add) {
		Double currentValue = map.get(key);
		if(currentValue == null){		//no Entry yet
			map.put(key, add);
		}
		else{
			currentValue+=add;
			map.put(key, currentValue);
		}
	}
	
	public static Parameters getParams(){
		return RunEnvironment.getInstance().getParameters();
	}
	
	// Start of user code (user defined methods for Helper)
	public static int weightedPick(HashMap<Integer,Double> probalisticMap){
		double totalQuality = Helper.sumDouble(probalisticMap.values()); 
		double randomDeterminer = RandomHelper.nextDoubleFromTo(0, totalQuality);
		int chosenAction =10;
		
		Iterator it = probalisticMap.entrySet().iterator();
		while(randomDeterminer > 0) {
			HashMap.Entry pair = (HashMap.Entry) it.next(); 
			chosenAction = (int) pair.getKey();
			randomDeterminer = randomDeterminer - (double) pair.getValue();
		}
		return chosenAction;
	}
	// End of user code


}
