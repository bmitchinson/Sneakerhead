import javax.swing.*;
import java.awt.*;

public class AddItemFrame extends JFrame {

    AddItemFrame(){
        super("Add Item for Sale");
        setLayout(new BorderLayout());

        //JPanel pic = new JPanel();

        JPanel itemDetails = new JPanel();
        itemDetails.setLayout(new GridLayout(4,2));
        itemDetails.setPreferredSize(new Dimension(300,250));
        itemDetails.add(new JLabel("Description: "));
        itemDetails.add(new JLabel("Size: "));
        itemDetails.add(new JLabel(""));
        itemDetails.add(new JLabel("Color: "));
        itemDetails.add(new JLabel(""));
        itemDetails.add(new JLabel("Price: "));
        itemDetails.add(new JLabel(""));
        itemDetails.add(new JButton("Buy Item!"));

        //add(pic,BorderLayout.WEST);
        add(itemDetails,BorderLayout.EAST);
    }
}
