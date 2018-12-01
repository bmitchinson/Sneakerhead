import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ItemTile extends JPanel{
    public ItemTile(Item item){
        setPreferredSize(new Dimension(480,170));
        //setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setLayout(new GridLayout(1,1));

        ImageIcon image = new ImageIcon();
        BufferedImage tempImage = null;

        try{
            URL url = new URL("https://i.imgur.com/C6iJSYy.jpg");
            tempImage = ImageIO.read(url);

        }catch (IOException e){
            System.out.println(e);
        }

        image.setImage(tempImage);
        add(new JLabel(image));



    }

    public static void main(String[] args){

    }
}
