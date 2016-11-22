package ai;


import java.awt.Point;

import model.Board;
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


	/**
	 * マンハッタン距離を返す．
	 *
	 * @param p1 座標 1
	 * @param p2 座標 2
	 * @return 座標 1 と座標 2 のマンハッタン距離
	 */
	public double d1(Point p1, Point p2) {
		return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
	}


	public void evaluate(Board origin) {
		Board copy = origin.clone();
		copy.move(this);
		this.eValue = evalueDiffense(copy) + evalueOffense(copy);
	}


	public double evalueDiffense(Board copy) {
		double ev = 0;
		double u = 1;
		for (int id = 0; id < 8; id++) {
			Ghost ghost = copy.getGhost(true, id);
			if (ghost.isAlive()) {
				double c = d1(new Point(-1, 6), ghost.getPosition());
				double d = d1(new Point(6, 6), ghost.getPosition());
				double v = 1 / Math.min(c, d);
				for (Direction dir : Direction.values())
					if (copy.isEnemy(ghost.getX() + dir.x(), ghost.getY() + dir.y()))
						v *= u;
				ev += v;
			}
		}
		return ev;
	}


	public double evalueOffense(Board copy) {
		double ev = 0;
		double u = 1;
		for (int id = 0; id < 8; id++) {
			Ghost ghost = copy.getGhost(true, id);
			if (ghost.isAlive()) {
				double a = d1(new Point(-1, -1), ghost.getPosition());
				double b = d1(new Point(6, -1), ghost.getPosition());
				double v = 1 / Math.min(a, b);
				for (Direction dir : Direction.values())
					if (copy.isEnemy(ghost.getX() + dir.x(), ghost.getY() + dir.y()))
						v *= u;
				ev += v;
			}
		}
		return ev;
	}


	public double getEValue() {
		return eValue;
	}


	public Ghost getGhost() {
		return ghost;
	}


	public void setEValue(double eValue) {
		this.eValue = eValue;
	}
}
