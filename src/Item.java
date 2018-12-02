import javax.swing.*;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

public class Item implements Serializable {
    private int id;
    private String name;
    private String description;
    private String brand;
    private String condition;
    private double size;
    private String color;
    private String gender;
    private double cost;
    private int quantity;
    private String imageURL;
    private String seller;
    private ItemTile itemTile;

    public Item(String name, String description, String brand,
                String condition, String color, String gender, double size,
                double cost, int quantity, String imageURL, String seller) {
        this.id = -1;
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

    public Item(int id, String name, String description, String brand,
                String condition, String color, String gender, double size,
                double cost, int quantity, String imageURL, String seller) {
        this.id = id;
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

    public int getId(){return id;};

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

    public String getSeller() {
        return seller;
    }

    public String getCost() {
        String costString = String.format("$%.2f", cost);
        return costString;
    }

    //TODO: Verify how we want size returned from the getSize method
    public double getSize() {
        return size;
    }

    //TODO: Create an ItemTile Class that shows a condensed version of the Item's data in a (JPanel)
    public ItemTile getItemTile() {
        return itemTile;
    }


    public int getQuantity() {
        return quantity;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setId(int id){
        this.id = id;
    }

    public void incrementQuantity() {
        quantity++;
    }

    public void decrementQuantity() {
        if (quantity != 0) {
            quantity--;
        }
    }

    public void updateTile() {
        SwingUtilities.invokeLater(() -> {
            itemTile.updateQuantityText();
            itemTile.updateImage();
        });
    }

    //TODO: Create an ItemWindow Class that shows the item in its own JFrame
    public void startItemWindow() {
        if(quantity != 0){
            ItemViewFrame frame = new ItemViewFrame(this);
        }
    }

    @Override
    public String toString() {
        String desc = (description.length() > 25) ?
                (description.substring(0, 25) + "...") : (description);

        String shortURL = (imageURL.length() > 10) ?
                (imageURL.substring(0, 10)+ "...") : (imageURL);

        return ("Item Contents:\nName:" + name + ", Description:" + desc
                + ", Color:" + color + ", Gender:" + gender + ", Size:" + size
                + ", Cost:" + cost + ", Quantity:" + quantity + ", ImgURL:"
                + shortURL + ", Seller:" + seller);
    }

    public static ArrayList<Item> getTestItems() {
        ArrayList<Item> items = new ArrayList<Item>(1024);

        items.add(new Item(
                "Nike Air Max" + 1,
                "These are shoes I bought but couldn't ever wear. They are basically like new and I'm willing to negotiate on the price", "Nike",
                "New",
                "Blue",
                "Mens",
                8,
                40.65,
                1,
                "https://i.imgur.com/C6iJSYy.jpg",
                "Sam"));


        items.add(new Item(
                "Converse High Tops 2",
                "Converse High Tops feature super grip bottoms so you don't slip around in those icy winters",
                "Converse",
                "Like New",
                "Black",
                "Womens",
                4.0,
                59.95,
                1,
                "https://i.imgur.com/daU2fPw.jpg",
                "BigSeller123"
                ));
        return items;
    }

    public static void main(String args[]) {
        Item testItem = new Item( "Nike Air Max",
                "These are shoes I bought but couldn't ever wear. They are basically like new and I'm willing to negotiate on the price", "Nike",
                "New",
                "Blue",
                "Male",
                8,
                60.,
                2,
                "https://i.imgur.com/C6iJSYy.jpg",
                "Sam");

        System.out.println(testItem.getCost());
    }
}
