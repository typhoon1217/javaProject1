package food;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import main.RestaurantMain;

public class FoodList {        //싱글톤 사용 메뉴는 변화가 없는 고정값
    private static FoodList instance = null;
    private ArrayList<Food> foods;

    
    private FoodList() {
        this.setFoods(new ArrayList<>());
        
        // 초기값
        this.getFoods().add(new Food("A1","한식", "냉면", 12000, "계절 메뉴 "));
        this.getFoods().add(new Food("A2","한식", "쫄면", 8000, "셰프 강력 추천 "));
        this.getFoods().add(new Food("A3","한식", "고기국수", 9000, "강추 "));
        this.getFoods().add(new Food("B1","중식", "자작면", 7000, "하루 10접시 한정 "));
        this.getFoods().add(new Food("B2","중식", "마라우육면", 12000, "맵찔이는 가라 "));
        this.getFoods().add(new Food("B3","중식", "볶음면", 8000, "칼로리 폭탄(맛있다는 뜻) "));
        this.getFoods().add(new Food("C1","일식", "냉모밀", 9000, "시원한 "));
        this.getFoods().add(new Food("C2","일식", "라멘", 12000, "셰츠의 강력 추천"));
        this.getFoods().add(new Food("C3","일식", "우동", 9000, "쫄깃한 쫄깃한"));
    }
    public static FoodList getInstance() {
        if (instance == null) {
            instance = new FoodList();
        }
        return instance;
    }


    public void addFood(Food food) {
        for (Food existingFood : getFoods()) {
            // 같은 아이디나 이름을 가진 음식이 이미 존재하는 경우
            if (existingFood.getId().equals(food.getId()) || existingFood.getName().equals(food.getName())) {
                System.out.println("같은 아이디나 이름을 가진 음식이 이미 존재합니다.");
                return;
            }
        }
        // 중복된 아이디나 이름을 가진 음식이 없는 경우, 음식을 추가
        this.getFoods().add(food);
    }

    
    public void removeFood(Food food) {
        this.getFoods().remove(food);
    }
   
    
    public void sortFoodsById() {
        Collections.sort(getFoods(), new Comparator<Food>() {
            public int compare(Food f1, Food f2) {
                return f1.getId().compareTo(f2.getId());
            }
        });
    }
    
    
    public Food getFoodInfoFromMenu(String foodName) {
        for (Food food : this.getFoods()) {  // this.getFoods() 대신 this.foods를 사용
            if (food.getName().equals(foodName)) {
                return food; // 일치하는 음식 찾음
            }
        }
        return null; // 음식을 없음
    }

	
	public void displayMenu() {
	    System.out.println(RestaurantMain.BAR);
	    for (Food food : getFoods()) {  // 'foodList.getFoods()' 대신 'foodList.foods'를 사용
	        System.out.println(food.getId()+"\t\t"+ food.getCategory() +"\n"+ food.getName()+ "\t\t" + food.getPrice()+ "\n"+ food.getComment()+"\n\n");
	    }
	    System.out.println(RestaurantMain.BAR);
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(getFoods());
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FoodList other = (FoodList) obj;
		return Objects.equals(getFoods(), other.getFoods());
	}
	
	
	public ArrayList<Food> getFoods() {
		return foods;
	}
	
	
	public void setFoods(ArrayList<Food> foods) {
		this.foods = foods;
	}
    
}

//
//public ArrayList<Food> getFoods() {
//  return this.foods;
//}