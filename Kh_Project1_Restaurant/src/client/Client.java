package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import main.RestaurantMain;

public class Client {
	private static final String SERVER_IP = "::1"; // 서버 IP 주소
	private static final int SERVER_PORT = 1111; // 서버 포트 번호


	
	//기능 테스트용
//	public static void main(String[] args) {
//		login();
//		adminClient();
//	}
	
	// 로그인 클라이언트
	public static void login() {
		try {
			// 서버에 연결
			Socket socket = new Socket(SERVER_IP, SERVER_PORT);

			// 사용자 입력을 받기 위한 BufferedReader 객체 생성
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

			// 아이디와 비밀번호 입력 요청 출력
			System.out.println("로그인 하시오");
			System.out.println(RestaurantMain.BAR);

			// 로그인 시도
			loginProcess(socket, reader);

			// 소켓 닫기
			socket.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// 로그인 과정을 처리하는 메서드
	private static void loginProcess(Socket socket, BufferedReader reader) {
		try {
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			DataInputStream dis = new DataInputStream(socket.getInputStream());

			// 사용자로부터 아이디 입력 받기
			System.out.print("ID: ");
			String id = reader.readLine();
			System.out.println(RestaurantMain.BAR);

			// 사용자로부터 비밀번호 입력 받기
			System.out.print("PW: ");
			String password = reader.readLine();
			System.out.println(RestaurantMain.BAR);

			// 아이디와 비밀번호를 서버로 전송
			dos.writeUTF(id);
			dos.writeUTF(password);

			// 서버로부터 응답 받기
			String response = dis.readUTF();

			// 서버 응답 출력
			System.out.println(response);

			// 로그인 성공 여부 확인
			if (response.equals("ADMIN")) {
				System.out.println("관리자로 로그인하셨습니다.");
				adminClient();	
				//RestaurantMain.AdminMenu();//관리자 메뉴
			} else if (response.equals("STAFF")) {
				System.out.println("직원으로 로그인하셨습니다.");
				RestaurantMain.introMenu();
				//RestaurantMain.introMenu();			//메인메뉴
				System.out.println(RestaurantMain.BAR);
			} else {
				System.out.println("로그인 정보가 올바르지 않습니다. 다시 시도해주세요.");
				login(); 							// 실패한 경우 다시 로그인을 호출
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	//관리자 클라이언트
	public static void adminClient() {
		try (Socket socket = new Socket("::1", 0001)) {
			PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// 액션 입력 받기 (1 또는 2만 허용)
			String action;
			while (true) {
				System.out.print("직원 추가: 1, 직원 삭제: 2를 입력하세요: ");
				action = RestaurantMain.sc.nextLine();
				if (action.equals("1") || action.equals("2")) {
					break;
				} else {
					System.out.println("1 또는 2를 입력하세요.");
				}
			}

			// 아이디 입력 받기 (알파벳과 숫자로만 구성, 최소 4글자)
			String id;
			while (true) {
				System.out.print("아이디를 입력하세요 (알파벳과 숫자로만 구성, 최소 4글자): ");
				id = RestaurantMain.sc.nextLine();
				if (id.matches("[a-zA-Z0-9]+") && id.length() >= 4) {
					break;
				} else {
					System.out.println("아이디는 알파벳과 숫자로만 구성되어야 하며, 최소 4글자여야 합니다.");
				}
			}

			// 직원 삭제인 경우 비밀번호를 묻지 않음
			String password = "";
			if (!action.equals("2")) {
				// 비밀번호 입력 받기 (알파벳과 숫자로만 구성, 최소 6글자)
				while (true) {
					System.out.print("비밀번호를 입력하세요 (알파벳과 숫자로만 구성, 최소 6글자): ");
					password = RestaurantMain.sc.nextLine();
					if (password.matches("[a-zA-Z0-9]+") && password.length() >= 6) {
						break;
					} else {
						System.out.println("비밀번호는 알파벳과 숫자로만 구성되어야 하며, 최소 6글자여야 합니다.");
					}
				}
			}

			// 서버로 요청 보내기
			pw.println(action);
			pw.println(id);
			pw.println(password);

			// 서버로부터 응답 받기
			String response = br.readLine();
			System.out.println("서버로부터의 응답: " + response);

			// 응답에 따라 적절한 메소드 호출
			if (response.equals("SUCCESS")) {
				if (action.equals("1")) {
					System.out.println("직원 추가 성공");
					RestaurantMain.introMenu();
				} else if (action.equals("2")) {
					System.out.println("직원 삭제 성공");
					RestaurantMain.introMenu();
				}
			} else {
				System.out.println("작업 실패");
				adminClient();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

