package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class AdminClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1234)) {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            
            // 사용자로부터 액션 선택 받기
            System.out.println("사용할 액션을 선택하세요:");
            System.out.println("1. 직원 추가 (ADD_EMPLOYEE)");
            System.out.println("2. 직원 삭제 (DELETE_EMPLOYEE)");
            System.out.print("액션을 선택하세요 (1 또는 2): ");
            
            int choice = Integer.parseInt(reader.readLine());
            String action = (choice == 1) ? "ADD_EMPLOYEE" : "DELETE_EMPLOYEE";
            
            // 사용자로부터 아이디, 비밀번호 입력 받기
            System.out.print("아이디를 입력하세요: ");
            String id = reader.readLine();
            System.out.print("비밀번호를 입력하세요: ");
            String password = reader.readLine();
            
            // 서버에 액션, 아이디, 비밀번호 전송
            dos.writeUTF(action);
            dos.writeUTF(id);
            dos.writeUTF(password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
