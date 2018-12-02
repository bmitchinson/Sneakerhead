import javax.swing.*;
import java.awt.*;


//TODO: Add space to put description, size, color, price in frame.
//TODO: Pull the item description from item class.

public class ItemViewFrame extends JFrame {

    ItemViewFrame(Item item){
        super("");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel insideBox = new JPanel();
        insideBox.setLayout(new BorderLayout());
        insideBox.setMaximumSize(new Dimension(475,250));

        //components set in border layout
        String itemName = item.getName();
        JLabel name = new JLabel(itemName);
        name.setHorizontalAlignment(SwingConstants.CENTER);
        name.setVerticalAlignment(SwingConstants.BOTTOM);
        name.setFont(new Font("Helvetica", Font.BOLD, 16));

        //JPanel pic = new JPanel();

        ImageIcon picPass = new ImageIcon();
        picPass.setImage(ScaledImage.getScaledImage(item.getImageURL(),200,200));
        JLabel pic = new JLabel(picPass);

        JTextArea descriptionText = new JTextArea(50,375);
        descriptionText.setBackground(insideBox.getBackground());
        descriptionText.setText(item.getDescription());

        JPanel itemDetails = new JPanel();
        itemDetails.setLayout(new GridLayout(9,1));
        itemDetails.setMaximumSize(new Dimension(400,450));
        itemDetails.add(new JLabel("Description: " ));
        //itemDetails.add(descriptionText);
        itemDetails.add(new JLabel("Condition: " + item.getCondition()));
        itemDetails.add(new JLabel("Size: "+ item.getSize() + " " + item.getGender()));
        itemDetails.add(new JLabel("Color: "+ item.getColor()));
        itemDetails.add(new JLabel("Price: $" + item.getCost()));
        itemDetails.add(new JLabel("Quantity: " + item.getQuantity()));
        itemDetails.add(new JLabel(""));
        itemDetails.add(new JButton("Buy Shoe!"));


        insideBox.add(name,BorderLayout.NORTH);
        insideBox.add(pic,BorderLayout.WEST);
        insideBox.add(itemDetails,BorderLayout.EAST);

        insideBox.setAlignmentX(CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(10));
        add(insideBox);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600,350);
        this.setVisible(true);
    }

    public static void main(String[] args){
        ItemViewFrame frame = new ItemViewFrame(new Item());
    }
}
