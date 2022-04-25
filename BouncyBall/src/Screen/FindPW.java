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
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import DB.DB;
import Utils.MyTextField;

public class FindPW extends JPanel implements ActionListener, KeyListener {
	private DB db;
	private final int BTN_SIZE = 4;
	private final int TXT_SIZE = 4;
	private final int LBL_SIZE = 5;
	private JButton[] btn = new JButton[BTN_SIZE];
	private MyTextField[] t = new MyTextField[TXT_SIZE];
	private String[] question_list = {"내 이메일 주소는?", "나의 보물 1호는?", "나의 출신 초등학교는?", "나의 출신 고향은?", "어머니 성함은?", "아버지 성함은?", "좋아하는 음식은?", "좋아하는 색깔은?"};
	private String[] txt_name = {"ID", "비밀번호 찾기 답변", "PW", "PW Check"};
	private String[] lbl_name = {"아이디 확인 : ", "질문 확인", "답변 확인 : ", "비밀번호(5자 이상, 10자 이하), 영어 숫자 조합) 사용 가능 : ", "비밀번호 일치 : "};
	private String[] btn_name = {"아이디 확인", "질문 확인", "Reset", "Go Login"};
	private JComboBox question_box2 = new JComboBox(question_list);
	private boolean[] f_check = {false, false, false, false};
	private JLabel[] l = new JLabel[LBL_SIZE];
	private Login loginF;	
	
	public FindPW(DB db, Login loginF) {
		this.db = db;
		this.loginF = loginF;
		setLayout(null);
		setBackground(new Color(189, 215, 238));
		
		JPanel forgot_p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 30)); forgot_p1.setOpaque(false);	//gridlayout은 컴포넌트 개별 크기 조절안되서 flowlayout으로 수정함
		JPanel forgot_p2 = new JPanel(new GridLayout(1, 0, 10, 0)); forgot_p2.setOpaque(false);
		JPanel forgot_p3 = new JPanel(new GridLayout(0, 1, 0, 0)); forgot_p3.setOpaque(false);
		
		forgot_p1.setBounds(30, 65, 430, 500);
		forgot_p2.setBounds(145, 400, 200, 40);
		forgot_p3.setBounds(30, 105, 430, 300);
		
		JLabel f_label = new JLabel("비밀번호 재설정");
		f_label.setFont(new Font(null, Font.BOLD, 28));
		f_label.setOpaque(false);
		f_label.setBounds(30, 20, 250, 30);
		add(f_label);
		
		for (int i=2; i<BTN_SIZE; i++) {
			btn[i] = new JButton(btn_name[i]);
			btn[i].addActionListener(this);
			forgot_p2.add(btn[i]);
		}
		
		for (int i=0; i<TXT_SIZE; i++) {
			if (i == 1) {
				forgot_p1.add(question_box2);
				question_box2.setPreferredSize(new Dimension(420,30));
			}
			if (i < 2) t[i] = new MyTextField(29, txt_name[i]);
			else t[i] = new MyTextField(38, txt_name[i]);
			t[i].setPreferredSize(new Dimension(0, 30));
			forgot_p1.add(t[i]);		// 로그인 텍스트
			
			if (i < 2) {
				btn[i] = new JButton(btn_name[i]);
				btn[i].addActionListener(this);
				btn[i].setPreferredSize(new Dimension(100,30));
				forgot_p1.add(btn[i]);
			}
		}
		t[1].enableInputMethods(true);
		t[2].addKeyListener(this);
		t[3].addKeyListener(this);
		question_box2.addActionListener(this);
		
		for (int i=0; i<LBL_SIZE; i++) {                           //1205오류수정: 원래 87열이 없고, 89열의 i-1이 아닌 i였으나 f_check[]길이를 줄이면서 길이가 안맞는 관계로 수정함.
	         if (i == 0) l[i] = new JLabel(lbl_name[i] + f_check[i]);      //1205오류수정: 128열에서 오류가 떠서 f_check[]길이를 줄이면서 lbl_size랑 길이가 안 맞아지게되어 추가함.
	         else if (i == 1) l[i] = new JLabel(lbl_name[i]);
	         else l[i] = new JLabel(lbl_name[i] + f_check[i-1]);            //1205오류수정: 위와 같은 이유로 수정. f_check[]길이 줄어들면서 -1을 해줬음.
	         forgot_p3.add(l[i]);
	      }
		add(forgot_p1); add(forgot_p2); add(forgot_p3);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn[0]) {			// 아이디 중복 확인
			f_check[0] = !db.overlap_check("ID", t[0].getTextF(), !t[0].getTextF().equals(" "+t[0].getName()));
			l[0].setText(lbl_name[0] + f_check[0]);			// 사용 가능한 아이디
		} else if (e.getSource() == btn[1]) {	// 질문 확인
			try {
				ResultSet rs = db.search("ID", t[0].getTextF());
				if (rs == null) {
					JOptionPane.showMessageDialog(this, "ID를 다시 확인해 주십시오");
					return;
				}	// 검색되는 아이디가 없을경우
				
				if (question_box2.getSelectedItem().equals(rs.getString("question"))) {			// 확인된 ID의 질문이 선택된 경우
					if (rs.getString("answer").equals(t[1].getTextF())) 						// 확인된 질문의 답변이 일치하는 경우
						f_check[1] = true;
					else 
						f_check[1] = false;
					l[2].setText(lbl_name[2] + f_check[1]);
				} else {
					JOptionPane.showMessageDialog(this, "질문이 다릅니다.");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == btn[2]) {	// Reset
			for (int i=0; i<TXT_SIZE; i++) {
				if (t[i].getTextF().equals(" " + t[i].getName()) ) {
					JOptionPane.showMessageDialog(this, "빈칸이 있습니다.");
					loginF.setLinebd(t[i]);
					return;
				}
			}
			for (int i=0; i<f_check.length; i++) {
				if (f_check[i] == false) {
					JOptionPane.showMessageDialog(this, "조건이 일치하지 않는 정보가 있습니다.");
					loginF.setLinebd(t[i]);
					return;
				}
			}
			
			db.PasswordUpdate(t[0].getTextF(), t[2].getTextF());
			loginF.pageChange(this, loginF.login_all);
			FormReset();
		} else if (e.getSource() == btn[3]) {	// Go Login
			loginF.pageChange(this, loginF.login_all);
		} else if (e.getSource() == question_box2) {
			f_check[1] = false;
			l[2].setText(lbl_name[2] + f_check[1]);
		}
	}
	
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {
		if (e.getSource() == t[2]) {
			if (t[2].getTextF().matches("^(?=.*[a-zA-z])(?=.*[0-9]).{5,15}$") && t[2].getTextF().length() >= 5) 
				f_check[2] = true;
			else 
				f_check[2] = false;
			
			l[3].setText(lbl_name[3] + f_check[2]);
		} else if (e.getSource() == t[3]) {
			if (t[2].getTextF().equals(t[3].getTextF()) && f_check[2] == true)
				f_check[3] = true;
			else 
				f_check[3] = false;
			
			l[4].setText(lbl_name[4] + f_check[3]);
		}
	}
	private void FormReset() {
		for (int i=0; i<TXT_SIZE; i++) {
			t[i].setText(" " + t[i].getName());
			t[i].setEchoChar((char) 0);
		}
		for (int i=0; i<LBL_SIZE; i++) {
			if (i!=LBL_SIZE-1) f_check[i] = false;
			if (i == 0) l[i].setText(lbl_name[i] + f_check[i]);
			else if (i == 1) l[i].setText(lbl_name[i]);
			else l[i].setText(lbl_name[i] + f_check[i-1]);
		}
		question_box2.setSelectedIndex(0);
	}
}
