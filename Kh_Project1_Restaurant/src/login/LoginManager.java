package login;

import java.util.HashMap;

public class LoginManager {
    private static LoginManager instance = null;
    //로그인 정보는 아이디(키값)을 입력받아 비밀번호를 매치하기 좋은 구조라고 생각됨. 해쉬맵 사용.
    private HashMap<String, String> users;
	
    //싱글톤 패턴 사용    
    private LoginManager() {
        users = new HashMap<>();
        // 초기 사용자 추가
        users.put("admin", "admin");
        users.put("1", "1");
        users.put("3", "2");
    }
    public static LoginManager getInstance() {
        if (instance == null) {
            instance = new LoginManager();
        }
        return instance;
    }
    
    // 로그인 시도 
    public String login(String id, String password) {
        String correctPassword = users.get(id);
        if (correctPassword != null && correctPassword.equals(password)) {
            return "로그인 성공";
        }
        return "로그인 실패";
    }
}
