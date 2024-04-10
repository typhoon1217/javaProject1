package login;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// 로그인 관리자 클래스
public class LoginManager {
    private Admin admin;
    private Staff user;

    public LoginManager() {
        // 관리자 정보와 일반 사용자 정보를 텍스트 파일에서 불러옵니다.
        try {
            BufferedReader adminReader = new BufferedReader(new FileReader("admin.txt"));
            String adminId = adminReader.readLine();
            String adminPassword = adminReader.readLine();
            admin = new Admin(adminId, adminPassword);

            BufferedReader userReader = new BufferedReader(new FileReader("user.txt"));
            String userId = userReader.readLine();
            String userPassword = userReader.readLine();
            user = new Staff(userId, userPassword);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String login(String id, String password) {
        if (admin.LogIn(id, password)) {
            return "관리자 로그인 성공";
        } else if (user.LogIn(id, password)) {
            return "사용자 로그인 성공";
        } else {
            return "로그인 실패";
        }
    }
}
