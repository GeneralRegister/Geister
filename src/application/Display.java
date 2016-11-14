/**
 * Geister v3.0
 *
 * Copyright (c) 2016 tatsumi
 *
 * This software is released under the MIT License.
 * https://github.com/GeneralRegister/Geister/blob/master/LICENSE
 */
package application;


import javafx.embed.swing.SwingNode;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.GamePlayer;
import view.IconPanel;
import view.Texture;


public class Display {
	private Label[] deadGhost = new Label[8];

	private int iconSpace = 100;
	private int iconBorder = 15;
	private IconPanel iconPanel;

	private int ghostIconSize = iconSpace - iconBorder * 2;
	String fileGhostBlue = "ghostBlue.png";
	String fileGhostRed = "ghostRed.png";
	private Texture ghostBlue = new Texture(fileGhostBlue, ghostIconSize, ghostIconSize);
	private Texture ghostRed = new Texture(fileGhostRed, ghostIconSize, ghostIconSize);


	public Display(Stage primaryStage, double x, double y) throws Exception {
		VBox display = new VBox();
		display.setAlignment(Pos.CENTER);
		
		StackPane stack1 = enemyInfo();
		StackPane stack2 = enemyInfo();
		display.getChildren().addAll(stack2, gameBoard(), stack1);

		Stage stage = new Stage();
		stage.initOwner(primaryStage);

		double width = 800;
		double height = iconSpace * 2 + 600;

		stage.setScene(new Scene(display, width, height));
		stage.setTitle("Geister v3.0");
		stage.setResizable(false);
		stage.show();
	}


	/*
	 * 敵ゴーストの死亡リストを表示する
	 */
	private StackPane enemyInfo() {
		SwingNode swing = new SwingNode();
		iconPanel = new IconPanel(8, 1, iconSpace - iconBorder * 2, iconBorder);
		swing.setContent(iconPanel);

		StackPane stack = new StackPane();
		stack.getChildren().addAll(swing);
		stack.setAlignment(Pos.CENTER);

		//TODO サイズ確認
		stack.setMaxWidth(800);
		stack.setMaxHeight(100);
		return stack;
	}


	public void setProgress(double value) {

	}


	public void wait(boolean isWaiting) {

	}


	public void addMsg(String msg) {
	}


	public void pushMsg(String msg) {
	}


	public void setGhostCnt(boolean isBlue, int num) {
		if (isBlue) {
			for (int i = 0; i < 4; i++)
				if (i < num)
					iconPanel.setIcon(i, 0, ghostBlue);
			iconPanel.repaint();
		} else {
			for (int i = 4; i < 8; i++)
				if (i - 4 < num)
					iconPanel.setIcon(i, 0, ghostRed);
			iconPanel.repaint();
		}
	}


	private StackPane gameBoard() {
		GamePlayer panel = new GamePlayer();
		panel.setDisplay(this);

		//TODO ここでゲーム開始
		panel.start();

		SwingNode swingNode = new SwingNode();
		swingNode.setContent(panel);
		swingNode.getContent().setMaximumSize(panel.getPreferredSize());
		swingNode.setOnMouseClicked((event) -> {
			((GamePlayer) panel).mousePressed(event);
		});

		StackPane gameArea = new StackPane();
		gameArea.getChildren().addAll(swingNode);
		gameArea.setAlignment(Pos.CENTER);

		//TODO サイズ確認
		gameArea.setMaxWidth(800);
		gameArea.setMaxHeight(600);

		return gameArea;
	}
}
