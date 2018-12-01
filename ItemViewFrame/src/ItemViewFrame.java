import javax.swing.*;
import java.awt.*;

public class ItemViewFrame extends JFrame {

    ItemViewFrame(){
        super("Item for Sale");
        setLayout(new BorderLayout());

        String itemName = "Test Item";
        JLabel name = new JLabel(itemName);
        name.setHorizontalAlignment(SwingConstants.CENTER);
        name.setVerticalAlignment(SwingConstants.BOTTOM);
        name.setFont(new Font("Helvetica", Font.BOLD, 16));

        JPanel pic = new JPanel();

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


        add(name,BorderLayout.NORTH);
        add(pic,BorderLayout.WEST);
        //add(new JSeparator(),BorderLayout.CENTER);
        add(itemDetails,BorderLayout.EAST);
    }
}
