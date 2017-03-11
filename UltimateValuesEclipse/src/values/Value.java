package values;

import repast.simphony.random.RandomHelper;

public abstract class Value {
   private double satisfaction;
   private double strengthWeight; //Agent specific value-strength
   private double beta;
   private double k;

   public Value(double strengthWeight, double beta, double k){
      this.strengthWeight = strengthWeight;
      this.satisfaction = getThreshold();
      //this.satisfaction = RandomHelper.nextDoubleFromTo(0, 2*getThreshold()); //Any satisfaction within 0 and boundry
      this.beta = beta;
      this.k = k;
   }
   
   public abstract double getStrengthAvarage(); //valuetype specific avarage strength;
   public double getStrength() {
      return strengthWeight * getStrengthAvarage();
   }
   public double getThreshold(){
      return getStrength();     
   }
   public double getSatisfaction() {
      return satisfaction;
   }
   public double getNeed() {
      if(satisfaction < 0) satisfaction = 0.01; //does it give a overflow?
      return getThreshold()/satisfaction;  //only works if satisfaction stays positive, else the Needs get lower when satisfaction gets lower
   }
   //getNeed met hypothetical satisfaction
   public double getNeed(double hsatisfaction) {
	      if(satisfaction < 0) hsatisfaction = 0.01; //does it give a overflow?
	      return getThreshold()/hsatisfaction;  //only works if satisfaction stays positive, else the Needs get lower when satisfaction gets lower
	   }
   
   private double getK() {
      double modifier = getStrength();
      return getStrength() * k;
   }

	
   public double newSatisfactionFunctionOld(double connectedFeaturesSum){
      double increment = Math.tanh( beta * (connectedFeaturesSum - getK())); //do you really need the sigmund tanh? //k=0.5 beta 0.1, connectedfeaturesum=[0,1] standaard.
      System.out.println("Increment: " + this + "by: "+ increment);
      double newSatisfaction = satisfaction + increment;
      return newSatisfaction;
   }	
   
   public double newSatisfactionFunction(double connectedFeaturesSum){
	      double increment = beta*(connectedFeaturesSum - getK()); //do you really need the sigmund tanh? //k=0.5 beta 0.1, connectedfeaturesum=[0,1] standaard.
//	      System.out.println("Increment: " + this + "by: "+ increment);
	      double newSatisfaction = satisfaction + increment;
	      return newSatisfaction;
	   }
   

  public abstract double newSatisfaction(double result); //Value-specific function that converts input for satisfactionfunction;
 	  
 
   public void setSatisfaction(double satisfaction){
	   this.satisfaction = satisfaction;
   }
   
   public void updateSatisfaction(double result){
		setSatisfaction(newSatisfaction(result));
	}

   public double tryAction(double result){
	   return getNeed(newSatisfaction(result));
   }

   
   public void setBeta(double beta){
      this.beta = beta;
   }
   public void setK(double k){
      this.k = k;
   }

   public abstract double getGain(double result);
   
   public double thresholdDivideUtility(double result){
	   return -(getThreshold()/(getGain(result) +0.0001));
   }


}