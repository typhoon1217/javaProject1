package serverAdminManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// 로그인 관리자 클래스
public class LoginManager {
    // 로그인 정보를 저장하는 해시맵
    private Map<String, String> adminCredentials;
    private Map<String, String> staffCredentials;

    // 생성자: 로그인 정보를 로드함
    public LoginManager() {
        adminCredentials = new HashMap<>();
        staffCredentials = new HashMap<>();
        loadCredentials("admin.txt", adminCredentials);
        loadCredentials("staff.txt", staffCredentials);
    }

    // 파일읽어서 해시맵에 저장하는 메서드
    public void loadCredentials(String filePath, Map<String, String> credentials) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                credentials.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 로그인 메서드
    public String login(String id, String password) {
        if (adminCredentials.containsKey(id) && adminCredentials.get(id).equals(password)) {
            return "ADMIN";
        } else if (staffCredentials.containsKey(id) && staffCredentials.get(id).equals(password)) {
            return "STAFF";
        } else {
            return "FAILURE";
        }
    }

    // 인스턴스 생성 메서드
    public static LoginManager createNewInstance() {
        return new LoginManager();
    }
}
