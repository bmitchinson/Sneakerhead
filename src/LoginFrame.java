import javax.swing.*;

public class LoginFrame extends JFrame {
    private final JTextField usernameField;
    private final JTextField passwordField;
    private final JButton loginButton;
    private final JButton registerButton;

    public LoginFrame(){
        usernameField = new JTextField();
        passwordField = new JTextField();

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(600,600);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args){

    }
}
