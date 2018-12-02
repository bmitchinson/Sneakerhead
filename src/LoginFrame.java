import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginFrame extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton registerButton;
    private final JLabel usernameLabel;
    private final JLabel passwordLabel;
    private final JComboBox userTypeBox;
    private final HomeFrame mainFrame;

    public LoginFrame(HomeFrame mainFrame){
        this.mainFrame = mainFrame;

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

        String[] types = {"Buyer", "Seller", "Buyer & Seller"};
        userTypeBox = new JComboBox(types);
        userTypeBox.setMaximumSize(new Dimension(150,50));

        setLayout(new BoxLayout(getContentPane(),BoxLayout.PAGE_AXIS));

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
        add(Box.createRigidArea(new Dimension(500,20)));
        add(userTypeBox);
        add(Box.createRigidArea(new Dimension(500,220)));

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(500,500);
        setResizable(false);
        setVisible(true);
    }

    public LoginFrame getThis(){
        return this;
    }

    private class ButtonHandler implements ActionListener{
        //TODO: possibly register Boxes to this action Listener too?
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == loginButton){

            }
            if(e.getSource() == registerButton){
                AddUserRequest request = new AddUserRequest(usernameField.getText(), String.valueOf(passwordField.getPassword()), userTypeBox.getSelectedIndex()+1);
                Boolean response = (Boolean) mainFrame.makeRequest(request);
                if(response == true){
                    System.out.println("Successfully created user...");

                    mainFrame.updateLogin(request.getUsername(), (String) userTypeBox.getSelectedItem());

                    getThis().dispatchEvent(new WindowEvent(getThis(), WindowEvent.WINDOW_CLOSING));
                }else{
                    JOptionPane.showMessageDialog(null, "Error logging in");
                }
            }
        }
    }
}



