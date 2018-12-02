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

            ResultSetMetaData rsmd = result.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while (result.next()) {
                for (int i = 1; i <= 11; i++) {
                    if (i > 1) System.out.print(",  ");
                    System.out.print(result.getString(i));
                }
                System.out.println("");
            }

            /*Item test = new Item(result.getInt(0),
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getString(4),
                    result.getString(5),
                    result.getString(6),
                    result.getFloat(7),
                    result.getDouble(8),
                    result.getInt(9),
                    result.getString(10));*/

            /*Item test = new Item(result.getInt("ID"),
                    result.getString("Name"),
                    result.getString("Description"),
                    result.getString("Brand"),
                    result.getString("Condition"),
                    result.getString("Color"),
                    result.getString("Gender"),
                    result.getFloat("Size"),
                    result.getDouble("Price"),
                    result.getInt("Quantity"),
                    result.getString("URL"));*/

            //System.out.println(test);
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("ERROR: Null item returned");
        return null;
    }

    /*try {
        Connection connection = DriverManager.getConnection("jdbc:mysql://s-l112.engr.uiowa.edu/engr_class037", "engr_class037", "mbs123");
        System.out.println("Connection Established.");
        Statement statement = connection.createStatement();
        System.out.println("Statement out of connection");
        ResultSet resultSet = statement.executeQuery("SELECT Password FROM Users");

        while(resultSet.next()){
            System.out.println(resultSet.getString(1));
        }

    } catch (SQLException sqlException) {
        sqlException.printStackTrace();
    }*/

    public static void main(String[] args) {
        Wrapper wrapper = new Wrapper();
        Item test = wrapper.getItemInfo(2);
        System.out.println(test);
    }

    /*
    TODO: Methods
    getItemInfo(ID)
    getAllItems() - Uses get getItemInfo and returns an item array
    buyItem(ID)
    sellItem(Item)
    createUser(Name, Pass, Type (123))
    login(name, pass) - true or false, telling gui to use provided username in corner
    getUserType(name) - return string of user type

     */
}
