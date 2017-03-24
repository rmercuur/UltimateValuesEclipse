package agents;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.Schedule;
//import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersParser;
import repast.simphony.scenario.ScenarioUtils;
import ultimateValuesEclipse.UltimateValuesBuilder;

@RunWith(Parameterized.class)
public class SocialAgentThresholdDivideTest {
	public Context context ;
	public double valueDifference;
	public int resultingDemand;
	
	public SocialAgentThresholdDivideTest(double valueDifference, int resultingDemand) {
	      this.valueDifference = valueDifference;
	      this.resultingDemand = resultingDemand;
	   }
	
	// creates the test data
    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {{-2,500}, {0,525}, {0.2,567},{2,1000}};
        return Arrays.asList(data);
    }
    
	 @BeforeClass
	 public static void setUpBeforeClass () throws Exception {
		 String scenarioDirString = "UltimateValuesEclipse.rs";
		 ScenarioUtils . setScenarioDir ( new File ( scenarioDirString ));
		 File paramsFile = new File ( ScenarioUtils . getScenarioDir () ,
				 "parameters.xml");
	  
		 ParametersParser pp = new ParametersParser ( paramsFile );
		 repast.simphony.parameter.Parameters params = pp.getParameters ();
		 RunEnvironment . init ( new Schedule () , null , params , true );
		 Context context = new DefaultContext ();
		 UltimateValuesBuilder builder = new UltimateValuesBuilder();
		 context = builder . build (context);
	 }
	  
	@Test
	public void testMyPropose() {
		SocialAgentThresholdDivide tester=new SocialAgentThresholdDivide(0,valueDifference);
		assertEquals(resultingDemand, tester.myPropose(null)); //argument does not matter
	}

}
