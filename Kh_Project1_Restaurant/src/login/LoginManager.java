package login;

import java.util.HashMap;
import java.util.Map;

public class LoginManager {
    private static LoginManager instance;
    private Map<String, User> userMap;

    // 싱글톤 패턴
    private LoginManager() {
        // 해시맵 초기화
        userMap = new HashMap<>();
        // 사용자 정보 추가
        userMap.put("admin", new Admin("admin", "admin123"));
        userMap.put("staff", new Staff("staff", "staff123"));
    }

    //  getInstance 정적 메서드
    public static synchronized LoginManager getInstance() {
        if (instance == null) {
            instance = new LoginManager();
        }
        return instance;
    }

    // 로그인 시도 메서드
    public String Login(String id, String password) {
        // 사용자가 존재하는지 확인 후 로그인 시도
        User user = userMap.get(id);										//ID Key 호출
        if (user instanceof Admin && user.LogIn(id, password)) {			//ID&PW 로 Admin의 로그인 메서드 실행
            return "ADMIN 로그인 성공";										
        } else if (user instanceof Staff && user.LogIn(id, password)) {		//ID&PW 로 Staff의 로그인 메서드 실행
            return "STAFF 로그인 성공";
        } else {
            return "로그인 실패";												//로그인 실패
        }
    }

}//end of class
