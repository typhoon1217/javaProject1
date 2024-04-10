package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class AdminClient {
	public static void main(String[] args) {
		try (Socket socket = new Socket("::1", 0001)) {
			PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			Scanner scanner = new Scanner(System.in);

			// 액션 입력 받기 (1 또는 2만 허용)
			String action;
			while (true) {
				System.out.print("직원 추가: 1, 직원 삭제: 2를 입력하세요: ");
				action = scanner.nextLine();
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
				id = scanner.nextLine();
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
					password = scanner.nextLine();
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
					// 추가()
				} else if (action.equals("2")) {
					System.out.println("직원 삭제 성공");
					// 추가()
				}
			} else {
				System.out.println("작업 실패");
				// 추가()
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
