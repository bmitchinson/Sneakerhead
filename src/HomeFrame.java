import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class HomeFrame extends JFrame {
    private final JButton sellButton;
    private final JButton loginButton;
    private final JPanel itemPanel;
    private final JScrollPane scrollPane;
    private final Wrapper wrapper;

    public HomeFrame(){
        wrapper = new Wrapper();
        sellButton = new JButton("Sell Item");
        sellButton.setMinimumSize(new Dimension(100,25));
        loginButton = new JButton("Login");
        loginButton.setMinimumSize(new Dimension(75,25));

        JPanel topPanel = new JPanel();
        topPanel.setMaximumSize(new Dimension(500,50));
        topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.LINE_AXIS));
        topPanel.add(Box.createHorizontalStrut(5));
        topPanel.add(sellButton);
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(loginButton);
        topPanel.add(Box.createHorizontalStrut(5));

        itemPanel = new JPanel();
        initializeItemPanel();

        scrollPane = new JScrollPane(itemPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setMaximumSize(new Dimension(480,510));
        scrollPane.setAlignmentX(CENTER_ALIGNMENT);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        add(Box.createVerticalStrut(10));
        add(topPanel);
        add(Box.createVerticalStrut(15));
        add(scrollPane);
        add(Box.createVerticalStrut(15));
        
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(500,600);
        this.setResizable(false);
        this.setVisible(true);


    }

    //TODO: Get Items from wrapper and add them to the JPanel ItemPanel. Item Panel is then added to scrollPane
    //TODO: Remove temporary panels used for initializing after a set size for ItemPanel is determined
    private void initializeItemPanel(){
        Item[] items = Item.getTestItems();
        items[0] = wrapper.getItemInfo(1);
        items[1] = wrapper.getItemInfo(2);
        System.out.println(items[0]);
        System.out.println(items[1]);
        itemPanel.setPreferredSize(new Dimension(460,100 * items.length));
        itemPanel.setLayout(new GridLayout(items.length,1));

        for(int i=0; i<items.length; i++){
            itemPanel.add(items[i].getItemTile());
        }
    }

    public static void main(String[] args){
        HomeFrame frame = new HomeFrame();
    }
}


