/**
 * Geister v3.0
 *
 * Copyright (c) 2016 tatsumi
 *
 * This software is released under the MIT License.
 * https://github.com/GeneralRegister/Geister/blob/master/LICENSE
 */
package ai;


import java.util.ArrayList;
import java.util.Random;

import model.Board;
import model.Direction;


public class AI {
	private Board board;


	public AI(Board board) {
		this.board = board;
	}


	public Hand nextHand() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return simpleNextHand();
	}


	public Hand simpleNextHand() {
		ArrayList<Hand> hands = simpleNextHandList();
		if (hands.size() == 0)
			return null;
		return hands.get(new Random().nextInt(hands.size()));
	}


	public ArrayList<Hand> simpleNextHandList() {
		ArrayList<Hand> hands = new ArrayList<Hand>();

		for (int id = 0; id < 8; id++)
			for (Direction dir : Direction.values())
				if (board.canMove(true, id, dir))
					hands.add(new Hand(board.getPosition(true, id), dir));

		return hands;
	}
}
