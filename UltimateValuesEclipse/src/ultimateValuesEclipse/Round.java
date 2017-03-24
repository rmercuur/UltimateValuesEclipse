package ultimateValuesEclipse;

import java.util.ArrayList;
import java.util.OptionalDouble;

public class Round {
	ArrayList<Game> games;
	AbstractBuilder main;
	int ID;
	
	
	public Round(int ID, AbstractBuilder main){
		this.ID = ID;
		games=new ArrayList<Game>();
		this.main = main;
	}
	
	public void addGame(Game game){
		games.add(game);
	}
	
	public double getAvarageResult(){
		OptionalDouble average = games
	            .stream()
	            .mapToInt(a -> a.getOutcome())
	            .average();
		return average.orElse(-1); //gives -1 back if there is an error somewhere
	}

	public double getAvarageDemand() {
		OptionalDouble average = games
	            .stream()
	            .mapToInt(a -> a.getDemand())
	            .average();
		return average.orElse(-1); //gives -1 back if there is an error somewhere
	}

	public double getPercentageAccepted() {
		OptionalDouble average = games
	            .stream()
	            .mapToDouble(a -> a.isAccepted()? 1:0)
	            .average();
		return average.orElse(-1);
	}
	
}
