/**
 * Geister v3.0
 *
 * Copyright (c) 2016 tatsumi
 *
 * This software is released under the MIT License.
 * https://github.com/GeneralRegister/Geister/blob/master/LICENSE
 */
package controller;

import ai.AI;
import ai.Hand;
import model.Board;
import model.Direction;
import model.Ghost;
import view.GamePlayer;


public class GameController implements Runnable {
	/*
	 * ゲーム情報
	 */
	private Board board;
	private boolean isPlaying;
	private boolean isMyTurn;

	/*
	 * プレイヤー情報
	 */
	private GamePlayer player;
	private String username;

	/*
	 * AI情報
	 */
	private AI ai;
	private Board boardAI;
	private boolean isMyTurnAI;


	public GameController(GamePlayer player) {
		board = new Board();
		isMyTurn = false;
		isPlaying = true;
		username = "";
		this.player = player;

		boardAI = new Board();
		ai = new AI(boardAI);
		isMyTurnAI = false;
	}


	public Board getBoard() {
		return board;
	}


	/**
	 * 勝敗を判定する．勝敗が決まってる場合，isPlayingをfalseに設定し，trueを返す．
	 *
	 * @return 勝敗が決まってる場合，true
	 */
	public boolean judge() {
		if (board.isWin()) {
			isPlaying = false;
			//System.out.println("You Win!");
			player.addMsg("You WIN!");
			return true;
		} else if (board.isLoss()) {
			isPlaying = false;
			//System.out.println("You Loss...");
			player.addMsg("You LOSS...");
			return true;
		}
		return false;
	}


	public boolean isMyTurn() {
		return isMyTurn;
	}


	public boolean isPlaying() {
		return isPlaying;
	}


	public boolean move(int x, int y, Direction dir) {
		//System.out.println(x + ", " + y + ", " + dir);

		if (!isPlaying()) {
			return false;
		}

		if (!isMyTurn) {
			//System.out.println(username + ">> 相手の手番です．");
			player.addMsg(">> 相手の手番です．");
			return false;
		}

		if (!board.isFriend(x, y)) {
			//System.out.println(username + ">> 味方のゴーストを動かしてください．");
			player.addMsg(">> 味方のゴーストを動かしてください．");
			return false;
		}

		Ghost ghost = board.getGhost(x, y);
		if (!board.move(ghost, dir)) {
			//System.out.println(username + ">> そこへは動かせません．");
			player.addMsg(">> そこへは動かせません．");
			return false;
		}

		setMyTurn(false);

		//AIの盤面に反映させる
		int actorId = ghost.getId();
		int id = 7 - actorId;
		boardAI.move(false, id, dir.reverse());

		//AIの盤面の死亡リストを取る
		int db = boardAI.cntDeadFriendBlue();
		int dr = boardAI.cntDeadFriendRed();

		//AIの盤面の死亡リストを自分の盤に反映させる
		board.setDeadEnemyBlue(db);
		board.setDeadEnemyRed(dr);

		//System.out.println("AI側死亡リスト：青" + db + ", 赤" + dr);
		player.setGhostCnt(true, db);
		player.setGhostCnt(false, dr);

		//ゲームが終わっていなければAIの手
		judge();

		repaint();

		if (isPlaying) {
			//AI
			isMyTurnAI = true;
			new Thread(this).start();
		}

		return true;
	}


	public void repaint() {
		player.repaint();
	}


	public void setMyTurn(boolean isMyTurn) {
		this.isMyTurn = isMyTurn;
		player.wait(!isMyTurn);
	}


	public void setUserName(String username) {
		this.username = username;
	}


	public void start(boolean isInitiative) {
		if (isInitiative) {
			setMyTurn(true);
			isMyTurnAI = false;
		} else {
			setMyTurn(false);
			isMyTurnAI = true;
			/*
			 * 相手の番
			 */
			new Thread(this).start();
		}
	}


	private boolean nextAiHand() {
		if (!isPlaying()) {
			return false;
		}

		if (!isMyTurnAI) {
			//System.out.println("AI>> 相手の手番です．");
			return false;
		}

		Hand next = ai.nextHand();

		if (!boardAI.isFriend(next.getX(), next.getY())) {
			//System.out.println("AI>> AIのゴーストを動かしてください．");
			//panel.addMsg(">> 味方のゴーストを動かしてください．");
			return false;
		}

		Ghost ghost = boardAI.getGhost(next.getX(), next.getY());
		Direction dir = next.getDirection();
		if (!boardAI.move(ghost, dir)) {
			//System.out.println("AI>> そこへは動かせません．");
			//panel.addMsg(">> そこへは動かせません．");
			return false;
		}

		isMyTurnAI = false;

		//人の盤面に反映させる
		int actorId = ghost.getId();
		int id = 7 - actorId;
		board.move(false, id, dir.reverse());

		//ひとの盤面の死亡リストを取る
		//今は必要ない
		int db = board.cntDeadFriendBlue();
		int dr = board.cntDeadFriendRed();

		//AIの盤面の死亡リストを自分の盤に反映させる
		boardAI.setDeadEnemyBlue(db);
		boardAI.setDeadEnemyRed(dr);

		//System.out.println("ひと側死亡リスト：青" + db + ", 赤" + dr);

		repaint();

		//ゲームが終わっていなければひとの手
		judge();

		if (isPlaying) {
			//ひと
			setMyTurn(true);
		}

		return true;
	}


	@Override
	public void run() {
		nextAiHand();
	}
}
