package ai;


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
	 * 全ての可能着手を返す．
	 *
	 * @return 全ての可能着手
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
	 * 着手を返す．
	 *
	 * @return 着手
	 */
	public Hand nextHand() {
		//		try {
		//			Thread.sleep(700);
		//		} catch (InterruptedException e) {
		//			e.printStackTrace();
		//		}
		return testNextHand();
	}


	/**
	 * 可能着手をランダムに返す．
	 *
	 * @return ランダムな可能着手
	 */
	public Hand randomNextHand() {
		ArrayList<AIHand> hands = canNextHandList();
		if (hands.size() == 0)
			return null;
		return hands.get(new Random().nextInt(hands.size()));
	}


	/**
	 * TODO テスト用です．
	 */
	public Hand testNextHand() {
		ArrayList<AIHand> hands = canNextHandList();
		if (hands.size() == 0)
			return null;

		for (AIHand hand : hands)
			hand.evaluate(board);

		Collections.sort(hands);

		for (AIHand hand : hands)
			System.out.println(hand);

		System.out.println("-----------------------------------");

		return hands.get(hands.size() - 1);
	}
}
