package serverAdminManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// 직원 정보를 관리하는 클래스
public class EmployeeManager {
    private Map<String, String> employeeCredentials;

    public EmployeeManager() {
        employeeCredentials = new HashMap<>();
        loadCredentials("staff.txt");
    }

    private synchronized void loadCredentials(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                employeeCredentials.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean addEmployee(String id, String password) {
        if (!employeeCredentials.containsKey(id)) {
            employeeCredentials.put(id, password);
            saveCredentials("staff.txt");
            return true; // 성공
        } else {
            return false; // 실패: 이미 존재하는 ID
    }
    }

    public synchronized boolean deleteEmployee(String id) {
        if (employeeCredentials.containsKey(id)) {
            employeeCredentials.remove(id);
            saveCredentials("staff.txt");
            return true; // 성공
        } else {
            return false; // 실패: 해당 ID가 존재하지 않음
        }
    }


    private synchronized void saveCredentials(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, String> entry : employeeCredentials.entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

