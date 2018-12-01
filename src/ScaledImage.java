import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ScaledImage {
    public static Image getScaledImage(String url, int width, int height){
        ImageIcon image = new ImageIcon();
        BufferedImage tempImage = null;

        try{
            URL imageURL = new URL(url);
            tempImage = ImageIO.read(imageURL);
        }catch (IOException e){
            System.out.println(e);
        }

        Image finalImage = tempImage.getScaledInstance(400,400, Image.SCALE_DEFAULT);

        return finalImage;
    }
}
