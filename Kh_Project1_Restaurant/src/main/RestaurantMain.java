package main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import Food.Food;
import Food.FoodList;
import order.OrderManager;

public class RestaurantMain {
	
	private static SettingManager settingManager = SettingManager.getInstance();
	private static FoodList foodList = FoodList.getInstance();
	private static OrderManager orderManager = new OrderManager();
	private static Scanner sc = new Scanner(System.in); 
	private static StringBuffer tableNumber = new StringBuffer();
	public static final String BAR = "=========================================";
 
	//메인 
	
	public static void main(String[] args) {
		reset();
	}
	
	//초기화면

	public static void reset() {			
		//login();							//로그인 
		//최초 선택창
		//기능 테스트용
		//introMenu();	                    //초기메뉴
		AdminMenu();						//관리자 메뉴
		//menuLoadTable();                  //주문메뉴
		
	}

	//로그인 
	
//	public static void login() {
//		try {
//			boolean loggedIn = false;
//			while (!loggedIn) {
//				int count = 0;
//				while (count < SettingManager.MAX_ATTEMPTS) {
//					System.out.println(BAR);
//					System.out.println("아이디를 입력하세요:");
//					System.out.println(BAR);
//					String id = sc.nextLine();
//					System.out.println(BAR);
//					System.out.println("비밀번호를 입력하세요:");
//					System.out.println(BAR);
//					String password = sc.nextLine();
//					System.out.println(BAR);
//
//					User user = new User(id, password);
//					LogInInterface adminLogin = new Admin();
//
//					if (adminLogin.logIn(user)) {
//						System.out.println("관리자 로그인 성공");
//						AdminMenu();
//						loggedIn = true;
//					} else {
//						LogInInterface employeeLogin = new Employee();
//						if (employeeLogin.logIn(user)) {
//							System.out.println("직원 로그인 성공");
//							introMenu();	
//							loggedIn = true;
//						} else {
//							System.out.println("로그인 실패");
//							user.resetCredentials();  // 로그인 실패 시, 아이디와 비밀번호를 리셋합니다.
//							count++;
//							if (count == SettingManager.MAX_ATTEMPTS) {
//								System.out.println("로그인 시도 횟수 초과. 프로그램을 종료합니다.");
//								loggedIn = false;
//							}
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			System.out.println(e+"에러가 발생했습니다. 로그인을 재시도합니다.");
//		}
//	}
	
	//0.0 관리자메뉴

	public static void AdminMenu() {
		System.out.println(BAR);
		System.out.println("관리자 메뉴를 선택하세요:");
		System.out.println("1. 직원 관리");
		System.out.println("2. 메뉴 관리");
		System.out.println("3. 메인 메뉴");
		System.out.println(BAR);

		String choice;
		while (true) {
			choice = sc.nextLine();
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
			System.out.println(BAR);
			System.out.println("메인메뉴 실행.");
			introMenu();	
		}
	}

	//0.1 유저관리 메서드

	public static void manageUsers() {
		try {
			List<String> employees = getEmployees();  // 직원 목록을 가져옵니다.
			for (String employee : employees) {
				System.out.println(employee);  // 각 직원의 정보를 출력합니다.
			}

			System.out.println(BAR);
			System.out.println("1. 직원 추가");
			System.out.println("2. 직원 삭제");

			String choice;
			while (true) {
				choice = sc.nextLine();
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
		System.out.println(BAR);
		System.out.println("추가할 직원의 아이디를 입력하세요:");
		String userId = sc.nextLine();
		System.out.println("추가할 직원의 비밀번호를 입력하세요:");
		String password = sc.nextLine();

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
		System.out.println(BAR);
		System.out.println("삭제할 직원의 아이디를 입력하세요:");
		String userId = sc.nextLine();

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

	//#.0 직원 목록 메서드

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
	
	//인트로 메뉴

	public static void introMenu() {
		settingManager.listAllTable();
		String greeting = "안녕하세요 " + SettingManager.R_NAME + "에 방문해주셔서 감사합니다.";
		String regexMenu = "([0-4])"; // 입력값 범위
		boolean flag = false;
		while (!flag) {
			System.out.println(BAR);
			System.out.println(greeting);
			System.out.println(BAR);
			System.out.println("0. 테이블 변경\t\t1. 테이블 배정");
			System.out.println("2. 주문 받기\t\t3. 종료");
			System.out.println("4. 테이블 삭제");
			System.out.println(BAR);
			System.out.println("원하는 메뉴 번호를 입력하세요:");
			System.out.println(BAR);
			String choice = sc.nextLine();
			System.out.println(BAR);
			if (Pattern.matches(regexMenu, choice)) {
				switch (Integer.parseInt(choice)) {
				case 0:
					System.out.println("테이블 변경을 선택하셨습니다.");
					menuChangeTable();
					break;
				case 1:
					System.out.println("테이블 배정을 선택하셨습니다.");
					menuSetTable();
					break;
				case 2:
					System.out.println("주문 받기를 선택하셨습니다.");
					menuLoadTable();
					break;
				case 3:
					System.out.println("프로그램을 종료합니다.");
					flag = true;
					break;
				case 4:
					System.out.println("테이블 삭제를 선택하셨습니다.");
					menuDeleteTable();
					break;
				}// end of switch
			} else {
				System.out.println("0~4 까지 범위의 숫자를 입력해 주세요");
			}
		}sc.close();   // end of while
	}// end of introMenu

	//1.0메인메뉴: 테이블 변경

	public static void menuChangeTable() {
		File dir = new File(SettingManager.TABLE_DIR);
		File[] tables = dir.listFiles();

		// 테이블이 없는 경우 메소드 종료
		if (tables.length == 0) {
			System.out.println("테이블이 없습니다.");
			return;
		}

		settingManager.listAllTable();
		boolean flag = false;
		while (!flag) {
			System.out.println(BAR);
			System.out.println("이동을 요청한 테이블의 번호를 입력하세요.");
			String currentTable = inputFileName();
			
			File currentF = setFileTxt(currentTable);
			if (currentF.exists()) {
				System.out.println("이동할 테이블 번호를 입력하세요");
				String newTable = inputTableNumber();
				File newF = setFileTxt(newTable);
				if (!newF.exists()) {
					boolean renamed = currentF.renameTo(newF);
					if (renamed) {
						System.out.println(currentTable + " 테이블이 " + newTable + "로 성공적으로 변경되었습니다.");
						flag = true;
					} else {
						System.out.println("테이블 이름 변경에 실패했습니다.");
					}
				} else {
					System.out.println("이미 존재하는 테이블 이름입니다. 다른 이름을 선택해주세요.");
				}
			} else {
				System.out.println("입력하신 테이블이 존재하지 않습니다.");
			}
		}
		settingManager.listAllTable();
	} // end of menuChangeTable

	//1.1메인메뉴: 테이블 배정

	public static void menuSetTable() {
		settingManager.listAllTable();
		boolean flag = false;
		while (!flag) {
			String setTable = inputTableNumber(); // 테이블 번호 입력 메서드
			File file = setFileTxt(setTable); 

			if (file.exists()) {
				System.out.println("이미 배정된 테이블입니다. 다른 테이블을 선택해주세요.");
			} else {
				try {
					if (file.createNewFile()) {
						System.out.println("테이블 번호: " + setTable + "이(가) 성공적으로 배정되었습니다.");
						settingManager.listAllTable();
						flag = true;
					} else {
						System.out.println("테이블 배정에 실패했습니다.");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	} // end of menuSetTable

	//4테이블 삭제 선택창

	private static void menuDeleteTable() {
		System.out.println(BAR);
		toMainIfDirIsEmpty();
		System.out.println("1: 테이블 삭제\n2: 전체 테이블 삭제");
		boolean response = Input1or2();
		if (response) {
			deleteATable();
		} else {
			deleteAllTables();
		}
	}

	//2 주문 메뉴

	public static void menuLoadTable() {
		toMainIfDirIsEmpty();
		settingManager.listAllTable();
		System.out.println(BAR);
		System.out.println("주문받을 테이블 번호를 입력해주세요");
		inputTableNum();
		System.out.print("안녕하세요 ");

		String regex = "([0-5])";							
		boolean flag = false;
		while (!flag) {										//display order list 추가하까? 하면 3번 삭제 ㄱ
			System.out.println(tableNumber + " 테이블 고객님, 무엇을 도와드릴까요?");
			System.out.println(BAR);
			System.out.println("0. 메뉴표시\t\t1. 주문 추가");
			System.out.println("2. 주문 제거\t\t3. 주문 확인");
			System.out.println("4. 결제 요청\t\t5. 메인메뉴로");  
			System.out.println(BAR);
			System.out.println("원하는 메뉴 번호를 입력하세요:");
			System.out.println(BAR);
			String choice = sc.nextLine();
			System.out.println(BAR);
			if (Pattern.matches(regex, choice)) {
				switch (Integer.parseInt(choice)) {
				case 0:
					System.out.println("메뉴를 표시합니다.");
					foodList.displayMenu();
					break;
				case 1:
					System.out.println("주문을 추가합니다.");
					addOrder();
					break;
				case 2:
					System.out.println("주문을 제거합니다.");
					removeOrder();
					break;
				case 3:
					System.out.println("주문을 확인합니다.");
					displayOrderList();
					break;
				case 4:
					System.out.println("영수증을 출력합니다.");
					printReceipt();
					break;
				case 5:
					System.out.println("메인 메뉴로 돌아갑니다.");
					flag = true;
					break;
				}
			} else {
				System.out.println("0~5까지 범위의 숫자를 입력해 주세요");	
			}
		}
	}
		
	//2.5 영수증 (결제하시겠습니까?)

	private static void printReceipt() {
		// 주문 목록을 출력합니다
		System.out.println(BAR);
		displayOrderList();

		// 사용자에게 결제 여부를 묻습니다
		System.out.println(BAR);
		System.out.println("결제하시겠습니까? (1: 예, 2: 아니오)");
		boolean pay = Input1or2();

		if (pay) {
			// 총 금액을 계산합니다
			int totalPrice = orderManager.getTotalPrice();

			// 총 금액을 출력하고 감사 메시지를 표시합니다
			System.out.println("총 금액은 " + totalPrice + " 원 입니다. 감사합니다  \n다음에 또 " +SettingManager.R_NAME+ "에 방문해 주십시오");
			delFileInTableNum(); //지정된 테이블 삭제
			introMenu();
		} else {
			System.out.println("결제를 취소하셨습니다.");
		}
	}
    
	//2.1 주문 추가

	public static void addOrder() {
		// 해당 테이블 파일을 확인합니다.
		File tableFile = setFileTxt(tableNumber.toString()); 

		// 음식명을 입력하고, 입력한 음식이 메뉴에 있는지 확인합니다.
		String foodName = inputFoodExist();

		// 메뉴에서 음식 정보를 받아와 주문 목록에 추가합니다.
		Food food = foodList.getFoodInfoFromMenu(foodName); 
		orderManager.addOrderItem(food, tableFile);
		updateTableFile();
		
	    System.out.println(foodName + " 1개를 주문목록에 추가 했습니다.");
	}

	//2. 선택한 테이블 주문 삭제

	public static void removeOrder() {
		//주문 목록 출력
		System.out.println(BAR);
		displayOrderList();
		System.out.println(BAR);
	    // 해당 테이블 파일을 확인합니다.
	    File table = setFileTxt(tableNumber.toString()); 
	    
	    // 음식명을 입력하고, 입력한 음식이 메뉴에 있는지 확인합니다.
	    String foodName = inputFoodExist();

	    // 메뉴에서 음식 정보를 받아와 주문 목록에서 삭제합니다.
	    Food food = foodList.getFoodInfoFromMenu(foodName); 
	    orderManager.removeOrderItem(food.getId(), table);
	    updateTableFile();
	}
	
	//4.1테이블 선택 삭제

	public static void deleteATable() {
		settingManager.listAllTable();
		inputTableNum();
		delFileInTableNum();
	} // end of deleteATable

	//4.2전체 테이블 삭제

	private static void deleteAllTables() {
		File dir = new File(SettingManager.TABLE_DIR);
		File[] files = dir.listFiles();
		if(files != null) { 
			for (File file : files) {
				if (!file.isDirectory()) { 
					file.delete();
				}
			}System.out.println("전부 삭제 했습니다.");
		}else
			System.out.println("이미 비여있습니다.");
	}
	
	//#. 주문(2.) 목록을 파일에 업데이트

	public static void updateTableFile() {
		// 해당 테이블 파일을 확인
		File table = setFileTxt(tableNumber.toString()); 
		if (table.exists()) {
			orderManager.updateTableFile(table);
		} else {
			System.out.println("입력하신 테이블이 존재하지 않습니다.");
		}
	}

	//#.출력: 주문(2.) 목록 전체 출력

	public static void displayOrderList() {
		// 해당 테이블 파일을 확인합니다
		File file = setFileTxt(tableNumber.toString()); 
		if (file.exists()) {
			try {
				List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
				if (lines.isEmpty()) {
					System.out.println("주문 목록이 비어 있습니다.");
					return;
				}

				System.out.println("주문 목록:");
				for (String line : lines) {
					System.out.println(line);
				}
				System.out.println("총 가격: " + orderManager.getTotalPrice());
			} catch (IOException e) {
				System.out.println(file.getName() + "에서 주문 목록을 조회하는 중 오류가 발생했습니다");
				e.printStackTrace();
			}
		} else {
			System.out.println("입력하신 테이블이 존재하지 않습니다.");
		}
	}

	//#.2 입력:선택한 테이블 주문 목록에 존재하는 음식

	public static String inputFoodExist() {
		while (true) {
			System.out.println("음식명을 입력하세요:");
			System.out.println(BAR);
			String foodName = sc.nextLine();
			System.out.println(BAR);

			// 메뉴에서 음식 정보를 받아옵니다.
			Food food = foodList.getFoodInfoFromMenu(foodName);
			// 음식이 메뉴에 없으면 다시 입력하라는 메시지를 출력합니다.
			if (food == null) {
				System.out.println("입력하신 음식이 메뉴에 없습니다. 다시 입력해주세요.");
			} else {
				return foodName;
			}
		}
	}

	//#.2 입력1 or 2

	public static boolean Input1or2() {
		while (true) {
			System.out.println(BAR);
			String input = sc.nextLine();

			if (input.equals("1")) {
				return true;
			} else if (input.equals("2")) {
				return false;
			} else {
				System.out.println("잘못된 입력입니다. 1 또는 2를 입력해주세요.");
			}
		}
	}

	//#.입력: 테이블 번호(범위:변수 테이블번호 업데이트) (입력하는건 메인클래스 밖에서쓰지말자 ㅠ

	public static String inputTableNumber() {
		boolean flag = false;
		String Table = " ";
		while (!flag) {
			System.out.println(
					SettingManager.ROW_START + "~" + SettingManager.ROW_END + "와" + SettingManager.COL_START + "~" + SettingManager.COL_END + " 범위의 좌석을 입력하세요 (예: B3):");
			Table = sc.nextLine().toUpperCase(); // 대문자 자동 변환
			System.out.println(BAR);

			// 정규식 생성
			String regex = "[" + SettingManager.ROW_START + "-" + SettingManager.ROW_END + "]" + "[" + SettingManager.COL_START + "-" + SettingManager.COL_END + "]";

			// 입력된 좌석이 범위 내에 있는지 확인
			if (Pattern.matches(regex, Table)) {
				flag = true;
			} else {
				System.out.println("입력이 올바르지 않습니다. " + SettingManager.ROW_START + "~" + SettingManager.ROW_END + "와" + SettingManager.COL_START + "~" + SettingManager.COL_END
						+ " 범위의 좌석을 입력해주세요.");
			}
		} // end of while
		return Table;
	}// end of getSeatNumber

	//#.입력: 테이블 파일명 입력

	public static String inputFileName() { 
		String filename = null;
		boolean flag = false;
		while (flag == false) {
			System.out.println("테이블번호를 입력하세요:");
			System.out.println(BAR);
			filename = sc.nextLine().toUpperCase(); // 대문자 자동 변환
			System.out.println(BAR);
			File file = setFileTxt(filename); 
			if (file.exists()) {
				flag = true;
			} else {
				System.out.println("해당 테이블이 존재하지 않습니다. 다시 입력해주세요.");
				flag = false;
			}
		}
		return filename;
	}

	//#.삭제: TableNum에 지정된 파일 삭제

	public static void delFileInTableNum() {  //테이블 없을시 if else 제거함 반드시 inputfilename으로 받아올것
		File file = new File(SettingManager.TABLE_DIR, tableNumber.toString() + ".txt"); 
		if (file.delete()) {
			System.out.println("테이블 번호: " + tableNumber + "이(가) 성공적으로 삭제되었습니다.");
		} else {
			System.out.println("테이블 삭제에 실패했습니다.");
		}
	} 
	
	//#.사용할 파일 지정(에 스트링 파일명 넣어주면됨.)
	
	public static File setFileTxt(String tableNum2) {
	    File table = new File(SettingManager.TABLE_DIR, tableNum2 + ".txt");
	    return table;
	}
	
	//#.입력: 전역변수 테이블 번호 수정하는 메서드(전역변수 테이블 리셋)
	
	public static void inputTableNum() {
		String table=inputFileName();
		tableNumber.setLength(0);							// 기존 내용을 지웁니다
		tableNumber.append(table);							// 새로운 테이블 번호를 저장합니다
	}
	
	//#.테이블 디렉토리가 비여있을 경우 메인선택창으로 돌아가기 
	
	public static void toMainIfDirIsEmpty() {
	    File dir = new File(SettingManager.TABLE_DIR);
	    File[] files = dir.listFiles();
	    if (files != null && files.length > 0) {
	        // 디렉토리가 비어 있지 않습니다. 다음 단계로 넘어감
	    } else {
	        System.out.println("테이블이 없습니다.");
	        introMenu();  // introMenu 메소드를 호출
	    }
	}



}//end of Main class
