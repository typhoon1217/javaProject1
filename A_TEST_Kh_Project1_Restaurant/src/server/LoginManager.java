package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



// 로그인 관리자 클래스
public class LoginManager {
    // 로그인 정보를 저장하는 해시맵
    private Map<String, String> adminCredentials;
    private Map<String, String> staffCredentials;

    // 생성자: 로그인 정보를 로드함
    public  LoginManager() {
//    	if (adminCredentials != null) {
//    	    adminCredentials.clear();
//    	}
//    	if (staffCredentials != null) {
//    	    staffCredentials.clear();
//    	}
        adminCredentials = new HashMap<>();
        staffCredentials = new HashMap<>();
        loadCredentials("admin.txt", adminCredentials);
        System.out.println("관리자 정보 로드");
        loadCredentials("staff.txt", staffCredentials);
        System.out.println("스태프 정보 로드");
    }

    // 파일읽어서 해시맵에 저장하는 메서드
    public void loadCredentials(String filePath, Map<String, String> credentials) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                credentials.put(parts[0], parts[1]);
                System.out.println("정보 로드");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("정보 로드 에러");
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

    public synchronized boolean addEmployee(String id, String password) {
        if (!adminCredentials.containsKey(id)&&!staffCredentials.containsKey(id)) {
        	staffCredentials.put(id, password);
            saveCredentials("staff.txt");
            System.out.println("---직원 추가 성공");
            return true; // 성공
        } else {
            System.out.println("---직원 추가 실패");
            return false; // 실패: 이미 존재하는 ID
    }
    }

    public synchronized boolean deleteEmployee(String id) {
        if (staffCredentials.containsKey(id)) {
        	staffCredentials.remove(id);
            saveCredentials("staff.txt");

            System.out.println("---직원 삭제 성공");
            return true; // 성공
        } else {

            System.out.println("---직원 삭제 실패");
            return false; // 실패: 해당 ID가 존재하지 않음
        }
    }

    private synchronized void saveCredentials(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, String> entry : staffCredentials.entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue());
                writer.newLine();
                System.out.println("직원 정보 변경 적용");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("직원 정보 변경 적용 오류");
        }
    }
}

