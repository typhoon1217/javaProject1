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

    public synchronized void addEmployee(String id, String password) {
        employeeCredentials.put(id, password);
        saveCredentials("staff.txt");
    }

    public synchronized void deleteEmployee(String id) {
        employeeCredentials.remove(id);
        saveCredentials("staff.txt");
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

