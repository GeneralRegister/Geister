/**
 * Geister v3.0
 *
 * Copyright (c) 2016 tatsumi
 *
 * This software is released under the MIT License.
 * https://github.com/GeneralRegister/Geister/blob/master/LICENSE
 */
package model;


public class Ghost {
	private boolean isFriend;
	/**
	 * 存在を表す．盤面上に存在する場合，true．
	 */
	private boolean isAlive;

	/**
	 * x座標を表す．盤面上に存在しない場合，-1を設定する．
	 */
	private int x;

	/**
	 * y座標を表す．盤面上に存在しない場合，-1を設定する．
	 */
	private int y;

	/**
	 * 色を表す．
	 *
	 * 味方のゴーストを生成する時は，Soul.blue（青）又はSoul.red（赤）に設定する．
	 *
	 * 敵のゴーストを生成する時は，soulをSoul.unknown（不明）に設定する．
	 * さらに，敵のゴーストが死んだ時は，敵側が管理していたそのゴーストのsoulを再設定する．
	 */
	private Soul soul;

	private int id;


	public Ghost(boolean isFriend, int id) {
		this(isFriend, id, -1, -1);
	}


	public Ghost(boolean isFriend, int id, int x, int y) {
		this(isFriend, id, x, y, Soul.unknown);
	}


	public Ghost(boolean isFriend, int id, Soul soul) {
		this(isFriend, id, -1, -1, soul);
	}


	public Ghost(boolean isFriend, int id, int x, int y, Soul soul) {
		this.isFriend = isFriend;
		isAlive = true;
		setX(x);
		setY(y);
		this.soul = soul;
		this.id = id;
	}


	public void disappear() {
		isAlive = false;
		setX(-1);
		setY(-1);
	}


	public int getX() {
		return x;
	}


	public int getY() {
		return y;
	}


	public boolean isAlive() {
		return isAlive;
	}


	public boolean isBlue() {
		return soul == Soul.blue;
	}


	public boolean isDead() {
		return !isAlive;
	}


	public boolean isEnemy() {
		return !isFriend;
	}


	public boolean isFriend() {
		return isFriend;
	}


	public boolean isRed() {
		return soul == Soul.red;
	}


	public void setPosition(int x, int y) {
		setX(x);
		setY(y);
	}


	public int getId() {
		return id;
	}


	public void setX(int x) {
		this.x = x;
	}


	public void setY(int y) {
		this.y = y;
	}


	public String toString() {
		if (isFriend)
			return "ghost: friend, (" + x + ", " + y + ")";
		else
			return "ghost: enemy, (" + x + ", " + y + ")";
	}
}
