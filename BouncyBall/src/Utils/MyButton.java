package Utils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class MyButton extends JButton implements MouseListener {
	private String ImageName=null;
	private int width = 0, height = 0;
	public MyButton(String ImageName, int width, int height) {	// 이미지명, 사이즈 width, height
		this.ImageName = ImageName;
		this.width = width;
		this.height = height;
		setSize(width, height);
		setIcon(MyImage.getImage(ImageName, width, height));
		setBorderPainted(false);		// 버튼 테두리 설정
		setContentAreaFilled(false);	// 버튼 영역 배경 표시 설정
		setFocusPainted(false);			// 포커스 표시 설정
		setOpaque(false);
		setBackground(Color.white);
		
		addMouseListener(this);
	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		setIcon(MyImage.getImage(ImageName + "_Entered", width, height));
		setCursor(new Cursor(Cursor.HAND_CURSOR));			// 손가락 모양 있음
	}

	public void mouseExited(MouseEvent e) {
		setIcon(MyImage.getImage(ImageName, width, height));
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));			// 손가락 모양 없음
	}
	
}
