package communication;


import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * @since Geister 2.0.0
 * @author tatsumi
 */
public class Client extends Exchanger {
	private Socket socket;


	public Client() {
		super(false);
	}


	@Override
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * TODO 接続が完了しないときは，falseを返し，再度ユーザに接続要求を呼びかけるなど
	 *
	 * @param host
	 * @param port
	 */
	public void connect(String host, int port) {
		try {
			socket = new Socket(host, port);
			super.setIO(socket);
		} catch (UnknownHostException e) {
			System.out.println(">>>>>>>1");
			e.printStackTrace();
		} catch (IOException e) {
			/*
			 * 原因1：サーバポートをもつプログラムが実行されていないとき
			 */
			System.out.println(">>>>>>>2");
			e.printStackTrace();
		}
	}
}
