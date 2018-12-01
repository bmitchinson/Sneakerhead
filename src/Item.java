import javax.swing.*;

public class Item {
    private String name;
    private String description;
    private String brand;
    private String condition;
    private Integer size;
    private String color;
    private String gender;
    private Double cost;

    public Item(String name, String description, String brand, String condition, String color, String gender, Integer size, Double cost){
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.condition = condition;
        this.size = size;
        this.color = color;
        this.gender = gender;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getBrand() {
        return brand;
    }

    public String getCondition() {
        return condition;
    }

    public String getColor() {
        return color;
    }

    public String getGender() {
        return gender;
    }

    //TODO: Verify how we want cost returned from getCost method
    public String getCost() {
        String costString = String.format("%.2f", cost);
        return costString;
    }

    //TODO: Verify how we want size returned from the getSize method
    public String getSize() {
        return size.toString();
    }

    //TODO: Create an ItemTile Class that shows a condensed version of the Item's data in a (JPanel)
    public JPanel getItemTile(){
        //ItemTile tile = new ItemTile(this);
        //return tile;
        return null;
    }

    //TODO: Create an ItemWindow Class that shows the item in its own JFrame
    public JFrame getItemWindow(){
        //ItemWindow window = new ItemWindow(this);
        //return window;
        return null;
    }

    public static void main(String args[]){
        Item testItem = new Item("Nike Air Max",
                "These are shoes I bouht but coudn't ever wear. They are basically like new and I'm willing to negotiate on the price","Nike",
                "New",
                "Blue",
                "Male",
                8,
                60.);

        System.out.println(testItem.getCost());
    }
}
