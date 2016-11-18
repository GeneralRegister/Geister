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

import model.Board;
import model.Hand;
import view.GamePlayer;


public class GameController {
	// 表示用
	private GamePlayer gamePanel;

	private boolean playing;
	private int nextPlayer;
	private Player[] player;
	private Board[] board;


	public GameController() {
		nextPlayer = -1;
		board = new Board[2];
		board[0] = new Board();
		board[1] = new Board();
		player = new Player[2];
	}


	public Board getBoard(int index) {
		return board[index];
	}


	public boolean getPlaying() {
		return playing;
	}


	public boolean isMyTurn(int index) {
		return index == nextPlayer;
	}


	public void judge() {
		if (board[0].isWin()) {
			setPlaying(false);
			System.out.println("player0 WIN!");
		} else if (board[0].isLoss()) {
			setPlaying(false);
			System.out.println("player1 WIN!");
		}
	}


	// #ゲームシーケンス
	public void move(int player, Hand hand) {
		if (!getPlaying()) {
			// ゲームが終了している．．．
			return;
		} else if (!isMyTurn(player)) {
			// player の番でない．．．
			return;
		} else if (!board[player].isFriend(hand.getX(), hand.getY())) {
			// 味方のゴーストを動かしていない．．．
			return;
		} else if (!board[player].move(hand)) {
			// ゴーストを動かせない．．．
			return;
		}

		// 相手．．．
		int opponent = (player + 1) % 2;

		// 相手の盤面を自分の着手の逆で反映させる．．．
		board[opponent].move(hand.reverse());

		// 相手の盤面の死亡リストを取る．．．
		int db = board[opponent].getDeadFriendBlue();
		int dr = board[opponent].getDeadFriendRed();

		// 相手の盤面の死亡リストを自分の盤に反映させる．．．
		board[player].setDeadEnemyBlue(db);
		board[player].setDeadEnemyRed(dr);

		// 死亡リストを表示
		gamePanel.setGhostCnt(player, true, db);
		gamePanel.setGhostCnt(player, false, dr);

		repaint();

		judge();

		if (getPlaying()) {
			playerChange();
		} else {
			nextPlayer = -1;
		}
	}


	// #ゲームシーケンス
	public void playerChange() {
		if (nextPlayer == 0) {
			nextPlayer = 1;
		} else if (nextPlayer == 1) {
			nextPlayer = 0;
		}
	}


	// #ゲームシーケンス
	public void next() {
		if (getPlaying()) {
			player[nextPlayer].nextHand();
		} else {
			// ゲームが終了している．．．
		}
	}


	// #表示
	public void repaint() {
		gamePanel.repaint();
	}


	// #表示
	public void setPanel(GamePlayer panel) {
		this.gamePanel = panel;
	}


	public void setPlayer(int index, Player player) {
		this.player[index] = player;
	}


	public void setPlaying(boolean playing) {
		this.playing = playing;
	}


	public void start() {
		setPlaying(true);
		if (new Random().nextBoolean()) {
			nextPlayer = 0;
		} else {
			nextPlayer = 1;
		}
		next();
	}
}
