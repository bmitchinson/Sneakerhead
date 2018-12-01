import javax.swing.*;
import java.awt.*;

public class ItemTile extends JPanel{
    private final ImageIcon itemPicture;

    public ItemTile(){
        setPreferredSize(new Dimension(480,170));
        //setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setLayout(new GridLayout(1,1));
       itemPicture = new ImageIcon();
       itemPicture.setImage(ScaledImage.getScaledImage("https://i.imgur.com/C6iJSYy.jpg",150,150));
    }

    public static void main(String[] args){
        JFrame frame = new JFrame();
        ItemTile tile = new ItemTile();

        frame.setLayout(new FlowLayout());
        frame.add(tile);
        frame.setSize(500,500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
