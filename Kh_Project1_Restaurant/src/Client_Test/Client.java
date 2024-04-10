package Client_Test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
	private static final String SERVER_IP = "::1";  				// 서버 IP 주소
	public static void main(String[] args) {
		try {
			Socket s = new Socket(SERVER_IP, 1234);

			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("ID: ");
			String id = reader.readLine();
			System.out.print("PW: ");
			String password = reader.readLine();

			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeUTF(id);											
			dos.writeUTF(password);										

			DataInputStream dis = new DataInputStream(s.getInputStream());
			String response = dis.readUTF();

			System.out.println(response);

			s.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
