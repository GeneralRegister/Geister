package communication;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * @since Geister 2.0.0
 * @author tatsumi
 */
public abstract class Exchanger {
	private boolean isServer;
	private BufferedReader reader;
	private PrintWriter writer;


	public Exchanger(boolean isServer) {
		this.isServer = isServer;
	}


	abstract public void close();


	public BufferedReader getReader() {
		return reader;
	}


	public PrintWriter getWriter() {
		return writer;
	}


	public boolean isServer() {
		return isServer;
	}


	public boolean isClient() {
		return !isServer();
	}


	public void setIO(Socket socket) {
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
