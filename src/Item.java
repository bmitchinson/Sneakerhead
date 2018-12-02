import javax.swing.*;
import java.net.URL;

public class Item {
    private String name;
    private String description;
    private String brand;
    private String condition;
    private Integer size;
    private String color;
    private String gender;
    private double cost;
    private int quantity;
    private String imageURL;
    private String seller;
    private ItemTile itemTile;

    public Item(String name, String description, String brand, String condition, String color, String gender, Integer size, Double cost, int quantity, String imageURL, String seller){
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.condition = condition;
        this.size = size;
        this.color = color;
        this.gender = gender;
        this.cost = cost;
        this.quantity = quantity;
        this.imageURL = imageURL;
        this.seller = seller;
        itemTile = new ItemTile(this);
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

    public String getSeller(){
        return seller;
    }

    public String getCost() {
        String costString = String.format("$%.2f", cost);
        return costString;
    }

    //TODO: Verify how we want size returned from the getSize method
    public String getSize() {
        return size.toString();
    }

    //TODO: Create an ItemTile Class that shows a condensed version of the Item's data in a (JPanel)
    public JPanel getItemTile(){
        return itemTile;
    }


    public String getQuantity() {
        return String.valueOf(quantity);
    }

    public String getImageURL() {
        return imageURL;
    }

    //TODO: Create an ItemWindow Class that shows the item in its own JFrame
    public JFrame getItemWindow(){
        //ItemWindow window = new ItemWindow(this);
        //return window;
        return null;
    }

    public static Item[] getTestItems(){
        Item[] items = new Item[20];
        for(int i=0; i<items.length; i++){
            items[i] = new Item("Nike Air Max" + i,
                    "These are shoes I bought but couldn't ever wear. They are basically like new and I'm willing to negotiate on the price","Nike",
                    "New",
                    "Blue",
                    "Male",
                    8,
                    60.,
                    2,
                    "https://i.imgur.com/C6iJSYy.jpg", "Sam");
        }
        return items;
    }

    public static void main(String args[]){
        Item testItem = new Item("Nike Air Max",
                "These are shoes I bought but couldn't ever wear. They are basically like new and I'm willing to negotiate on the price","Nike",
                "New",
                "Blue",
                "Male",
                8,
                60.,
                2,
                "https://i.imgur.com/C6iJSYy.jpg", "Sam");

        System.out.println(testItem.getCost());
    }
}
