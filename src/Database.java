import java.sql.*;
import java.util.ArrayList;

public class Database {

    // private instance of a connection to the database

    private Connection connection;

    // constructor establishes connection for all queries

    public Database() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://s-l112.engr.uiowa.edu/engr_class037", "engr_class037", "mbs123");
        } catch (SQLException e) {
            System.out.println("Unable to connect to remote database");
            e.printStackTrace();
        }
    }

    // query Item table to return all currently in table
    // return array list of all the items

    public ArrayList<Item> getAllItems(){
        Statement statement = null;
        Statement linkedSellerStatement = null;

        try {
            statement = connection.createStatement();
            linkedSellerStatement = connection.createStatement();

            ArrayList<Item> allItems = new ArrayList<Item>(1024);

            // get all items from the table

            ResultSet result =
                    statement.executeQuery("SELECT * FROM Items" );

            ResultSet sellerResult;

            // for each row found in result set, add new item to the array list

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

    // decrement the quantity of a bought item in the Item table.
    // return true if item quantity is decremented, false otherwise

    public boolean buyItem(int itemID){

        try {
            // query for the quantity of the item given its ID

            Statement statement = connection.createStatement();
            ResultSet quantity = statement.executeQuery("SELECT Quantity FROM Items WHERE ID =" + itemID);
            quantity.next();

            // decrement the quantity if it is greater than zero

            int q = quantity.getInt(1);
            if (q == 0){
                return false;
            }
            q--;

            // update item with new quantity

            String query = "UPDATE Items SET Quantity =" + q + " WHERE ID =" + itemID;
            statement.executeUpdate(query);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // create a user from registration information

    public boolean createUser(String Name,String Pass,int Type){

        try {
            // establish statement from the connection

            Statement statement = connection.createStatement();

            // check to see if username is in User table

            String insertName = insertSQL(Name);
            ResultSet result = statement.executeQuery("SELECT Username FROM Users WHERE Username = " + insertName);

            // if no username found, add new entry to user's table

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

    // inserts new item for sale into database with given seller name

    public boolean sellItem(String username, Item item){
        try {
            Statement statement = connection.createStatement();

            //formats strings for SQL command

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

            //get user id of identified seller

            ResultSet result = statement.executeQuery("SELECT ID FROM Users WHERE Username = " + user);
            result.next();
            int id = result.getInt(1);
            String values = "(" + insertName + "," + insertDescrip + "," + insertBrand + "," + item.getQuantity() + "," + insertCondition + "," + insertSize + "," + insertColor + "," + insertGender + "," + insertCost + "," + insertURL + "," + id + ")";

            // command which inserts
            statement.execute("INSERT INTO `Items` (`Name`,`Description`,`Brand`,`Quantity`,`Condition`,`Size`,`Color`,`Gender`,`Price`,`URL`,`Seller`) VALUES " + values);

            // get item id of the newly created item and set it for the item class

            ResultSet idResult = statement.executeQuery("SELECT ID FROM Items WHERE Name =" + insertSQL(item.getItemName()));
            idResult.next();
            item.setId(idResult.getInt(1));

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // checks that user login is valid given a username and password.
    // return 0 - login not successful, 1 - seller, 2 - buyer, 3 - seller and buyer

    public int login(String name, String password){
        int userType = 0;
        try {

            //query database for user name

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT Password,Usertype FROM Users WHERE Username =" + insertSQL(name));
            result.next();

            // if user name found, match password and set userType 1,2, or 3

            if(result.getString(1).equals(password)){
                userType = result.getInt(2);
            }

        } catch(SQLException e){
            e.printStackTrace();
        }
       return userType;
    }

    // formats strings for SQL query

    public String insertSQL(String s){
        return "'" + s + "'";
    }


}
