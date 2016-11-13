/**
 * Geister v3.0
 *
 * Copyright (c) 2016 tatsumi
 *
 * This software is released under the MIT License.
 * https://github.com/GeneralRegister/Geister/blob/master/LICENSE
 */
package ai;


import java.awt.Point;

import model.Direction;


public class Hand {
	private Point p;
	private Direction dir;


	public Hand(Point p, Direction dir) {
		this.p = p;
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
}
