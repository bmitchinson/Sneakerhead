import jdk.nashorn.internal.scripts.JO;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;

public class LoginFrame extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton registerButton;
    private final JLabel usernameLabel;
    private final JLabel passwordLabel;
    private final JLabel logoLabel;
    private final JComboBox userTypeBox;
    private final HomeFrame homeFrame;
    private final Color color = new Color(0, 128, 128);

    public LoginFrame(HomeFrame homeFrame) {
        this.homeFrame = homeFrame;

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        usernameField.setMaximumSize(new Dimension(150, 50));

        passwordField.setMaximumSize(new Dimension(150, 50));


        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(75, 25));

        registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(100, 25));

        ButtonHandler handler = new ButtonHandler();

        loginButton.addActionListener(handler);
        registerButton.addActionListener(handler);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(color);
        buttonPanel.setMinimumSize(new Dimension(500, 50));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(50, 25)));
        buttonPanel.add(registerButton);
        buttonPanel.add(Box.createHorizontalGlue());

        usernameLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");

        usernameLabel.setSize(new Dimension(150, 25));
        usernameLabel.setAlignmentX(CENTER_ALIGNMENT);
        usernameLabel.setBackground(color);


        passwordLabel.setSize(new Dimension(150, 25));
        passwordLabel.setAlignmentX(CENTER_ALIGNMENT);
        passwordLabel.setBackground(color);

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

        String[] types = {"Select Type to Register", "Buyer", "Seller", "Buyer & Seller"};
        userTypeBox = new JComboBox(types);
        userTypeBox.setMaximumSize(new Dimension(175, 50));

        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        getContentPane().setBackground(color);

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

    public LoginFrame getThis() {
        return this;
    }

    private class ButtonHandler implements ActionListener {
        //TODO: possibly register Boxes to this action Listener too?
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> {

                if (e.getSource() == loginButton) {
                    LoginRequest request = new LoginRequest(usernameField.getText(), String.valueOf(passwordField.getPassword()));
                    Integer response = (Integer) homeFrame.makeRequest(request);

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
                if (e.getSource() == registerButton) {
                    if(userTypeBox.getSelectedIndex() != 0){
                        AddUserRequest request = new AddUserRequest(usernameField.getText(), String.valueOf(passwordField.getPassword()), userTypeBox.getSelectedIndex() + 1);
                        Boolean response = (Boolean) homeFrame.makeRequest(request);
                        if (response == true) {
                            System.out.println("Successfully created user...");

                            homeFrame.updateLogin(request.getUsername(), (String) userTypeBox.getSelectedItem());

                            getThis().dispatchEvent(new WindowEvent(getThis(), WindowEvent.WINDOW_CLOSING));
                        } else {
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



