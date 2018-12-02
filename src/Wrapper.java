import java.sql.*;
import java.util.ArrayList;

public class Wrapper {

    private Connection connection;

    public Wrapper() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://s-l112.engr.uiowa.edu/engr_class037", "engr_class037", "mbs123");
        } catch (SQLException e) {
            System.out.println("Unable to connect to remote database");
            e.printStackTrace();
        }
    }

    public Item getItemInfo(int id) {
        Statement itemStatement = null;
        Statement linkedSellerStatement = null;

        try {
            itemStatement = connection.createStatement();
            ResultSet result =
                    itemStatement.executeQuery("SELECT * FROM Items where ID = " + id);

            result.next();

            linkedSellerStatement = connection.createStatement();
            ResultSet sellerResult = linkedSellerStatement.executeQuery(
                    "SELECT * FROM Users where ID = " +
                            result.getInt("Seller")
            );

            sellerResult.next();

            String name = sellerResult.getString("Username");

            Item test = new Item(result.getInt("ID"),
                    result.getString("Name"),
                    result.getString("Description"),
                    result.getString("Brand"),
                    result.getString("Condition"),
                    result.getString("Color"),
                    result.getString("Gender"),
                    result.getFloat("Size"),
                    result.getDouble("Price"),
                    result.getInt("Quantity"),
                    result.getString("URL"),
                    name);

            return test;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("ERROR: Null item returned");
        return null;
    }

    public ArrayList<Item> getAllItems(){
        Statement statement = null;
        Statement linkedSellerStatement = null;

        try {
            statement = connection.createStatement();
            linkedSellerStatement = connection.createStatement();

            ArrayList<Item> allItems = new ArrayList<Item>(1024);

            ResultSet result =
                    statement.executeQuery("SELECT * FROM Items" );

            ResultSet sellerResult;

            while(result.next()) {
                sellerResult = linkedSellerStatement.executeQuery(
                        "SELECT * FROM Users where ID = " +
                                result.getInt("Seller"));
                sellerResult.next();

                Item test = new Item(result.getInt("ID"),
                        result.getString("Name"),
                        result.getString("Description"),
                        result.getString("Brand"),
                        result.getString("Condition"),
                        result.getString("Color"),
                        result.getString("Gender"),
                        result.getFloat("Size"),
                        result.getDouble("Price"),
                        result.getInt("Quantity"),
                        result.getString("URL"),
                        sellerResult.getString("Username"));

                allItems.add(test);
            }

            return allItems;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("ERROR: Null item returned");
        return null;
    }

    public void buyItem(int itemID){

        try {
            Statement statement = connection.createStatement();
            ResultSet quantity = statement.executeQuery("SELECT Quantity FROM Items WHERE ID =" + itemID);
            quantity.next();
            System.out.println("Before decrement: " + quantity.getInt(1));
            int q = quantity.getInt(1)-1;
            String query = "UPDATE Items SET Quantity = " + q + "WHERE ID = " + itemID;
            statement.executeUpdate(query);
            ResultSet result = statement.executeQuery("SELECT Quantity FROM Items WHERE ID =" + itemID);
            result.next();
            System.out.println("After decrement: " + result.getInt(1));

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        Wrapper wrapper = new Wrapper();
        ArrayList<Item> test = wrapper.getAllItems();
    }

    /*
    TODO: Methods
    buyItem(ID)
    sellItem(Item)
    createUser(Name, Pass, Type (123))
    login(name, pass) - true or false, telling gui to use provided username in corner
    getUserType(name) - return string of user type
     */
}
