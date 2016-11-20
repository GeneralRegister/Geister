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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import model.Board;
import model.Direction;
import model.Hand;


public class AI {
	private Board board;


	public AI(Board board) {
		this.board = board;
	}


	/**
	 * 着手可能な手を全て返す．
	 *
	 * @return 着手可能な全ての手
	 */
	public ArrayList<AIHand> canNextHandList() {
		ArrayList<AIHand> hands = new ArrayList<AIHand>();
		for (int id = 0; id < 8; id++)
			for (Direction dir : Direction.values())
				if (board.canMove(true, id, dir))
					hands.add(new AIHand(board.getGhost(true, id), dir));
		return hands;
	}


	/**
	 * マンハッタン距離を返す．
	 *
	 * @param p1
	 * @param p2
	 * @return
	 */
	public double d1(Point p1, Point p2) {
		return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
	}


	/**
	 * 次の着手を返す．
	 *
	 * @return 次の着手
	 */
	public Hand nextHand() {
		//Scanner stdIn = new Scanner(System.in);
		//stdIn.nextInt();

		try {
			Thread.sleep(700);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//return randomNextHand();
		return testNextHand();
	}


	/**
	 * 着手可能な手をランダムに返す．
	 *
	 * @return ランダムな次の着手
	 */
	public Hand randomNextHand() {
		ArrayList<AIHand> hands = canNextHandList();
		if (hands.size() == 0)
			return null;
		return hands.get(new Random().nextInt(hands.size()));
	}


	/*
	 * TODO テスト用です．
	 */
	public Hand testNextHand() {
		ArrayList<AIHand> hands = canNextHandList();
		if (hands.size() == 0)
			return null;

		double[] step = new double[2];
		step[0] = Double.MAX_VALUE;
		step[1] = Double.MAX_VALUE;
		Point pA = new Point(-1, -1);
		Point pB = new Point(6, -1);
		for (int id = 0; id < 8; id++)
			if (board.getGhost(false, id).isAlive()) {
				Point p = board.getGhost(false, id).getPosition();
				step[0] = Math.min(step[0], d1(pA, p));
				step[1] = Math.min(step[1], d1(pB, p));
				//System.out.println(
				//board.getGhost(false, id) + ":" + d1(pA, p) + "," + d1(pB, p) + "->" + step[0] + "," + step[1]);
			}

		double stepA;
		double stepB;
		for (AIHand hand : hands) {
			if (hand.getGhost().isBlue()) {
				Point p = hand.getGhost().getPosition();
				p.translate(hand.getDirection().x(), hand.getDirection().y());
				stepA = d1(pA, p);
				stepB = d1(pB, p);
				if (stepA > step[0] - 2) {
					stepA = Double.MAX_VALUE;
				}
				if (stepB > step[1] - 2) {
					stepB = Double.MAX_VALUE;
				}
				hand.setEValue(1 / Math.min(stepA, stepB));

				System.out
						.println(hand.getGhost() + "--" + hand.getDirection() + "--" + p + "--" + stepA + "--" + stepB);
			}
		}

		Collections.shuffle(hands);
		Collections.sort(hands);
		return hands.get(hands.size() - 1);
	}
}
