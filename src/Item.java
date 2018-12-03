import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;

public class Item implements Serializable {
    private int id;
    private String name;
    private String description;
    private String brand;
    private String condition;
    private double size;
    private String color;
    private String gender;
    private double cost;
    private int quantity;
    private String imageURL;
    private String seller;
    private ItemTile itemTile = null;
    private HomeFrame homeFrame;
    private static int colorDecider = 0;

    public Item() {
        this.id = -1;
        this.name = "Item";
        this.description = "Low tops";
        this.brand = "Jordans";
        this.condition = "New";
        this.size = 6.5;
        this.color = "Black";
        this.gender = "Womens";
        this.cost = 25;
        this.quantity = 1;
        this.imageURL = "https://i.imgur.com/aQ4IWz6.png";
        this.seller = "Melanie";
    }

    public Item(String name, String description, String brand,
                String condition, String color, String gender, double size,
                double cost, int quantity, String imageURL, String seller) {
        this.id = -1;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.condition = condition;
        this.size = size;
        this.color = color;
        this.gender = gender;
        this.cost = cost;
        this.quantity = quantity;
        this.imageURL = imageURL;
        this.seller = seller;
    }

    public Item(int id, String name, String description, String brand,
                String condition, String color, String gender, double size,
                double cost, int quantity, String imageURL, String seller) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.condition = condition;
        this.size = size;
        this.color = color;
        this.gender = gender;
        this.cost = cost;
        this.quantity = quantity;
        this.imageURL = imageURL;
        this.seller = seller;
    }

    public int getId() {
        return id;
    }

    ;

    public String getItemName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getBrand() {
        return brand;
    }

    public String getCondition() {
        return condition;
    }

    public String getColor() {
        return color;
    }

    public String getGender() {
        return gender;
    }

    public String getSeller() {
        return seller;
    }

    public double getCost() {
        return cost;
    }

    //TODO: Verify how we want size returned from the getShoeSize method
    public double getShoeSize() {
        return size;
    }

    //TODO: Create an ItemTile Class that shows a condensed version of the Item's data in a (JPanel)
    public ItemTile getItemTile() {
        if (itemTile == null) {
            itemTile = new ItemTile();
            return itemTile;
        } else {
            return itemTile;
        }
    }


    public int getQuantity() {
        return quantity;
    }

    public String getImageURL() {
        return imageURL;
    }

    private Item getThis() {
        return this;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHomeFrame(HomeFrame frame) {
        homeFrame = frame;
    }

    public void decrementQuantity() {
        SwingUtilities.invokeLater(() -> {
            if (quantity != 0) {
                quantity--;
            }
        });
    }

    public void updateTile() {
        SwingUtilities.invokeLater(() -> {
            itemTile.updateQuantityText();
            itemTile.updateImage();
        });
    }

    //TODO: Create an ItemWindow Class that shows the item in its own JFrame
    public void startItemWindow() {
        if (quantity != 0) {
            ItemViewFrame frame = new ItemViewFrame();
        }
    }

    @Override
    public String toString() {
        String desc = (description.length() > 25) ?
                (description.substring(0, 25) + "...") : (description);

        String shortURL = (imageURL.length() > 10) ?
                (imageURL.substring(0, 10) + "...") : (imageURL);

        return ("Item Contents:\nName:" + name + ", Description:" + desc
                + ", Color:" + color + ", Gender:" + gender + ", Size:" + size
                + ", Cost:" + cost + ", Quantity:" + quantity + ", ImgURL:"
                + shortURL + ", Seller:" + seller);
    }

    public static ArrayList<Item> getTestItems() {
        ArrayList<Item> items = new ArrayList<Item>(1024);

        items.add(new Item(
                "Nike Air Max" + 1,
                "These are shoes I bought but couldn't ever wear. They are basically like new and I'm willing to negotiate on the price", "Nike",
                "New",
                "Blue",
                "Mens",
                8,
                40.65,
                1,
                "https://i.imgur.com/C6iJSYy.jpg",
                "Sam"));


        items.add(new Item(
                "Converse High Tops 2",
                "Converse High Tops feature super grip bottoms so you don't slip around in those icy winters",
                "Converse",
                "Like New",
                "Black",
                "Womens",
                4.0,
                59.95,
                1,
                "https://i.imgur.com/daU2fPw.jpg",
                "BigSeller123"
        ));

        for (int i = 0; i < 20; i++) {
            items.add(new Item(
                    "Converse High Tops 2",
                    "Converse High Tops feature super grip bottoms so you don't slip around in those icy winters",
                    "Converse",
                    "Like New",
                    "Black",
                    "Womens",
                    4.0,
                    59.95,
                    1,
                    "https://i.imgur.com/daU2fPw.jpg",
                    "BigSeller123"
            ));
        }
        return items;
    }

    private class ItemTile extends JPanel {
        private final JLabel itemPictureLabel;
        private final JLabel nameLabel;
        private final JLabel costLabel;
        private final JLabel sellerLabel;
        private final JLabel quantityLabel;
        private final JTextArea descriptionArea;
        private final JPanel rightPanel;
        private final JPanel bottomPanel;

        public ItemTile() {
            setPreferredSize(new Dimension(480, 100));
            setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            if (colorDecider == 0) {
                setBackground(new Color(240, 248, 255));
                colorDecider = 1;
            } else if (colorDecider == 1) {
                setBackground(new Color(220, 220, 220));
                colorDecider = 0;
            }

            //Create image JLabel
            ImageIcon itemImageIcon = new ImageIcon();
            itemImageIcon.setImage(ScaledImage.getScaledImage(getImageURL(), 100, 100));
            itemPictureLabel = new JLabel();
            itemPictureLabel.setIcon(itemImageIcon);
            itemPictureLabel.setMinimumSize(new Dimension(100, 100));
            updateImage();

            nameLabel = new JLabel(getItemName());
            nameLabel.setAlignmentX(CENTER_ALIGNMENT);

            costLabel = new JLabel("Cost: " + getCost());
            costLabel.setAlignmentX(RIGHT_ALIGNMENT);
            costLabel.setHorizontalAlignment(SwingConstants.RIGHT);


            sellerLabel = new JLabel("Seller: " + getSeller());
            sellerLabel.setAlignmentX(LEFT_ALIGNMENT);
            sellerLabel.setHorizontalAlignment(SwingConstants.LEFT);
            //sellerLabel.setPreferredSize(new Dimension(120,25));

            quantityLabel = new JLabel("Quantity: " + getQuantity());
            quantityLabel.setAlignmentX(LEFT_ALIGNMENT);
            quantityLabel.setHorizontalAlignment(SwingConstants.LEFT);


            descriptionArea = new JTextArea();
            initializeDescriptionArea(getDescription());

            //Panel that holds bottom 3 tables
            bottomPanel = new JPanel();
            bottomPanel.setBackground(this.getBackground());
            bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
            bottomPanel.add(sellerLabel);
            bottomPanel.add(Box.createHorizontalStrut(10));
            bottomPanel.add(quantityLabel);
            bottomPanel.add(Box.createHorizontalGlue());
            bottomPanel.add(costLabel);
            bottomPanel.add(Box.createHorizontalStrut(5));

            //Panel that holds Name description and bottom Panel, This panel is to the right of the image
            rightPanel = new JPanel();
            rightPanel.setBackground(this.getBackground());
            rightPanel.setPreferredSize(new Dimension(365, 100));
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
            rightPanel.add(nameLabel);
            rightPanel.add(Box.createVerticalStrut(20));
            rightPanel.add(descriptionArea);
            rightPanel.add(Box.createVerticalStrut(10));
            rightPanel.add(bottomPanel);

            //add image and rightPanel to the base JPanel
            add(Box.createHorizontalStrut(5));
            add(itemPictureLabel);
            add(Box.createHorizontalStrut(5));
            add(rightPanel);
            add(Box.createHorizontalStrut(5));

            //Listeners to start window
            PanelListener listener = new PanelListener();
            addMouseListener(listener);
            rightPanel.addMouseListener(listener);
            descriptionArea.addMouseListener(listener);
        }

        private void initializeDescriptionArea(String description) {
            //descriptionArea.setAlignmentX(LEFT_ALIGNMENT);
            descriptionArea.setBackground(this.getBackground());
            //descriptionArea.setMinimumSize(new Dimension(365, 25));
            descriptionArea.setEditable(false);
            descriptionArea.setLineWrap(true);
            descriptionArea.setWrapStyleWord(true);

            if (description.length() >= 55) {
                descriptionArea.setText(description.substring(0, 55) + "...");
            } else {
                descriptionArea.setText(description);
            }
        }

        public void updateQuantityText() {
            quantityLabel.setText("Quantity: " + getQuantity());
        }

    /*public void updateBackGround(){
        if(item.getQuantity() == 0){
            int r = 255;
            int g = 182;
            int b = 178;
            Color color = new Color(r,g,b);
            setBackground(color);
            rightPanel.setBackground(color);
            bottomPanel.setBackground(color);
            descriptionArea.setBackground(color);
        }
    }*/

        public void updateImage() {
            if (getQuantity() == 0) {
                Image image = ScaledImage.getScaledImage(getImageURL(), 100, 100);
                itemPictureLabel.setIcon(new ImageIcon(ScaledImage.getSoldImage(image)));
            }

        }


        private class PanelListener extends MouseAdapter {
            @Override
            public void mouseClicked(MouseEvent e) {
                startItemWindow();
            }
        }

    }

    public class ItemViewFrame extends JFrame {

        private JButton buyButton = new JButton("Buy Shoe!");
        private JPanel itemDetails = new JPanel();
        private JPanel boxedFrame = new JPanel();
        private JTextArea descriptionText = new JTextArea();
        private JLabel quantityLabel;
        //private Color color = new Color(255,215,204);
        //private Color color = new Color(240,248,255);
        private Color color = new Color(255, 250, 240);
        private NumberFormat formatter = NumberFormat.getCurrencyInstance();

        //TODO: Change cost string

        ItemViewFrame() {

            setTitle("");
            setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

            //add a JPanel to initial frame, put a border layout
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            mainPanel.setPreferredSize(new Dimension(590, 500));
            mainPanel.setBackground(new Color(245, 245, 245));

            //add name label to top of border layout
            JLabel name = new JLabel(getItemName());

            name.setHorizontalAlignment(SwingConstants.CENTER);
            name.setFont(new Font("Helvetica", Font.BOLD, 18));

            //created new JPanel with 1 row, 2 columns to add to center of border layout
            boxedFrame.setLayout(new GridLayout(1, 2));
            boxedFrame.setBackground(color);

            //Put a picture in column 1
            ImageIcon picPass = new ImageIcon();
            picPass.setImage(ScaledImage.getScaledImage(getImageURL(), 250, 250));
            JLabel pic = new JLabel(picPass);
            boxedFrame.add(pic);

            //Put item description in column 2
            descriptionText.setBackground(color);
            descriptionText.setText(getDescription());
            descriptionText.setLineWrap(true);
            descriptionText.setWrapStyleWord(true);
            descriptionText.setEditable(false);

            //JPanel itemDetails = new JPanel();
            //itemDetails.setLayout(new GridLayout(10,1));
            itemDetails.setLayout(new BoxLayout(itemDetails, BoxLayout.PAGE_AXIS));
            //itemDetails.setPreferredSize(new Dimension(400,450));
            itemDetails.setBackground(color);

            JLabel descripLabel = new JLabel("Description: ");
            JLabel conditionLabel = new JLabel("Condition: " + getCondition());
            JLabel sizeLabel = new JLabel("Size: " + getShoeSize() + " " + getGender());
            JLabel colorLabel = new JLabel("Color: " + getColor());
            JLabel priceLabel = new JLabel("Price: " + getCost());
            JLabel sellerLabel = new JLabel("Seller: " + getSeller());
            quantityLabel = new JLabel("Quantity: " + getQuantity());

            itemDetails.add(leftJustify(descripLabel));
            itemDetails.add(descriptionText);
            itemDetails.add(leftJustify(conditionLabel));
            itemDetails.add(leftJustify(sizeLabel));
            itemDetails.add(leftJustify(colorLabel));
            itemDetails.add(leftJustify(priceLabel));
            itemDetails.add(leftJustify(quantityLabel));
            itemDetails.add(leftJustify(sellerLabel));
            itemDetails.add(buyButton);

            //add all components
            boxedFrame.add(pic);
            boxedFrame.add(itemDetails);

            mainPanel.add(name, BorderLayout.NORTH);
            mainPanel.add(boxedFrame, BorderLayout.CENTER);
            add(mainPanel);

            //add listener to the buyButton
            buyButton.addActionListener(e -> buttonHit());
            System.out.println("Buyer stat: " + homeFrame.isBuyer());
            buyButton.setEnabled(homeFrame.isBuyer());

            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setSize(600, 510);
            this.setVisible(true);
        }

        private void buttonHit() {
            BuyItemRequest buyItemRequest = new BuyItemRequest(getThis());
            SwingUtilities.invokeLater(() -> {
                if ((Boolean) homeFrame.makeRequest(buyItemRequest)) {
                    buyButton.setText("Bought!");
                    buyButton.setEnabled(false);
                    itemDetails.setBackground(Color.LIGHT_GRAY);
                    boxedFrame.setBackground(Color.LIGHT_GRAY);
                    descriptionText.setBackground(Color.LIGHT_GRAY);
                    decrementQuantity();
                    updateTile();
                } else {
                    homeFrame.updateAllItems();
                    JOptionPane.showMessageDialog(null, "Error buying item please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                quantityLabel.setText("Quantity: " + getQuantity());
            });
        }

        private Component leftJustify(JLabel label) {
            Box b = Box.createHorizontalBox();
            b.add(label);
            b.add(Box.createHorizontalGlue());
            // (Note that you could throw a lot more components
            // and struts and glue in here.)
            return b;
        }
    }
}


