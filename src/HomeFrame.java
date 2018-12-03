import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.security.ntlm.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HomeFrame extends JFrame {
    private final JButton postItem;
    private final JButton loginButton;
    private final JPanel itemPanel;
    private final JScrollPane scrollPane;
    private final JPanel topPanel;
    private final JPanel bottomPanel;
    private JPanel loginState;
    private ArrayList<Item> items;
    private final Color color1 = new Color(245, 245, 245);
    private final Color color2 = new Color(240, 248, 255);
    private final ServerRequester serverRequester;

    public HomeFrame() {
        serverRequester = new ServerRequester("localhost");
        if(!serverRequester.start()){
            System.exit(0);
        }

        getContentPane().setBackground(color1);
        postItem = new JButton("Post Item");
        postItem.setMinimumSize(new Dimension(100, 25));
        loginButton = new JButton("Login");
        loginButton.setMinimumSize(new Dimension(75, 25));

        loginState = new JPanel();
        loginState.setMaximumSize(new Dimension(75, 30));
        loginState.setLayout(new GridLayout(1, 1));
        loginState.add(loginButton);
        loginState.setBackground(color2);

        ButtonHandler handler = new ButtonHandler();
        postItem.addActionListener(handler);
        loginButton.addActionListener(handler);

        topPanel = new JPanel();
        topPanel.setMaximumSize(new Dimension(500, 50));
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
        topPanel.add(Box.createHorizontalStrut(5));
        topPanel.add(postItem);
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

        bottomPanel = new JPanel();
        bottomPanel.setSize(new Dimension(400, 300));
        bottomPanel.setBackground(Color.BLUE);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        add(Box.createVerticalStrut(5));
        add(topPanel);
        add(Box.createVerticalStrut(15));
        add(scrollPane);
        add(Box.createVerticalStrut(15));
        add(bottomPanel);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(500, 900);
        this.setResizable(false);
        this.setVisible(true);
    }

    private void initializeItemPanel() {
        items = (ArrayList<Item>) makeRequest(new GetAllItemsRequest());
        //items = Item.getTestItems();
        itemPanel.setPreferredSize(new Dimension(460, 100 * items.size()));
        itemPanel.setLayout(new GridLayout(items.size(), 1));

        for (int i = 0; i < items.size(); i++) {
            itemPanel.add(items.get(i).getItemTile());
        }
    }

    private HomeFrame getThis() {
        return this;
    }

    public void updateLogin(String username, String type) {
        SwingUtilities.invokeLater(() -> {
            JLabel loggedInLabel = new JLabel(username);
            JLabel loggedInType = new JLabel(type);
            loggedInLabel.setMinimumSize(new Dimension(75, 25));
            loginState.remove(loginButton);
            loginState.revalidate();
            loginState.repaint();
            loginState.setLayout(new GridLayout(2, 1));
            loginState.add(loggedInLabel);
            loginState.add(loggedInType);
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


