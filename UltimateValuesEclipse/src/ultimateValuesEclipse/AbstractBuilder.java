package ultimateValuesEclipse;
import java.util.ArrayList;
import java.util.List;

import agents.Agent;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.PriorityType;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.RandomGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

/**
 * Description of AbstractBuilder.
 * 
 * @author rijk
 */
public abstract class AbstractBuilder implements ContextBuilder<Object> {
	public Context<Object> context;
	public Grid<Object> grid;
	public GridFactory myGridFactory;
	public Parameters params;	
	public ArrayList<Agent> agents;
	public ArrayList<Game>	games; 
	public ArrayList<Round> rounds;
	public DataCollector myDataCollector;
	
	/**
	 * Description of the method build.
	 */
	@Override
	public Context build(Context<Object> context) {
		this.context = context;
		params = RunEnvironment.getInstance().getParameters(); /*retrieves GUI-made parameters*/
		agents=new ArrayList<Agent>();
		games=new ArrayList<Game>();
		rounds=new ArrayList<Round>();
		
		setCFG();
		
		setID();
		makeGrid();
		addAgents();
//		addEnvironment();
		
		//after making agents, should not really matter though...
		myDataCollector=new DataCollector(this);
		context.add(myDataCollector);
		
		/*Schedules a performance context task each timestep.*/
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		ScheduleParameters scheduleparams = ScheduleParameters.createRepeating(1, 1, ScheduleParameters.FIRST_PRIORITY);
		schedule.schedule(scheduleparams, this, "createGames");
		
		
		
		/*Specifies simulation endTime*/
		RunEnvironment.getInstance().endAt(params.getInteger("EndTime"));
		
		return context;
	}
	 
	public abstract void setCFG();
	/**
	 * Description of the method setID.
	 */
	public abstract void setID();
	 
	/**
	 * Description of the method makeGrid.
	 */
	public void makeGrid() {
		myGridFactory = GridFactoryFinder.createGridFactory(null);
		int gameCount = params.getInteger("agentCount")/2;
		int amountOfRounds = params.getInteger("EndTime");
		grid = myGridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(),
				new RandomGridAdder<Object>(),
				true, gameCount, amountOfRounds+2)); //width and height are amount of games and space for 3;
	}	
	 
	/**
	 * Description of the method addAgents.
	 */
	public abstract void addAgents();
	 
	/**
	 * Description of the method addEnvironment.
	 */
	public abstract void addEnvironment();


	public void createGames() {
		games.clear(); //List doesn't become infinite, there are new games every time step.
		int gameCount = params.getInteger("agentCount")/2; //Does it cast to int automaticly?
		int roundID = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		Round newRound =new Round(roundID,this);
		rounds.add(newRound);
		List<Agent> players =new ArrayList<Agent>(agents);
		for(int i =0; i < gameCount; i++){	
			int ID = (int) roundID *100 + i;
			Agent agent1 = players.remove(RandomHelper.nextIntFromTo(0, players.size()-1));
			Agent agent2 = players.remove(RandomHelper.nextIntFromTo(0, players.size()-1));
			Game newGame=new Game(ID,newRound,agent1,agent2);
			agent1.setGame(newGame);
			agent2.setGame(newGame);
			newRound.addGame(newGame);
			games.add(newGame);
			context.add(newGame);
			Helper.moveToObject(grid, agent1, newGame);
			Helper.moveToObject(grid, agent2, newGame);
		}
	}
}
