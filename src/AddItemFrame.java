import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddItemFrame extends JFrame {

    //TODO: add link to browse for url
    private final JButton postButton = new JButton("Post Item!");
    private final JTextField nameField = new JTextField();
    private final JTextField descriptionField = new JTextField();
    private final JTextField priceField = new JTextField();
    private final JTextField colorField= new JTextField();
    private final JTextField brandField = new JTextField();
    private final JComboBox<String> conditionBox = new JComboBox();
    private final JComboBox sizeBox = new JComboBox();
    private final JComboBox<String> genderBox = new JComboBox();
    private final JTextField quantityField = new JTextField();
    private final JTextField imageURLField = new JTextField();
    private final HomeFrame homeFrame;

    AddItemFrame(HomeFrame homeFrame){
        super("Add Item for Sale");
        this.homeFrame = homeFrame;

        JPanel itemDetails = new JPanel();
        itemDetails.setLayout(new GridLayout(11,2,5,5));

        genderBox.addItem("Men's");
        genderBox.addItem("Women's");

        conditionBox.addItem("Brand New");
        conditionBox.addItem("Like New");
        conditionBox.addItem("Used");
        conditionBox.addItem("Very Used");
        conditionBox.addItem("Heavily Used");

        for(double i=1; i<16.5; i += .5){
            sizeBox.addItem(String.valueOf(i));
        }

        itemDetails.add(new JLabel("Name of Item: "));
        itemDetails.add(new JLabel("Description: "));
        itemDetails.add(nameField);
        itemDetails.add(descriptionField);
        itemDetails.add(new JLabel("Brand: "));
        itemDetails.add(new JLabel("Color: "));
        itemDetails.add(brandField);
        itemDetails.add(colorField);
        itemDetails.add(new JLabel("Condition: "));
        itemDetails.add(new JLabel("sizeField: "));
        itemDetails.add(conditionBox);
        itemDetails.add(sizeBox);
        itemDetails.add(new JLabel("Gender: "));
        itemDetails.add(new JLabel("Quantity: "));
        itemDetails.add(genderBox);
        itemDetails.add(quantityField);
        itemDetails.add(new JLabel("Image URL: "));
        itemDetails.add(new JLabel("Price: "));
        itemDetails.add(imageURLField);
        itemDetails.add(priceField);
        itemDetails.add(new JPanel());
        itemDetails.add(postButton);

        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                //TODO: Talk to database
                //if (addSuccessful){
                //  updateGUI
                //}

                Item testItem = new Item(
                        nameField.getText(),
                        descriptionField.getText(),
                        brandField.getText(),
                        (String) conditionBox.getSelectedItem(),
                        colorField.getText(),
                        (String) genderBox.getSelectedItem(),
                        Double.valueOf((String) sizeBox.getSelectedItem()),
                        Double.valueOf(priceField.getText()),
                        Integer.valueOf(quantityField.getText()),
                        imageURLField.getText(),
                        "TestSeller");

                homeFrame.addItem(testItem);

                //updateGUI Code here
            }
        });

        add(itemDetails);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(500,500);
        this.setVisible(true);
    }

}
