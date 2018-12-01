import java.sql.*;

public class Wrapper {

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://s-l112.engr.uiowa.edu", "engr_class037", "mbs123");
            System.out.println("Connection Established.");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Password FROM Users");
            System.out.println(resultSet.getObject(0));

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
