package order;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import food.Food;

public class OrderManager {

    private static List<OrderItem> orderList = new ArrayList<>();

    public void addOrderItem(Food food, File tableFile) {
        addToOrderListWithQuantity(tableFile);
        for (OrderItem item : orderList) {
            if (item.getFood().getId().equals(food.getId())) {
                item.incrementQuantity();
                return;
            }
        }
        orderList.add(new OrderItem(food, 1));
    }

    
    public void removeOrderItem(String foodId, File tableFile) {
        addToOrderListWithQuantity(tableFile);
        int initialSize = orderList.size(); //주문목록의 크기 조회

        orderList.removeIf(item -> item.getFood().getId().equals(foodId));

        if (orderList.size() == initialSize) {  //크기에 변동이 있었는지 여부
            System.out.println("주문목록에서 삭제되지 않았습니다.");
        } else {
            System.out.println("주문목록에서 성공적으로 삭제되었습니다.");
        }
    }


    public void updateTableFile(File tableFile) {
        try (FileWriter writer = new FileWriter(tableFile, false)) {
            for (OrderItem item : orderList) {
                Food food = item.getFood();
                writer.write(food.getId() + ", " + food.getName() + ", " + food.getPrice() + ", 수량: " + item.getQuantity() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    //주문목록(orderList) 불러와서 정보 추가 (수량정보 추가) 음식(food)정보+수량정보
    public void addToOrderListWithQuantity(File tableFile) {
        orderList.clear();
        try (Scanner scanner = new Scanner(tableFile)) {		//스캐너 충돌 우려
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(", ");
                
                Food food = new Food(parts[0], parts[1], Integer.parseInt(parts[2]));
                String[] quantityParts = parts[3].split(": ");
//                if (quantityParts.length < 2) {
//                    System.out.println("수량 정보가 잘못되었습니다: " + parts[5]);
//                    continue;
//                }
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityParts[1]);
                } catch (Exception e) {
                    System.out.println("수량을 숫자로 변환할 수 없습니다: " + quantityParts[1]);
                    continue;
                }
                orderList.add(new OrderItem(food, quantity));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
	
    
    //총가격 가져오는 코드
    public int getTotalPrice() {
    	int total = 0;
        for (OrderItem item : orderList) {
            total += item.getTotalPrice();
        }
        return total;
    }
}
