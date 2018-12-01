import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton registerButton;
    private final JLabel usernameLabel;
    private final JLabel passwordLabel;

    public LoginFrame(){
        usernameField = new JTextField();
        passwordField = new JPasswordField();

        usernameField.setMaximumSize(new Dimension(150,50));

        passwordField.setMaximumSize(new Dimension(150,50));


        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(75,25));

        registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(100,25));

        ButtonHandler handler = new ButtonHandler();

        loginButton.addActionListener(handler);
        registerButton.addActionListener(handler);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setMinimumSize(new Dimension(500,50));
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.LINE_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(50,25)));
        buttonPanel.add(registerButton);
        buttonPanel.add(Box.createHorizontalGlue());

        usernameLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");

        usernameLabel.setSize(new Dimension(150,25));
        usernameLabel.setAlignmentX(CENTER_ALIGNMENT);

        passwordLabel.setSize(new Dimension(150,25));
        passwordLabel.setAlignmentX(CENTER_ALIGNMENT);

        add(Box.createRigidArea(new Dimension(500,100)));
        add(usernameLabel);
        add(Box.createRigidArea(new Dimension(500,10)));
        add(usernameField);
        add(Box.createRigidArea(new Dimension(500,40)));
        add(passwordLabel);
        add(Box.createRigidArea(new Dimension(500,10)));
        add(passwordField);
        add(Box.createRigidArea(new Dimension(500,50)));
        add(buttonPanel);
        add(Box.createRigidArea(new Dimension(500,290)));


        setLayout(new BoxLayout(getContentPane(),BoxLayout.PAGE_AXIS));

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500,500);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args){
        LoginFrame window = new LoginFrame();
    }

    private class ButtonHandler implements ActionListener{
        //TODO: possibly register Boxes to this action Listener too?
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == loginButton){
                //TODO: Use wrapper to attempt to login, may need to also notify server/client if the login was successful so the user can buy and sell now
                /*if(!wrapper.login){
                    JOptionPane.showMessageDialog(getContentPane(),
                        "The username or password you entered was invalid, please try again.",
                        "Error with registration",
                        JOptionPane.ERROR_MESSAGE);
                    passwordField.setText("");
                  }*/

            }
            if(e.getSource() == registerButton){
                //TODO: Use wrapper to attempt to create a new user account in the database, will need to prompt the user if they want to be a buyer seller or both
                /*if(!wrapper.register){
                    JOptionPane.showMessageDialog(getContentPane(),
                        "The username you tried to register is already taken. Please enter a new username.",
                        "Error with registration",
                        JOptionPane.ERROR_MESSAGE);
                }*/
            }
        }
    }
}



