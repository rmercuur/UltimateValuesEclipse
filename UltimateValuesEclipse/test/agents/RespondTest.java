package agents;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import au.com.bytecode.opencsv.CSVReader;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.parameter.ParametersParser;
import repast.simphony.scenario.ScenarioUtils;
import ultimateValuesEclipse.UltimateValuesBuilder;

@RunWith(Parameterized.class)
public class RespondTest {
	public Context context ;
	public double valueDifference;
	public int demand;
	public boolean resultingResponse;
	
	
	public RespondTest(double valueDifference, int demand, boolean resultingResponse) {
	      this.valueDifference = valueDifference;
	      this.demand = demand;
	      this.resultingResponse = resultingResponse;
	   }
	
    @Parameters
    public static Collection<Object[]> data() {
    	
    	//This thus gives: valueDifference, demand, and what it should say
        Object[][] data = new Object[][] {{0,100,true},
				  						  {0,824,true},
        								  {0,825,true},
        								  {0,826,false}, //alleen true als rejectUtility wealth(1!)
        								  {0,827,false},
        								  
        								  {0,828,false},
        								  {0,829,false},
        								  {0,824,true},
        								  {0,807,true},
        								  {0,712,true},
        								  
        								  {0,400,true},
        								  {0.1,836,true},
        								  {0.1,837,false},
        								  {1,917,true},
        								  {1,918,false},
        								  
        								  {2,998,true},
        								  {2,999,false},
        								  {-1,486,true},
        								  {-1,487,false},
        								  {-1.9,55,true},
        								  
        								  {-1.9,56,false}};
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
	public void testMyRespondSocialAgent() {
		ValueBasedAgentDivide tester=new ValueBasedAgentDivide(0,valueDifference,true);
		assertEquals(resultingResponse, tester.myRespond(demand, null));
	}
	
	@Test
	public void testMyRespondValueNormAgent() {
		TestValueNormAgent tester=new TestValueNormAgent(0,valueDifference,true);
		assertEquals(resultingResponse, tester.myRespond(demand, null));
	}
	
}
