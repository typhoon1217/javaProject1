package main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import serverAdminManager.LoginManager;

// 서버 클래스
public class Main_Server {
	private static final int SOCKET = 7913;
	private static final String SUCCESS_MESSAGE = "SUCCESS";
	private static final String FAILURE_MESSAGE = "FAILURE";
	private static final int THREAD_POOL_SIZE = 20; // 쓰레드풀 크기
	private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
	


	public static void main(String[] args) {
		serverStart();
	}
	
    public static void serverStart() {
	    try (ServerSocket ss = new ServerSocket(SOCKET)) { // 서버 소켓 생성
	        System.out.println("Login Server: 서버가 클라이언트를 기다리는 중...");

	        while (true) {
	            Socket s = ss.accept(); // 클라이언트의 접속 대기
	            System.out.println("Login Server: 클라이언트가 연결되었습니다!");

	            // 각 클라이언트 요청을 별도의 스레드로 처리
	            threadPool.submit(() -> {
	            	loginServer(s);
	            	
	            	threadPool.shutdown();
	            });
	        }
	    } catch (IOException e) {
	        System.out.println("Login Server: " + e);
	    }
	}
				

    // LoginSever 메소드
    public static void loginServer(Socket s) {
        LoginManager loginManager = new LoginManager();

    	try {
    		DataInputStream dis = new DataInputStream(s.getInputStream());

    		for (int loginFailures = 0; loginFailures < 5 ; loginFailures++) { // 로그인 시도 횟수가 5회 미만인 경우 반복
    			String id = dis.readUTF(); // 클라이언트로부터 아이디 읽기 
    			String password = dis.readUTF(); // 클라이언트로부터 비밀번호 읽기
    			System.out.println("Login Server: ID: " + id + "로부터 로그인 요청을 받았습니다.");

    			DataOutputStream dos = new DataOutputStream(s.getOutputStream()); // 데이터 출력 스트림 생성

    			// 로그인 시도
    			String loginResult = loginManager.createNewInstance().login(id, password);

    			if (loginResult.equals("ADMIN") || loginResult.equals("STAFF")) {
    				// 로그인 성공 시 반복문 종료
    				dos.writeUTF(loginResult); // 클라이언트에게 로그인 결과 전송
    				System.out.println("Login Server: 로그인 결과 (" + loginResult + ")를 클라이언트에게 전송했습니다.");
    				adminSever(s);
    				break;
    			} else {
    				dos.writeUTF(loginResult); // 클라이언트에게 로그인 결과 전송
    				System.out.println("Login Server: 로그인 결과 (" + loginResult + ")를 클라이언트에게 전송했습니다.");
    			} if (loginFailures >= 5) {
        			System.out.println("Login Server: 로그인 시도 횟수가 초과되었습니다. 클라이언트 접속을 종료합니다.");
        		}
    		}//end of while2

    	} catch (Exception e) { // try1 end
    		System.out.println("Login Server: " + e);
    		try {
    			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
    			dos.writeUTF("ERROR"); // 클라이언트에게 에러 정보 전송
    		} catch (IOException ex) {
    			ex.printStackTrace();
    		}
    	}
    }

    public static void adminSever(Socket s) {
        LoginManager loginManager = new LoginManager();
        
    	try (BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
    			PrintWriter pw = new PrintWriter(s.getOutputStream(), true)) {

    		// 클라이언트로부터 요청 받기
    		String action = br.readLine();
    		String id = br.readLine();
    		String password = br.readLine();

    		System.out.println("Admin Server: Action: " + action + ", ID: " + id + ", Password: " + password);

//----------성공 실패의 불린값을 보내고 받게 수정 
//----------성공 실패의 불린값을 보내고 받게 수정 
//----------성공 실패의 불린값을 보내고 받게 수정
//----------성공 실패의 불린값을 보내고 받게 수정 
//----------성공 실패의 불린값을 보내고 받게 수정 
//----------성공 실패의 불린값을 보내고 받게 수정 
    		
    		String result = "";
    		// 요청에 따라 처리
    		if (action.equals("1")) {
    			// 직원 추가 요청 처리
    			boolean success = loginManager.addEmployee(id, password);
    			result = (success) ? SUCCESS_MESSAGE : FAILURE_MESSAGE;
    			System.out.println("Admin Server: 직원 추가 결과: " + result);
    		} else if (action.equals("2")) {
    			// 직원 삭제 요청 처리
    			boolean success = loginManager.deleteEmployee(id);
    			result = (success) ? SUCCESS_MESSAGE : FAILURE_MESSAGE;
    			System.out.println("Admin Server: 직원 삭제 결과: " + result);
    		} else {
    			// 기타 요청은 로그인 관리자에게 전달
    			result = loginManager.login(id, password);
    			System.out.println("Admin Server: 로그인 결과: " + result);
    		}
    		// 클라이언트에 결과 전송
    		pw.println(result);

    		System.out.println("Admin Server: 클라이언트에 결과 전송: " + result);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
}
