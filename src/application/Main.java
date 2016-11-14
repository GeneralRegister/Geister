/**
 * Geister v3.0
 *
 * Copyright (c) 2016 tatsumi
 *
 * This software is released under the MIT License.
 * https://github.com/GeneralRegister/Geister/blob/master/LICENSE
 */
package application;


import javafx.application.Application;
import javafx.stage.Stage;


/**
 * @since Geister 2.0.0
 * @author tatsumi
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		new Display(primaryStage, 0, 0);
	}


	public static void main(String[] args) {
		launch(args);
	}
}
