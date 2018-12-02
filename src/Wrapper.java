import java.sql.*;

public class Wrapper {

    private Connection connection;

    public Wrapper(){
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://s-l112.engr.uiowa.edu/engr_class037", "engr_class037", "mbs123");
        } catch (SQLException e) {
            System.out.println("Unable to connect to remote database");
            e.printStackTrace();
        }
    }/*
    public Item getItemInfo(int id){
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("");

    }
    */

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
