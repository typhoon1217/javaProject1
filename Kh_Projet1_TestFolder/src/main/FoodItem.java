package main;

public abstract class FoodItem {
	String foodid; 
	String foodName; 
	int foodPrice;
	public FoodItem() {
	}
	public FoodItem(String id, String name, int price) {
		this.foodid = id;
		this.foodName = name;
		this.foodPrice = price;
	}
	public abstract String getId();
	public abstract String getName();
	public abstract int getPrice();
	public abstract void setId(String id);
	public abstract void setName(String name);
	public abstract void setPrice(int price);
} 