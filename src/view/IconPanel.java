/**
 * Geister v3.0
 *
 * Copyright (c) 2016 tatsumi
 *
 * This software is released under the MIT License.
 * https://github.com/GeneralRegister/Geister/blob/master/LICENSE
 */
package view;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;


public class IconPanel extends JPanel {
	private int height;
	private int width;
	private int cellSize;
	private int border;
	private int outerCellSize;

	private Texture[][] texture;

	private final static Color colorGrid = new Color(0, 0, 0);
	private final static Color colorDefault = new Color(70, 60, 80);


	public IconPanel(int width, int height, int cellSize, int border) {
		this.width = width;
		this.height = height;
		this.cellSize = cellSize;
		this.border = border;
		this.outerCellSize = cellSize + border * 2;
		texture = new Texture[height][width];
		setPreferredSize(new Dimension(width * outerCellSize, height * outerCellSize));
	}


	public void setIcon(int x, int y, Texture icon) {
		if (isOnScreen(x, y))
			texture[y][x] = icon;
	}


	public boolean isOnScreen(int x, int y) {
		return x >= 0 && x < width && y >= 0 && y < height;
	}


	public void repaint() {
		super.repaint();
	}


	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.draw(g);
	}


	public void draw(Graphics g) {
		g.setColor(colorGrid);
		g.fillRect(0, 0, width * outerCellSize, height * outerCellSize);

		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++) {
				if (texture[y][x] != null) {
					g.drawImage(texture[y][x].getImage(), x * outerCellSize + border, y * outerCellSize + border, null);
				} else {
					g.setColor(colorDefault);
					g.fillRect(x * outerCellSize + border, y * outerCellSize + border, cellSize, cellSize);
				}
			}
	}
}
