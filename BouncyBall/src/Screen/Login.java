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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.LineBorder;

import DB.DB;
import Utils.MyPanel;
import Utils.MyTextField;

public class Login extends JFrame implements ActionListener {
	private DB db=new DB();	// DB 연결 시도
	private final int BTN_SIZE = 4;
	private final int TXT_SIZE = 2;
	private JButton[] b = new JButton[BTN_SIZE];
	private MyTextField[] t = new MyTextField[TXT_SIZE];
	private String[] btn_name = {"Login", "Sign Up", "Forgot Password", "close"};
	private String[] txt_name = {"ID", "PW"};
	public JPanel login_all, sign_all, forgot_all;
	
	public Login() {
		setSize(500, 500);
		setLocationRelativeTo(null);						// 처음에 화면을 가운데에 나타내기
		setResizable(false);								// 화면 크기 조정 잠금
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		login_all=new MyPanel("login_background", 485, 460); login_all.setLayout(null);
		sign_all = new SignUp(db, this);
		forgot_all=new FindPW(db, this);
		
		JPanel login_p1=new JPanel(new GridLayout(0, 1, 0, 10)); login_p1.setOpaque(false);
		JPanel login_p2=new JPanel(new GridLayout(0, 1, 0, 10)); login_p2.setOpaque(false);
		login_p1.setBounds(40, 170, 400, 60);
		login_p2.setBounds(40, 250, 400, 180);
		
		for (int i=0; i<TXT_SIZE; i++) {		// 입력란, 설명란
			t[i] = new MyTextField(38, txt_name[i]);
			t[i].setPreferredSize(new Dimension(0, 30));
			login_p1.add(t[i]);		// 로그인 텍스트
		}
		
		for (int i=0; i<BTN_SIZE; i++) {
			b[i] = new JButton(btn_name[i]);
			b[i].addActionListener(this);
			login_p2.add(b[i]);				// 회원가입 버튼
		}

		login_all.add(login_p1); login_all.add(login_p2);
		
		add(login_all);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b[0]) {			// 로그인
			String id = t[0].getTextF();
			String pw = t[1].getTextF();
			int login_code = db.login(id, pw);
			if (login_code == -1) {
				setLinebd(t[0]);
			} else if (login_code == 0) {
				setLinebd(t[1]);
			} else {
				this.dispose();
				new MainMenu("Bouncy Ball", 1280, 720);
			}
		} else if (e.getSource() == b[1]) {		// Sign Up
			pageChange(login_all, sign_all);
		} else if (e.getSource() == b[2]) {
			pageChange(login_all, forgot_all);
		} else if (e.getSource() == b[3]) {		// Close
			System.exit(0);
		}
	}
	
	public void pageChange(JPanel prev, JPanel next) {
		remove(prev);					// 이전 패널 제거
		getContentPane().add(next);		// 다음 패널 붙이기
		revalidate();					// 새로고침
		repaint();						// 새로고침
	}
	
	public void setLinebd(JPasswordField t) {
		t.setBorder(new LineBorder(Color.red, 2));
	}

}