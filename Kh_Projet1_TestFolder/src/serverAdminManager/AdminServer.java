package serverAdminManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

// 서버 클래스
public class AdminServer {
	private static final String SUCCESS_MESSAGE = "SUCCESS";
	private static final String FAILURE_MESSAGE = "FAILURE";

	public static void main(String[] args) {
		try (ServerSocket ss = new ServerSocket(0001)) {
			System.out.println("서버가 클라이언트를 기다리는 중...");

			EmployeeManager employeeManager = new EmployeeManager();
			LoginManager loginManager = new LoginManager();

			while (true) {
				Socket socket = ss.accept();
				System.out.println("클라이언트가 연결되었습니다!");

				new Thread(() -> {
					try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {

						// 클라이언트로부터 요청 받기
						String action = br.readLine();
						String id = br.readLine();
						String password = br.readLine();

						System.out.println("Action: " + action + ", ID: " + id + ", Password: " + password);

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

						System.out.println("Result: " + result);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}).start(); // 새로운 스레드에서 클라이언트 요청 처리
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


