package login;

import java.util.HashMap;
import java.util.Map;

public class LoginManager {
    private static LoginManager instance;
    private Map<String, User> userMap;
    private Map<String, Integer> loginAttempts; // 로그인 시도 횟수를 저장할 맵

    // 싱글톤 패턴
    private LoginManager() {
        // 해시맵 초기화
        userMap = new HashMap<>();
        // 사용자 정보 추가
        userMap.put("admin", new Admin("admin", "Admin123!"));
        userMap.put("staff", new Staff("staff", "Staff123!"));

        loginAttempts = new HashMap<>(); // 로그인 시도 횟수 맵 초기화
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
        //정규식: 또 쓰는 이유: 클라이언트에서 이상한짓을 하진 않을까? 이 프로그램엔 필요 없지만 다른 보안이 중요한 로그인에 필요할듯함.
        String idRegex = "^[a-z][a-z0-9]{2,}$";
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=.*[a-zA-Z0-9]).{8,}$";
        // ID Key 호출 사용자가 존재하는지 확인 후 로그인 시도
        User user = userMap.get(id); 														
        if (user != null && id.matches(idRegex) && password.matches(passwordRegex)) {
            // ID&PW 로 Admin의 로그인 메서드 실행
            if (user instanceof Admin && user.LogIn(id, password)) {
                return "ADMIN";
            // ID&PW 로 Staff의 로그인 메서드 실행
            } else if (user instanceof Staff && user.LogIn(id, password)) {
                return "STAFF";
            }
        }
        return "FAIL"; 
    }
}
