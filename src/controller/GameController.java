/**
 * Geister v3.0
 *
 * Copyright (c) 2016 tatsumi
 *
 * This software is released under the MIT License.
 * https://github.com/GeneralRegister/Geister/blob/master/LICENSE
 */
package controller;


import java.util.Random;

import ai.AI;
import ai.Hand;
import javafx.application.Platform;
import model.Board;
import view.GamePlayer;


public class GameController {
	// 表示用
	private GamePlayer gamePanel;

	//game
	private boolean isPlaying;

	// プレーヤー用
	private Player[] player = new Player[2];
	private Board[] board = new Board[2];
	private int nextPlayer = -1;


	public GameController() {

	}


	public void initBoard() {

		board[0] = new Board();
		board[1] = new Board();
	}


	public void setPlayer(Player player1, Player player2) {
		this.player[0] = player1;
		this.player[1] = player2;
	}


	// #表示
	public void setPanel(GamePlayer panel) {
		this.gamePanel = panel;
	}


	// #表示
	public void repaint() {
		gamePanel.repaint();
	}


	// #（新）操作
	public void move(Hand hand) {

	}


	public void autoAIs() {
		int index = 0;
		isPlaying = true;

		AI[] ai = { new AI(board[0]), new AI(board[1]) };

		while (true) {
			System.out.println("\n\n ai" + index + "::-->攻撃:");
			Hand hand = ai[index].nextHand();

			boolean me = board[index].move(hand);
			if (me) {
				System.out.println(hand + "Yes");

				boolean you = board[(index + 1) % 2].move(hand.reverse());
				System.out.println((index + 1) % 2 + "-->守り:" + you);

				int db = board[(index + 1) % 2].cntDeadFriendBlue();
				int dr = board[(index + 1) % 2].cntDeadFriendRed();
				board[index].setDeadEnemyBlue(db);
				board[index].setDeadEnemyRed(dr);
				gamePanel.setGhostCnt(true, db);
				gamePanel.setGhostCnt(false, dr);
				repaint();

				judge();

				if (isPlaying()) {
					index = (index + 1) % 2;
				} else {
					return;
				}

			} else {
				System.out.println(hand + "No");
			}
		}
	}


	// #（新）操作
	/*
	 * TODO 着手に失敗したとき，再度着手要求をする．
	 */
	public boolean move(int index, Hand hand) {
		System.out.println("index:" + index + ", hand:" + hand + "<-->" + hand.reverse());

		if (!isPlaying()) {
			return false;
		} else if (!isMyTurn(index)) {
			return false;
		} else if (!board[index].isFriend(hand.getX(), hand.getY())) {
			return false;
		} else if (!board[index].move(hand)) {
			return false;
		}

		// 相手の番号
		int next = (index + 1) % 2;

		// 相手の盤面を自分の着手の逆で反映させる．
		board[next].move(hand.reverse());

		System.out.println("index:" + index + ", hand:" + hand + "<-->" + hand.reverse());

		//相手の盤面の死亡リストを取る
		int db = board[next].cntDeadFriendBlue();
		int dr = board[next].cntDeadFriendRed();

		//相手の盤面の死亡リストを自分の盤に反映させる
		board[index].setDeadEnemyBlue(db);
		board[index].setDeadEnemyRed(dr);

		//System.out.println("AI側死亡リスト：青" + db + ", 赤" + dr);
		gamePanel.setGhostCnt(true, db);
		gamePanel.setGhostCnt(false, dr);

		judge();

		Platform.runLater(() -> repaint());
		//repaint();

		if (isPlaying()) {
			nextPlayer = (index + 1) % 2;
			player[nextPlayer].nextHand();
		} else {
			nextPlayer = -1;
		}

		return true;
	}


	public Board getBoard(int index) {
		return board[index];
	}


	public void judge() {
		if (board[0].isWin()) {
			isPlaying = false;

			System.out.println("player0 WIN!");

			gamePanel.addMsg("player1 WIN!");
			gamePanel.addMsg("player2 LOSS...");
		} else if (board[0].isLoss()) {
			isPlaying = false;

			System.out.println("player1 WIN!");

			gamePanel.addMsg("player2 WIN!");
			gamePanel.addMsg("player1 LOSS...");
		}
	}


	public boolean isMyTurn(int index) {
		return index == nextPlayer;
	}


	public boolean isPlaying() {
		return isPlaying;
	}


	public void start() {
		boolean initiative = new Random().nextBoolean();
		isPlaying = true;
		if (initiative) {
			nextPlayer = 0;
		} else {
			nextPlayer = 1;
		}
		player[nextPlayer].nextHand();
	}
}
