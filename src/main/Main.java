/**
 * Geister v3.0
 *
 * Copyright (c) 2016 tatsumi
 *
 * This software is released under the MIT License.
 * https://github.com/GeneralRegister/Geister/blob/master/LICENSE
 */
package main;


import javax.swing.JFrame;

import view.GamePlayer;


public class Main extends JFrame {
	public static final String TITLE = "Geister";
	public static final String VERSION = "3.0.0";


	public static void main(String[] args) {
		new Main();
	}


	public Main() {
		setTitle(TITLE + "-" + VERSION);
		getContentPane().add(new GamePlayer());
		setResizable(false);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
