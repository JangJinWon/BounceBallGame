package Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Server {
	static Set<SocketChannel> allClient = new HashSet<>();
	// 연결된 클라이언트를 관리할 컬렉션
	static ByteBuffer inputBuf = ByteBuffer.allocate(8);
	static ByteBuffer outputBuf = ByteBuffer.allocate(1024);
	static ByteBuffer outputBuf2 = ByteBuffer.allocate(1024);
	
	// 입출력 시 사용할 바이트버퍼 생성
	public void Server() {	}
	
	public void printString(String str) {
		outputBuf.put(str.getBytes());
		for(SocketChannel s : allClient) {
				outputBuf.flip();
					try {
						s.write(outputBuf);
					} catch (IOException e) {
						e.printStackTrace();
					}
		}
		outputBuf.clear();
	}
	
	public static void main(String[] args) {
		try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {

			// 서비스 포트 설정 및 논블로킹 모드로 설정
			serverSocket.bind(new InetSocketAddress(5150));
			serverSocket.configureBlocking(false);

			// 채널 관리자(Selector) 생성 및 채널 등록
			Selector selector = Selector.open();
			serverSocket.register(selector, SelectionKey.OP_ACCEPT);

			System.out.println("----------서버 접속 준비 완료----------");
			// 버퍼의 모니터 출력을 위한 출력 채널 생성

			int num=0;
			// 클라이언트 접속 시작
			while (true) {
				selector.select(); // 이벤트 발생할 때까지 스레드 블로킹
		
				// 발생한 이벤트를 모두 Iterator에 담아줌
				Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
				// 발생한 이벤트들을 담은 Iterator의 이벤트를 하나씩 순서대로 처리함
				while (iterator.hasNext()) {

					// 현재 순서의 처리할 이벤트를 임시 저장하고 Iterator에서 지워줌
					SelectionKey key = iterator.next();
					iterator.remove();

					// 연결 요청중인 클라이언트를 처리할 조건문 작성
					if (key.isAcceptable()) {

						// 연결 요청중인 이벤트이므로 해당 요청에 대한 소켓 채널을 생성해줌
						ServerSocketChannel server = (ServerSocketChannel) key.channel();
						SocketChannel clientSocket = server.accept();

						// Selector의 관리를 받기 위해서 논블로킹 채널로 바꿔줌
						clientSocket.configureBlocking(false);

						// 연결된 클라이언트를 컬렉션에 추가
						allClient.add(clientSocket);
						num++;
				
						// 아이디를 입력받을 차례이므로 읽기모드로 셀렉터에 등록해줌
						clientSocket.register(selector, SelectionKey.OP_READ);
					// 읽기 이벤트(클라이언트 -> 서버)가 발생한 경우
					} else if (key.isReadable()) {

						// 현재 채널 정보를 가져옴 (attach된 사용자 정보도 가져옴)
						SocketChannel readSocket = (SocketChannel) key.channel();
					
						// 채널에서 데이터를 읽어옴
						try {readSocket.read(inputBuf);
							
							// 만약 클라이언트가 연결을 끊었다면 예외가 발생하므로 처리
						} catch (Exception e) {
							key.cancel(); // 현재 SelectionKey를 셀렉터 관리대상에서 삭제
							allClient.remove(readSocket); // Set에서도 삭제
							num--;
							outputBuf.clear();
							continue;
						}
					
						inputBuf.flip();
						ByteBuffer outputBuf3 = inputBuf;
						IntBuffer readIntBuffer = outputBuf3.asIntBuffer();
						int[] readData = new int[readIntBuffer.capacity()];
						readIntBuffer.get(readData);
						//System.out.println("배열복원: "+Arrays.toString(readData));

						if(num==2 ) {
							if(readData.length==0) {
								key.cancel(); // 현재 SelectionKey를 셀렉터 관리대상에서 삭제
								allClient.remove(readSocket); // Set에서도 삭제
								outputBuf.clear();
								num--;

								continue;
							}
						}
					
						for(SocketChannel s : allClient) { 
							if (!readSocket.equals(s)) { //이게 자신을 제외한 사용자한테 보내는거
								s.write(outputBuf3); //위치보내기
							}
						}
						
						inputBuf.clear();
						outputBuf.clear();
						outputBuf2.clear();
						
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
