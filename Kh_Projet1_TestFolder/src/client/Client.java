package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    private static final String SERVER_IP = "::1"; // 서버 IP 주소
    private static final int SERVER_PORT = 1234; // 서버 포트 번호

    // 클라이언트 시작 메서드
    public static void Login() {
        try {
            // 서버에 연결
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);

            // 사용자 입력을 받기 위한 BufferedReader 객체 생성
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // 아이디와 비밀번호 입력 요청 출력
            System.out.println("로그인 하시오");
            System.out.println(ClientUI.BAR);

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

            // 사용자로부터 비밀번호 입력 받기
            System.out.print("PW: ");
            String password = reader.readLine();

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
                ClientUI.AdminMenu();	
                //RestaurantMain.AdminMenu();//관리자 메뉴
            } else if (response.equals("STAFF")) {
                System.out.println("직원으로 로그인하셨습니다.");
                ClientUI.introMenu();
                //RestaurantMain.introMenu();			//메인메뉴
            	System.out.println(ClientUI.BAR);
            } else {
                System.out.println("로그인 정보가 올바르지 않습니다. 다시 시도해주세요.");
                Login(); 							// 실패한 경우 다시 로그인을 호출
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

