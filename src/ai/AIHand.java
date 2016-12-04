package ai;


import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import model.Board;
import model.Direction;
import model.Ghost;
import model.Hand;
import model.Soul;


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


	// モンテカルロ法により，勝ち確率を評価
	public void evaluate(Board origin) {
		// シミュレート回数
		int tmax = 1000;
		// 勝ち回数
		int win = 0;
		for (int i = 0; i < tmax; i++) {
			if (simulateRandom(origin.clone(), this)) {
				win++;
			}
		}

		setEValue((double) win / tmax);
	}


	// Board の敵の色をランダムに設定し，ランダムに進行するゲームで最終的に勝てたか返す．ただし，最初の手は Hand である．
	public boolean simulateRandom(Board virtual, Hand hand) {
		// 盤面で未だ消滅していない敵の色をランダムに設定（予測できたら良いな．．．）
		int unkB = 4 - virtual.getDeadEnemyBlue();
		int unkR = 4 - virtual.getDeadEnemyRed();
		ArrayList<Integer> aliveList = new ArrayList<Integer>();
		for (int id = 0; id < 8; id++)
			if (virtual.getGhostEnemy(id).isAlive())
				aliveList.add(id);
		Random rand = new Random();
		for (int i = 0; i < unkB; i++)
			virtual.getGhostEnemy(aliveList.remove(rand.nextInt(aliveList.size()))).setSoul(Soul.blue);
		for (int i = 0; i < unkR; i++)
			virtual.getGhostEnemy(aliveList.remove(rand.nextInt(aliveList.size()))).setSoul(Soul.red);

		// TODO シミュレートせよ
		// 味方の着手 Hand から
		boolean player = true;
		virtual.move(hand);

		while (true) {
			// 勝敗が付いたら終了！！
			if (virtual.isWin())
				return true;
			if (virtual.isLoss())
				return false;

			// プレーヤ交代
			player = !player;
			// 可能着手
			ArrayList<Hand> list = virtual.posNextHandList(player);
			// ランダムな着手
			virtual.move(list.get(rand.nextInt(list.size())));
		}
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


	public String toString() {
		return super.toString() + ", " + String.format("%.4f", getEValue());
	}
}
