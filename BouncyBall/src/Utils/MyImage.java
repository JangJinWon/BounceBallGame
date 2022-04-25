package Utils;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class MyImage {			// 이름에 맞게 이미지를 출력해줌
	public static ImageIcon getImage(String name, int x, int y) {
		ImageIcon icon = null;
		
		try {
			Image img = ImageIO.read(new File("ImageFile\\" + name + ".png"));			// 해당 폴더에 파일이름을 이미지 객체에 저장
			Image re = img.getScaledInstance(x, y, Image.SCALE_SMOOTH);					// 크기는 xy, 품질을 중점으로 스케일링
			icon = new ImageIcon(re);													// 아이콘으로 생성
		} catch (Exception e) {e.printStackTrace();}
		
		return icon;
	}
}
