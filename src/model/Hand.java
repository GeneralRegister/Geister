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


public class Hand {
	private Point p;
	private Direction dir;


	public Hand(Point p, Direction dir) {
		this.p = new Point(p);
		this.dir = dir;
	}


	public Point getPosition() {
		return p;
	}


	public int getX() {
		return getPosition().x;
	}


	public int getY() {
		return getPosition().y;
	}


	public Direction getDirection() {
		return dir;
	}


	/**
	 * 着手を反転させる．自分から見た着手を，相手から見た着手に反転させる．
	 *
	 * （※Geister の盤面は 6 * 6 です．）
	 *
	 * @return
	 */
	public Hand reverse() {
		return new Hand(new Point(5 - p.x, 5 - p.y), dir.reverse());
	}


	public String toString() {
		return "[" + p.x + ", " + p.y + "], " + dir;
	}
}
