package food;

import java.util.Objects;

public class Food extends FoodItem{
    private String category;
    private String comment;

	public Food(String id, String name, int price) {
		super(id, name, price);
	}
    
	public Food(String id, String category, String name, int price, String comment) {
		super(id, name, price);
		this.category = category;
		this.comment = comment;
	}

	@Override
	public String getId() {
		return foodid;
	}
	
	@Override
	public String getName() {
		return foodName;
	}
	
	@Override
	public int getPrice() {
		return foodPrice;
	}
	
	@Override
	public void setId(String id) {
		this.foodid = id;
	}
	
	@Override
	public void setName(String name) {
		this.foodName = name;
		
	}
	
	@Override
	public void setPrice(int price) {
		this.foodPrice = price;	
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Food other = (Food) obj;
        return Objects.equals(getName(), other.getName()); 
    }
}