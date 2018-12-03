import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.security.ntlm.Server;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

public class HomeFrame extends JFrame {
    private final JButton postItem;
    private final JButton loginButton;
    private final JButton logoutButton;
    private final JPanel itemPanel;
    private final JScrollPane scrollPane;
    private final JPanel topPanel;
    private final JPanel bottomPanel;
    private JPanel loginState;
    private ArrayList<Item> items;
    private final Color color1 = new Color(245, 245, 245);
    private final Color color2 = new Color(0, 128, 128);
    private static ServerRequester serverRequester = new ServerRequester("localhost");
    private static boolean connected = false;
    private boolean isBuyer = false;
    private boolean isSeller = false;
    private final JLabel logoLabel;

    public HomeFrame() {
        if(!connected){
            if (!serverRequester.start()) {
                System.exit(0);
            }
            connected = true;
        }

        getContentPane().setBackground(color1);
        postItem = new JButton("Post Item");
        postItem.setMinimumSize(new Dimension(100, 25));
        postItem.setEnabled(false);

        loginButton = new JButton("Login");
        loginButton.setMinimumSize(new Dimension(75, 25));

        loginState = new JPanel();
        loginState.setMaximumSize(new Dimension(75, 30));
        loginState.setLayout(new GridLayout(1, 1));
        loginState.add(loginButton);
        loginState.setBackground(color2);
        updateLogin("Login to buy or sell", "");

        ButtonHandler handler = new ButtonHandler();
        postItem.addActionListener(handler);
        loginButton.addActionListener(handler);

        Image banner = null;
        try{
            File image = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getFile() + File.separator + "Image" + File.separator + "logo.png");
            banner = ImageIO.read(image);
        }catch (Exception e){
            System.out.println(e);
        }

        ImageIcon logoIcon = new ImageIcon(banner.getScaledInstance(150,50, Image.SCALE_DEFAULT));
        logoLabel = new JLabel(logoIcon);

        topPanel = new JPanel();
        topPanel.setMaximumSize(new Dimension(500, 50));
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
        topPanel.add(Box.createHorizontalStrut(5));
        topPanel.add(postItem);
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(logoLabel);
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(loginState);
        topPanel.add(Box.createHorizontalStrut(5));
        topPanel.setBackground(color2);

        itemPanel = new JPanel();
        initializeItemPanel();

        scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(itemPanel);
        scrollPane.setPreferredSize(new Dimension(480, 510));
        scrollPane.setAlignmentX(CENTER_ALIGNMENT);
        scrollPane.setBackground(color1);

        // Items for bottom panel
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener((e) -> {
            logout();
        });

        bottomPanel = new JPanel();
        bottomPanel.setMinimumSize(new Dimension(400, 50));
        //bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
        bottomPanel.setBackground(color2);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        add(Box.createVerticalStrut(5));
        add(topPanel);
        add(Box.createVerticalStrut(15));
        add(scrollPane);
        add(Box.createVerticalStrut(15));
        add(bottomPanel);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(500, 650);
        this.setResizable(false);
        this.setVisible(true);
    }

    private void initializeItemPanel() {
        items = (ArrayList<Item>) makeRequest(new GetAllItemsRequest());
        SwingUtilities.invokeLater(() -> {
            itemPanel.removeAll();
            itemPanel.setPreferredSize(new Dimension(460, 100 * items.size()));
            itemPanel.setLayout(new GridLayout(items.size(), 1));

            for (int i = 0; i < items.size(); i++) {
                items.get(i).setHomeFrame(this);
                itemPanel.add(items.get(i).getItemTile());
            }
            System.out.println("");
            itemPanel.revalidate();
            itemPanel.repaint();
        });
    }

    public void setBuyer(boolean buyer) {
        System.out.println("buyer set in setter to " + buyer);
        isBuyer = buyer;
    }

    public void setSeller(boolean seller) {
        isSeller = seller;
    }

    public boolean isBuyer() {
        return isBuyer;
    }

    public boolean isSeller() {
        return isSeller;
    }

    public ServerRequester getServerRequester() {
        return serverRequester;
    }

    private HomeFrame getThis() {
        return this;
    }

    public void updateLogin(String username, String type) {
        SwingUtilities.invokeLater(() -> {
            loginState.removeAll();
            JLabel loggedInLabel = new JLabel(username);
            JLabel loggedInType = new JLabel(type);
            loggedInLabel.setMinimumSize(new Dimension(75, 25));
            loginState.setLayout(new GridLayout(2, 1));
            loginState.add(loggedInLabel);
            if (!isBuyer && !isSeller) loginState.add(loginButton);

            else if (isBuyer && isSeller) {
                loginState.add(loggedInType);
                bottomPanel.add(logoutButton);
                postItem.setEnabled(true);
            } else if (isSeller) {
                loginState.add(loggedInType);
                bottomPanel.add(logoutButton);
                postItem.setEnabled(true);
            } else if (isBuyer) {
                bottomPanel.add(logoutButton);
            }
            loginState.revalidate();
            loginState.repaint();
        });
    }

    public void addItem(Item item) {
        items.add(item);
        itemPanel.setPreferredSize(new Dimension(460, itemPanel.getHeight() + 100));
        itemPanel.setLayout(new GridLayout(items.size(), 1));
        itemPanel.add(item.getItemTile());
        itemPanel.revalidate();
        itemPanel.repaint();
    }

    public Object makeRequest(Request request) {
        Object response = serverRequester.makeRequest(request);
        return response;
    }

    public void updateAllItems() {
        initializeItemPanel();
    }

    // Called before any action is called just in case something has changed
    public void updateItem(Item itemUpdate) {

    }

    private void logout() {
        SwingUtilities.invokeLater(() -> {
            updateLogin("Login to buy or sell", "");
            makeRequest(new LogoutRequest());
            bottomPanel.remove(logoutButton);
            isBuyer = false;
            isSeller = false;
            postItem.setEnabled(false);
        });
    }

    private class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == loginButton) {
                LoginFrame frame = new LoginFrame(getThis());
            }

            if (e.getSource() == postItem) {
                AddItemFrame frame = new AddItemFrame(getThis());
            }
        }
    }

    public static void main(String[] args) {
        HomeFrame frame = new HomeFrame();
    }
}


