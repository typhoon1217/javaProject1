package testClient;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class TestClient {
	private static final String SERVER_IP = "localhost";  // 서버 IP 주소
	public static void main(String[] args) {
		try {
			Socket s = new Socket(SERVER_IP, 6666);

			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("아이디를 입력하세요: ");
			String id = reader.readLine();
			System.out.print("비밀번호를 입력하세요: ");
			String password = reader.readLine();

			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeUTF(id);  // 아이디 입력
			dos.writeUTF(password);  // 비밀번호 입력

			DataInputStream dis = new DataInputStream(s.getInputStream());
			String response = dis.readUTF();

			System.out.println(response);

			s.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
