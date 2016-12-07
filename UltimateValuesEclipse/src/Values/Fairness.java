package Values;

import ultimateValuesEclipse.Helper;

public class Fairness extends Value {

	public Fairness(double strengthWeigth) {
		super(strengthWeigth, Helper.getParams().getDouble("beta"), Helper.getParams().getDouble("k")); //Different implementation of using constuctor than before.
	}
	


	@Override
	public double getStrengthAvarage() {
		return Helper.getParams().getDouble("selfTrAvgStrength");
	}


	@Override
	public double newSatisfaction(double result) {
		double halfPie = (Helper.getParams().getInteger("pieSize") /2);
		return newSatisfactionFunction((1-(Math.abs(halfPie-result)/halfPie))); //simplify with newgetgain
	}



	@Override
	public double getGain(double result) {
		double halfPie = (Helper.getParams().getInteger("pieSize") /2);
		return (1-(Math.abs(halfPie-result)/halfPie));
	}
	
}
