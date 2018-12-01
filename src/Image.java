import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Image {
    private class CardImage extends JPanel {
        BufferedImage cardImage;

        /**
         * Initialize the image based on the requested suit, value, and where it
         * lays in the pile in order on screen.
         *
         * @param suit  suit of the card
         * @param value value of the card
         * @param index order of card in onscreen pile
         */
        CardImage(char suit, char value, int index) {
            String url = "/img/cards/" +
                    Character.toString(suit) + Character.toString(value) +
                    ".png";
            try {
                cardImage = ImageIO.read(this.getClass().getResource(url));
                cardImage = resize(cardImage, 65, 96);
            } catch (IOException e) {
                e.printStackTrace();
            }

            setBounds(index * 25, 0, 65, 93);
        }

        /**
         * A method to resize an image as needed
         *
         * @param img  the image to be resized
         * @param newW the desired width of the newly resized image
         * @param newH the desired height of the newly resized image
         * @return the resized image
         */
        private BufferedImage resize(BufferedImage img, int newW, int newH) {
            java.awt.Image tmp = img.getScaledInstance(newW, newH, java.awt.Image.SCALE_SMOOTH);
            BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = dimg.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();

            return dimg;
        }

        /**
         * Draw the stored card image post resize to the panel for use in the GUI
         *
         * @param g the graphic to be painted
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(cardImage, 0, 0, null);
        }

    }
}
