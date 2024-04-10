package Client_Test;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    private static final String SERVER_IP = "::1"; // 서버 IP 주소

    public static void main(String[] args) {
        try {
            // 서버에 한 번 연결
            Socket s = new Socket(SERVER_IP, 1234);

            // 입력을 받기 위한 BufferedReader 객체 생성
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // 사용 가능한 아이디와 비밀번호 형식 설명 출력
            System.out.println("아이디는 영문자로 시작하고, 영문자와 숫자의 조합으로 최소 3자 이상이어야 합니다.");
            System.out.println("비밀번호는 대소문자 알파벳, 숫자, 특수문자의 조합으로 최소 8자 이상이어야 합니다.");

            boolean loginSuccess = false; // 로그인 성공 여부를 나타내는 플래그
            while (!loginSuccess) {
                // 사용자로부터 아이디 입력 받기
                System.out.print("ID: ");
                String id = reader.readLine();

                // 아이디 형식 검증
                String idRegex = "^[a-z][a-z0-9]{2,}$";
                if (!id.matches(idRegex)) {
                    System.out.println("올바른 형식의 아이디가 아닙니다.");
                    continue; // 아이디 형식이 올바르지 않으면 다시 반복
                }

                // 사용자로부터 비밀번호 입력 받기
                System.out.print("PW: ");
                String password = reader.readLine();

                // 비밀번호 형식 검증
                String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=.*[a-zA-Z0-9]).{8,}$";
                if (!password.matches(passwordRegex)) {
                    System.out.println("올바른 형식의 비밀번호가 아닙니다.");
                    continue; // 비밀번호 형식이 올바르지 않으면 다시 반복
                }

                // 서버로 데이터 전송
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(id);
                dos.writeUTF(password);

                // 서버로부터 응답 받기
                DataInputStream dis = new DataInputStream(s.getInputStream());
                String response = dis.readUTF();

                // 서버로부터 받은 응답 출력
                System.out.println(response);

                // 로그인 성공 여부 확인
                if (response.equals("ADMIN")||response.equals("STAFF")) {   
                    loginSuccess = true; // 로그인 성공 시 플래그 설정하여 반복문 종료
                }
            }

            // 소켓 닫기
            s.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
