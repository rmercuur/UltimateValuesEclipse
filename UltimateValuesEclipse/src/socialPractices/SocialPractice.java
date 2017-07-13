package socialPractices;

public abstract class SocialPractice {
	Graph myPlanPattern;
	public abstract void makePlanPattern();
	
	public SocialPractice(){
		makePlanPattern();
	}
}
