package Utils;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class MyPanel extends JPanel {				// 메인 화면
	private String imgName;
	private int width, height;
	private Image img;
	
	public MyPanel(String imgName, int width, int height) {
		this.imgName = imgName;
		this.width = width;
		this.height = height;
		img = MyImage.getImage("Background\\" + imgName, width, height).getImage();
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
		setOpaque(false);
		super.paintComponent(g);
	}
}