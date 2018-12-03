import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.text.NumberFormat;

public class ItemViewFrame extends JFrame {

    private JButton buyButton = new JButton("Buy Shoe!");
    private JPanel itemDetails = new JPanel();
    private JPanel boxedFrame = new JPanel();
    private JTextArea descriptionText = new JTextArea();
    private JLabel quantityLabel;
    private Item item;
    private Color color = Color.orange;
    private NumberFormat formatter = NumberFormat.getCurrencyInstance();

    ItemViewFrame(Item item){

        //set item
        this.item = item;

        //add a JPanel to initial frame, put a border layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10,10,10,10));
        mainPanel.setPreferredSize(new Dimension(590,500));
        mainPanel.setBackground(Color.LIGHT_GRAY);

        //add name label to top of border layout
        String itemName = item.getName();
        JLabel name = new JLabel(itemName);
        name.setHorizontalAlignment(SwingConstants.CENTER);
        name.setFont(new Font("Helvetica", Font.BOLD, 18));

        //created new JPanel with 1 row, 2 columns to add to center of border layout
        boxedFrame.setLayout(new GridLayout(1,2));
        boxedFrame.setBackground(color);

        //Put a picture in column 1
        ImageIcon picPass = new ImageIcon();
        picPass.setImage(ScaledImage.getScaledImage(item.getImageURL(),250,250));
        JLabel pic = new JLabel(picPass);
        boxedFrame.add(pic);

        //Put item description in column 2
        descriptionText.setBackground(boxedFrame.getBackground());
        descriptionText.setText(item.getDescription());
        descriptionText.setLineWrap(true);
        descriptionText.setWrapStyleWord(true);
        descriptionText.setEditable(false);

        quantityLabel = new JLabel("Quantity: " + item.getQuantity());

        itemDetails.setLayout(new GridLayout(10,1));
        itemDetails.setPreferredSize(new Dimension(400,450));
        itemDetails.setBackground(color);
        itemDetails.add(new JLabel("Description: " ));
        itemDetails.add(descriptionText);
        itemDetails.add(new JLabel("Condition: " + item.getCondition()));
        itemDetails.add(new JLabel("Size: "+ item.getSize() + " " + item.getGender()));
        itemDetails.add(new JLabel("Color: "+ item.getColor()));
        itemDetails.add(new JLabel("Price: " + formatter.format(item.getCost())));
        itemDetails.add(quantityLabel);
        itemDetails.add(new JLabel("Seller: " + item.getSeller()));
        itemDetails.add(new JLabel(""));
        itemDetails.add(buyButton);

        //add all components
        boxedFrame.add(pic);
        boxedFrame.add(itemDetails);
        mainPanel.add(name,BorderLayout.NORTH);
        mainPanel.add(boxedFrame,BorderLayout.CENTER);

        add(mainPanel);

        //add listener to the buyButton
        buyButton.addActionListener(e -> buttonHit());

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(new Dimension(600,510));
        this.setVisible(true);
    }


    public static void main(String[] args){ ItemViewFrame frame = new ItemViewFrame(new Item()); }

    private void buttonHit(){
        buyButton.setText("Bought!");
        buyButton.setEnabled(false);
        itemDetails.setBackground(Color.LIGHT_GRAY);
        boxedFrame.setBackground(Color.LIGHT_GRAY);
        descriptionText.setBackground(Color.LIGHT_GRAY);


        item.decrementQuantity();
        item.updateTile();
        quantityLabel.setText("Quantity: " + item.getQuantity());
    }
}
