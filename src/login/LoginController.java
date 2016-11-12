package login;


import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import launch.Launch;

/**
 * @since Geister 2.1.0
 * @author tatsumi
 */
public class LoginController implements Initializable {
	@FXML
	private Label startHost;

	@FXML
	private TextField joinHost;

	@FXML
	private TextField port;

	@FXML
	private Button start;

	@FXML
	private Button join;

	private Stage ownerStage;
	private Stage stage;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			startHost.setText(addr.getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}


	@FXML
	public void start(ActionEvent aEvent) {
		stage.close();

		String host = startHost.getText();
		int port = Integer.valueOf(this.port.getText());

		try {
			Launch launch = new Launch(ownerStage, stage.getX(), stage.getY());
			launch.connect(true, host, port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@FXML
	public void join(ActionEvent aEvent) {
		stage.close();

		String host = joinHost.getText();
		int port = Integer.valueOf(this.port.getText());

		try {
			Launch launch = new Launch(ownerStage, stage.getX(), stage.getY());
			launch.connect(false, host, port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void setOwnerStage(Stage ownerStage) {
		this.ownerStage = ownerStage;
	}


	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
