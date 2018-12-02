import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ItemTile extends JPanel{
    private final JLabel itemPictureLabel;
    private final JLabel nameLabel;
    private final JLabel costLabel;
    private final JLabel sellerLabel;
    private final JLabel quantityLabel;
    private final JTextArea descriptionArea;
    private final Item item;
    private static int colorDecider = 0;

    public ItemTile(Item item){
        this.item = item;
        setPreferredSize(new Dimension(480,100));
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        if(colorDecider == 0){
            setBackground(Color.GRAY);
            colorDecider = 1;
        }
        else if(colorDecider == 1){
            setBackground(Color.LIGHT_GRAY);
            colorDecider = 0;
        }

        //Create image JLabel
        ImageIcon itemImageIcon = new ImageIcon();
        itemImageIcon.setImage(ScaledImage.getScaledImage(item.getImageURL(),100,100));
        itemPictureLabel = new JLabel(itemImageIcon);
        itemPictureLabel.setMinimumSize(new Dimension(100,100));

        nameLabel = new JLabel(item.getName());
        nameLabel.setAlignmentX(LEFT_ALIGNMENT);

        costLabel = new JLabel("Cost: " + item.getCost());
        costLabel.setAlignmentX(RIGHT_ALIGNMENT);

        sellerLabel = new JLabel("Seller: " + item.getSeller());
        sellerLabel.setAlignmentX(LEFT_ALIGNMENT);

        quantityLabel = new JLabel("Quantity: " + item.getQuantity());
        quantityLabel.setAlignmentX(LEFT_ALIGNMENT);

        descriptionArea = new JTextArea();
        initializeDescriptionArea(item.getDescription());

        //Panel that holds bottom 3 tables
        JPanel bottomPanel = new JPanel();
        //bottomPanel.setAlignmentX(LEFT_ALIGNMENT);
        bottomPanel.setBackground(this.getBackground());
        bottomPanel.add(sellerLabel);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(quantityLabel);
        bottomPanel.add(Box.createHorizontalStrut(100));
        bottomPanel.add(costLabel);

        //Panel that holds Name description and bottom Panel, This panel is to the right of the image
        JPanel rightPanel = new JPanel();
        rightPanel.setAlignmentX(LEFT_ALIGNMENT);
        rightPanel.setBackground(this.getBackground());
        rightPanel.setPreferredSize(new Dimension(365,100));
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

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JOptionPane.showMessageDialog(getParent(),"This worked" + item.getName());
            }
        });
        PanelListener listener = new PanelListener();
        addMouseListener(listener);
        descriptionArea.addMouseListener(listener);
    }

    private void initializeDescriptionArea(String description){
        //descriptionArea.setAlignmentX(LEFT_ALIGNMENT);
        descriptionArea.setBackground(this.getBackground());
        //descriptionArea.setMinimumSize(new Dimension(365, 25));
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        if(description.length() >= 55){
            descriptionArea.setText(description.substring(0, 60) + "...");
        }else{
            descriptionArea.setText(description);
        }
    }

    private class PanelListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            JOptionPane.showMessageDialog(getParent(), item.getName());
        }
    }

    public static void main(String[] args){
        JFrame frame = new JFrame();
        Item[] items = Item.getTestItems();
        ItemTile tile = new ItemTile(items[0]);

        frame.setLayout(new FlowLayout());
        frame.add(tile);
        frame.setSize(500,150);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
