package display;

import communication.Exchanger;
import controller.GameController;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.GamePanel;


public class Display {
	private Label[] label = new Label[2];
	private int labelHeight = 20;

	private Label ghostBlue = new Label("0");
	private Label ghostRed = new Label("0");

	private ProgressIndicator pin;


	public Display(Stage primaryStage, double x, double y, Exchanger exchanger, boolean isInitiative) throws Exception {
		this.pin = new ProgressIndicator();
		pin.setProgress(-1.0f);

		/*
		 * borderPane（画面全体）を生成
		 */
		BorderPane borderPane = new BorderPane();

		/*
		 * GameArea（ゲームの盤面）を中央に配置
		 */
		StackPane stack = mkGameArea(exchanger, isInitiative);
		borderPane.setBottom(stack);

		System.out.println(stack.getMaxWidth() + ", " + stack.getMaxWidth());

		/*
		 * メッセージ画面を上に配置
		 */
		for (int i = 0; i < label.length; i++) {
			label[i] = new Label();
			label[i].setMaxHeight(labelHeight);
			label[i].setMinHeight(labelHeight);
		}

		borderPane.setLeft(mkMsgArea());
		borderPane.setRight(mkGhostArea());

		/*
		 * Progress Ing
		 */
		borderPane.setCenter(pin);

		/*
		 * stage を生成する．
		 */
		Stage stage = new Stage();
		stage.initOwner(primaryStage);

		double width = stack.getMaxWidth();
		double height = stack.getMaxHeight() + labelHeight * label.length;
		Scene scene = new Scene(borderPane, width, height);

		stage.setScene(scene);
		stage.setTitle("Hello..");
		stage.setResizable(false);
		stage.setX(x);
		stage.setY(y);
		stage.show();

		addMsg("msg1");
		addMsg("msg2");
		addMsg("msg3");
	}


	public void setProgress(double value) {
		pin.setProgress(value);
	}


	public void wait(boolean isWaiting) {
		if (isWaiting) {
			Platform.runLater(() -> setProgress(-1.0f));
		} else {
			Platform.runLater(() -> setProgress(1.0f));
		}
	}


	/**
	 * メッセージエリアにメッセージを追加する．
	 *
	 * @param msg
	 */
	public void addMsg(String msg) {
		Platform.runLater(() -> pushMsg(msg));
	}


	public void pushMsg(String msg) {
		for (int i = 0; i < label.length - 1; i++) {
			label[i].setText(label[i + 1].getText());
		}
		label[label.length - 1].setText(msg);
	}


	/**
	 * ゴーストエリアの数を設定する．
	 *
	 * @param isBlue
	 * @param num
	 */
	public void setGhostCnt(boolean isBlue, int num) {
		if (isBlue) {
			Platform.runLater(() -> ghostBlue.setText(String.valueOf(num)));
		} else {
			Platform.runLater(() -> ghostRed.setText(String.valueOf(num)));
		}
	}


	/**
	 * GhostArea を生成する．
	 */
	private VBox mkGhostArea() {
		VBox ghostArea = new VBox(0);
		ghostArea.setAlignment(Pos.CENTER);
		ghostArea.getChildren().addAll(mkGhostInfo(true), mkGhostInfo(false));
		return ghostArea;
	}


	/**
	 * GhostInfo を生成する．
	 */
	private HBox mkGhostInfo(boolean isBlue) {
		HBox ghostInfo = new HBox(0);
		ghostInfo.setAlignment(Pos.CENTER);
		if (isBlue)
			ghostInfo.getChildren().addAll(new Label("Blue"), new Label(" x "), ghostBlue, new Label("  "));
		else
			ghostInfo.getChildren().addAll(new Label("Red"), new Label(" x "), ghostRed, new Label("  "));
		return ghostInfo;
	}


	/**
	 * MsgArea を生成する．
	 */
	private VBox mkMsgArea() {
		// VBox 生成
		VBox msgArea = new VBox(0);
		msgArea.setAlignment(Pos.CENTER);
		for (int i = 0; i < label.length; i++)
			msgArea.getChildren().add(label[i]);
		msgArea.setMaxHeight(labelHeight * label.length);
		msgArea.setMinHeight(labelHeight * label.length);

		return msgArea;
	}


	/**
	 * GameArea を生成する．
	 */
	private StackPane mkGameArea(Exchanger exchanger, boolean isInitiative) {
		// GameController 生成
		GameController game = new GameController(exchanger);
		if (isInitiative) {
			game.setUserName("ユーザA");
		} else {
			game.setUserName("ユーザB");
		}

		// GamePanel 生成
		GamePanel panel = new GamePanel(game);
		panel.setDisplay(this);
		panel.start(isInitiative);

		// SwingNode 生成
		SwingNode swingNode = new SwingNode();
		swingNode.setContent(panel);
		swingNode.getContent().setMaximumSize(panel.getPreferredSize());
		swingNode.setOnMouseClicked((event) -> {
			((GamePanel) panel).mouseClicked(event);
		});

		// Pane 生成
		StackPane gameArea = new StackPane();
		gameArea.getChildren().addAll(swingNode);
		gameArea.setAlignment(Pos.CENTER);
		gameArea.setMaxWidth(panel.getPreferredSize().width);
		gameArea.setMaxHeight(panel.getPreferredSize().height);

		return gameArea;
	}
}
