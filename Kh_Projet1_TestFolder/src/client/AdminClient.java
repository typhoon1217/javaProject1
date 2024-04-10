package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class AdminClient {
    public static void main(String[] args) {
    	// 클라이언트
    	try (Socket socket = new Socket("localhost", 1234)) {
    	    PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
    	    Scanner scanner = new Scanner(System.in);
    	    
    	    System.out.print("직원추가:1 직원삭제:2): ");
    	    String action = scanner.nextLine();
    	    System.out.print("아이디를 입력하세요: ");
    	    String id = scanner.nextLine();
    	    System.out.print("비밀번호를 입력하세요: ");
    	    String password = scanner.nextLine();
    	    
    	    pw.println(action);
    	    pw.println(id);
    	    pw.println(password);
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}

    }
}
