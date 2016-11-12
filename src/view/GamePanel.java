package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

import controller.GameController;
import display.Display;
import model.Board;
import model.Direction;


/**
 * @since Geister 1.0.0
 * @author tatsumi
 */
public class GamePanel extends JPanel {
	/*
	 * 1. テクスチャのファイル場所を指定して下さい。
	 *
	 * 例：ghost.png
	 *
	 * （ファイルはviewパッケージ直下に置いてください）
	 */
	String file_ghost = "ghost.png";
	String file_ghostBlue = "ghostBlue.png";
	String file_ghostRed = "ghostRed.png";
	String file_cell = "cell.png";
	String file_exitRight = "exit.png";
	String file_exitLeft = "exit.png";

	/*
	 * 2. グリッドの色を指定して下さい。
	 */
	private final static Color COLOR_GRID = new Color(50, 50, 50);

	/*
	 * 3. ステージテクスチャが無い場合，デフォルトの背景色を指定して下さい。
	 */
	private final static Color COLOR_DEFAULT = new Color(0, 0, 0);

	/*
	 * 以下，変更する必要はありません。
	 */
	private Texture ghost = new Texture(file_ghost, CELL, CELL);
	private Texture ghostBlue = new Texture(file_ghostBlue, CELL, CELL);
	private Texture ghostRed = new Texture(file_ghostRed, CELL, CELL);
	private Texture cell = new Texture(file_cell, CELL, CELL);
	private Texture exitRight = new Texture(file_exitRight, CELL, CELL);
	private Texture exitLeft = new Texture(file_exitLeft, CELL, CELL);

	private final static int HEIGHT = 6;
	private final static int WIDTH = 6 + 2;
	private final static int CELL = 100;
	private final static int BORDER = 2;
	private final static int OUTER_CELL = CELL + BORDER * 2;

	public final static Color COLOR_GOAL = new Color(130, 130, 130); //盤面外の色
	public final static Color COLOR_SELECT = new Color(0, 155, 119);

	private GameController game;
	private Point Select = new Point(-1, -1);

	private Display display;


	public GamePanel(GameController game) {
		setPreferredSize(new Dimension(WIDTH * OUTER_CELL, HEIGHT * OUTER_CELL));
		setFocusable(true);

		this.game = game;
		game.setPanel(this);

		repaint();
	}


	public void wait(boolean isWaiting) {
		display.wait(isWaiting);
	}


	public void addMsg(String msg) {
		display.addMsg(msg);
	}


	public void setGhostCnt(boolean isBlue, int num) {
		display.setGhostCnt(isBlue, num);
	}


	public void start(boolean isInitiative) {
		repaint();
		game.start(isInitiative);
	}


	public void repaint() {
		super.repaint();
	}


	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.draw(g);
	}


	/**
	 * 表示用です．
	 *
	 * @param g
	 */
	public void draw(Graphics g) {
		Board board = game.getBoard();

		g.setColor(COLOR_GRID);
		g.fillRect(0, 0, WIDTH * OUTER_CELL, HEIGHT * OUTER_CELL);

		for (int y = 0; y < HEIGHT; y++)
			for (int x = 0; x < WIDTH; x++) {
				if (Select.x == x && Select.y == y) {
					g.setColor(COLOR_SELECT);
					g.fillRect(x * OUTER_CELL, y * OUTER_CELL, OUTER_CELL, OUTER_CELL);
				}

				if (board.isOnScreen(x - 1, y)) {
					if (board.isExit(x - 1, y)) {
						/*
						 * 出口
						 */
						g.drawImage(cell.getImage(), x * OUTER_CELL + BORDER, y * OUTER_CELL + BORDER, null);
						//g.setColor(COLOR_BOARD);
						//g.fillRect(x * CELL + WIDTH_BORDER, y * CELL + WIDTH_BORDER, INNER_CELL, INNER_CELL);
					} else {
						/*
						 * 盤面
						 */
						g.drawImage(exitRight.getImage(), x * OUTER_CELL + BORDER, y * OUTER_CELL + BORDER, null);
					}

					if (board.isEnemy(x - 1, y)) {
						/*
						 * 敵のゴースト
						 */
						g.drawImage(ghost.getImage(), x * OUTER_CELL + BORDER, y * OUTER_CELL + BORDER, null);
						//g.setColor(Color.WHITE);
						//g.fillRect(x * CELL + WIDTH_BORDER, y * CELL + WIDTH_BORDER, INNER_CELL, INNER_CELL);
					} else if (board.isFriend(x - 1, y)) {
						if (board.getGhost(x - 1, y).isBlue()) {
							/*
							 * 味方のゴースト（青）
							 */
							g.drawImage(ghostBlue.getImage(), x * OUTER_CELL + BORDER, y * OUTER_CELL + BORDER, null);
							//g.setColor(Color.BLUE);
							//g.fillRect(x * CELL + WIDTH_BORDER, y * CELL + WIDTH_BORDER, INNER_CELL, INNER_CELL);
						} else {
							/*
							 * 味方のゴースト（赤）
							 */
							g.drawImage(ghostRed.getImage(), x * OUTER_CELL + BORDER, y * OUTER_CELL + BORDER, null);
							//g.setColor(Color.RED);
							//g.fillRect(x * CELL + WIDTH_BORDER, y * CELL + WIDTH_BORDER, INNER_CELL, INNER_CELL);
						}
					}
				} else if (board.isGoal(x - 1, y)) {
					/*
					 * 盤面外のゴール（出口の隣）
					 */
					g.setColor(COLOR_GOAL);
					g.fillRect(x * OUTER_CELL + BORDER, y * OUTER_CELL + BORDER, BORDER, BORDER);
				} else {
					/*
					 * ただの盤面外
					 */
					g.setColor(COLOR_DEFAULT);
					g.fillRect(x * OUTER_CELL + BORDER, y * OUTER_CELL + BORDER, BORDER, BORDER);
				}
			}
	}


	private void selectSelect(Point p) {
		if (p != null) {
			p.setLocation(p.x / OUTER_CELL, p.y / OUTER_CELL);
			if (Select.equals(p))
				Select.setLocation(-1, -1);
			else
				Select.setLocation(p);
		}
	}


	public void selectAction(Direction dir) {
		int x = Select.x;
		int y = Select.y;
		Select.setLocation(-1, -1);
		game.move(x - 1, y, dir);
	}


	public void mouseClicked(javafx.scene.input.MouseEvent event) {
		Point mp = new Point((int) event.getX(), (int) event.getY());
		Point p = new Point(mp.x / OUTER_CELL, mp.y / OUTER_CELL);

		/*
		 * 味方のゴーストを選択・解除する
		 */
		if (game.getBoard().isFriend(p.x - 1, p.y)) {
			selectSelect(mp);
			repaint();
		}

		/*
		 * 味方のゴーストが選択されている場合，その四近傍を選択すると，移動メソッドを呼ぶ．
		 */
		if (!Select.equals(new Point(-1, -1))) {
			if (p.equals(new Point(Select.x, Select.y - 1)))
				selectAction(Direction.up);
			if (p.equals(new Point(Select.x + 1, Select.y)))
				selectAction(Direction.right);
			if (p.equals(new Point(Select.x, Select.y + 1)))
				selectAction(Direction.down);
			if (p.equals(new Point(Select.x - 1, Select.y)))
				selectAction(Direction.left);
		}
	}


	public void setDisplay(Display display) {
		this.display = display;
	}
}
