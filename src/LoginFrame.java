import Requests.AddUserRequest;
import Requests.LoginRequest;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;

//LoginFrame allows the user to sign in or create an account
public class LoginFrame extends JFrame {
    //username field allows the user to input username
    private final JTextField usernameField;
    //password field allows he user to input a password
    private final JPasswordField passwordField;
    //login button can be clicked to make a login attempt
    private final JButton loginButton;
    //register button can be clicked to attempt to register the account
    private final JButton registerButton;
    //usernameLabel shows the user where to enter username
    private final JLabel usernameLabel;
    //passwordLabel shows the user where to enter password
    private final JLabel passwordLabel;
    //logoLabel displays the logo of the shop
    private final JLabel logoLabel;
    //userTypeBox allows the user to pick what type of account to create
    private final JComboBox userTypeBox;
    //homeFrame holds a reference to the main HomeFrame that the client does all the interacting with
    private final HomeFrame homeFrame;
    //holds color that colors the components to make it look nice
    private final Color color = new Color(0, 128, 128);

    //Constructor of loginFrame takes a reference to the HomeFrame it is started from
    public LoginFrame(HomeFrame homeFrame) {
        this.homeFrame = homeFrame;

        //create and format labels
        usernameLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");

        usernameLabel.setSize(new Dimension(150, 25));
        usernameLabel.setAlignmentX(CENTER_ALIGNMENT);
        usernameLabel.setBackground(color);
        passwordLabel.setSize(new Dimension(150, 25));
        passwordLabel.setAlignmentX(CENTER_ALIGNMENT);
        passwordLabel.setBackground(color);

        //create and format fields
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        usernameField.setMaximumSize(new Dimension(150, 50));
        passwordField.setMaximumSize(new Dimension(150, 50));

        //create and format JComboBox
        String[] types = {"Select Type to Register", "Buyer", "Seller", "Buyer & Seller"};
        userTypeBox = new JComboBox(types);
        userTypeBox.setMaximumSize(new Dimension(175, 50));

        //create and format buttons
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(75, 25));
        registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(100, 25));

        //add action listeners with private inner class
        ButtonHandler handler = new ButtonHandler();
        loginButton.addActionListener(handler);
        registerButton.addActionListener(handler);

        //format button panel which holds login and register buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(color);
        buttonPanel.setMinimumSize(new Dimension(500, 50));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(50, 25)));
        buttonPanel.add(registerButton);
        buttonPanel.add(Box.createHorizontalGlue());

        //load logo image
        Image banner = null;
        try{
            File image = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getFile() + File.separator + "Image" + File.separator + "logo.png");
            banner = ImageIO.read(image);
        }catch (Exception e){
            System.out.println(e);
        }

        ImageIcon logoIcon = new ImageIcon(banner.getScaledInstance(250,100, Image.SCALE_DEFAULT));
        logoLabel = new JLabel(logoIcon);
        logoLabel.setAlignmentX(CENTER_ALIGNMENT);

        //setLayout of JFrame
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        getContentPane().setBackground(color);

        //add components
        add(Box.createVerticalStrut(50));
        add(logoLabel);
        add(Box.createVerticalStrut(30));
        add(usernameLabel);
        add(Box.createVerticalStrut(10));
        add(usernameField);
        add(Box.createVerticalStrut(30));
        add(passwordLabel);
        add(Box.createVerticalStrut(10));
        add(passwordField);
        add(Box.createVerticalStrut(30));
        add(buttonPanel);
        add(Box.createVerticalStrut(30));
        add(userTypeBox);
        add(Box.createVerticalStrut(10));

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setResizable(false);
        setVisible(true);
    }

    //return reference o this JFrame
    public LoginFrame getThis() {
        return this;
    }

    //Handler used for attempting to login or register
    private class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> {

                if (e.getSource() == loginButton) {
                    //attempt to login
                    LoginRequest request = new LoginRequest(usernameField.getText(), String.valueOf(passwordField.getPassword()));
                    Integer response = (Integer) homeFrame.makeRequest(request);

                    //based on respond we know what user logged in, 1 = Buyer, 2 = Seller, 3 = Both
                    //else we know the login failed
                    if (response == 1) {
                        System.out.println("Logged in as buyer...");
                        homeFrame.setBuyer(true);
                        homeFrame.setSeller(false);
                        homeFrame.updateLogin(request.getUsername(), "Buyer");
                        getThis().dispatchEvent(new WindowEvent(getThis(), WindowEvent.WINDOW_CLOSING));

                    } else if (response == 2) {
                        System.out.println("Logged in as seller...");
                        homeFrame.setBuyer(false);
                        homeFrame.setSeller(true);
                        homeFrame.updateLogin(request.getUsername(), "Seller");
                        getThis().dispatchEvent(new WindowEvent(getThis(), WindowEvent.WINDOW_CLOSING));
                    } else if (response == 3) {
                        homeFrame.setBuyer(true);
                        homeFrame.setSeller(true);
                        System.out.println("Logged in as buyer/seller");
                        homeFrame.updateLogin(request.getUsername(), "Buyer/Seller");
                        getThis().dispatchEvent(new WindowEvent(getThis(), WindowEvent.WINDOW_CLOSING));
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Username/Password. Both Username and Password are case sensitive so," +
                                " please check your entries and try again.");
                        passwordField.setText("");
                    }
                }
                //register user
                if (e.getSource() == registerButton) {
                    //if user did not select a type display an error
                    if(userTypeBox.getSelectedIndex() != 0){
                        //otherwise make request, send 1 for Buyer, 2 for Seller, 3 for Both
                        AddUserRequest request = new AddUserRequest(usernameField.getText(), String.valueOf(passwordField.getPassword()),
                                userTypeBox.getSelectedIndex() + 1);
                        Boolean response = (Boolean) homeFrame.makeRequest(request);
                        if (response == true) {
                            System.out.println("Successfully created user...");

                            homeFrame.updateLogin(request.getUsername(), (String) userTypeBox.getSelectedItem());

                            getThis().dispatchEvent(new WindowEvent(getThis(), WindowEvent.WINDOW_CLOSING));
                        } else {
                            //display error if username is already in use
                            JOptionPane.showMessageDialog(null, "Username is already in use. Please choose another.");
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Select a user type to register as.",
                                "Error Registering", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }
    }
}



