package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// 로그인 관리자 클래스
public class LoginManager {
    private Admin admin;
    private Staff staff;

    public LoginManager() {
        // 관리자 정보와 일반 사용자 정보를 텍스트 파일에서 불러옵니다.
        try (BufferedReader adminReader = new BufferedReader(new FileReader("admin.txt"))) {
            String adminId = adminReader.readLine();
            String adminPassword = adminReader.readLine();
            admin = new Admin(adminId, adminPassword);

            try (BufferedReader staffReader = new BufferedReader(new FileReader("staff.txt"))) {
				String staffId = staffReader.readLine();
				String staffPassword = staffReader.readLine();
				staff = new Staff(staffId, staffPassword);
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    public String login(String id, String password) {
        if (admin.LogIn(id, password)) {
            return "ADMIN";
        } else if (staff.LogIn(id, password)) {
            return "STAFF";
        } else {
            return "FAILL";
        }
    }

    // 새로운 인스턴스 생성 메서드
    public static LoginManager createNewInstance() {
        return new LoginManager();
    }
}
