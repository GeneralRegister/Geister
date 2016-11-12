package view;

import java.awt.Image;
import java.awt.image.ImageProducer;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;


/**
 * Textureクラスはテクスチャを表す．
 *
 * コンストラクタで指定した画像を指定したサイズに伸縮させて保存する．
 *
 * @since Geister 2.0
 * @author tatsumi
 */
public class Texture extends JFrame {
	private ImageIcon texture;


	public Texture(String name, int width, int height) {
		Image im = null;
		URL url = this.getClass().getResource(name);
		System.out.println("URL=" + url);
		try {
			im = this.createImage((ImageProducer) url.getContent());
		} catch (Exception ex) {
			System.out.println("Resource Error!");
			im = null;
		}
		ImageIcon ii = null;
		if (im != null)
			ii = new ImageIcon(im.getScaledInstance(width, height, Image.SCALE_SMOOTH));

		texture = ii;
	}


	public Image getImage() {
		return texture.getImage();
	}
}
