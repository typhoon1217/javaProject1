package order;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import food.Food;
import food.FoodList;
import main.Main;
import main.Setting;

public class Order {

	private static OrderManager orderManager = new OrderManager();
	public static FoodList foodList = FoodList.getInstance();
	//2.5 영수증 (결제하시겠습니까?)

	public static void printReceipt() {
		// 주문 목록을 출력합니다
		System.out.println(Main.BAR);
		displayOrderList();

		// 사용자에게 결제 여부를 묻습니다
		System.out.println(Main.BAR);
		System.out.println("결제하시겠습니까? (1: 예, 2: 아니오)");
		boolean pay = Main.Input1or2();

		if (pay) {
			// 총 금액을 계산합니다
			int totalPrice = orderManager.getTotalPrice();

			// 총 금액을 출력하고 감사 메시지를 표시합니다
			System.out.println("총 금액은 " + totalPrice + " 원 입니다. 감사합니다  \n다음에 또 " +Setting.R_NAME+ "에 방문해 주십시오");
			Main.delFileInTableNum(); //지정된 테이블 삭제
			Main.introMenu();
		} else {
			System.out.println("결제를 취소하셨습니다.");
		}
	}

	//2.1 주문 추가

	public static void addOrder() {
		// 해당 테이블 파일을 확인합니다.
		File tableFile = Main.setFileTxt(Main.tableNumber.toString()); 

		// 음식명을 입력하고, 입력한 음식이 메뉴에 있는지 확인합니다.
		String foodName = Main.inputFoodExist();

		// 메뉴에서 음식 정보를 받아와 주문 목록에 추가합니다.
		Food food = foodList.getFoodInfoFromMenu(foodName); 
		orderManager.addOrderItem(food, tableFile);
		Main.updateTableFile();

		System.out.println(foodName + " 1개를 주문목록에 추가 했습니다.");
	}
	
	public static void displayOrderList() {
		// 해당 테이블 파일을 확인합니다
		File file = Main.setFileTxt(Main.tableNumber.toString()); 
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
}