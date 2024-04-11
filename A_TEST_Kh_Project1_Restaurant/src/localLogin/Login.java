package localLogin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.Main;
import main.Setting;

public class Login {



	//0.0 관리자메뉴

	public static void AdminMenu() {
		System.out.println(Main.BAR);
		System.out.println("관리자 메뉴를 선택하세요:");
		System.out.println("1. 직원 관리");
		System.out.println("2. 메뉴 관리");
		System.out.println("3. 메인 메뉴");
		System.out.println(Main.BAR);

		String choice;
		while (true) {
			choice = Main.sc.nextLine();
			if (choice.matches("[123]")) {  // 정규식을 사용하여 1, 2, 3 중 하나인지 확인합니다.
				break;
			} else {
				System.out.println("잘못된 입력입니다. 1, 2, 3 중 하나를 입력해주세요.");
			}
		}

		switch (choice) {
		case "1":
			manageUsers();
			break;
		case "2":
			//ㅇㅇㅇㅇ();
			break;
		case "3":
			System.out.println(Main.BAR);
			System.out.println("메인메뉴 실행.");
			Main.introMenu();	
		}
	}
	public static void login() {
		try {
			boolean loggedIn = false;
			while (!loggedIn) {
				int count = 0;
				while (count < Setting.MAX_ATTEMPTS) {
					System.out.println(Main.BAR);
					System.out.println("아이디를 입력하세요:");
					System.out.println(Main.BAR);
					String id = Main.sc.nextLine();
					System.out.println(Main.BAR);
					System.out.println("비밀번호를 입력하세요:");
					System.out.println(Main.BAR);
					String password = Main.sc.nextLine();
					System.out.println(Main.BAR);

					User user = new User(id, password);
					LogInInterface adminLogin = new Admin();

					if (adminLogin.logIn(user)) {
						System.out.println("관리자 로그인 성공");
						AdminMenu();
						loggedIn = true;
					} else {
						LogInInterface employeeLogin = new Employee();
						if (employeeLogin.logIn(user)) {
							System.out.println("직원 로그인 성공");
							Main.introMenu();	
							loggedIn = true;
						} else {
							System.out.println("로그인 실패");
							user.resetCredentials();  // 로그인 실패 시, 아이디와 비밀번호를 리셋합니다.
							count++;
							if (count == Setting.MAX_ATTEMPTS) {
								System.out.println("로그인 시도 횟수 초과. 프로그램을 종료합니다.");
								loggedIn = false;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e+"에러가 발생했습니다. 로그인을 재시도합니다.");
		}
	}
	//0.1 유저관리 메서드

	public static void manageUsers() {
		try {
			List<String> employees = getEmployees();  // 직원 목록을 가져옵니다.
			for (String employee : employees) {
				System.out.println(employee);  // 각 직원의 정보를 출력합니다.
			}

			System.out.println(Main.BAR);
			System.out.println("1. 직원 추가");
			System.out.println("2. 직원 삭제");

			String choice;
			while (true) {
				choice = Main.sc.nextLine();
				if (choice.matches("[12]")) {  // 정규식을 사용하여 1 또는 2인지 확인합니다.
					break;
				} else {
					System.out.println("잘못된 입력입니다. 1 또는 2를 입력해주세요.");
				}
			}
			switch (choice) {
			case "1":
				addUser();
				break;
			case "2":
				deleteUser();
				break;
			}
			AdminMenu();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//0.1.1 직원 추가 메서드

	public static void addUser() throws IOException {
		System.out.println(Main.BAR);
		System.out.println("추가할 직원의 아이디를 입력하세요:");
		String userId = Main.sc.nextLine();
		System.out.println("추가할 직원의 비밀번호를 입력하세요:");
		String password = Main.sc.nextLine();

		List<String> employees = getEmployees();
		for (String employee : employees) {
			if (employee.split(",")[0].equals(userId)) {
				System.out.println("이미 존재하는 아이디입니다.");
				return;
			}
		}

		BufferedWriter writer = new BufferedWriter(new FileWriter("employees.txt", true));
		writer.write(userId + "," + password);
		writer.newLine();
		writer.close();
		System.out.println("직원 추가 성공");
	}

	//0.1.2 직원 삭제 메서드	

	public static void deleteUser() throws IOException {
		System.out.println(Main.BAR);
		System.out.println("삭제할 직원의 아이디를 입력하세요:");
		String userId = Main.sc.nextLine();

		List<String> employees = getEmployees();
		employees.removeIf(employee -> employee.split(",")[0].equals(userId));

		BufferedWriter writer = new BufferedWriter(new FileWriter("employees.txt"));
		for (String employee : employees) {
			writer.write(employee);
			writer.newLine();
		}
		writer.close();
		System.out.println("직원 삭제 성공");
	}

	//#.0 직원 목록 

	public static List<String> getEmployees() throws IOException {
		List<String> employees = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader("employees.txt"));
		String line;
		while ((line = reader.readLine()) != null) {
			employees.add(line);
		}
		reader.close();
		return employees;
	}
}
