package launch;


import communication.Client;
import communication.Exchanger;
import communication.Server;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import setup.Setup;


/**
 * @since Geister 2.1.0
 * @author tatsumi
 *
 * @see
 * <ui>
 * <li>「進行状況バーと進行状況インジケータ」について
 * https://docs.oracle.com/javase/jp/8/javafx/user-interface-tutorial/progress.htm
 *
 * <li>「JavaFXでスレッドを使って描画するときの注意」について
 * http://qiita.com/takaki@github/items/7dba9afaf11002c78719
 * </ui>
 */
public class Launch implements Runnable {
	private Exchanger exchanger;

	private ProgressIndicator pin;

	private boolean isServer;
	private String hostname;
	private int port;

	private Stage stage;
	private Stage primaryStage;


	public Launch(Stage primaryStage, double x, double y) throws Exception {
		final ProgressIndicator pin = this.pin = new ProgressIndicator();
		pin.setProgress(-1.0f);

		StackPane stack = new StackPane();
		stack.getChildren().addAll(pin);
		stack.setAlignment(Pos.CENTER);
		stack.setMaxSize(100, 100);

		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(stack);

		this.primaryStage = primaryStage;

		stage = new Stage();
		stage.initOwner(primaryStage);

		Scene scene = new Scene(borderPane, 400, 400);
		stage.setScene(scene);
		stage.setTitle("Connection status");

		stage.setX(x);
		stage.setY(y);

		stage.setResizable(false);
		stage.show();
	}


	public void next() {
		stage.close();

		try {
			new Setup(primaryStage, stage.getX(), stage.getY(), exchanger);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void connect(boolean isServer, String hostname, int port) {
		this.isServer = isServer;
		this.hostname = hostname;
		this.port = port;

		/*
		 * ソケット接続処理
		 */
		new Thread(this).start();
	}


	public void setProgress(double value) {
		pin.setProgress(value);
	}


	/**
	 * ソケット接続処理を行う．
	 *
	 * スレッドとして処理を分離することで，進捗バーが動作し続けることが出来る．
	 */
	@Override
	public void run() {
		/*
		 * Progress indicator：接続中...
		 */
		Platform.runLater(() -> setProgress(-1.0f));

		/*
		 * 接続開始...
		 */
		exchanger = getExchanger(isServer, hostname, port);

		/*
		 * Progress indicator：接続完了！
		 */
		//Platform.runLater(() -> setProgress(1.0f));
		Platform.runLater(() -> next());
	}


	/**
	 * TODO 同じポートが既に開かれているとき，サーバとして開くとエラーになる
	 *
	 * @param isServer
	 * @param hostname
	 * @param port
	 * @return
	 */
	public Exchanger getExchanger(boolean isServer, String hostname, int port) {
		Exchanger exchanger;
		System.out.println("フェーズ1...実行中...");
		if (isServer) {
			exchanger = new Server();
			((Server) exchanger).accept(hostname, port);
		} else {
			exchanger = new Client();
			((Client) exchanger).connect(hostname, port);
		}
		System.out.println("接続が完了しました...");
		return exchanger;
	}
}
