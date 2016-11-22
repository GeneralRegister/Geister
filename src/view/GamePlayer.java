/**
 * Geister v3.0
 *
 * Copyright (c) 2016 tatsumi
 *
 * This software is released under the MIT License.
 * https://github.com/GeneralRegister/Geister/blob/master/LICENSE
 */
package view;

import java.awt.Point;

import ai.AI;
import application.Display;
import controller.AIPlayer;
import controller.GameController;
import controller.HumanPlayer;
import javafx.scene.input.MouseEvent;
import model.Direction;


public class GamePlayer extends GamePanel implements Runnable {
	private GameController game;

	private int cellSize = 96;
	private int border = 2;
	private int outerCellSize = cellSize + border * 2;

	private Display display;

	private HumanPlayer human;
	private AIPlayer ai0;
	private AIPlayer ai1;

	private boolean humanVsAi = true;


	public GamePlayer() {
		super();
		game = new GameController();
		game.setPanel(this);

		//プレイヤーを２つ用意
		if (humanVsAi) {
			human = new HumanPlayer(game, 0);
			ai1 = new AIPlayer(game, 1, new AI(game.getBoard(1)));
			game.setPlayer(0, human);
			game.setPlayer(1, ai1);
		} else {
			ai0 = new AIPlayer(game, 0, new AI(game.getBoard(0)));
			ai1 = new AIPlayer(game, 1, new AI(game.getBoard(1)));
			game.setPlayer(0, ai0);
			game.setPlayer(1, ai1);
		}

		super.init(game.getBoard(0), cellSize, border);
	}


	public void setDisplay(Display display) {
		this.display = display;
	}


	/*
	 * ディスプレイ関係
	 */
	public void wait(boolean isWaiting) {
		display.wait(isWaiting);
	}


	public void addMsg(String msg) {
		display.addMsg(msg);
	}


	public void setGhostCnt(int index, boolean isBlue, int num) {
		display.setGhostCnt(index, isBlue, num);
	}


	public void start() {
		new Thread(this).start();
	}


	private void selectSelect(Point p) {
		if (p != null) {
			p.setLocation(p.x, p.y);
			if (super.getSelect().equals(p))
				super.setSelect(new Point(-1, -1));
			else
				super.setSelect(p);
		}
	}


	public void selectAction(Direction dir) {
		int x = super.getSelect().x;
		int y = super.getSelect().y;
		super.setSelect(new Point(-1, -1));
		//game.move(x - 1, y, dir);
		human.move(x - 1, y, dir);
	}


	public void select(Point p) {
		/*
		 * 味方のゴーストを選択・解除する
		 */
		if (game.getBoard(0).isFriend(p.x - 1, p.y)) {
			selectSelect(p);
			repaint();
		}

		/*
		 * 味方のゴーストが選択されている場合，その四近傍を選択すると，移動メソッドを呼ぶ．
		 */
		Point select = super.getSelect();
		if (!select.equals(new Point(-1, -1))) {
			if (p.equals(new Point(select.x, select.y - 1)))
				selectAction(Direction.up);
			if (p.equals(new Point(select.x + 1, select.y)))
				selectAction(Direction.right);
			if (p.equals(new Point(select.x, select.y + 1)))
				selectAction(Direction.down);
			if (p.equals(new Point(select.x - 1, select.y)))
				selectAction(Direction.left);
		}
	}


	public void mousePressed(MouseEvent event) {
		if (humanVsAi)
			select(new Point((int) event.getX() / outerCellSize, (int) event.getY() / outerCellSize));
	}


	@Override
	public void run() {
		game.start();
	}
}
