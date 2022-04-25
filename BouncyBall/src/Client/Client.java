package Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

import Maps.MapView;


public class Client {
	static SocketChannel socket2;
	static String pp="전송가능";
	
	public void papa() {
		Thread push_locate;
		Thread systemIn;
		
		// 서버 IP와 포트로 연결되는 소켓채널 생성
		try (SocketChannel socket = SocketChannel.open(new InetSocketAddress("10.30.4.7", 5150))) {

			// 모니터 출력에 출력할 채널 생성
			WritableByteChannel out = Channels.newChannel(System.out);

			// 버퍼 생성
			ByteBuffer buf = ByteBuffer.allocate(256);

			// 출력을 담당할 스레드 생성 및 실행
			// systemIn = new Thread(new SystemIn(socket)); //1회성 아이디입력, 서버 info.isID 잠깐 닫아놈
			// 좌표만 찍어보게
			// systemIn.start();
			socket2=socket;
			
			//이거왜잇는지모르겠음 일단보류
			//push_locate = new Thread(new push_locate(socket));
			//push_locate.start();

			while (true) {

				socket.read(buf); // 읽어서 버퍼에 넣고
				buf.flip();
				ByteBuffer outputBuf3 = buf;
				IntBuffer readIntBuffer = outputBuf3.asIntBuffer();
				int[] readData = new int[readIntBuffer.capacity()];
				readIntBuffer.get(readData);
				
				MapView.set_startPos(readData[0], readData[1]);
	
				
				buf.clear();

			}

		} catch (IOException e) {

			System.out.println("서버와 연결이 종료되었습니다.");
		}
	}

	public void push_lo() {
		Thread push_locate = new Thread(new push_locate(socket2));
		push_locate.start();
		
	}
	
	public String getpp() {
		return pp;
	}

	public void setpp(String p) {
		this.pp=p;
	}

}

// 입력을 담당하는 클래스
class SystemIn implements Runnable {

	SocketChannel socket;
	// boolean sendString = false;

	// 연결된 소켓 채널과 모니터 출력용 채널을 생성자로 받음
	SystemIn(SocketChannel socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		// 키보드 입력받을 채널과 저장할 버퍼 생성
		ReadableByteChannel in = Channels.newChannel(System.in);
		ByteBuffer buf = ByteBuffer.allocate(1024);

		try { // 1회성 아이디 주기용

			in.read(buf); // 읽어올때까지 블로킹되어 대기상태
			buf.flip();
			socket.write(buf); // 입력한 내용을 서버로 출력
			buf.clear();

		} catch (IOException e) {
			System.out.println("채팅 불가.");
		}

	}
	/*
	 * public void sendStringData(String data) { ReadableByteChannel in =
	 * Channels.newChannel(System.in);
	 * 
	 * byte[] bytes = data.getBytes(); ByteBuffer sendbuf = ByteBuffer.wrap(bytes);
	 * 
	 * try { in.read(sendbuf); sendbuf.flip(); socket.write(sendbuf);
	 * sendbuf.clear(); } catch (IOException e) { System.out.println("채팅 불가.?"); }
	 * 
	 * }
	 */

}

class push_locate implements Runnable {
	SocketChannel socket;

	push_locate(SocketChannel socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		int[] point_lo;
		// 키보드 입력받을 채널과 저장할 버퍼 생성
		 ReadableByteChannel in = Channels.newChannel(System.in);
		IntBuffer intbuf;
		ByteBuffer bytebuf;

		try {

		//	while (true) {
				point_lo = MapView.get_startPx();
				// System.out.println(Arrays.toString(point_lo));
				intbuf = IntBuffer.wrap(point_lo);
				bytebuf = ByteBuffer.allocate(intbuf.capacity() * 4);
				for (int i = 0; i < intbuf.capacity(); i++) {
					bytebuf.putInt(intbuf.get(i));
				}
				bytebuf.flip();
				/*
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
				if(this.socket!=null)
				socket.write(bytebuf); // 입력한 내용을 서버로 출력
				bytebuf.clear();
			//}

		} catch (IOException e) {
			Client c = new Client();
			c.setpp("전송불가");
			System.out.println("전송불가.");
		}
	}

}
