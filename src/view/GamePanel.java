/**
 * Geister v3.0
 *
 * Copyright (c) 2016 tatsumi
 *
 * This software is released under the MIT License.
 * https://github.com/GeneralRegister/Geister/blob/master/LICENSE
 */
package view;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

import model.Board;


public class GamePanel extends JPanel {
	/*
	 * 1. テクスチャの画像ファイル
	 *
	 * 例：ghost.png
	 *
	 * （※ファイルはviewパッケージ直下に置く）
	 */
	String fileGhost = "ghost.png";
	String fileGhostBlue = "ghostBlue.png";
	String fileGhostRed = "ghostRed.png";
	String fileCell = "cell.png";
	String fileExitRight = "exit.png";
	String fileExitLeft = "exit.png";

	/*
	 * 2. テクスチャ
	 */
	private Texture ghost;
	private Texture ghostBlue;
	private Texture ghostRed;
	private Texture cell;
	private Texture exitRight;
	private Texture exitLeft;

	/*
	 * 3. サイズ
	 */
	private int height;
	private int width;
	private int cellSize;
	private int border;
	private int outerCellSize;

	/*
	 * 4. 色
	 */
	private final static Color colorGrid = new Color(50, 50, 50);
	private final static Color colorDefault = new Color(0, 0, 0);
	public final static Color colorGoal = new Color(130, 130, 130);
	public final static Color colorSelect = new Color(0, 155, 119);

	/*
	 * 5. 盤
	 */
	private Board board;
	private Point select;


	public GamePanel() {
		setPreferredSize(new Dimension(600, 600));
	}


	public void init(Board board, int cellSize, int border) {
		this.board = board;
		this.cellSize = cellSize;
		this.border = border;

		height = board.getHeight();
		width = board.getWidth() + 2;
		outerCellSize = cellSize + border * 2;

		setSelect(new Point(-1, -1));
		setPreferredSize(new Dimension(width * outerCellSize, height * outerCellSize));

		ghost = new Texture(fileGhost, cellSize, cellSize);
		ghostBlue = new Texture(fileGhostBlue, cellSize, cellSize);
		ghostRed = new Texture(fileGhostRed, cellSize, cellSize);
		cell = new Texture(fileCell, cellSize, cellSize);
		exitRight = new Texture(fileExitRight, cellSize, cellSize);
		exitLeft = new Texture(fileExitLeft, cellSize, cellSize);
	}


	public Point getSelect() {
		return select;
	}


	public void setSelect(Point select) {
		this.select = select;
	}


	public void repaint() {
		super.repaint();
	}


	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.draw(g);
	}


	public void draw(Graphics g) {
		g.setColor(colorGrid);
		g.fillRect(0, 0, width * outerCellSize, height * outerCellSize);

		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++) {
				if (getSelect().x == x && getSelect().y == y) {
					g.setColor(colorSelect);
					g.fillRect(x * outerCellSize, y * outerCellSize, outerCellSize, outerCellSize);
				}

				if (board.isOnScreen(x - 1, y)) {
					if (board.isExit(x - 1, y)) {
						/*
						 * 出口
						 */
						g.drawImage(exitRight.getImage(), x * outerCellSize + border, y * outerCellSize + border, null);
					} else {
						/*
						 * 盤面
						 */
						g.drawImage(cell.getImage(), x * outerCellSize + border, y * outerCellSize + border, null);
					}

					if (board.isEnemy(x - 1, y)) {
						/*
						 * 敵のゴースト
						 */
						g.drawImage(ghost.getImage(), x * outerCellSize + border, y * outerCellSize + border, null);
					} else if (board.isFriend(x - 1, y)) {
						if (board.getGhost(x - 1, y).isBlue()) {
							/*
							 * 味方のゴースト（青）
							 */
							g.drawImage(ghostBlue.getImage(), x * outerCellSize + border, y * outerCellSize + border,
									null);
						} else {
							/*
							 * 味方のゴースト（赤）
							 */
							g.drawImage(ghostRed.getImage(), x * outerCellSize + border, y * outerCellSize + border,
									null);
						}
					}
				} else if (board.isGoal(x - 1, y)) {
					/*
					 * 出口の隣（ゴール）
					 */
					g.setColor(colorGoal);
					g.fillRect(x * outerCellSize + border, y * outerCellSize + border, border, border);
				} else {
					/*
					 * 盤面の外
					 */
					g.setColor(colorDefault);
					g.fillRect(x * outerCellSize, y * outerCellSize, outerCellSize, outerCellSize);
				}
			}
	}
}
