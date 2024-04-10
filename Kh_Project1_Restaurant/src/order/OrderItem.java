package order;

import food.Food;

public class OrderItem {
    private Food food;
    private int quantity;

    public OrderItem(Food food, int quantity) {
        this.food = food;
        this.quantity = quantity;
    }

    public void incrementQuantity() {
        this.quantity++;
    }

    public Food getFood() {
        return food;
    }

    public int getQuantity() {
        return quantity;
    }
    public int getTotalPrice() {
        return food.getPrice() * quantity;
    }
}
