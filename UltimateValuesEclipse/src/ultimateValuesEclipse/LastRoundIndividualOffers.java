package ultimateValuesEclipse;

import repast.simphony.data2.NonAggregateDataSource;
//Don't know how this works...

public class LastRoundIndividualOffers implements NonAggregateDataSource {
	AbstractBuilder main;
	
	@Override
	public String getId() {
		return "lastRoundIndividualOffers";
	}

	@Override
	public Class<?> getDataType() {
		return Integer.class;
	}

	@Override
	public Class<?> getSourceType() {
		return Game.class;
	}

	@Override
	public Object get(Object obj) {
		return obj;
	}

}
