package Utils;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.ResultSet;

import javax.swing.JPasswordField;
import javax.swing.border.LineBorder;

public class MyTextField extends JPasswordField implements FocusListener {
	public MyTextField(int size, String name) {
		setColumns(size);				// 크기
		setOpaque(false);				// 불투명도 해제
		addFocusListener(this);			// 포커스 리스너
		setEchoChar((char)0);			// 처음에는 글자 표시
		setName(name);					// 이름 지정
		setText(" " + getName());		// 기본 텍스트 지정
		setBorder(new LineBorder(new Color(0, 0, 0)));	// 검은색 테두리 지정
	}
	
	public String getTextF() {
		return String.valueOf(getPassword());
	}

	public void focusGained(FocusEvent e) {
		JPasswordField text = (JPasswordField) e.getSource();		// JPasswordField형태로 getSource 받기
		text.setBorder(new LineBorder(new Color(0, 0, 0)));			// 포커스 얻으면 테두리 검은색으로
		text.setBackground(new Color(79, 167, 255));				// 포커스를 얻으면 배경색 바꾸기
		text.setOpaque(true);										// 불투명도 true
		if (String.valueOf(text.getPassword()).equals(" " + text.getName())) {	// 기본 텍스트 값이 있으면
			text.setText("");													// 빈칸으로 만들기
			if (text.getName().equals("PW") || text.getName().equals("PW Check")) text.setEchoChar('●');				// 컴포넌트 이름이 PW이면 비밀번호 암호화 실행
		}
	}

	public void focusLost(FocusEvent e) {
		JPasswordField text = (JPasswordField) e.getSource();		// JPasswordField형태로 getSource 받기
		text.setBackground(new Color(0, 0, 0));						// 포커스 잃으면 배경색 없애기
		text.setOpaque(false);										// 불투명도 false
		if (String.valueOf(text.getPassword()).equals("")) {					// 텍스트 박스가 비어 있으면
			text.setText(" " + text.getName());									// 기본 텍스트 값으로 변경
			/*for (int i=2; i<Main.Login.getTxtSize(); i++) {
				if (String.valueOf(text.getPassword()).equals(" " + text.getName())) {
					checkList[i-2] = false;
				}
			}*/
			if (text.getName().equals("PW") || text.getName().equals("PW Check")) text.setEchoChar((char)0);			// 컴포넌트 이름이 PW이면 비밀번호 암호화 해제
		}
	}
}
