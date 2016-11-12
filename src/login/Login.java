package login;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * @since Geister 2.1.0
 * @author tatsumi
 */
public class Login {
	public Login(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
		
		Parent root = loader.load();

		LoginController controller = (LoginController) loader.getController();

		Stage stage = new Stage();
		stage.initOwner(primaryStage);

		controller.setStage(stage);

		stage.setScene(new Scene(root, 400, 400));
		stage.setResizable(false);
		stage.show();
	}
}
