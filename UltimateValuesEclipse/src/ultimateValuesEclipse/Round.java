package ultimateValuesEclipse;

import java.util.ArrayList;
import java.util.OptionalDouble;

public class Round {
	ArrayList<Game> games;
	int ID;
	
	public Round(int ID){
		this.ID = ID;
		games=new ArrayList<Game>();
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

	public double getAvarageOffer() {
		OptionalDouble average = games
	            .stream()
	            .mapToInt(a -> a.getOffer())
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
