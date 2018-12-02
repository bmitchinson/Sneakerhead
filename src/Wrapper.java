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

    public Item[] getAllItems(){
        Statement statement = null;
        Statement linkedSellerStatement = null;

        try {
            statement = connection.createStatement();
            linkedSellerStatement = connection.createStatement();

            ResultSet count = statement.executeQuery("SELECT COUNT(*) FROM Items");
            count.next();
            int rows = count.getInt(1);
            Item[] allItems = new Item[rows];

            ResultSet result =
                    statement.executeQuery("SELECT * FROM Items" );

            ResultSet sellerResult;

            int i = 0;
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

    public void sellItem(Item item){
        try {
            Statement statement = connection.createStatement();

            String insertName = "'"+ item.getName() + "'";
            String insertDescrip = "'"+ item.getDescription() + "'";
            String insertBrand = "'"+ item.getBrand() + "'";
            String insertCondition = "'"+ item.getCondition() + "'";
            String insertColor = "'"+ item.getColor() + "'";
            String insertGender = "'"+ item.getGender() + "'";
            String insertURL = "'"+ item.getImageURL() + "'";
            String seller = "'"+ item.getSeller() + "'";
            ResultSet result = statement.executeQuery("SELECT ID FROM Users WHERE Username =" + seller);
            result.next();
            int id = result.getInt(1);
            String values = "(" + insertName + "," + insertDescrip + "," + insertBrand + "," + item.getQuantity() + "," + insertCondition + "," + item.getSize() + "," + insertColor + "," + insertGender + "," + item.getCost() + "," + insertURL + "," + id + ")";
            statement.execute("INSERT INTO Items(Name,Description,Brand,Quantity,Condition,Size,Color,Gender,Price,URL,Seller) VALUES " + values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Wrapper wrapper = new Wrapper();
        Item test = wrapper.getItemInfo(2);
        System.out.println(test);
        //wrapper.buyItem(1);
        //wrapper.createUser("Mel","123",1);
    }

    /*
    TODO: Methods
    sellItem(Item) - populate item list with new item with user attached
    login(name, pass) - true or false, telling gui to use provided username in corner, check to make sure vaild
    getUserType(name) - return string of user type - 1, buyer 2, seller, 3 both

     */
}
