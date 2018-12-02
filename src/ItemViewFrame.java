import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;

public class ItemViewFrame extends JFrame {

    private JButton buyButton = new JButton("Buy Shoe!");
    private JPanel itemDetails = new JPanel();
    private JPanel boxedFrame = new JPanel();
    private JTextArea descriptionText = new JTextArea();
    private JLabel quantityLabel;
    private Item item;

    ItemViewFrame(Item item){
        //set item
        this.item = item;

        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        //add a JPanel to initial frame, put a border layout
        JPanel insideBox = new JPanel();
        insideBox.setLayout(new BorderLayout());
        insideBox.setPreferredSize(new Dimension(590,500));
        insideBox.setBackground(Color.LIGHT_GRAY);

        //add name label to top of border layout
        String itemName = item.getName();
        JLabel name = new JLabel(itemName);
        name.setHorizontalAlignment(SwingConstants.CENTER);
        name.setFont(new Font("Helvetica", Font.BOLD, 18));

        //created new JPanel with 1 row, 2 columns to add to center of border layout
        //JPanel boxedFrame = new JPanel();
        boxedFrame.setLayout(new GridLayout(1,2));
        boxedFrame.setBackground(Color.orange);

        //Put a picture in column 1
        ImageIcon picPass = new ImageIcon();
        picPass.setImage(ScaledImage.getScaledImage(item.getImageURL(),250,250));
        JLabel pic = new JLabel(picPass);
        boxedFrame.add(pic);

        //Put item description in column 2
        //JTextArea descriptionText = new JTextArea();
        descriptionText.setBackground(boxedFrame.getBackground());
        descriptionText.setText(item.getDescription());
        descriptionText.setLineWrap(true);
        descriptionText.setWrapStyleWord(true);

        quantityLabel = new JLabel("Quantity: " + item.getQuantity());

        //JPanel itemDetails = new JPanel();
        itemDetails.setLayout(new GridLayout(10,1));
        itemDetails.setPreferredSize(new Dimension(400,450));
        itemDetails.setBackground(Color.orange);
        itemDetails.add(new JLabel("Description: " ));
        itemDetails.add(descriptionText);
        itemDetails.add(new JLabel("Condition: " + item.getCondition()));
        itemDetails.add(new JLabel("Size: "+ item.getSize() + " " + item.getGender()));
        itemDetails.add(new JLabel("Color: "+ item.getColor()));
        itemDetails.add(new JLabel("Price: " + item.getCost()));
        itemDetails.add(quantityLabel);
        itemDetails.add(new JLabel("Seller: " + item.getSeller()));
        itemDetails.add(new JLabel(""));
        itemDetails.add(buyButton);

        //add all components
        boxedFrame.add(pic);
        boxedFrame.add(itemDetails);
        insideBox.add(name,BorderLayout.NORTH);
        insideBox.add(boxedFrame,BorderLayout.CENTER);
        add(insideBox);

        //add listener to the buyButton
        buyButton.addActionListener(e -> buttonHit());

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(600,510);
        this.setVisible(true);
    }


    //public static void main(String[] args){ ItemViewFrame frame = new ItemViewFrame(new Item()); }

    //TODO: decrement the item quantity
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
