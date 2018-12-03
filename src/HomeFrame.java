import Requests.GetAllItemsRequest;
import Requests.LogoutRequest;
import Requests.Request;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class HomeFrame extends JFrame {
    //postItem is a button that will open a AddItemFrame and allow the user to add an item for sale
    private final JButton postItem;
    //loginButton is a button that will open a LoginFrame and allow the user to register or login
    private final JButton loginButton;
    //logout will log the user out of an account and then disappear
    private final JButton logoutButton;
    private final JButton sortCostButton;
    private final JButton sortGenderButton;
    private final JButton sortSizeButton;
    private final JButton sortQuantityButton;
    //ItemPanel will holds the ItemTiles of all the Items
    private final JPanel itemPanel;
    //ItemPanel will be added to ScrollPane so the user can scroll through the Items
    private final JScrollPane scrollPane;
    //topPanel will hods the logo, the login button, and the postItem button
    private final JPanel topPanel;
    //bottomPanel will holds the filters
    private final JPanel bottomPanel;
    //loginState will either hold the login button or show the username and type that is logged up
    private JPanel loginState;
    //items is the ArrayList that holds Items
    private ArrayList<Item> items;
    //Color one is used for the very back panel
    private final Color color1 = new Color(245, 245, 245);
    //Color two is used to color the GUI to make it look nice
    private final Color color2 = new Color(0, 128, 128);
    //ServerRequester is a static variable that will make server requests for all the JFrames
    private static ServerRequester serverRequester = new ServerRequester("localhost");
    //connected is a boolean that will be true if the first client started connects to the server
    private static boolean connected = false;
    //isBuyer is a boolean that is true if the logged in user can buy
    private boolean isBuyer = false;
    //isSeller is a boolean that is true if the logged in user can sell items
    private boolean isSeller = false;
    //logolabel holds the logo of the shop
    private final JLabel logoLabel;

    public HomeFrame() {
        //connect serverRequester to the Server
        if(!connected){
            if (!serverRequester.start()) {
                System.exit(0);
            }
            connected = true;
        }

        //create and format buttons
        getContentPane().setBackground(color1);
        postItem = new JButton("Post Item");
        postItem.setMinimumSize(new Dimension(100, 25));
        postItem.setEnabled(false);

        loginButton = new JButton("Login");
        loginButton.setMinimumSize(new Dimension(75, 25));

        //create JPanel to holds login state
        loginState = new JPanel();
        loginState.setMaximumSize(new Dimension(75, 30));
        loginState.setLayout(new GridLayout(1, 1));
        loginState.add(loginButton);
        loginState.setBackground(color2);
        updateLogin("Login to buy or sell", "");

        //register handlers
        ButtonHandler handler = new ButtonHandler();
        postItem.addActionListener(handler);
        loginButton.addActionListener(handler);

        //Get and create image of logo
        Image banner = null;
        try{
            File image = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getFile() + File.separator + "Image" + File.separator + "logo.png");
            banner = ImageIO.read(image);
        }catch (Exception e){
            System.out.println(e);
        }

        ImageIcon logoIcon = new ImageIcon(banner.getScaledInstance(150,50, Image.SCALE_DEFAULT));
        logoLabel = new JLabel(logoIcon);

        //create top panel with image and two buttons
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

        //initialize scroll pane and item pane that shows items
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

        // Buttons to call their respective sorting functions detailed below
        sortCostButton = new JButton("Sort Cost");
        sortCostButton.addActionListener((e) -> {
            sortCost();
        });

        sortGenderButton = new JButton("Sort Gender");
        sortGenderButton.addActionListener((e) -> {
            sortGender();
        });

        sortSizeButton = new JButton("Sort Size");
        sortSizeButton.addActionListener((e) -> {
            sortSize();
        });

        sortQuantityButton = new JButton("Sort Quantity");
        sortQuantityButton.addActionListener((e) -> {
            sortQuantity();
        });

        bottomPanel = new JPanel();
        bottomPanel.setMinimumSize(new Dimension(400, 50));
        bottomPanel.setBackground(color2);

        // Adds sorting buttons to bottomPanel
        bottomPanel.add(sortCostButton);
        bottomPanel.add(sortGenderButton);
        bottomPanel.add(sortQuantityButton);
        bottomPanel.add(sortSizeButton);

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

    //initializeItemPanel pulls the items from the database and adds them to the ItemPanel then repaints it to make sure they show
    private void initializeItemPanel() {
        items = (ArrayList<Item>) makeRequest(new GetAllItemsRequest());
        updateAllLocalItems();
    }

    //set if the client is now a buyer
    public void setBuyer(boolean buyer) {
        System.out.println("buyer set in setter to " + buyer);
        isBuyer = buyer;
    }

    //set it the client is now a seller
    public void setSeller(boolean seller) {
        isSeller = seller;
    }

    //get if the client is a buyer
    public boolean isBuyer() {
        return isBuyer;
    }

    //get reference to this HomeFrame which acts as the homepage of the client
    private HomeFrame getThis() {
        return this;
    }

    //update login is called if the user is successfully logged in
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

    //add item to itemPanel
    public void addItem(Item item) {
        items.add(item);
        itemPanel.setPreferredSize(new Dimension(460, itemPanel.getHeight() + 100));
        itemPanel.setLayout(new GridLayout(items.size(), 1));
        itemPanel.add(item.getItemTile());
        itemPanel.revalidate();
        itemPanel.repaint();
    }

    //makeRequest is used so objects with a reference to this JFrame can talk to the server and make Requests
    public Object makeRequest(Request request) {
        Object response = serverRequester.makeRequest(request);
        return response;
    }

    //updates panels by reinitializing it
    public void updateAllItems() {
        initializeItemPanel();
    }

    public void updateAllLocalItems() {
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

    //logs the user out of the client
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

    // Updates all items and then begins a sort using Item's Comparator
    private void sortCost(){
        updateAllItems();
        Collections.sort(items, Item.PriceComparator);
    }

    // Updates all items and then begins a sort using Item's Comparator
    private void sortGender(){
        updateAllItems();
        Collections.sort(items, Item.GenderComparator);
    }

    // Updates all items and then begins a sort using Item's Comparator
    private void sortQuantity(){
        updateAllItems();
        Collections.sort(items, Item.QuantityComparator);
    }

    // Updates all items and then begins a sort using Item's Comparator
    private void sortSize(){
        updateAllItems();
        Collections.sort(items, Item.SizeComparator);
    }

    //handles the login and post item button
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


