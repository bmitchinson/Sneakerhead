import javax.swing.*;
import java.net.URL;

public class Item {
    private int id;
    private String name;
    private String description;
    private String brand;
    private String condition;
    private float size;
    private String color;
    private String gender;
    private double cost;
    private int quantity;
    private String imageURL;
    private String seller;
    private ItemTile itemTile;

    public Item(int id, String name, String description, String brand,
                String condition, String color, String gender, float size,
                Double cost, int quantity, String imageURL, String seller) {
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
    public float getSize() {
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

    public static Item[] getTestItems() {
        Item[] items = new Item[20];
        for (int i = 0; i < items.length; i++) {
            items[i] = new Item(i,
                    "Nike Air Max" + (i + 1),
                    "These are shoes I bought but couldn't ever wear. They are basically like new and I'm willing to negotiate on the price", "Nike",
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

    public static void main(String args[]) {
        Item testItem = new Item(0, "Nike Air Max",
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
