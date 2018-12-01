import javax.swing.*;
import java.awt.*;

public class AddItemFrame extends JFrame {

    AddItemFrame(){
        super("Add Item for Sale");
        setLayout(new BorderLayout());

        //JPanel pic = new JPanel();

        JPanel itemDetails = new JPanel();
        itemDetails.setLayout(new GridLayout(7,2));
        itemDetails.setPreferredSize(new Dimension(300,250));
        itemDetails.add(new JLabel("Enter Description of Item: "));
        itemDetails.add(new JLabel("Sell for: "));
        itemDetails.add(new JTextField());
        itemDetails.add(new JTextField());
        itemDetails.add(new JLabel("Color of Shoe: "));
        itemDetails.add(new JLabel("Shoe Size: "));
        itemDetails.add(new JTextField());
        itemDetails.add(new JTextField());
        itemDetails.add(new JLabel("Shoe Brand: "));
        itemDetails.add(new JLabel("Condition: "));
        itemDetails.add(new JTextField());
        itemDetails.add(new JTextField());
        itemDetails.add(new JLabel(""));
        itemDetails.add(new JButton("Sell Item!"));


        //add(pic,BorderLayout.WEST);
        add(itemDetails,BorderLayout.CENTER);
    }
}
