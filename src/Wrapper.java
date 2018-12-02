import java.sql.*;

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
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet result =
                    statement.executeQuery("SELECT * FROM Items where ID = " + id);

            result.next();

            for (int i = 1; i <= 11; i++) {
                if (i > 1) System.out.print(",  ");
                System.out.print(result.getString(i));
            }
            System.out.println("");

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
                    "Sam");

            System.out.println(test);
            return test;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("ERROR: Null item returned");
        return null;
    }

    public Item[] getAllItems(){
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet count = statement.executeQuery("SELECT COUNT(*) FROM Items");
            count.next();
            int rows = count.getInt(1);
            Item[] allItems = new Item[rows];


            ResultSet result =
                    statement.executeQuery("SELECT * FROM Items" );

            int i = 0;
            while(result.next()) {
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
                        result.getString("Seller"));

                allItems[i] = test;
                i++;
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
            int q = quantity.getInt(1)-1;
            String query = "UPDATE Items SET Quantity =" + q + " WHERE ID =" + itemID;
            statement.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean createUser(String Name,String Pass,int Type){

        boolean createUser = true;

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT Username FROM Users");

            while(result.next()){
                if(result.getString(1).equals(Name)){
                    createUser = false;
                }
            }

            if(createUser){
                String insertName = "'"+ Name + "'";
                String insertPass = "'" + Pass + "'";
                statement.execute("INSERT INTO Users (Username,Password,Usertype) VALUES ( " + insertName + "," + insertPass + "," + Type + ")");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return createUser;
    }


    public static void main(String[] args) {
        Wrapper wrapper = new Wrapper();
        //Item test = wrapper.getItemInfo(2);
        //System.out.println(test);
        //wrapper.buyItem(1);
        wrapper.createUser("Mel","123",1);
    }

    /*
    TODO: Methods
    sellItem(Item) - populate item list with new item with user attached
    createUser(Name, Pass, Type (123))
    login(name, pass) - true or false, telling gui to use provided username in corner, check to make sure vaild
    getUserType(name) - return string of user type - 1, buyer 2, seller, 3 both

     */
}
