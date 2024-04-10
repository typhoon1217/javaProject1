package login;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

// 서버 코드
public class Server {
    public static void main(String[] args) {
        try (ServerSocket ss = new ServerSocket(1234)) {// 서버 소켓 생성
            System.out.println("서버가 클라이언트를 기다리는 중...");

            Map<Socket, Integer> loginAttempts = new HashMap<>(); // 클라이언트별 로그인 시도 횟수를 관리하는 맵

            while (true) {
                // 클라이언트 접속 대기
                Socket s = ss.accept();
                System.out.println("클라이언트가 연결되었습니다!");

                // 새로운 스레드에서 클라이언트 요청 처리
                new Thread(() -> {
                    int loginFailures = 0; // 클라이언트의 로그인 실패 횟수 초기화
                    try {
                        // 데이터 입력 스트림 생성
                        DataInputStream dis = new DataInputStream(s.getInputStream());

                        while (loginFailures < 5) { // 5번의 로그인 실패까지 반복
                            // 클라이언트로부터 아이디와 비밀번호 받기
                            String id = dis.readUTF();
                            String password = dis.readUTF();
                            System.out.println("ID: " + id + "로부터 로그인 요청을 받았습니다.");

                            // 로그인 매니저 인스턴스 가져오기
                            LoginManager loginManager = LoginManager.getInstance();

                            // 로그인 매니저 로그인 결과 호출
                            String loginResult = loginManager.Login(id, password);

                            // 데이터 출력 스트림 생성
                            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                            if (loginResult.equals("ADMIN")) {
                                // 관리자 로그인 성공 시 반복문 종료
                                dos.writeUTF(loginResult);
                                System.out.println("로그인 결과 (" + loginResult + ")를 클라이언트에게 전송했습니다.");
                                break;
                            } else if (loginResult.equals("STAFF")) {
                                // 스태프 로그인 성공 시 반복문 종료
                                dos.writeUTF(loginResult);
                                System.out.println("로그인 결과 (" + loginResult + ")를 클라이언트에게 전송했습니다.");
                                break;
                            } else {
                                // 로그인 실패 시 실패 횟수 증가
                                loginFailures++;

                                // 클라이언트별 로그인 시도 횟수 업데이트
                                loginAttempts.put(s, loginFailures);

                                dos.writeUTF(loginResult);
                                System.out.println("로그인 결과 (" + loginResult + ")를 클라이언트에게 전송했습니다.");
                            }
                        }
                        // 소켓 닫기
                        s.close();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }).start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

