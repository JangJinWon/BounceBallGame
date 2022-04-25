package BAB;

import javax.swing.JLabel;
import Utils.MyImage;

public class BlockAndBall extends JLabel {	
	private String[][] imageName = {{"없음", "default_block", "iron_block", "thorn_block", "iteamDash_block", "jump_block", "Left_block", "electricity_block",
		"Rightdiagonal_block", "jump", "going", "BlackPortal_block", "WhitePortal_block", "Warp_block", "move_block", "ChangeColor_block",
		 "Star", "StarRainbow", "switch_On", "switch_Off"}, {"blue_circle","blue_square","red_circle","red_square"}};
	private int floor = 0;						// 몇 층에 배치하는지 표현할 인덱스
	private int x = 0;							// x좌표의 인덱스 저장
	public int posX, posY;
	public int block_num;
	
	public BlockAndBall(int block_num, int floor, int x) {			// 블럭 가져오기
		this.floor = floor;
		this.x = x;
		this.block_num = block_num;
		if (block_num != 0) {		// 블럭 인덱스가 0이 아닐때
			this.setIcon(MyImage.getImage("Block\\" + imageName[0][block_num], 30, 30));	// 블럭 폴더에 배열의 이름을 이용하여 이미지 삽입 사이즈는 30, 30으로 조정
			setSize(30, 30);															// 레이블의 크기 30, 30으로 지정
		}
	}
	
	public BlockAndBall(int ball_num) {								// 공 가져오기
		this.setIcon(MyImage.getImage("Ball\\" + imageName[1][ball_num], 20, 20));	// 볼 폴더에 배열의 이름을 이용하여 이미지 삽입 사이즈는 20, 20으로 조정
		setSize(20, 20);															// 레이블의 크기 20, 20으로 지정
	}
	
	public int getFloor() { return floor; } 
	public int getXpoint() { return x; } 
}
