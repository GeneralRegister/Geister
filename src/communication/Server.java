package communication;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @since Geister 2.0.0
 * @author tatsumi
 */
public class Server extends Exchanger {
	private ServerSocket server;
	private Socket socket;


	public Server() {
		super(true);
	}


	@Override
	public void close() {
		try {
			socket.close();
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void accept(String hostname, int port) {
		try {
			server = new ServerSocket();
			server.bind(new InetSocketAddress(hostname, port));
			socket = server.accept();
			super.setIO(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
