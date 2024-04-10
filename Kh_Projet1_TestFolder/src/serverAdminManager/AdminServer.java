package serverAdminManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

//서버 클래스
public class AdminServer {
 private static final String SUCCESS_MESSAGE = "SUCCESS";

 public static void main(String[] args) {
     try (ServerSocket ss = new ServerSocket(1234)) {
         System.out.println("서버가 클라이언트를 기다리는 중...");

         EmployeeManager employeeManager = new EmployeeManager();
         LoginManager loginManager = new LoginManager();

         while (true) {
             Socket socket = ss.accept();
             System.out.println("클라이언트가 연결되었습니다!");

             new Thread(() -> {
                 try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                      PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {

                     String action = br.readLine();
                     String id = br.readLine();
                     String password = br.readLine();

                     System.out.println("Action: " + action + ", ID: " + id + ", Password: " + password);

                     String result = "";
                     if (action.equals("1")) {
                         employeeManager.addEmployee(id, password);
                         result = SUCCESS_MESSAGE;
                     } else if (action.equals("2")) {
                         employeeManager.deleteEmployee(id);
                         result = SUCCESS_MESSAGE;
                     } else {
                         result = loginManager.login(id, password);
                     }

                     pw.println(result);

                     System.out.println("Result: " + result);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }).start(); // Thread 생성 코드 블록을 닫는 중괄호 추가
         }
     } catch (IOException e) {
         e.printStackTrace();
     }
 }
}
