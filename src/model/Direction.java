package model;


/**
 * @since Geister 1.0.0
 * @author tatsumi
 */
public enum Direction {
	up(0, -1),
	right(1, 0),
	down(0, 1),
	left(-1, 0),
	center(0, 0),
	;

	private int x;
	private int y;


	private Direction(int x, int y) {
		this.x = x;
		this.y = y;
	}


	public int x() {
		return x;
	}


	public int y() {
		return y;
	}


	public Direction reverse() {
		switch (this) {
		case up:
			return down;
		case right:
			return left;
		case down:
			return up;
		case left:
			return right;
		default:
			return center;
		}
	}
}
