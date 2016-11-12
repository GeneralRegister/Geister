package application;


import javafx.application.Application;
import javafx.stage.Stage;
import login.Login;


/**
 * @since Geister 2.0.0
 * @author tatsumi
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		new Login(primaryStage);
	}


	public static void main(String[] args) {
		launch(args);
	}
}
