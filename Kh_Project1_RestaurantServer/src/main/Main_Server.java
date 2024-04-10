package main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import serverAdminManager.EmployeeManager;
import serverAdminManager.LoginManager;

// 서버 클래스
public class Main_Server {
	private static final String SUCCESS_MESSAGE = "SUCCESS";
	private static final String FAILURE_MESSAGE = "FAILURE";

	public static void main(String[] args) {
		// LoginSever 메소드를 실행하는 스레드 생성 및 시작
		Thread loginServerThread = new Thread(() -> {
			loginSever();
		});
		loginServerThread.start();

		// AdminSever 메소드를 실행하는 스레드 생성 및 시작
		Thread adminServerThread = new Thread(() -> {
			adminSever();
		});
		adminServerThread.start();
	}

	// LoginSever 메소드
	public static void loginSever() {
		try (ServerSocket ss = new ServerSocket(1111)) { // 서버 소켓 생성
			System.out.println("Login Server: 서버가 클라이언트를 기다리는 중...");

			// 각 클라이언트별 로그인 시도 횟수를 관리하는 맵
			Map<Socket, Integer> loginAttempts = new HashMap<>();

			while (true) {
				Socket s = ss.accept(); // 클라이언트의 접속 대기
				System.out.println("Login Server: 클라이언트가 연결되었습니다!");

				// 각 클라이언트 요청을 별도의 스레드로 처리
				new Thread(() -> {
					try {
						DataInputStream dis = new DataInputStream(s.getInputStream());

						int loginFailures = loginAttempts.getOrDefault(s, 0); // 해당 클라이언트의 로그인 시도 횟수 가져오기

						while (loginFailures < 5) { // 로그인 시도 횟수가 5회 미만인 경우 반복
							String id = dis.readUTF(); // 클라이언트로부터 아이디 읽기
							String password = dis.readUTF(); // 클라이언트로부터 비밀번호 읽기
							System.out.println("Login Server: ID: " + id + "로부터 로그인 요청을 받았습니다.");

							DataOutputStream dos = new DataOutputStream(s.getOutputStream()); // 데이터 출력 스트림 생성

							// 로그인 시도
							String loginResult = LoginManager.createNewInstance().login(id, password);

							if (loginResult.equals("ADMIN") || loginResult.equals("STAFF")) {
								// 로그인 성공 시 반복문 종료
								dos.writeUTF(loginResult); // 클라이언트에게 로그인 결과 전송
								System.out.println("Login Server: 로그인 결과 (" + loginResult + ")를 클라이언트에게 전송했습니다.");
								break;
							} else {
								// 로그인 실패 시 실패 횟수 증가
								loginFailures++;
								// 클라이언트별 로그인 시도 횟수 업데이트
								loginAttempts.put(s, loginFailures);
								dos.writeUTF(loginResult); // 클라이언트에게 로그인 결과 전송
								System.out.println("Login Server: 로그인 결과 (" + loginResult + ")를 클라이언트에게 전송했습니다.");
							}
						}//end of while2

						// 로그인 실패 횟수가 5회를 초과한 경우
						if (loginFailures >= 5) {
							System.out.println("Login Server: 로그인 시도 횟수가 초과되었습니다. 클라이언트 접속을 종료합니다.");
						}

						s.close(); // 클라이언트 소켓 닫기
					} catch (Exception e) { // try1 end
						System.out.println("Login Server: " + e);
						try {
							DataOutputStream dos = new DataOutputStream(s.getOutputStream());
							dos.writeUTF("ERROR"); // 클라이언트에게 에러 정보 전송
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				}).start(); // 새로운 스레드 시작
			} // end of while1
		} catch (Exception e) { // try2 end
			System.out.println("Login Server: " + e);
		}
	}

	// AdminSever 메소드
	public static void adminSever() {
		try (ServerSocket ss = new ServerSocket(0001)) {
			System.out.println("Admin Server: 서버가 클라이언트를 기다리는 중...");

			EmployeeManager employeeManager = new EmployeeManager();
			LoginManager loginManager = new LoginManager();

			while (true) {
				Socket socket = ss.accept();
				System.out.println("Admin Server: 클라이언트가 연결되었습니다!");

				new Thread(() -> {
					try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {

						// 클라이언트로부터 요청 받기
						String action = br.readLine();
						String id = br.readLine();
						String password = br.readLine();

						System.out.println("Admin Server: Action: " + action + ", ID: " + id + ", Password: " + password);

						String result = "";
						// 요청에 따라 처리
						if (action.equals("1")) {
							// 직원 추가 요청 처리
							boolean success = employeeManager.addEmployee(id, password);
							result = (success) ? SUCCESS_MESSAGE : FAILURE_MESSAGE;
						} else if (action.equals("2")) {
							// 직원 삭제 요청 처리
							boolean success = employeeManager.deleteEmployee(id);
							result = (success) ? SUCCESS_MESSAGE : FAILURE_MESSAGE;
						} else {
							// 기타 요청은 로그인 관리자에게 전달
							result = loginManager.login(id, password);
						}

						// 클라이언트에 결과 전송
						pw.println(result);

						System.out.println("Admin Server: Result: " + result);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}).start(); // 새로운 스레드에서 클라이언트 요청 처리
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void serverStart() {
		// LoginSever 메소드를 실행하는 스레드 생성 및 시작
		Thread loginServerThread = new Thread(() -> {
			loginSever();
		});
		loginServerThread.start();
	
		// AdminSever 메소드를 실행하는 스레드 생성 및 시작
		Thread adminServerThread = new Thread(() -> {
			adminSever();
		});
		adminServerThread.start();
	}
	
}
