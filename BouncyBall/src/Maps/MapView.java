package Maps;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import BAB.BlockAndBall;
import Client.Client;
import Screen.MainMenu;
import Utils.MyButton;
import Utils.MyPanel;

class Player {
   int x = 0, y = 620;
   public Player(int x, int y) {
      this.x = x;
      this.y = y;
   }
   public void setPosXY(int x, int y, JLabel lb) {
      this.x = x;
      this.y = y;
      lb.setLocation(x, y);
   }
   public void setPosXY(JLabel lb) {
      lb.setLocation(x, y);
   }
}

public class MapView extends JFrame implements KeyListener, ActionListener, Runnable {
   private ArrayList<BlockAndBall> l = new ArrayList<>();
   private int[][] maps = MyMaps.MapList(0);            // 맵의 인덱스 설
   private static Player player1, player2;
   private boolean isLeft=false, isRight=false, isJump=false, isUp=false, isDown=false, isGame=false;
   private JLabel lb, lb2;
   private JPanel gamePanel = new JPanel();
   private JButton game_back;
   private int myW = 1400, myH = 800;   
   private BufferedImage image;
   private int nowSelect;
   private int jumpPowerValue=60;
   private int jumpPower=jumpPowerValue;
   Client cli;
   Thread t3;
   
   
   public static int[] get_startPx() {
      int[] startLo = {player1.x, player1.y};
      return startLo;
   }
   
   public static void set_startPos(int num1, int num2) {
      player2.x=num1;
      player2.y=num2;

   }

   private class p2moving implements Runnable{
      public void run() {
         while(true)
         lb2.setLocation(player2.x, player2.y);
      }
   }
   public void startp2mv() {
      Thread t7=new Thread(new p2moving());
      t7.start();
   }
   
   private class check_onlyp2 implements Runnable{
      public void run() {
         boolean check=false;
         while(true) {
            try {
               Thread.sleep(5);
            } catch (InterruptedException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
            //System.out.println(startPx2);
            System.out.println(player2.y);
            if(player2.y!=620) {
               check=true;
               if(check) {
                  startp2mv();
               }
               break;
            }
           
         }
      }
   }
   
   public MapView(Client c, int nowSelect) {
	   cli = c;
	   maps = MyMaps.MapList(nowSelect); // 맵의 인덱스 설정
       this.nowSelect=nowSelect;
      
      setSize(myW, myH);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLocationRelativeTo(null); // 가운데 배치
      setResizable(false); // 화면 크기 고정
      setTitle("In Game");
      game_back = new MyButton("Button\\game_back_Button", 30, 30); game_back.setLocation(20, 20);
      add(game_back);
      game_back.addActionListener(this);
      gamePanel=new MyPanel("game_background", 1400, 800);
      this.setFocusable(true);
      addKeyListener(this);
      
      
      int f = 0, x = 0;
      for (int i = 0; i < maps.length; i++) {
         for (int j = 0; j < maps[i].length; j++) {
            l.add(new BlockAndBall(maps[i][j], f, x));
            x++;
         }
         x = 0;
         f++;
      }

      for (int i = 0; i < l.size(); i++) {
         JLabel label = l.get(i);            // 한 블럭 한 블럭 가져오기
         int floor = (maps.length - l.get(i).getFloor()) * 30; // 현재 층 저장 
         int w = l.get(i).getXpoint() * 30; // x값 저장
         label.setLocation(w, myH - 100 - floor);
         l.get(i).posX = w;            // 블록 좌표 x
         l.get(i).posY = myH-100-floor;   // 블록 좌표 y
         add(label);
      }
      if(nowSelect==0) { 
         player1 = new Player(0, 620);
         player2 = new Player(0, 620);         
      } else if(nowSelect==1) {
         player1 = new Player(0, 620);
         player2 = new Player(0, 620);
      }
      
      lb = new BlockAndBall(0); // 공
      player1.setPosXY(lb);
      lb2 = new BlockAndBall(2);
      add(lb);
      add(lb2);
      add(gamePanel);
      
      setVisible(true);
      
      Thread t=new Thread(this);
      Thread t1=new Thread(new moving());
      Thread t2=new Thread(new check_onlyp2());
      t3=new Thread(new JumpProcess());
      Thread t4=new Thread(new DeadProcess());
      t.start();
      t1.start();
      t2.start();
      t3.start();
      t4.start();
      isGame=true;
   }
   
   public class pushlo implements Runnable{
      public void run() {
         
         while (!cli.getpp().equals("전송불가")) {
            try {
               Thread.sleep(3);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
            //여기에 sleep줘야 연결되는동안 에러안남
            cli.push_lo();
         }
      }
   }
   
   private class DeadProcess implements Runnable {
      public void run() {
         try {
            while (true) {
               if (player1.x > 1240 || player1.y > 740 || player1.x < 0) {
                  player1.setPosXY(0, 620, lb);
               }
               Thread.sleep(10);
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }
   
   private boolean Crash(int startPx, int startPy, BlockAndBall block) {      // 충돌 체크
      if ( Math.abs((startPx + 20 / 2) - (block.posX + 30 / 2)) < (30 / 2 + 20 / 2) && Math.abs((startPy + 20 / 2) - (block.posY + 30 / 2)) < (30 / 2 + 20 / 2) ) {
         return true;
      }
      return false;
   }
   
   int value = 1;
   int bn;
   private void CrashEvents() {
      if (bn == 3 || bn == 7) {            // 가시 or 전기 
         player1.setPosXY(0, 620, lb);
      }
      else if(bn == 4) {      // 포물선 점프
         int x=player1.x-120;
         while(x<player1.x) {
            try {
               player1.x-=value;
               Thread.sleep(3);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
         }
      }
      else if (bn == 5 || bn == 9) {      // 점프대
         isJump = true;
         jumpPower = 70;
         value=2;
      } 
      else if(bn == 6) {   // 왼쪽 방향 점프대
         t3.suspend();
         while(bn!=2 && !isLeft && !isRight) {
            try {
               blockNumber();
               if(bn==2) break;
               player1.x-=value;
               Thread.sleep(3);
            } catch (InterruptedException e) {
               e.printStackTrace();
            } 
         }
         t3.resume();
      }
      else if(bn == 10) {            // 순간 가속
         t3.suspend();
         while(bn!=2 && !isLeft && !isRight) {
            try {
               blockNumber();
               if(bn!=0) break;
               player1.x+=value;
               Thread.sleep(3);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
         }
         t3.resume();
      }
      else if(bn == 11) {         // 블랙 포탈 (순간 이동)
         for(int r=0;r<maps.length;r++) {
            for(int k=0;k<maps[r].length;k++) {
               if(maps[r][k]==12) {   // 흰색 포탈 위치
                  player1.x = l.get(r*42+k).getX();
                  player1.y = l.get(r*42+k).getY();
                  break;
               }
            }
         }
      }
      else if (bn == 16) {      // 별
         int i=player1.y/30, j=player1.x/30;
         l.get(i*42+j).setVisible(false);
         maps[i][j]=0;
      }
   }
   
   private class moving implements Runnable {
      Thread t3=new Thread(new pushlo());
      public void run() {
         t3.start();
         while(!cli.getpp().equals("전송불가")) {
            moveProcess(); 
         }

      }      // 무한 루프로 움직임 입력 체크
   }
   
   private void blockProcess() {
      for (int i=0; i<l.size(); i++) {
         if (Crash(player1.x, player1.y, l.get(i)) && l.get(i).block_num != 0) {
            bn = l.get(i).block_num;
            CrashEvents();
            break;
         } else {
            bn = 0;
         }
      }
   }
   
   private void blockNumber() {   // 블럭 번호 체크 함수 
      for (int i=0; i<l.size(); i++) {
         if (Crash(player1.x, player1.y, l.get(i)) && l.get(i).block_num != 0) {
            bn = l.get(i).block_num;
            break;
         } else {
            bn = 0;
         }
      }
   }
   
   public void down() {
      try {
         while (!isJump && bn != 2) {
            player1.y+=value;
            Thread.sleep(5);
         }
         isJump = true;
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public void jump() {
      try {
         while (isJump && jumpPower != 0) {
            player1.y-=value;
            jumpPower--;
            Thread.sleep(5);
         }
         isJump = false;
         jumpPowerValue = 60;
         value = 1;
         jumpPower = jumpPowerValue;
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   private class JumpProcess implements Runnable {
      public void run() {
         while (true) {
            try {
               down();
               jump();
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      }
   }
   
   private void moveProcess() {
      if (player1.x>0 && isLeft) player1.x -= 1;
      if (player1.x<1240 && isRight) player1.x += 1;
      try {
         Thread.sleep(5);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
      player1.setPosXY(lb);
   }
   
   public void keyTyped(KeyEvent e) {}
   public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
         case KeyEvent.VK_LEFT:
            isLeft = true;
            break;
         case KeyEvent.VK_RIGHT:
            isRight = true;
            break;
      }
   }
   public void keyReleased(KeyEvent e) {
      switch (e.getKeyCode()) {
         case KeyEvent.VK_LEFT:
            isLeft = false;
            break;
         case KeyEvent.VK_RIGHT:
            isRight = false;
            break;
      }
   }
   
   @Override
   public void actionPerformed(ActionEvent e) {
      if(e.getSource() == game_back) {
         isGame=false;
         this.dispose();
         t3.stop();
         new MainMenu("Bouncy Ball", 1280, 720); 
      }
   }

   public void run() {
      try {
         while (true) {
            blockProcess();
            Thread.sleep(5);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}