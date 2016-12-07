package Values;

import ultimateValuesEclipse.Helper;

public class SelfEnhancement extends Value {

	public SelfEnhancement(double strengthWeigth) {
		super(strengthWeigth, Helper.getParams().getDouble("beta"), Helper.getParams().getDouble("k")); //Different implementation of using constuctor than before.
	}
	

	@Override
	public double getStrengthAvarage() {
		return Helper.getParams().getDouble("selfEnAvgStrength");
	}

	@Override
	public double newSatisfaction(double result) {
		return newSatisfactionFunction(Helper.getParams().getInteger("pieSize") -result);
	}


	@Override
	public double getGain(double result) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
