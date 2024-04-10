package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import serverAdminManager.LoginManager;

// 서버 클래스
public class Server {
    public static void main(String[] args) {
        try (ServerSocket ss = new ServerSocket(1234)) { // 서버 소켓 생성
            System.out.println("서버가 클라이언트를 기다리는 중...");

            // 각 클라이언트별 로그인 시도 횟수를 관리하는 맵
            Map<Socket, Integer> loginAttempts = new HashMap<>();

            while (true) {
                Socket s = ss.accept(); // 클라이언트의 접속 대기
                System.out.println("클라이언트가 연결되었습니다!");

                // 각 클라이언트 요청을 별도의 스레드로 처리
                new Thread(() -> {
                    try {
                        DataInputStream dis = new DataInputStream(s.getInputStream());

                        int loginFailures = loginAttempts.getOrDefault(s, 0); // 해당 클라이언트의 로그인 시도 횟수 가져오기

                        while (loginFailures < 5) { // 로그인 시도 횟수가 5회 미만인 경우 반복
                            String id = dis.readUTF(); // 클라이언트로부터 아이디 읽기
                            String password = dis.readUTF(); // 클라이언트로부터 비밀번호 읽기
                            System.out.println("ID: " + id + "로부터 로그인 요청을 받았습니다.");

                            DataOutputStream dos = new DataOutputStream(s.getOutputStream()); // 데이터 출력 스트림 생성

                            // 로그인 시도
                            String loginResult = LoginManager.createNewInstance().login(id, password);

                            if (loginResult.equals("ADMIN") || loginResult.equals("STAFF")) {
                                // 로그인 성공 시 반복문 종료
                                dos.writeUTF(loginResult); // 클라이언트에게 로그인 결과 전송
                                System.out.println("로그인 결과 (" + loginResult + ")를 클라이언트에게 전송했습니다.");
                                break;
                            } else {
                                // 로그인 실패 시 실패 횟수 증가
                                loginFailures++;
                                // 클라이언트별 로그인 시도 횟수 업데이트
                                loginAttempts.put(s, loginFailures);
                                dos.writeUTF(loginResult); // 클라이언트에게 로그인 결과 전송
                                System.out.println("로그인 결과 (" + loginResult + ")를 클라이언트에게 전송했습니다.");
                            }
                        }//end of while2

                        // 로그인 실패 횟수가 5회를 초과한 경우
                        if (loginFailures >= 5) {
                            System.out.println("로그인 시도 횟수가 초과되었습니다. 클라이언트 접속을 종료합니다.");
                        }

                        s.close(); // 클라이언트 소켓 닫기
                    } catch (Exception e) {  //try1 end
                        System.out.println(e);
                        try {
                            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                            dos.writeUTF("ERROR"); // 클라이언트에게 에러 정보 전송
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start(); // 새로운 스레드 시작
            }//end of while1
        } catch (Exception e) {		//try2 end
            System.out.println(e);
        }
    }
}
