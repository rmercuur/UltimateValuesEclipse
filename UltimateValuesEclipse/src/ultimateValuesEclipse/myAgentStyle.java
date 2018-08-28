package ultimateValuesEclipse;

import java.awt.Color;

import agents.Agent;
import repast.simphony.random.RandomHelper;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;

public class myAgentStyle extends DefaultStyleOGL2D {

		

		@Override
		public Color getColor(Object o) {
			Agent agent = (Agent)o;
			return new Color((float)agent.g,(float)agent.r,(float)agent.b);
		}
}
