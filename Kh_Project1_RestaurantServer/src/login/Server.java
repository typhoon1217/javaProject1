package login;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

// 서버 코드
public class Server {  
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(1217);
            Socket s = ss.accept();

            DataInputStream dis = new DataInputStream(s.getInputStream());

            String id = dis.readUTF();
            String password = dis.readUTF();

            LoginManager loginManager = LoginManager.getInstance();
            String loginResult = loginManager.login(id, password);

            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            dos.writeUTF(loginResult);

            ss.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}



