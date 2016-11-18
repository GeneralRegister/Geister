package controller;


import java.awt.Point;

import model.Direction;
import model.Hand;


public class HumanPlayer extends Player {
	private boolean ready;


	public HumanPlayer(GameController game, int id) {
		super(game, id);
		ready = false;
	}


	public boolean getReady() {
		return ready;
	}


	public void move(int x, int y, Direction dir) {
		if (getReady()) {
			setReady(false);
			getGame().move(getId(), new Hand(new Point(x, y), dir));
			getGame().next();
		}
	}


	@Override
	public void nextHand() {
		setReady(true);
	}


	public void setReady(boolean ready) {
		this.ready = ready;
	}
}