package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.LoginManager;

// 서버 클래스
public class Main_Server {
	private static final int SOCKET = 7913;
	private static final int THREAD_POOL_SIZE = 50; // 쓰레드풀 크기
	public static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

	public static void main(String[] args) {
		serverStart();
	}
	
	public static void serverStart() {
		int count=0;
		System.out.println("Server start");
	    try (ServerSocket ss = new ServerSocket(SOCKET)) { // 서버 소켓 생성
	        System.out.println("Login Server: 서버가 클라이언트를 기다리는 중...");

	        while (true) {
	        	Socket s = ss.accept(); // 클라이언트의 접속 대기
	        	count++;
	        	System.out.println("Login Server:클라이언트" + count + "가 연결되었습니다!"+s+"#s는 테스트용 실제론 보안때문에 보여주지 말것.");

	        	// 각 클라이언트 요청을 별도의 스레드로 처리
	        	threadPool.submit(() -> {
	        		System.out.println("쓰레드풀 생성");
	        		loginServer(s);
	        	});
	        }
	    } catch (IOException e) {
	    	System.out.println("Login Server: " + e);
	    	System.out.println("Login Server IO오류");
	    } finally {
	    	threadPool.shutdown();
	    	System.out.println("Login Server: 클라이언트" + count + " 쓰레드풀 종료");
	    }
	}


	// LoginSever 메소드
	public static void loginServer(Socket s) { 
		try {
			System.out.println("접속 수락"+s+"#s는 테스트용 실제론 보안때문에 보여주지 말것.");
			DataInputStream dis = new DataInputStream(s.getInputStream());
			LoginManager loginManager = new LoginManager();
			for (int loginFailures = 0; loginFailures < 5 ; loginFailures++) { // 로그인 시도 횟수가 5회 미만인 경우 반복
				String id = dis.readUTF(); // 클라이언트로부터 아이디 읽기 
				String password = dis.readUTF(); // 클라이언트로부터 비밀번호 읽기
				
				System.out.println("id:"+id+"pw:"+password+"정보를 받아왔습니다.\n# 비밀번호는 보안때문에 보여주면 안되지만 연습용이기 때문에 호출함 ");
				
				DataOutputStream dos = new DataOutputStream(s.getOutputStream()); // 데이터 출력 스트림 생성
				
				System.out.println("Login Server: ID: " + id + "로부터 로그인 요청을 받았습니다.");

				// 로그인 시도
				String loginResult = loginManager.login(id, password);

				if (loginResult.equals("ADMIN") || loginResult.equals("STAFF")) {
					// 로그인 성공 시 반복문 종료
					dos.writeUTF(loginResult); // 클라이언트에게 로그인 결과 전송
					dos.flush();
					System.out.println("Login Server: 로그인 결과 (" + loginResult + ")를 클라이언트에게 전송했습니다.");
					adminSever(s);
					break;
				} else {
					dos.writeUTF(loginResult); // 클라이언트에게 로그인 결과 전송
					dos.flush();
					System.out.println("Login Server: 로그인 결과 (" + loginResult + ")를 클라이언트에게 전송했습니다.");
				} if (loginFailures >= 5) {
					System.out.println("Login Server: 로그인 시도 횟수가 초과되었습니다. 클라이언트 접속을 종료합니다.");
				}
			}//end of for
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Login Server: 에러");
		}
	}

	public static void adminSever(Socket s) {
		LoginManager loginManager = new LoginManager();
		boolean result = false;
		boolean three = false;
		int action = 0;
		
		System.out.println("adminSever Activated"+s+"#s는 테스트용 실제론 보안때문에 보여주지 말것.");
		
        try {
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            while (!three) {
                action = dis.readInt();
            	System.out.println("action:"+action);
            	if (action==3) {
            		three=true;
            	}
            // 클라이언트로부터 요청 받기
            if(action!=3) {
            	 String id = dis.readUTF();
                 String password = dis.readUTF();

                 System.out.println("Admin Server: Action: " + action + ", ID: " + id + ", Password: " + password+"#비밀번호는 보안문제로 실제로는 보여주면 안됨");
                 // 요청에 따라 처리
                 
                 	if (action==1) {
                         // 직원 추가 요청 처리
                         result = loginManager.addEmployee(id, password);
                         System.out.println("Admin Server: 직원 추가 결과: " + result);
                     } else if(action==2){
                         // 직원 삭제 요청 처리
                         result = loginManager.deleteEmployee(id);
                         System.out.println("Admin Server: 직원 삭제 결과: " + result);
                     }
                     // 클라이언트에 결과 전송
                     dos.writeBoolean(result);
                     dos.flush(); // 스트림을 플러시하여 버퍼를 비움
                     System.out.println("Admin Server: 클라이언트에 결과 전송: " + result);
            }
           
            }
        } catch (IOException e) {
        }
    }
}
