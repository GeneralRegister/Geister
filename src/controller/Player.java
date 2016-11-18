package controller;


public abstract class Player {
	private GameController game;
	private int id;


	public Player(GameController game, int id) {
		this.setGame(game);
		this.setId(id);
	}


	public GameController getGame() {
		return game;
	}


	public int getId() {
		return id;
	}


	public abstract void nextHand();


	public void setGame(GameController game) {
		this.game = game;
	}


	public void setId(int id) {
		this.id = id;
	}
}
