package controller;

import ai.AI;
import ai.Hand;


public class Player implements Runnable {
	private GameController game;
	private AI ai;
	private int id;


	public Player(GameController game, int id) {
		this.game = game;
		this.id = id;
		ai = new AI(game.getBoard(id));
	}


	public void nextHand() {
		new Thread(this).start();
	}


	@Override
	public void run() {
		// 手を入力して...

		Hand hand;
		//hand = new Hand(new Point(), Direction.center);

		hand = ai.nextHand();

		System.out.println("AI[" + id + "]");

		game.move(id, hand);

		//game.move(id, hand);
	}
}
