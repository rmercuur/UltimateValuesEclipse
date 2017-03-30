package ultimateValuesEclipse;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.BeforeClass;
import org.junit.Test;

import agents.Agent;

import com.oracle.webservices.internal.api.databinding.Databinding.Builder;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.parameter.ParametersParser;
import repast.simphony.scenario.ScenarioUtils;

public class AbstractBuilderTest {
	public static Context context ;
	public static UltimateValuesBuilder builder;
	
	@BeforeClass
	 public static void setUpBeforeClass () throws Exception {
		 String scenarioDirString = "UltimateValuesEclipse.rs";
		 ScenarioUtils . setScenarioDir ( new File ( scenarioDirString ));
		 File paramsFile = new File ( ScenarioUtils . getScenarioDir () ,
				 "parameters.xml");
	  
		 ParametersParser pp = new ParametersParser ( paramsFile );
		 repast.simphony.parameter.Parameters params = pp.getParameters ();
		 RunEnvironment . init ( new Schedule () , null , params , true );
		 context = new DefaultContext ();
		 builder = new UltimateValuesBuilder();
		 context = builder . build (context);
		 params.setValue("GUI", "FALSE");
	 }
	
	@Test
	public void test() {
		Agent testAgent =builder.agents.get(0);
		ConcurrentMap<Agent, AtomicInteger> countAgents=new ConcurrentHashMap<Agent, AtomicInteger>();
		
		for(int i=0; i < 100000; i++){
			builder.createGames();
			countPairedAgent(countAgents,testAgent);
		}
		
		for (final Agent k : countAgents.keySet()) {
			  System.out.println(k + ": " + countAgents.get(k).get());
			}
		for (final Agent k : countAgents.keySet()){
			assertEquals(countAgents.get(k).get(), 100000/countAgents.keySet().size(),300);
		}
	}
	
	public void countPairedAgent(ConcurrentMap<Agent, AtomicInteger> countAgents, Agent testAgent){
		for(Game g:builder.games){
			Agent pairedAgent =null;
			if(g.getProposer().equals(testAgent)) pairedAgent =g.getResponder();
			if(g.getResponder().equals(testAgent)) pairedAgent =g.getProposer();
			if (pairedAgent != null) {
				countAgents.putIfAbsent(pairedAgent, new AtomicInteger(0));
				countAgents.get(pairedAgent).incrementAndGet();
			}
		}
	}

}
