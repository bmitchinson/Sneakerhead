import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Wrapper {

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://s-l112.engr.uiowa.edu/phpMyAdmin/db_structure.php?server=1&db=engr_class037", "engr_class037", "smb123");
            System.out.println("Connection Established.");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
