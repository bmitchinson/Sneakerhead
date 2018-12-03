import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
//Represents an Item
public class Item implements Serializable {
    //id is the id the item is in the database
    private int id;
    //name is the name of the item displayed to the shoppers
    private String name;
    //description is a description of the shoe
    private String description;
    //brand is the brand of the shoe
    private String brand;
    //condition is the condition of the shoe
    private String condition;
    //size is the size of the shoe
    private double size;
    //color is the color of the shoe
    private String color;
    //gender is the gender the shoe is made for
    private String gender;
    //cost is the cost of the shoe
    private double cost;
    //quantity is the number of shoes available
    private int quantity;
    //imageURL is the url that points to the image of the shoe
    private String imageURL;
    //seller is the seller of the shoe
    private String seller;
    //itemTile is the ItemTile the shoe is displayed on in HomeFrame
    private ItemTile itemTile = null;
    //homeFrame is reference to the HomeFrame that displays the list of shoes
    private HomeFrame homeFrame;
    //color decider is a static int used by the ItemTile class to decide how to color the JPanel
    private static int colorDecider = 0;

    //item constructor without an ID that can be used to add and item to the data base, which will then set the id of the item correctly
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

    //item constructor with every variable initialized
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

    //return id
    public int getId() {
        return id;
    }

    //return name
    public String getItemName() {
        return name;
    }

    //return description
    public String getDescription() {
        return description;
    }

    //return brand
    public String getBrand() {
        return brand;
    }

    //return condition
    public String getCondition() {
        return condition;
    }

    //return color
    public String getColor() {
        return color;
    }

    //return gender
    public String getGender() {
        return gender;
    }

    //return seller
    public String getSeller() {
        return seller;
    }

    //return cost
    public double getCost() {
        return cost;
    }

    //return size
    public double getShoeSize() {
        return size;
    }

    //set id of item, used by database to correctly add item
    public void setId(int id) {
        this.id = id;
    }


    //get quantity
    public int getQuantity() {
        return quantity;
    }

    //get url of image
    public String getImageURL() {
        return imageURL;
    }

    //create item tile, or get reference if already created
    public ItemTile getItemTile() {
        if (itemTile == null) {
            itemTile = new ItemTile();
            return itemTile;
        } else {
            return itemTile;
        }
    }

    //get reference to this item
    private Item getThis() {
        return this;
    }

    //set reference to the HomeFrame serving as the homepage of the client
    public void setHomeFrame(HomeFrame frame) {
        homeFrame = frame;
    }

    //decrement quantity if item is bought
    public void decrementQuantity() {
        SwingUtilities.invokeLater(() -> {
            if (quantity != 0) {
                quantity--;
            }
        });
    }

    //updateTile updates the ItemTile after an item is bought
    public void updateTile() {
        SwingUtilities.invokeLater(() -> {
            itemTile.updateQuantityText();
            itemTile.updateImage();
        });
    }

    //startItemWindow to display detailed view to user
    public void startItemWindow() {
        if (quantity != 0) {
            ItemViewFrame frame = new ItemViewFrame();
        }
    }

    //method to print Item to String
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

    //ItemTile is a class that serves as a preview for the Item, the user will see this tile and click on it to buy, or view a more detailed view
    private class ItemTile extends JPanel {
        //displays picture of Item
        private final JLabel itemPictureLabel;
        //displays name of Item
        private final JLabel nameLabel;
        //displays cost of Item
        private final JLabel costLabel;
        //displays seller of Item
        private final JLabel sellerLabel;
        //displays quantity of Item
        private final JLabel quantityLabel;
        //displays description of Item
        private final JTextArea descriptionArea;
        //holds everything but the itemPictureLabel
        private final JPanel rightPanel;
        //holds seller, cost, and name labels
        private final JPanel bottomPanel;

        //constructor to make an ItemTile which extends JPanel
        public ItemTile() {
            //format window
            setPreferredSize(new Dimension(480, 100));
            setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

            //method to decide what color the JPanel should be, the store will alternate colors for easy viewing
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

            //format labels
            nameLabel = new JLabel(getItemName());
            nameLabel.setAlignmentX(CENTER_ALIGNMENT);

            costLabel = new JLabel("Cost: " + getCost());
            costLabel.setAlignmentX(RIGHT_ALIGNMENT);
            costLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            sellerLabel = new JLabel("Seller: " + getSeller());
            sellerLabel.setAlignmentX(LEFT_ALIGNMENT);
            sellerLabel.setHorizontalAlignment(SwingConstants.LEFT);

            quantityLabel = new JLabel("Quantity: " + getQuantity());
            quantityLabel.setAlignmentX(LEFT_ALIGNMENT);
            quantityLabel.setHorizontalAlignment(SwingConstants.LEFT);

            //create desription Area then initialize
            descriptionArea = new JTextArea();
            initializeDescriptionArea(getDescription());

            //Panel that holds bottom 3 labels
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

            //Listeners to start window on click
            PanelListener listener = new PanelListener();
            addMouseListener(listener);
            rightPanel.addMouseListener(listener);
            descriptionArea.addMouseListener(listener);
        }

        //method initializes description area
        private void initializeDescriptionArea(String description) {
            descriptionArea.setBackground(this.getBackground());
            descriptionArea.setEditable(false);
            descriptionArea.setLineWrap(true);
            descriptionArea.setWrapStyleWord(true);

            if (description.length() >= 55) {
                descriptionArea.setText(description.substring(0, 55) + "...");
            } else {
                descriptionArea.setText(description);
            }
        }

        //update quantity text of ItemTile
        public void updateQuantityText() {
            quantityLabel.setText("Quantity: " + getQuantity());
        }

        //update image to sold if quantity = 0
        public void updateImage() {
            if (getQuantity() == 0) {
                Image image = ScaledImage.getScaledImage(getImageURL(), 100, 100);
                itemPictureLabel.setIcon(new ImageIcon(ScaledImage.getSoldImage(image)));
            }

        }

        //listener to start ItemWindow on click
        private class PanelListener extends MouseAdapter {
            @Override
            public void mouseClicked(MouseEvent e) {
                homeFrame.updateAllItems();
                startItemWindow();
            }
        }

    }

    //ItemView frame gets started when the user clicks an ItemTile and shows a detailed view of the item
    public class ItemViewFrame extends JFrame {
        //button to buy shoe
        private JButton buyButton = new JButton("Buy Shoe!");
        //Panel holds itemDetails
        private JPanel itemDetails = new JPanel();
        //Frame to add to the JFrame
        private JPanel boxedFrame = new JPanel();
        //text area that holds description
        private JTextArea descriptionText = new JTextArea();
        //label that holds quantity
        private JLabel quantityLabel;
        //color to set panel as
        private Color color = new Color(255, 250, 240);

        //constructor for ItemViewFrame initializes the JFrame
        ItemViewFrame() {
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

            //add components
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

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    super.windowClosed(e);
                    homeFrame.updateAllItems();
                }
            });

            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setSize(600, 510);
            this.setVisible(true);
        }

        //on button click buy item
        private void buttonHit() {
            BuyItemRequest buyItemRequest = new BuyItemRequest(getThis());
            SwingUtilities.invokeLater(() -> {
                //request buy
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
                    JOptionPane.showMessageDialog(null, "Error buying item please try again. Please the item windows and try to re-open it to buy"
                            , "Error", JOptionPane.ERROR_MESSAGE);
                }
                quantityLabel.setText("Quantity: " + getQuantity());
            });
        }

        //Left justify a component
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


