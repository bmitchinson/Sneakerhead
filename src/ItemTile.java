import javax.swing.*;
import java.awt.*;

public class ItemTile extends JPanel{
    private final JLabel itemPictureLabel;
    private final JLabel nameLabel;
    private final JLabel costLabel;
    private final JLabel sellerLabel;
    private final JLabel quantityLabel;
    private final JTextArea descriptionArea;

    public ItemTile(){
        Item testItem = new Item("Nike Air Max",
                "These are shoes I bought but couldn't ever wear. They are basically like new and I'm willing to negotiate on the price","Nike",
                "New",
                "Blue",
                "Male",
                8,
                60.,
                2,
                "https://i.imgur.com/C6iJSYy.jpg", "Sam");

        setPreferredSize(new Dimension(480,100));
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        //Create image JLabel
        ImageIcon itemImageIcon = new ImageIcon();
        itemImageIcon.setImage(ScaledImage.getScaledImage(testItem.getImageURL(),100,100));
        itemPictureLabel = new JLabel(itemImageIcon);
        itemPictureLabel.setMinimumSize(new Dimension(100,100));

        nameLabel = new JLabel(testItem.getName());
        nameLabel.setAlignmentX(LEFT_ALIGNMENT);

        costLabel = new JLabel("Cost: " + testItem.getCost());
        costLabel.setAlignmentX(RIGHT_ALIGNMENT);

        sellerLabel = new JLabel("Seller: " + testItem.getSeller());
        sellerLabel.setAlignmentX(LEFT_ALIGNMENT);

        quantityLabel = new JLabel("Quantity: " + testItem.getQuantity());
        quantityLabel.setAlignmentX(LEFT_ALIGNMENT);

        descriptionArea = new JTextArea();
        initializeDescriptionArea(testItem.getDescription());


        JPanel bottomPanel = new JPanel();
        //bottomPanel.setAlignmentX(LEFT_ALIGNMENT);
        bottomPanel.add(sellerLabel);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(quantityLabel);
        bottomPanel.add(Box.createHorizontalStrut(100));
        bottomPanel.add(costLabel);

        JPanel rightPanel = new JPanel();
        rightPanel.setAlignmentX(LEFT_ALIGNMENT);
        rightPanel.setPreferredSize(new Dimension(365,100));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
        rightPanel.add(nameLabel);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(descriptionArea);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(bottomPanel);

        add(Box.createHorizontalStrut(5));
        add(itemPictureLabel);
        add(Box.createHorizontalStrut(5));
        add(rightPanel);
        add(Box.createHorizontalStrut(5));
    }

    private void initializeDescriptionArea(String description){
        //descriptionArea.setAlignmentX(LEFT_ALIGNMENT);
        descriptionArea.setBackground(this.getBackground());
        //descriptionArea.setMinimumSize(new Dimension(365, 25));
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        if(description.length() >= 55){
            descriptionArea.setText(description.substring(0, 60) + "...");
        }else{
            descriptionArea.setText(description);
        }
    }

    public static void main(String[] args){
        JFrame frame = new JFrame();
        ItemTile tile = new ItemTile();

        frame.setLayout(new FlowLayout());
        frame.add(tile);
        frame.setSize(500,150);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
