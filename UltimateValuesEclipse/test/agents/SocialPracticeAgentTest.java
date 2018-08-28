package agents;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersParser;
import repast.simphony.scenario.ScenarioUtils;
import ultimateValuesEclipse.UltimateSPBuilder;
import ultimateValuesEclipse.UltimateValuesBuilder;
import values.Wealth;

/*
 * NB: Get the idea agents aren't actully executed!!
 */
public class SocialPracticeAgentTest {
	static Context context;
	static UltimateSPBuilder builder;
	 @BeforeClass
	 public static void setUpBeforeClass () throws Exception {
		 String scenarioDirString = "UltimateValuesEclipse.rs";
	 ScenarioUtils . setScenarioDir ( new File ( scenarioDirString ));
	  File paramsFile = new File ( ScenarioUtils . getScenarioDir () ,
	  "parameters.xml");
	  
	  ParametersParser pp = new ParametersParser ( paramsFile );
	  Parameters params = pp.getParameters ();
	  
	  //Gives the runenvironment a schedule;
	  RunEnvironment.init( new Schedule () , null , params , true );
	  context = new DefaultContext ();
	  RunState.init().setMasterContext(context);
	  
	 builder = new UltimateSPBuilder();
	  context = builder . build (context);
	  }

	 /*
	  * NB: Get the idea agents aren't actully executed!!
	  */
	@Test
	public void testSPProcess() {
		ISchedule schedule = RunEnvironment . getInstance ().
				 getCurrentSchedule ();
		SocialPracticeAgent myAgent= (SocialPracticeAgent) builder.agents.get(20);
		SocialPracticeAgent newAgent =new SocialPracticeAgent(1,true);
		assertEquals(myAgent.currentAbstractAction.getName(), "start");
		schedule.execute();
		//get the idea that the agents aren't actually executed;
		
		System.out.print(schedule.getTickCount());
		//one of the vertices starting with start should have cost 1
		assertTrue("one of the vertices should have been activated",
				myAgent.myBargainingPractice.getMyPlanPattern()
				.getEdges().stream().
				filter(e -> e.getCost()==1).findAny().isPresent());
		
		/*
		 * assertTrue("one of the vertices should have been activated",
				myAgent.myBargainingPractice.getMyPlanPattern()
				.getRootVertex().getOutgoinEdges().stream().
				filter(e -> e.getCost()==1).findAny().isPresent());
		 */
		//one of the vertices after that should have a token
		for(int i=0; i<5; i++){
			schedule.execute();
			System.out.println(myAgent.getLastAction());
			System.out.println(myAgent.myGame.getID());
			System.out.println(myAgent.myBargainingPractice.getAbstractActions().get(2).getExpectedAction().getMyAction());
			
		}
		System.out.println(myAgent.currentAbstractAction.getName());
	}
}
