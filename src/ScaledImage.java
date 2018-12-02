import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ScaledImage {
    public static Image getScaledImage(String url, int width, int height){
        BufferedImage tempImage = null;

        try{
            URL imageURL = new URL(url);
            tempImage = ImageIO.read(imageURL);
        }catch (IOException e){
            System.out.println(e);
        }

        Image finalImage = tempImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);

        return finalImage;
    }

    public static Image getSoldImage(Image image){
        Image finalImage;
        Image soldImage = null;
        try{
            soldImage = ImageIO.read(new File(ScaledImage.class.getResource("Image/sold.png").getPath()));
            soldImage = soldImage.getScaledInstance(100,100, Image.SCALE_DEFAULT);
        }catch (IOException e){
            System.out.println(e);
        }

        BufferedImage combined = new BufferedImage(100,100, BufferedImage.TYPE_INT_ARGB);

        Graphics g = combined.getGraphics();

        g.drawImage(image,0,0, null);
        g.drawImage(soldImage, 0 , 0, null);

        finalImage = combined;

        return finalImage;
    }
}
