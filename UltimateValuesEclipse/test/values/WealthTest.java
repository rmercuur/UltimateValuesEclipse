package values;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersParser;
import repast.simphony.scenario.ScenarioUtils;
import ultimateValuesEclipse.UltimateValuesBuilder;

public class WealthTest {
	 public Context context ;
	 //static UserObserver observer;
	 
	 @BeforeClass
	 public static void setUpBeforeClass () throws Exception {
		 String scenarioDirString = "UltimateValuesEclipse.rs";
	 ScenarioUtils . setScenarioDir ( new File ( scenarioDirString ));
	  File paramsFile = new File ( ScenarioUtils . getScenarioDir () ,
	  "parameters.xml");
	  
	  ParametersParser pp = new ParametersParser ( paramsFile );
	  Parameters params = pp.getParameters ();
	  RunEnvironment . init ( new Schedule () , null , params , true );
	  Context context = new DefaultContext ();
	  UltimateValuesBuilder builder = new UltimateValuesBuilder();
	  context = builder . build (context);
	  }
	 
	 @Before
	 public void setUp () throws Exception {
	 }
	 
	@Test
	public void testGetGain() {
		Wealth tester = new Wealth(1);
	    assertEquals("10 x 5 must be 50", 1, tester.getGain(1000),0);
	}

}
