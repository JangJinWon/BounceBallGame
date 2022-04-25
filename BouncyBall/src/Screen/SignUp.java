package Screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import DB.DB;
import Utils.MyTextField;

public class SignUp extends JPanel implements ActionListener, KeyListener {
	private DB db;
	private final int BTN_SIZE = 4;
	private final int TXT_SIZE = 5;
	private final int LBL_SIZE = 6;
	private JButton[] btn = new JButton[BTN_SIZE];
	private MyTextField[] t = new MyTextField[TXT_SIZE];
	private String[] question_list = {"내 이메일 주소는?", "나의 보물 1호는?", "나의 출신 초등학교는?", "나의 출신 고향은?", "어머니 성함은?", "아버지 성함은?", "좋아하는 음식은?", "좋아하는 색깔은?"};
	private String[] btn_name = {"중복 확인", "중복 확인", "Insert", "Go Login"};
	private String[] txt_name = {"NickName", "ID", "PW", "PW Check", "비밀번호 찾기 답변"};
	private String[] lbl_name = {"닉네임 사용 가능 : ", "아이디 사용 가능 : ", "비밀번호(5자 이상, 10자 이하), 영어 숫자 조합) 사용 가능 : ", "비밀번호 일치 : ", "비밀번호 찾기 질문", "사용 가능: "};
	private JComboBox question_box1 = new JComboBox(question_list);
	private boolean[] l_check = {false, false, false, false, true, false};
	private JLabel[] l = new JLabel[LBL_SIZE];
	private Login loginF;
	
	public SignUp(DB db, Login loginF) {
		this.db = db;
		this.loginF = loginF;
		setLayout(null);
		setBackground(new Color(189, 215, 238));

		JPanel sign_p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 25)); sign_p1.setOpaque(false);	// 텍스트 필드 패널, gridlayout은 컴포넌트 개별 크기 조절안되서 flowlayout으로 수정함
		JPanel sign_p2 = new JPanel(new GridLayout(1, 0, 10, 0)); sign_p2.setOpaque(false);			// 버튼 패널
		JPanel sign_p3 = new JPanel(new GridLayout(0, 1, 0, 25)); sign_p3.setOpaque(false);			// 레이블 패널
		sign_p1.setBounds(30, 50, 430, 500);
		sign_p2.setBounds(145, 400, 200, 40);
		sign_p3.setBounds(30, 95, 430, 305);
		
		JLabel l_label = new JLabel("회원가입");
		l_label.setFont(new Font(null, Font.BOLD, 28));
		l_label.setOpaque(false);
		l_label.setBounds(30, 20, 200, 30);
		add(l_label);
		
		for (int i=2; i<BTN_SIZE; i++) {
			btn[i] = new JButton(btn_name[i]);
			btn[i].addActionListener(this);
			sign_p2.add(btn[i]);
		}

		for (int i=0; i<TXT_SIZE; i++) {
			if (i == 4) {
				sign_p1.add(question_box1);
				question_box1.setPreferredSize(new Dimension(420,30));
			}
			if (i < 2) t[i] = new MyTextField(29, txt_name[i]);
			else t[i] = new MyTextField(38, txt_name[i]);
			t[i].setPreferredSize(new Dimension(0, 30));
			sign_p1.add(t[i]);		// 로그인 텍스트
			
			if (i < 2) {
				btn[i] = new JButton(btn_name[i]);
				btn[i].addActionListener(this);
				btn[i].setPreferredSize(new Dimension(100,30));
				sign_p1.add(btn[i]);
			}
		}
		t[2].addKeyListener(this);
		t[3].addKeyListener(this);
		t[4].addKeyListener(this);
		t[4].enableInputMethods(true);
		
		for (int i=0; i<LBL_SIZE; i++) {
			if (i == 4) l[i] = new JLabel(lbl_name[i]);
			else l[i] = new JLabel(lbl_name[i] + l_check[i]);
			sign_p3.add(l[i]);
		}
		
		add(sign_p1); add(sign_p2); add(sign_p3);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn[0]) {			// 닉네임 중복 확인
			l_check[0]=db.overlap_check("NickName", t[0].getTextF(), !t[0].getTextF().equals(" "+t[0].getName()));	// 사용 가능 여부 체크
			l[0].setText(lbl_name[0] + l_check[0]);			// 사용 가능 여부 닉네임
		} else if (e.getSource() == btn[1]) {	// 아이디 중복 확인
			l_check[1]=db.overlap_check("id", t[1].getTextF(), !t[1].getTextF().equals(" "+t[1].getName()));	// 사용 가능 여부 체크
			l[1].setText(lbl_name[1] + l_check[1]);			// 사용 가능 여부 닉네임
		} else if (e.getSource() == btn[2]) {	// Insert
			for (int i=0; i<TXT_SIZE; i++) {
				if (t[i].getTextF().equals(" " + t[i].getName()) ) {
					JOptionPane.showMessageDialog(this, "빈칸이 있습니다.");
					loginF.setLinebd(t[i]);
					return;
				}
			}
			for (int i=0; i<LBL_SIZE; i++) {
				if (l_check[i] == false) {
					JOptionPane.showMessageDialog(this, "조건이 일치하지 않는 정보가 있습니다.");
					loginF.setLinebd(t[i]);
					return;
				}
			}
			
			db.SignUp(t[1].getTextF(), t[2].getTextF(), t[0].getTextF(), question_box1.getSelectedItem().toString(), t[4].getTextF());
			loginF.pageChange(this, loginF.login_all);
			FormReset();
		} else if (e.getSource() == btn[3]) {	// Go Login
			loginF.pageChange(this, loginF.login_all);
		}
	}
	
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {
		if (e.getSource() == t[2]) {
			if (t[2].getTextF().matches("^(?=.*[a-zA-z])(?=.*[0-9]).{5,10}$") && t[2].getTextF().length() >= 5) 
				l_check[2] = true;
			else 
				l_check[2] = false;
			
			l[2].setText(lbl_name[2] + l_check[2]);
		} else if (e.getSource() == t[3]) {
			if (t[2].getTextF().equals(t[3].getTextF()) && l_check[2] == true)
				l_check[3] = true;
			else 
				l_check[3] = false;
			
			l[3].setText(lbl_name[3] + l_check[3]);
		} else if(e.getSource()==t[4]) {  
	         if(t[4].getPassword().length>0) {
	             l_check[5]=true;
	             l[5].setText(lbl_name[5] + l_check[5]);
	          }
	          else {
	             l_check[5]=false;
	             l[5].setText(lbl_name[5] + l_check[5]);
	          }
	       }
	}
	
	private void FormReset() {
		for (int i=0; i<LBL_SIZE; i++) {
			if (i==4) {
				l_check[i] = true;
			} else {
				l_check[i] = false;
				l[i].setText(lbl_name[i] + l_check[i]);	
			}								
		}
		for (int i=0; i<TXT_SIZE; i++) {
			t[i].setText(" " + txt_name[i]);
			t[i].setEchoChar((char) 0);
		}
		question_box1.setSelectedIndex(0);
	}
}
