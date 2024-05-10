package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import main.Main;
import main.Setting;

public class Client {
	private static Socket socket;
	
	//기능 테스트용
	//public static void main(String[] args) {
		//loginClient();
		//adminClient(socket);
	//}
	


	// 로그인 클라이언트
	public static void loginClient() { 
		try {
			socket = new Socket(Setting.SERVER_IP, Setting.LOG_IN_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean isLoginSuccess =false;
		while(!isLoginSuccess){
			try {
				// 로그인 시도
				System.out.println(Main.BAR);
				System.out.println("|로그인|");
				isLoginSuccess=loginProcess(socket);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Main.introMenu();
		//System.out.println("가 종료되었습니다.");
	}

	// 로그인 과정처리+서버통신
	private static boolean loginProcess(Socket s) {
		boolean login = false;
		try {
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			DataInputStream dis = new DataInputStream(s.getInputStream());
			while(!login) {
				// 사용자로부터 아이디 입력 받기
				System.out.print("ID: ");
				String id = Main.sc.nextLine();
				System.out.println(Main.BAR);

				// 사용자로부터 비밀번호 입력 받기
				System.out.print("PW: ");
				String password = Main.sc.nextLine();
				System.out.println(Main.BAR);

				// 아이디와 비밀번호를 서버로 전송
				dos.writeUTF(id);
				dos.writeUTF(password);
				dos.flush();
				System.out.println("로그인 정보를 전송 했습니다.");

				// 서버로부터 응답 받기
				String response = dis.readUTF();



				// 서버 응답 출력
				System.out.println(response);

				// 로그인 성공 여부 확인
				if (response.equals("ADMIN")) {
					System.out.println("관리자로 로그인하셨습니다.");
					adminClient(s);
					login=true;
				} else if (response.equals("STAFF")) {
					System.out.println("직원으로 로그인하셨습니다.");
					login=true;
				} else {
					System.out.println("로그인 정보가 올바르지 않습니다. 다시 시도해주세요.");
					login=false;
				}
			}

		} catch (Exception e) {
			System.out.println(e);
			login=false;
		}
		return login;
	}

	//관리자 클라이언트                   
	public static void adminClient(Socket s) throws IOException {
		boolean flag = false;;
		while (!flag) {
			System.out.println("1.직원 추가,2,직원 삭제,3,메인 메뉴 ");
			String action = Main.sc.nextLine();
			System.out.println(Main.BAR);
			switch (action) {
			case "1": 
				System.out.println("직원 추가");
				adminProcess(1,s); 
				break;
			case "2":
				System.out.println("직원 삭제");
				adminProcess(2,s);
				break;
			case "3":
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				//dos.writeInt(3);
				flag=true;
				break;
			default:
				System.out.println("1, 2, 또는 3을 입력하세요.");
				System.out.println(Main.BAR);
			}
		}
	}
	
	public static void adminProcess(int choice, Socket socket) {

	    try {
	        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
	        DataInputStream dis = new DataInputStream(socket.getInputStream());

            dos.writeInt(choice);
            
	        if(choice != 3) {
	            // 아이디 입력 받기
	            String idOut = enterId();

	            // 직원 삭제인 경우 비밀번호를 묻지 않음
	            String pwOut = "";
	            if (choice == 1) {
	                // 비밀번호 입력 받기
	                pwOut = enterPassword();
	            }

	            // 서버로 요청 보내기
	            dos.writeUTF(idOut);
	            dos.writeUTF(pwOut);

	            // 서버로부터 응답 받기
	            boolean response = dis.readBoolean();
	            System.out.println("서버로부터의 응답: " + response);

	            // 응답에 따라 적절한 메소드 호출
	            if (response) {
	                if (choice == 1) {
	                    System.out.println("직원 추가 성공");
	                } else if (choice == 2) {
	                    System.out.println("직원 삭제 성공");
	                }
	            } else {
	                if (choice == 1) {
	                    System.out.println("직원 추가 실패");
	                } else if (choice == 2) {
	                    System.out.println("직원 삭제 실패");
	                }
	            }
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


	// 아이디 입력 메소드
	public static String enterId() {
		String id;
		while (true) {
			System.out.print("아이디를 입력하세요 (알파벳과 숫자로만 구성, 최소 4글자): ");
			id = Main.sc.nextLine();
			if (id.matches("[a-zA-Z0-9]+") && id.length() >= 4) {
				break;
			} else {
				System.out.println("아이디는 알파벳과 숫자로만 구성되어야 하며, 최소 4글자여야 합니다.");
			}
			System.out.println(Main.BAR);
		}
		return id;
	}

	// 비밀번호 입력 메소드
	public static String enterPassword() {
		String password;
		while (true) {
			System.out.print("비밀번호를 입력하세요 (알파벳과 숫자로만 구성, 최소 6글자): ");
			password = Main.sc.nextLine();
			if (password.matches("[a-zA-Z0-9]+") && password.length() >= 6) {
				break;
			} else {
				System.out.println("비밀번호는 알파벳과 숫자로만 구성되어야 하며, 최소 6글자여야 합니다.");
			}
		}
		System.out.println(Main.BAR);
		return password;
	}
}
