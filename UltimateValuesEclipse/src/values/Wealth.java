package values;

import ultimateValuesEclipse.Helper;

public class Wealth extends Value {

	public Wealth(double strengthWeigth) {
		super(strengthWeigth, Helper.getParams().getDouble("beta"), Helper.getParams().getDouble("k")); //Different implementation of using constuctor than before.
	}
	
	

	@Override
	public double getStrengthAvarage() {
		return Helper.getParams().getDouble("selfEnAvgStrength");
	}

	@Override
	public double getGain(double result){
		return result/1000;
	}
	
	@Override
	public double newSatisfaction(double result) {
		return newSatisfactionFunction((result/1000));
	}



}
