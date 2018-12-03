import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class AddItemFrame extends JFrame {

    //TODO: add link to browse for url
    private final JButton postButton = new JButton("Post Item!");
    private final JTextField nameField = new JTextField();
    private final JTextField descriptionField = new JTextField();
    private final JTextField priceField = new JTextField();
    private final JTextField colorField = new JTextField();
    private final JTextField brandField = new JTextField();
    private final JComboBox<String> conditionBox = new JComboBox();
    private final JComboBox sizeBox = new JComboBox();
    private final JComboBox<String> genderBox = new JComboBox();
    private final JTextField quantityField = new JTextField();
    private final JTextField imageURLField = new JTextField();
    private final JPanel blank = new JPanel();
    private final HomeFrame homeFrame;

    AddItemFrame(HomeFrame homeFrame) {
        super("Add Item for Sale");
        this.homeFrame = homeFrame;

        JPanel itemDetails = new JPanel();
        itemDetails.setBackground(new Color(204, 229, 255));
        itemDetails.setBorder(new EmptyBorder(10, 10, 10, 10));
        itemDetails.setLayout(new GridLayout(11, 2, 5, 5));

        blank.setBackground(itemDetails.getBackground());

        genderBox.addItem("Mens");
        genderBox.addItem("Womens");

        conditionBox.addItem("Brand New");
        conditionBox.addItem("Like New");
        conditionBox.addItem("Used");
        conditionBox.addItem("Very Used");
        conditionBox.addItem("Heavily Used");

        for (double i = 5; i < 16.5; i += .5) {
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
        itemDetails.add(blank);
        itemDetails.add(postButton);

        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
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


                    verifyItem(testItem);

                    SellItemRequest request = new SellItemRequest(testItem);
                    if ((boolean) homeFrame.makeRequest(request)){
                        homeFrame.addItem(testItem);
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Sorry, that Item is already on the market.",
                                "Error posting item", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (NumberFormatException ex) {
                    System.out.println(ex);
                    JOptionPane.showMessageDialog(null,
                            "Your cost or quantity input was invalid. Please check your inputs and try to re-post the Item.",
                            "Error posting item", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    System.out.println(ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(),
                            "Error posting item", JOptionPane.ERROR_MESSAGE);
                }


            }
        });

        add(itemDetails);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(500, 500);
        this.setVisible(true);
    }

    public void verifyItem (Item item) throws IllegalArgumentException, IOException {
        if (item.getItemName().equals("")) {
            throw new IllegalArgumentException("The name you entered was invalid. Please check your input and try to re-post the Item");
        } else if (item.getQuantity() < 1) {
            throw new IllegalArgumentException("The quantity that you entered was invalid. Please check your input and try to re-post the Item");
        } else if (item.getCost() < 0) {
            throw new IllegalArgumentException("The cost that you entered was invalid. Please check your input and try to re-post the Item");
        } else if (item.getColor().length() == 0) {
            throw new IllegalArgumentException("The color that you entered was invalid. Please check your input and try to re-post the Item");
        } else if (item.getSeller().length() == 0) {
            throw new IllegalArgumentException("The seller that you entered was invalid. Please check your input and try to re-post the Item");
        } else if (item.getDescription().length() == 0) {
            throw new IllegalArgumentException("The description you entered was invalid. Please check your input and try to re-post the Item");
        } else if (item.getBrand().length() == 0) {
            throw new IllegalArgumentException("The brand you entered was invalid. Please check your input and try to re-post the Item");
        } else if(item.getImageURL().length() == 0){
            throw new IllegalArgumentException("The URL you entered was invalid. Please check your input and try to re-post the Item");
        }

        try{
            BufferedImage testImage = ImageIO.read(new URL(item.getImageURL()));
        }catch (IOException e){
            throw new IllegalArgumentException("The URL you entered did not correctly find an image. Please check your input and try to re-post the Item.");
        }

    }

    public static void main(String[] args) {
        AddItemFrame frame = new AddItemFrame(new HomeFrame());
    }

}
