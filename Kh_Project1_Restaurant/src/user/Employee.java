package user;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Employee implements LogInInterface {
    private class EmployeeInfo {
        String id;
        String password;

        EmployeeInfo(String id, String password) {
            this.id = id;
            this.password = password;
        }
    }

    private ArrayList<EmployeeInfo> employees = new ArrayList<>();

    public Employee() {
        loadEmployees();
    }

    private void loadEmployees() {
        try {
            File file = new File("employees.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String[] userData = sc.nextLine().split(",");
                employees.add(new EmployeeInfo(userData[0], userData[1]));
            }

            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    } 

    @Override
    public boolean logIn(User user) {
        for (EmployeeInfo employee : employees) {
            if (employee.id.equals(user.getUserId()) && employee.password.equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }
}
