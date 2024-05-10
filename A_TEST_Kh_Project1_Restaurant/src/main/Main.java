package main;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;
import client.Client;
import food.Food;
import food.FoodList;
import order.Order;
import order.OrderManager;

public class Main { 

	private static Setting settingManager = Setting.getInstance();
	private static OrderManager orderManager = new OrderManager();
	public static Scanner sc = new Scanner(System.in); 
	public static StringBuffer tableNumber = new StringBuffer();
	public static final String BAR = "=========================================";

	public static FoodList foodList = FoodList.getInstance();
	//메인 +테이블관리

	public static void main(String[] args) {
		reset();
	}

	//초기화면

	public static void reset() {
		Client.loginClient();				//클라이언트 로그인
		introMenu();
		//login();							//로그인 
		//최초 선택창

		//기능 테스트용
		//Order.foodList.displayMenu();
		//Client.adminClient(); 			//관리자 클라이언트
		//introMenu();	                    //초기메뉴
		//menuLoadTable();                  //주문메뉴
		//Main_Server.serverStart(); 					//테스트 서버 시작		
	}

	//인트로 메뉴

	public static void introMenu() {
		String greeting = "안녕하세요 " + Setting.R_NAME + "에 방문해주셔서 감사합니다.";
		boolean flag = false;
		while (!flag) {
			settingManager.listAllTable();
			System.out.println(BAR);
			System.out.println(greeting);
			System.out.println(BAR);
			System.out.println("0. 테이블 변경\t\t1. 테이블 배정");
			System.out.println("2. 주문 받기\t\t3. 종료");
			System.out.println("4. 테이블 삭제");
			System.out.println(BAR);
			System.out.println("원하는 메뉴 번호를 입력하세요:");
			System.out.println(BAR);
			String regexMenu = "([0-4])"; // 입력값 범위
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
					addATable();
					break;
				case 2:
					System.out.println("주문 받기를 선택하셨습니다.");
					menuLoadTable();
					break;
				case 3:
					System.out.println("프로그램을 종료를 선택하셨습니다.");
					flag=true;
					break;
				case 4:
					System.out.println("테이블 삭제를 선택하셨습니다.");
					menuDeleteTable();
					break;
				}// end of switch
			} else {
				System.out.println("0~4 까지 범위의 숫자를 입력해 주세요");
				flag=false;
			}
		}   // end of while
		sc.close();
		System.out.println("프로그램이 종료되었습니다.");
	}// end of introMenu

	//1.0메인메뉴: 테이블 변경

	public static void menuChangeTable() {
		File dir = new File(Setting.TABLE_DIR);
		File[] tables = dir.listFiles();

		// 테이블이 없는 경우 메소드 종료
		if (tables.length == 0) {
			System.out.println("테이블이 없습니다.");
			return;
		}
		settingManager.listAllTable();
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

	//1.1메인메뉴: 테이블 배정

	public static void addATable() {
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
		toAddTifEmpty();
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
		toAddTifEmpty();
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
			System.out.println("0. 메뉴 표시\t\t1. 주문 추가");
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
					FoodList foodList = FoodList.getInstance();
					foodList.displayMenu();
					break;
				case 1:
					System.out.println("주문을 추가합니다.");
					Order.addOrder();
					break;
				case 2:
					System.out.println("주문을 제거합니다.");
					removeOrder();
					break;
				case 3:
					System.out.println("주문을 확인합니다.");
					Order.displayOrderList();
					break;
				case 4:
					System.out.println("영수증을 출력합니다.");
					Order.printReceipt();
					break;
				case 5:
					System.out.println("메인 메뉴로 돌아갑니다.");
					flag = true;
				}
			} else {
				System.out.println("0~5까지 범위의 숫자를 입력해 주세요");	
			}
		}
	}


	//4.1테이블 선택 삭제

	public static void deleteATable() {
		settingManager.listAllTable();
		inputTableNum();
		delFileInTableNum();
	} // end of deleteATable

	//4.2전체 테이블 삭제

	private static void deleteAllTables() {
		File dir = new File(Setting.TABLE_DIR);
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


	//#.2 입력:선택한 테이블 주문 목록에 존재하는 음식

	public static String inputFoodExist() {
		while (true) {
			System.out.println("음식명을 입력하세요:");
			System.out.println(BAR);
			String foodName = sc.nextLine();
			System.out.println(BAR);

			// 메뉴에서 음식 정보를 받아옵니다.
			Food food = Order.foodList.getFoodInfoFromMenu(foodName);
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
		String Table = null;
		boolean flag =false;
		while (!flag) {
			System.out.println(
			Setting.ROW_START + "~" + Setting.ROW_END + "와" + Setting.COL_START + "~" + Setting.COL_END + " 범위의 좌석을 입력하세요 (예: B3):");
			Table = sc.nextLine().toUpperCase(); // 대문자 자동 변환
			System.out.println(BAR);
			// 정규식 생성
			String regex = "[" + Setting.ROW_START + "-" + Setting.ROW_END + "]" + "[" + Setting.COL_START + "-" + Setting.COL_END + "]";
			// 입력된 좌석이 범위 내에 있는지 확인
			if (Pattern.matches(regex, Table)) {
				flag = true;
			} else {
				System.out.println("입력이 올바르지 않습니다. " + Setting.ROW_START + "~" + Setting.ROW_END + "와" + Setting.COL_START + "~" + Setting.COL_END
						+ " 범위의 좌석을 입력해주세요.");
				flag =false;
			}
		} // end of while
		return Table;
	}// end of inputTableNumber

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
		File file = new File(Setting.TABLE_DIR, tableNumber.toString() + ".txt"); 
		if (file.delete()) {
			System.out.println("테이블 번호: " + tableNumber + "이(가) 성공적으로 삭제되었습니다.");
		} else {
			System.out.println("테이블 삭제에 실패했습니다.");
		}
	} 

	//#.사용할 파일 지정(에 스트링 파일명 넣어주면됨.)

	public static File setFileTxt(String tableNum2) {
		File table = new File(Setting.TABLE_DIR, tableNum2 + ".txt");
		return table;
	}

	//#.입력: 전역변수 테이블 번호 수정하는 메서드(전역변수 테이블 리셋)

	public static void inputTableNum() {
		String table=inputFileName();
		tableNumber.setLength(0);							// 기존 내용을 지웁니다
		tableNumber.append(table);							// 새로운 테이블 번호를 저장합니다
	}

	//#.테이블 디렉토리가 비여있을 경우 첫테이블 배정 메소드로
	public static void toAddTifEmpty() {
		File dir = new File(Setting.TABLE_DIR);
		File[] files = dir.listFiles();
		if (files != null && files.length > 0) {
		} else {
		System.out.println("테이블이 없습니다.");	
		addATable();
		}
	}
	//2. 선택한 테이블 주문 삭제

	public static void removeOrder() {
		//주문 목록 출력
		System.out.println(Main.BAR);
		Order.displayOrderList();
		System.out.println(Main.BAR);
		// 해당 테이블 파일을 확인합니다.
		File table = Main.setFileTxt(Main.tableNumber.toString()); 

		// 음식명을 입력하고, 입력한 음식이 메뉴에 있는지 확인합니다.
		String foodName = Main.inputFoodExist();

		// 메뉴에서 음식 정보를 받아와 주문 목록에서 삭제합니다.
		Food food = Order.foodList.getFoodInfoFromMenu(foodName); 
		orderManager.removeOrderItem(food.getId(), table);
		Main.updateTableFile();
	}
}//end of Main class


