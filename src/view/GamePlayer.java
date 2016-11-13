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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import controller.GameController;
import model.Direction;


public class GamePlayer extends GamePanel implements MouseListener {
	private GameController game;

	private int cellSize = 100;
	private int border = 2;
	private int outerCellSize = cellSize + border * 2;


	public GamePlayer() {
		super();
		game = new GameController(this);
		super.init(game.getBoard(), cellSize, border);
		addMouseListener(this);

		game.start(new Random().nextBoolean());
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
		game.move(x - 1, y, dir);
	}


	public void select(Point p) {
		/*
		 * 味方のゴーストを選択・解除する
		 */
		if (game.getBoard().isFriend(p.x - 1, p.y)) {
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


	/*
	 * (非 Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(javafx.scene.input.MouseEvent event) {
		Point mp = new Point((int) event.getX(), (int) event.getY());
		select(new Point(mp.x / outerCellSize, mp.y / outerCellSize));
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// 遅い
	}


	@Override
	public void mousePressed(MouseEvent e) {
		Point mp = new Point((int) e.getX(), (int) e.getY());
		select(new Point(mp.x / outerCellSize, mp.y / outerCellSize));
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}
}
