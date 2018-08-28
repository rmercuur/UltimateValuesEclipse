package socialPractices;

import java.util.List;
import java.util.stream.Collectors;

public abstract class SocialPractice {
	private Graph<AbstractAction> myPlanPattern;
	public abstract Graph<AbstractAction> makePlanPattern(Graph<AbstractAction> myPlanPattern);
	
	public SocialPractice(){
		setMyPlanPattern(makePlanPattern(myPlanPattern));
	}

	public Graph<AbstractAction> getMyPlanPattern() {
		return myPlanPattern;
	}

	public void setMyPlanPattern(Graph<AbstractAction> myPlanPattern) {
		this.myPlanPattern = myPlanPattern;
	}
	
	public List<AbstractAction> getAbstractActions(){
		return myPlanPattern.getVerticies().stream().map(x -> x.getData()).collect(Collectors.toList());
	}
}
