package values;

import ultimateValuesEclipse.Helper;

public class SelfTranscendence extends Value {

	public SelfTranscendence(double strengthWeigth) {
		super(strengthWeigth, Helper.getParams().getDouble("beta"), Helper.getParams().getDouble("k")); //Different implementation of using constuctor than before.
	}
	

	@Override
	public double getStrengthAvarage() {
		return Helper.getParams().getDouble("selfTrAvgStrength");
	}

	@Override
	public double newSatisfaction(double result) {
		return newSatisfactionFunction(result);
	}


	@Override
	public double getGain(double result) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
