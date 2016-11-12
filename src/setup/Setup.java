package setup;


import java.io.IOException;
import java.util.Random;

import communication.Exchanger;
import display.Display;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class Setup implements Runnable {
	private Exchanger exchanger;

	private ProgressIndicator pin;

	private Stage stage;
	private Stage primaryStage;

	private StackPane stack;


	/**
	 * TODO 一つ前の stage から全ての情報が取れるのでは?
	 *
	 * @param primaryStage
	 * @param x
	 * @param y
	 * @throws Exception
	 */
	public Setup(Stage primaryStage, double x, double y, Exchanger exchanger) throws Exception {
		Button btn = new Button("決定");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				start();
			}
		});

		this.primaryStage = primaryStage;
		this.exchanger = exchanger;

		stack = new StackPane();
		stack.getChildren().addAll(btn);
		stack.setAlignment(Pos.CENTER);
		stack.setMaxSize(100, 100);

		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(stack);

		stage = new Stage();
		stage.initOwner(primaryStage);

		Scene scene = new Scene(borderPane, 400, 400);
		stage.setScene(scene);
		stage.setTitle("Game setup");

		stage.setX(x);
		stage.setY(y);

		stage.setResizable(false);
		stage.show();
	}


	public void next(boolean isInitiative) {
		stage.close();

		try {
			new Display(primaryStage, stage.getX(), stage.getY(), exchanger, isInitiative);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void start() {
		new Thread(this).start();
	}


	public void setProgress() {
		pin = new ProgressIndicator();
		stack.getChildren().setAll(pin);
	}


	public void setProgress(double value) {
		pin.setProgress(value);
	}


	@Override
	public void run() {
		/*
		 * ボタンを消し，進捗状況を表示
		 */
		Platform.runLater(() -> setProgress());

		/*
		 * Progress indicator：接続中...
		 */
		Platform.runLater(() -> setProgress(-1.0f));

		/*
		 * ゲーム開始プロトコル
		 */
		boolean isInitiative = beginProtocol();

		/*
		 * Progress indicator：接続完了！
		 */
		//Platform.runLater(() -> setProgress(1.0f));
		Platform.runLater(() -> next(isInitiative));
	}


	/**
	 * ゲーム開始プロトコルを開始する．
	 *
	 * パケットL1（クライアントの決定通知）を受信し，パケットL2（先手プレイヤ情報）を送信する．
	 *
	 * @return 自分が先手の場合，true
	 */
	public boolean beginProtocol() {
		try {
			if (exchanger.isServer()) {
				/*
				 * パケットL1（クライアントの決定通知）を受信する．
				 * ※待ち時間含む
				 */
				exchanger.getReader().readLine();

				/*
				 * パケットL2（先手プレイヤ情報）を送信する．
				 */
				if (new Random().nextBoolean()) {
					exchanger.getWriter().println("Server");
					return true;
				} else {
					exchanger.getWriter().println("Client");
					return false;
				}
			} else {
				/*
				 * パケットL1（クライアントの決定通知）を送信する．
				 */
				exchanger.getWriter().println("OK");

				/*
				 * パケットL2（先手プレイヤ情報）を受信する．
				 * ※待ち時間含む
				 */
				if (exchanger.getReader().readLine().equals("Server")) {
					return false;
				} else {
					return true;
				}
			}
		} catch (IOException e) {
			System.out.println("接続が切れました．");
			e.printStackTrace();
		}
		return false;
	}
}
