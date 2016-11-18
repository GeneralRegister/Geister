package controller;


import ai.AI;


public class AIPlayer extends Player implements Runnable {
	private AI ai;


	public AIPlayer(GameController game, int id, AI ai) {
		super(game, id);
		this.ai = ai;
	}


	public AI getAI() {
		return ai;
	}


	@Override
	public void nextHand() {
		new Thread(this).start();
	}


	@Override
	public void run() {
		getGame().move(getId(), ai.nextHand());
		getGame().next();
	}


	public void setAI(AI ai) {
		this.ai = ai;
	}
}
