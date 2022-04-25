package Screen;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Client.Client;
import Maps.MapView;
import Utils.MyButton;
import Utils.MyImage;
import Utils.MyPanel;

public class MainMenu extends JFrame implements ActionListener {
	private JPanel p1;
	private JButton b1, b2;
	private JPanel panel = new JPanel();
	private JButton play, ranking, back, exit, cooperation, pvp, game_back;
	private ImageIcon icon = new ImageIcon("ImageFile\\Label\\ranking_Label.png");
	private JLabel[] user = new JLabel[10];
	private JScrollPane scroll = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//   private Music introMusic = new Music("introMusic.mp3", true); // 배경 음악
	private static boolean op = false;

	public static void main(String[] args) {

		Thread ppp = new Thread(new popaq());
		ppp.start();
		new Login();
		// rr(0);

	}

	public static void rr(int n) {
		Client c = new Client();
		MapView mp = new MapView(c, n);
		c.papa();
	}

	private static class popaq implements Runnable {
		public void run() {
			while (true) {
				try {
		               Thread.sleep(3);
		            } catch (InterruptedException e) {
		               e.printStackTrace();
		            }
				if (op) {
					rr(0);
					break;
				}
			}
		}
	}

	public MainMenu(String title, int width, int height) {
		setSize(width, height); // 크기
		setTitle(title); // 제목
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프로세스 종료
		setLocationRelativeTo(null); // 가운데 배치
		setResizable(false); // 화면 크기 고정

//      introMusic.start(); // 배경음 On
		p1 = new MyPanel("main_background", 1280, 720); // 그리기 패널
		p1.setLayout(null);

		play = new MyButton("Button\\play_Button", 300, 100);
		play.setLocation(470, 300);
		ranking = new MyButton("Button\\RanKing_Button", 300, 100);
		ranking.setLocation(70, 300);
		exit = new MyButton("Button\\exit_Button", 300, 100);
		exit.setLocation(900, 300);
		back = new MyButton("Button\\back_Button", 100, 50);
		back.setLocation(50, 50);
		game_back = new MyButton("Button\\game_back_Button", 30, 30);
		game_back.setLocation(20, 20);
		cooperation = new MyButton("Button\\cooperation_Button", 500, 500);
		cooperation.setLocation(100, 160);
		pvp = new MyButton("Button\\PVP_Button", 500, 500);
		pvp.setLocation(680, 160);

		scroll.getVerticalScrollBar().setUnitIncrement(30); // 스크롤 속도

		add(play);
		add(ranking);
		add(back);
		add(scroll);
		add(exit);
		add(cooperation);
		add(pvp);
		add(game_back);
		add(p1);

		back.setVisible(false);
		scroll.setVisible(false);
		cooperation.setVisible(false);
		pvp.setVisible(false);
		game_back.setVisible(false);

		play.addActionListener(this);
		ranking.addActionListener(this);
		exit.addActionListener(this);
		cooperation.addActionListener(this);
		pvp.addActionListener(this);
		back.addActionListener(this);
		game_back.addActionListener(this);

		// 랭킹 스크롤
		scroll.setBounds(140, 160, 1000, 500);
		panel.setLayout(new GridLayout(user.length, 1));
		for (int i = 0; i < user.length; i++) {
			user[i] = new JLabel("");
			user[i].setIcon(MyImage.getImage("Label\\ranking_Label", 980, 100));
			panel.add(user[i]);
		}
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == play) {
			remove(p1);
			p1 = new MyPanel("mode_background", 1280, 720);
			add(p1);

			p1.setVisible(true);
			back.setVisible(true);
			cooperation.setVisible(true);
			pvp.setVisible(true);
			ranking.setVisible(false);
			play.setVisible(false);
			exit.setVisible(false);
		} else if (e.getSource() == ranking) {
			remove(p1);
			p1 = new MyPanel("ranking_background", 1280, 720);
			add(p1);

			p1.setVisible(true);
			scroll.setVisible(true);
			back.setVisible(true);
			ranking.setVisible(false);
			play.setVisible(false);
			exit.setVisible(false);
		} else if (e.getSource() == exit) {
			System.exit(0);
		} else if (e.getSource() == cooperation) {
//         introMusic.close();
			op = true;
			this.dispose();

			// Client c = new Client();
			// new MapView(c, 0);
			// c.main(null);
		} else if (e.getSource() == pvp) {
//         introMusic.close();
			this.dispose();
			op = true;
			rr(1);
			// Client c = new Client();
			// new MapView(c, 1);
			// c.main(null);
		} else if (e.getSource() == back) {
			remove(p1);
			p1 = new MyPanel("main_background", 1280, 720);
			add(p1);

			p1.setVisible(true);
			ranking.setVisible(true);
			play.setVisible(true);
			exit.setVisible(true);
			back.setVisible(false);
			scroll.setVisible(false);
			cooperation.setVisible(false);
			pvp.setVisible(false);
		}
	}
}