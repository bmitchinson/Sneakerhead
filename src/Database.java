import java.sql.*;
import java.util.ArrayList;

public class Database {

    private Connection connection;

    public Database() {
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

            if (result.next() == false){
                System.out.println("Wrapping returning null");
                return null;
            }

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

    public boolean buyItem(int itemID){

        try {
            Statement statement = connection.createStatement();
            ResultSet quantity = statement.executeQuery("SELECT Quantity FROM Items WHERE ID =" + itemID);
            quantity.next();
            int q = quantity.getInt(1);
            if (q == 0){
                return false;
            }
            q--;
            String query = "UPDATE Items SET Quantity =" + q + " WHERE ID =" + itemID;
            statement.executeUpdate(query);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createUser(String Name,String Pass,int Type){

        try {
            Statement statement = connection.createStatement();
            String insertName = insertSQL(Name);
            ResultSet result = statement.executeQuery("SELECT Username FROM Users WHERE Username = " + insertName);

            if(result.next() == false){
                String insertPass = "'" + Pass + "'";
                statement.execute("INSERT INTO Users (Username,Password,Usertype) VALUES ( " + insertName + "," + insertPass + "," + Type + ")");
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sellItem(String username, Item item){
        try {
            Statement statement = connection.createStatement();

            String insertName = "'"+ item.getItemName() + "'";
            String insertDescrip = "'"+ item.getDescription() + "'";
            String insertBrand = "'"+ item.getBrand() + "'";
            String insertCondition = "'"+ item.getCondition() + "'";
            String insertColor = "'"+ item.getColor() + "'";
            String insertGender = "'"+ item.getGender() + "'";
            String insertURL = "'"+ item.getImageURL() + "'";
            String insertSize = "'" + item.getShoeSize() + "'";
            String insertCost = "'" + item.getCost() + "'";
            String user = "'" + username + "'";
            ResultSet result = statement.executeQuery("SELECT ID FROM Users WHERE Username = " + user);

            result.next();
            int id = result.getInt(1);
            String values = "(" + insertName + "," + insertDescrip + "," + insertBrand + "," + item.getQuantity() + "," + insertCondition + "," + insertSize + "," + insertColor + "," + insertGender + "," + insertCost + "," + insertURL + "," + id + ")";
            System.out.println("INSERT INTO `Items` (`Name`,`Description`,`Brand`,`Quantity`,`Condition`,`Size`,`Color`,`Gender`,`Price`,`URL`,`Seller`) VALUES " + values);
            statement.execute("INSERT INTO `Items` (`Name`,`Description`,`Brand`,`Quantity`,`Condition`,`Size`,`Color`,`Gender`,`Price`,`URL`,`Seller`) VALUES " + values);

            ResultSet idResult = statement.executeQuery("SELECT ID FROM Items WHERE Name =" + insertSQL(item.getItemName()));
            idResult.next();
            item.setId(idResult.getInt(1));

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int login(String name, String password){
        int userType = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT Password,Usertype FROM Users WHERE Username =" + insertSQL(name));

            result.next();
            if(result.getString(1).equals(password)){
                userType = result.getInt(2);
            }

        } catch(SQLException e){
            e.printStackTrace();
        }
       return userType;
    }

    public String insertSQL(String s){
        return "'" + s + "'";
    }

    public static void main(String[] args) {
        Database database = new Database();
        ArrayList<Item> test = database.getAllItems();
    }

}
