package ai;


import model.Direction;
import model.Ghost;
import model.Hand;


public class AIHand extends Hand implements Comparable<AIHand> {
	private Ghost ghost;
	private double eValue;


	public AIHand(Ghost ghost, Direction dir) {
		super(ghost.getPosition(), dir);
		this.ghost = ghost;
		eValue = 0;
	}


	@Override
	public int compareTo(AIHand o) {
		return Double.compare(this.eValue, o.eValue);
	}


	public void evaluate() {
		// TODO
	}


	public Ghost getGhost() {
		return ghost;
	}


	public void setEValue(double eValue) {
		this.eValue = eValue;
	}
}
