package controller;

import java.io.IOException;

import communication.Exchanger;
import model.Board;
import model.Direction;
import model.Ghost;
import view.GamePanel;


/**
 * @since Geister 1.0
 * @author tatsumi
 */
public class GameController implements Runnable {
	private GamePanel panel;
	private Board board;
	private boolean isMyTurn;

	private static int ghostSize = 8;

	private boolean isPlaying;

	private String username;

	/*
	 * 通信関連
	 */
	private boolean isServer;

	private Exchanger exchanger;


	public GameController(Exchanger exchanger) {
		board = new Board();
		isMyTurn = false;
		isPlaying = true;
		username = "";

		this.exchanger = exchanger;
		isServer = exchanger.isServer();
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
		if (isWin()) {
			//System.out.println(username + ">> あなたの勝ちです．");
			panel.addMsg(">> ゲームは終了しました．");
			panel.addMsg(">> あなたの勝ちです．");
			isPlaying = false;
			return true;
		} else if (isLoss()) {
			//System.out.println(username + ">> あなたの負けです．");
			panel.addMsg(">> ゲームは終了しました．");
			panel.addMsg(">> あなたの負けです．");
			isPlaying = false;
			return true;
		}
		return false;
	}


	public boolean isLoss() {
		return board.isLoss();
	}


	/**
	 * 自分の番であるかを判定する．自分の番である時，trueを返す．
	 *
	 * @return 自分の番である時，true
	 */
	public boolean isMyTurn() {
		return isMyTurn;
	}


	public boolean isPlaying() {
		return isPlaying;
	}


	public boolean isWin() {
		return board.isWin();
	}


	public boolean move(int x, int y, Direction dir) {
		System.out.println(x + ", " + y + ", " + dir);

		if (!isPlaying()) {
			//System.out.print(username + ">> ゲームは終了しました．");
			panel.addMsg(">> ゲームは終了しました．");
			if (isWin()) {
				//System.out.println(">> あなたの勝ちです．");
				panel.addMsg(">> あなたの勝ちです．");
			} else {
				//System.out.println(">> あなたの負けです．");
				panel.addMsg(">> あなたの負けです．");
			}
			return false;
		}

		if (!isMyTurn) {
			//System.out.println(username + ">> 相手の手番です．");
			panel.addMsg(">> 相手の手番です．");
			return false;
		}

		if (!board.isFriend(x, y)) {
			//System.out.println(username + ">> 味方のゴーストを動かしてください．");
			panel.addMsg(">> 味方のゴーストを動かしてください．");
			return false;
		}

		Ghost ghost = board.getGhost(x, y);
		if (!board.move(ghost, dir)) {
			//System.out.println(username + ">> そこへは動かせません．");
			panel.addMsg(">> そこへは動かせません．");
			return false;
		}

		/*
		 * 送信プロトコルを開始する．
		 */
		sendProtocol(board.getId(ghost), dir);

		/*
		 * 受信プロトコルを開始する．
		 */
		new Thread(this).start();

		return true;
	}


	/**
	 * 受信プロトコルを開始する．
	 *
	 * パケットA（着手情報）を受け取り，パケットB（消滅しているゴースト情報）で応答する．
	 */
	public void recieveProtocol() {
		try {
			/*
			 * パケットA（着手情報）を受信する．
			 */
			String line = exchanger.getReader().readLine();
			String[] str = line.split(" ");

			int id = ghostSize - 1 - Integer.valueOf(str[0]);
			Direction dir = Direction.valueOf(str[1]).reverse();

			board.move(false, id, dir);

			/*
			 * パケットB（消滅しているゴースト情報）で応答する．
			 */
			String output = board.cntDeadFriendBlue() + " " + board.cntDeadFriendRed();
			exchanger.getWriter().println(output);

			if (judge()) {
				//System.out.println(username + ">> ゲームは終了しました．");
			} else {
				//System.out.println(username + ">> 次はあなたの番です．");
				panel.addMsg(">> 次はあなたの番です．");
				setMyTurn(true);
			}

			repaint();

			return;
		} catch (IOException e) {
			//System.out.println("接続が切れました．");
			e.printStackTrace();
		}
	}


	@Override
	public void run() {
		if (isPlaying) {
			if (isMyTurn) {
				// ユーザの入力を待つ...
			} else {
				// 受信プロトコルを開始する...
				recieveProtocol();
			}
		} else {
			// ゲームが終了している...
		}
	}


	public void repaint() {
		panel.repaint();
	}


	/**
	 * 送信プロトコルを開始する．
	 *
	 * パケットA（着手情報）を送信し，パケットB（消滅しているゴースト情報）の応答を待つ．
	 *
	 * @param actorId
	 * @param actorDir
	 */
	public void sendProtocol(int actorId, Direction actorDir) {
		try {
			/*
			 * パケットA（着手情報）を送信する．
			 */
			String output = actorId + " " + actorDir.name();
			exchanger.getWriter().println(output);

			/*
			 * パケットB（消滅しているゴースト情報）の応答を待つ．
			 */
			String line = exchanger.getReader().readLine();
			String[] str = line.split(" ");

			int deadSenderBlue = Integer.valueOf(str[0]);
			int deadSenderRed = Integer.valueOf(str[1]);

			if (board.getDeadEnemyBlue() != deadSenderBlue) {
				//System.out.println(username + ">> 青のゴーストを倒しました．");
				panel.addMsg(">> 青のゴーストを倒しました．");
				panel.setGhostCnt(true, deadSenderBlue);
			}
			if (board.getDeadEnemyRed() != deadSenderRed) {
				//System.out.println(username + ">> 赤のゴーストを倒しました．");
				panel.addMsg(">> 赤のゴーストを倒しました．");
				panel.setGhostCnt(false, deadSenderRed);
			}
			//System.out.println(
			//username + ">> 現在の敵の消滅したゴーストは，青" + deadSenderBlue + "体, 赤" + deadSenderRed + "体です．");

			board.setDeadEnemyBlue(deadSenderBlue);
			board.setDeadEnemyRed(deadSenderRed);

			if (judge()) {
				//System.out.println(username + ">> ゲームは終了しました．");
			}

			setMyTurn(false);

			repaint();

			return;
		} catch (IOException e) {
			//System.out.println("接続が切れました．");
			e.printStackTrace();
		}
	}


	public void setMyTurn(boolean isMyTurn) {
		panel.wait(!isMyTurn);
		this.isMyTurn = isMyTurn;
	}


	public void setPanel(GamePanel gamePanel) {
		this.panel = gamePanel;
	}


	public void setUserName(String username) {
		this.username = username;
	}


	public void start(boolean isInitiative) {
		if (isInitiative) {
			setMyTurn(true);
		} else {
			setMyTurn(false);
			new Thread(this).start();
		}
	}
}
