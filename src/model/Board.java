/**
 * Geister v3.0
 *
 * Copyright (c) 2016 tatsumi
 *
 * This software is released under the MIT License.
 * https://github.com/GeneralRegister/Geister/blob/master/LICENSE
 */
package model;


import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Board {
	private Ghost[] enemyGhosts;
	private Ghost[] friendGhosts;
	private Ghost[][] board;
	private int height = 6;
	private int width = 6;

	/*
	 * TODO ゴールにゴーストを配置するのではなく，ゴール仕方していないかだけをtrue, falseで判定する
	 *
	 * つまり，ゴールにいるかいないか
	 */
	private Ghost friendGoal;
	private Ghost enemyGoal;

	private int deadEnemyBlue;
	private int deadEnemyRed;


	public Board() {
		initRandom();
		board = new Ghost[height][width];

		for (Ghost ghost : enemyGhosts)
			board[ghost.getY()][ghost.getX()] = ghost;
		for (Ghost ghost : friendGhosts)
			board[ghost.getY()][ghost.getX()] = ghost;

		friendGoal = null;
		enemyGoal = null;
		deadEnemyBlue = 0;
		deadEnemyRed = 0;
	}


	private void initRandom() {
		/*
		 * TODO テスト用...
		 */
		enemyGhosts = new Ghost[8];
		friendGhosts = new Ghost[8];
		int num = 0;
		for (int y = 0; y <= 1; y++)
			for (int x = 1; x <= 4; x++)
				enemyGhosts[num] = new Ghost(false, num++, x, y);
		num = 0;

		List<Soul> soul = new ArrayList<Soul>();
		for (int i = 0; i < 4; i++)
			soul.add(Soul.blue);
		for (int i = 0; i < 4; i++)
			soul.add(Soul.red);
		Collections.shuffle(soul);

		for (int y = 4; y < 6; y++)
			for (int x = 1; x <= 4; x++)
				friendGhosts[num] = new Ghost(true, num, x, y, soul.get(num++));
	}


	/**
	 * ゴーストが動けるかどうかを判定する．動ける場合，trueを返す．
	 *
	 * @param isFriend 敵・味方
	 * @param id ゴースト番号
	 * @return ゴーストが動ける場合，true
	 */
	public boolean canMove(boolean isFriend, int id) {
		for (Direction dir : Direction.values())
			if (canMove(isFriend, id, dir))
				return true;
		return false;
	}


	/**
	 * ゴーストが指定の方向に動けるかどうかを判定する．動ける場合，trueを返す．
	 *
	 * @param isFriend 敵・味方
	 * @param id ゴースト番号
	 * @param dir 方向
	 * @return ゴーストが動ける場合，true
	 */
	public boolean canMove(boolean isFriend, int id, Direction dir) {
		Ghost ghost = getGhost(isFriend, id);
		if (ghost == null)
			return false;
		return canOccupy(isFriend, ghost.getX() + dir.x(), ghost.getY() + dir.y());
	}


	/**
	 * ゴーストが盤面を占領できるかを判定する．占領できる場合，trueを返す．
	 *
	 * @param isFriend 敵・味方
	 * @param x x座標
	 * @param y y座標
	 * @return ゴーストが占領できる場合，true
	 */
	public boolean canOccupy(boolean isFriend, int x, int y) {
		if (isOnScreen(x, y))
			return getGhost(x, y) == null || getGhost(x, y).isFriend() != isFriend;
		return false;
	}


	/**
	 * 消滅した味方の指定の色のゴーストを返す．
	 *
	 * @param isBlue ゴーストの色
	 * @return 消滅した味方の指定の色のゴーストの数
	 */
	public int cntDeadFriend(boolean isBlue) {
		if (isBlue)
			return cntDeadFriendBlue();
		else
			return cntDeadFriendRed();
	}


	/**
	 * 消滅した味方の青いゴーストを返す．
	 *
	 * @return 消滅した味方の青いゴーストの数
	 */
	public int cntDeadFriendBlue() {
		int cnt = 0;
		for (int i = 0; i < 8; i++)
			if (friendGhosts[i].isDead() && friendGhosts[i].isBlue())
				cnt++;
		return cnt;
	}


	/**
	 * 消滅した味方の赤いゴーストを返す．
	 *
	 * @return 消滅した味方の赤いゴーストの数
	 */
	public int cntDeadFriendRed() {
		int cnt = 0;
		for (int i = 0; i < 8; i++)
			if (friendGhosts[i].isDead() && friendGhosts[i].isRed())
				cnt++;
		return cnt;
	}


	/**
	 * 消滅した敵の指定の色のゴーストの数を返す．
	 *
	 * @param isBlue ゴーストの色
	 * @return 消滅した敵の指定の色のゴーストの数
	 */
	public int getDeadEnemy(boolean isBlue) {
		if (isBlue)
			return getDeadEnemyBlue();
		else
			return getDeadEnemyRed();
	}


	/**
	 * 消滅した敵の青いゴーストの数を返す．
	 *
	 * @return 消滅した敵の青いゴーストの数
	 */
	public int getDeadEnemyBlue() {
		return deadEnemyBlue;
	}


	/**
	 * 消滅した敵の赤いゴーストの数を返す．
	 *
	 * @return 消滅した敵の赤いゴーストの数
	 */
	public int getDeadEnemyRed() {
		return deadEnemyRed;
	}


	/**
	 * 盤面にあるゴーストを取り出す．盤面外またはゴーストが存在しない場合，nullを返す．
	 *
	 * @param x x座標
	 * @param y y座標
	 * @return ゴースト
	 */
	public Ghost getGhost(int x, int y) {
		if (isOnScreen(x, y))
			return board[y][x];
		return null;
	}


	/**
	 * 指定したゴーストを取り出す．idが不適切な場合，nullを返す．
	 *
	 * @param isFriend 敵・味方
	 * @param id ゴースト番号
	 * @return ゴースト
	 */
	public Ghost getGhost(boolean isFriend, int id) {
		if (id < 0 || id >= 8)
			return null;
		if (isFriend)
			return friendGhosts[id];
		else
			return enemyGhosts[id];
	}


	/**
	 * 盤面の高さを返す．
	 *
	 * @return 盤面の高さ
	 */
	public int getHeight() {
		return height;
	}


	public int getId(Ghost ghost) {
		return ghost.getId();
	}


	/**
	 * 指定のゴーストの位置を返す．ただし，ゴーストが存在しない場合，(-1, -1)を返す．
	 *
	 * @param isFriend 敵・味方
	 * @param id ゴースト番号
	 * @return ゴーストの位置
	 */
	public Point getPosition(boolean isFriend, int id) {
		Ghost ghost = getGhost(isFriend, id);
		if (ghost == null)
			return new Point(-1, -1);
		return new Point(ghost.getX(), ghost.getY());
	}


	/**
	 * 盤面の幅を返す．
	 *
	 * @return 盤面の幅
	 */
	public int getWidth() {
		return width;
	}


	/**
	 * 指定した盤面に敵のゴーストがいるか判定する．敵のゴーストがいる場合，trueを返す．
	 *
	 * @param x x座標
	 * @param y y座標
	 * @return 敵のゴーストがいる場合，true
	 */
	public boolean isEnemy(int x, int y) {
		Ghost ghost = getGhost(x, y);
		if (ghost == null)
			return false;
		return ghost.isFriend() == false;
	}


	/**
	 * 指定した座標が敵のゴールであるか判定する．敵のゴールである場合，trueを返す．
	 *
	 * @param x x座標
	 * @param y y座標
	 * @return 指定の座標が敵のゴールである場合，true
	 */
	public boolean isEnemyGoal(int x, int y) {
		if (x == -1 && y == getHeight() - 1)
			return true;
		if (x == getWidth() && y == getHeight() - 1)
			return true;
		return false;
	}


	/**
	 * 指定した座標が出口であるか判定する．出口である場合，trueを返す．
	 *
	 * @param x x座標
	 * @param y y座標
	 * @return 指定の座標が出口である場合，true
	 */
	public boolean isExit(int x, int y) {
		if (x == 0 && y == 0)
			return true;
		if (x == getWidth() - 1 && y == 0)
			return true;
		if (x == 0 && y == getHeight() - 1)
			return true;
		if (x == getWidth() - 1 && y == getHeight() - 1)
			return true;
		return false;
	}


	/**
	 * 指定した盤面に味方のゴーストがいるか判定する．味方のゴーストがいる場合，trueを返す．
	 *
	 * @param x x座標
	 * @param y y座標
	 * @return 味方のゴーストがいる場合，true
	 */
	public boolean isFriend(int x, int y) {
		Ghost ghost = getGhost(x, y);
		if (ghost == null)
			return false;
		return ghost.isFriend() == true;
	}


	/**
	 * 指定した座標が味方のゴールであるか判定する．味方のゴールである場合，trueを返す．
	 *
	 * @param x x座標
	 * @param y y座標
	 * @return 指定の座標が味方のゴールである場合，true
	 */
	public boolean isFriendGoal(int x, int y) {
		if (x == -1 && y == 0)
			return true;
		if (x == getWidth() && y == 0)
			return true;
		return false;
	}


	/**
	 * 指定した座標がゴールであるか判定する．ゴールである場合，trueを返す．
	 *
	 * @param x x座標
	 * @param y y座標
	 * @return 指定の座標がゴールである場合，true
	 */
	public boolean isGoal(int x, int y) {
		return isFriendGoal(x, y) || isEnemyGoal(x, y);
	}


	/**
	 * 自分が負けであるか判定する．負けの場合，trueを返す．
	 *
	 * @return 負けの場合，true
	 */
	public boolean isLoss() {
		// 勝ち条件1...自分の「良いオバケ」が全部取られる
		if (cntDeadFriendBlue() == 4)
			return true;

		// 勝ち条件2...相手の「悪いオバケ」を全部相手に取る
		if (deadEnemyRed == 4)
			return true;

		// 勝ち条件3...相手の「良いオバケ」のひとつが自分側脱出口から外に出る
		if (enemyGoal != null)
			return true;

		return false;
	}


	/**
	 * 盤面内であるか判定します．盤面内の場合，trueを返す．
	 *
	 * @param x x座標
	 * @param y y座標
	 * @return 盤面内の場合，true
	 */
	public boolean isOnScreen(int x, int y) {
		return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
	}


	/**
	 * 自分が勝ちであるか判定する．勝ちの場合，trueを返す．
	 *
	 * @return 勝ちの場合，true
	 */
	public boolean isWin() {
		// 勝ち条件1...相手の「良いオバケ」を全部取る
		if (deadEnemyBlue == 4)
			return true;

		// 勝ち条件2...自分の「悪いオバケ」が全部相手に取られる
		if (cntDeadFriendRed() == 4)
			return true;

		// 勝ち条件3...自分の「良いオバケ」のひとつを相手側脱出口から外に出す
		if (friendGoal != null)
			return true;

		return false;
	}


	/**
	 * 指定のゴーストで指定の方向の占領できる場合，占領する．占領出来た場合，trueを返す．
	 *
	 * @param isFriend 敵・味方
	 * @param id ゴースト番号
	 * @param dir 方向
	 * @return 占領出来た場合，true
	 */
	public boolean move(boolean isFriend, int id, Direction dir) {
		return move(getGhost(isFriend, id), dir);
	}


	/**
	 * 指定のゴーストで指定の方向に移動する．条件によって，出口へ移動できる．移動出来た場合，trueを返す．
	 *
	 * @param ghost ゴースト
	 * @param dir 方向
	 * @return 移動出来た場合，true
	 */
	public boolean move(Ghost ghost, Direction dir) {
		if (occupyCell(ghost, ghost.getX() + dir.x(), ghost.getY() + dir.y()))
			return true;

		if (occupyGoal(ghost, ghost.getX() + dir.x(), ghost.getY() + dir.y()))
			return true;

		return false;
	}


	/**
	 * ゴーストで指定の盤面を占領できる場合，占領し，元いた場所はnullに設定する．盤面を占領した場合，trueを返す．
	 *
	 * @param ghost ゴースト
	 * @param x x座標
	 * @param y y座標
	 * @return 盤面を占領した場合，true
	 */
	public boolean occupyCell(Ghost ghost, int x, int y) {
		if (!isOnScreen(x, y))
			return false;

		if (ghost == null)
			return false;

		if (getGhost(x, y) == null) {
			board[y][x] = ghost;
			board[ghost.getY()][ghost.getX()] = null;
			ghost.setPosition(x, y);
			return true;
		} else {
			if (getGhost(x, y).isFriend() != ghost.isFriend()) {
				getGhost(x, y).disappear();
				board[y][x] = ghost;
				board[ghost.getY()][ghost.getX()] = null;
				ghost.setPosition(x, y);
				return true;
			}
		}

		return false;
	}


	/**
	 * ゴーストでゴールを占領できる場合，占領し，元いた場所はnullに設定する．ゴールを占領した場合，trueを返す．
	 *
	 * @param ghost ゴースト
	 * @param x x座標
	 * @param y y座標
	 * @return ゴールを占領した場合，true
	 */
	public boolean occupyGoal(Ghost ghost, int x, int y) {
		if (ghost == null)
			return false;

		if (ghost.isFriend()) {
			if (ghost.isBlue()) {
				if (isFriendGoal(x, y)) {
					friendGoal = ghost;
					board[ghost.getY()][ghost.getX()] = null;
					ghost.setPosition(-1, -1);
					return true;
				}
			}
		} else {
			enemyGoal = ghost;
			board[ghost.getY()][ghost.getX()] = null;
			ghost.setPosition(-1, -1);
			return true;
		}

		return false;
	}


	/**
	 * 消滅した敵の指定の色のゴーストの数を設定する．
	 *
	 * @param deadEnemyBlue 消滅した敵の指定の色のゴーストの数
	 */
	public void setDeadEnemy(boolean isBlue, int deadEnemy) {
		if (isBlue)
			setDeadEnemyBlue(deadEnemy);
		else
			setDeadEnemyRed(deadEnemy);
	}


	/**
	 * 消滅した敵の青いゴーストの数を設定する．
	 *
	 * @param deadEnemyBlue 消滅した敵の青いゴーストの数
	 */
	public void setDeadEnemyBlue(int deadEnemyBlue) {
		this.deadEnemyBlue = deadEnemyBlue;
	}


	/**
	 * 消滅した敵の赤いゴーストの数を設定する．
	 *
	 * @param deadEnemyRed 消滅した敵の赤いゴーストの数
	 */
	public void setDeadEnemyRed(int deadEnemyRed) {
		this.deadEnemyRed = deadEnemyRed;
	}
}
