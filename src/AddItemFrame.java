import javax.swing.*;
import java.awt.*;

public class AddItemFrame extends JFrame {

    //TODO: add link to browse for url

    AddItemFrame(){
        super("Add Item for Sale");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel itemDetails = new JPanel();
        itemDetails.setLayout(new GridLayout(7,2));
        itemDetails.setMaximumSize(new Dimension(480,275));
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

        add(itemDetails);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,300);
        this.setVisible(true);
    }

    public static void main(String[] args){
        AddItemFrame frame = new AddItemFrame();
    }
}
