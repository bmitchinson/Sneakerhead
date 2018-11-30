import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A class to read in messages from the {@link Dealer} that's acting as a server.
 * It extents the {@link JFrame} class and implements {@link Runnable} so that it
 * may be executed alongside multiple instances. It holds all values necessary
 * for viewing the GUI elements and represented cards, as well as holding the
 * needed {@link Socket} connections.
 *
 * @see Dealer
 * @see JFrame
 * @see Runnable
 * @see Socket
 */
public class Client extends JFrame implements Runnable {

    // GUI Elements
    private JTextArea outputArea;
    private JScrollPane outputAreaScroll;
    private Background background;
    private DateTimeFormatter timeFormat;
    private JPanel buttonPanel;
    private JButton hitButton;
    private JButton stayButton;
    private JPanel opponentSpace;
    private JLabel opponentCards;
    private JLabel opponentTotal;
    private JPanel playerSpace;
    private JLabel playerCards;
    private JLabel playerTotal;
    private JLabel status;
    private Client self;

    // Network Connectivity
    private Socket connection;
    private Scanner input;
    private Formatter output;

    // Gameplay
    private boolean gameover = false;
    private Pile playerPile = new Pile(false);
    private Pile opponentPile = new Pile(false);

    /**
     * Configure the Client with the necessary GUI properties and placement
     * within their respective layouts.
     */
    public Client() {
        // Needed to revalidate and repaint entire after component updates
        self = this;

        timeFormat = DateTimeFormatter.ofPattern("hh:mm:ss.SSS - ");

        outputArea = new JTextArea();
        outputArea.setText(LocalTime.now().format(timeFormat) + "Client opened" + "\n");
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        DefaultCaret caret = (DefaultCaret) outputArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        outputAreaScroll = new JScrollPane();
        outputAreaScroll.setViewportView(outputArea);
        outputAreaScroll.setPreferredSize(new Dimension(400, 100));

        background = new Background("/img/table.png");

        hitButton = new JButton("Hit");
        hitButton.setPreferredSize(new Dimension(175, 50));
        hitButton.addActionListener(e -> sendHit());
        stayButton = new JButton("Stay");
        stayButton.setPreferredSize(new Dimension(175, 50));
        stayButton.addActionListener(e -> sendStay());

        buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(400, 80));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 15, 15));
        buttonPanel.add(hitButton);
        buttonPanel.add(stayButton);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(outputAreaScroll);
        add(background);
        add(buttonPanel);

        setSize(400, 700);
        setResizable(false);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setVisible(true);

        startClient();
    }

    /**
     * open up the needed {@link Socket} connections and execute the thread
     * itself.
     *
     * @see Socket
     */
    public void startClient() {
        try {
            connection = new Socket(InetAddress.getByName("127.0.0.1"), 23516);
            input = new Scanner(connection.getInputStream());
            output = new Formatter(connection.getOutputStream());
        } catch (IOException ioException) {
            System.out.println("\nConnection failed? Did you try starting a server first?\n");
            ioException.printStackTrace();
            System.exit(1);
        }
        ExecutorService worker = Executors.newFixedThreadPool(1);
        worker.execute(this);
    }

    /**
     * Await messages delivered by the server
     */
    public void run() {
        displayMessage("Beginning Run");
        setButtonsActive(false);
        while (!gameover) {
            displayMessage("Looking for network input.");
            if (input.hasNextLine()) {
                processMessage(input.nextLine());
            }
        }
    }

    /**
     * Process messages delivered from the server
     *
     * @param message message originally sent from server, interpreted to take
     *                action within the client.
     */
    private void processMessage(String message) {
        displayMessage("Received message type " + message
                + " from server");
        if (message.equals("Title")) {
            setTitle(input.nextLine());
            setStatus(getTitle());
        } else if (message.equals("Cards")) {
            updatePile(true);
            updateBoard();
            background.refreshPlayer();
        } else if (message.equals("OpCards")) {
            updatePile(false);
            updateBoard();
            background.refreshOpponent();
        } else if (message.equals("Buttons")) {
            if (input.nextLine().equals("On")) {
                setButtonsActive(true);
            } else {
                setButtonsActive(false);
            }
        } else if (message.equals("GameOver")) {
            setButtonsActive(false);
            gameover = true;
            String type = input.nextLine();
            if (type.equals("Win")) {
                displayMessage("YOU WON :D");
                setTitle(getTitle() + " - YOU WON!");
                setStatus("YOU WON!!");
            } else if (type.equals("Lose")) {
                displayMessage("...you lost :(");
                setTitle(getTitle() + "- ...you lost :(");
                setStatus("You lost :(");
            } else if (type.equals("Tie")) {
                displayMessage("Tie Game!");
                setTitle(getTitle() + "- Tie Game");
                setStatus("Tie Game");
            } else {
                displayMessage("Why Game Over");
                setTitle(getTitle() + "- Why Game Over");
                setStatus("Why Game Over");
            }
        } else if (message.equals("Message")) {
            displayMessage("" + input.nextLine());
        } else {
            displayMessage("Unsure how to process that.");
        }
    }

    /**
     * Used to trigger the update of a Player's pile. {@link Pile} is reconstructed
     * card by card from the server to accurately effect that player's hand visually
     * on screen.
     *
     * @param player choice of player that is being updated.
     */
    private void updatePile(boolean player) {
        ArrayList<Card> incomingCards = new ArrayList<>();

        String nextMessage = input.nextLine();
        while (!nextMessage.equals("END")) {
            displayMessage("Adding:" + nextMessage.charAt(0)
                    + nextMessage.charAt(1) + " to linked list");
            incomingCards.add(new Card(nextMessage.charAt(0),
                    nextMessage.charAt(1)));
            nextMessage = input.nextLine();
        }

        // Cast linked list back into card array for pile construction
        Object[] objArray = incomingCards.toArray();
        Card[] cardArray = Arrays.copyOf(objArray, objArray.length, Card[].class);
        Pile update = new Pile(cardArray);

        if (player) {
            playerPile = update;
        } else {
            opponentPile = update;
        }

    }

    /**
     * send a hit command to the server, called from the action listener
     * of hitButton and stayButton.
     */
    private void sendHit() {
        displayMessage("Sending Hit to server");
        output.format("Hit\n");
        output.flush();
    }

    /**
     * send a stay command to the server, called from the action listener
     * of hitButton and stayButton.
     */
    private void sendStay() {
        displayMessage("Sending Stay to server");
        output.format("Stay\n");
        output.flush();
    }

    /**
     * display in the onscreen log any updates to the board.
     */
    private void updateBoard() {
        String playerPileString = " ";
        for (String string : playerPile.pileAsStrings()) {
            playerPileString += string + " ";
        }
        String opponentPileString = " ";
        for (String string : opponentPile.pileAsStrings()) {
            opponentPileString += string + " ";
        }
        displayMessage("Player Hand:" + playerPileString);
        displayMessage("Player Hand Total:" + playerPile.getBlackjackTotal());
        displayMessage("Opponent Hand:" + opponentPileString);
        displayMessage("Opponent Hand Total:" + opponentPile.getBlackjackTotal());
    }

    /**
     * Display any message in the on screen terminal to the user.
     *
     * @param message the message to be displayed to the user
     */
    private void displayMessage(final String message) {
        SwingUtilities.invokeLater(
                () -> {
                    outputArea.append(LocalTime.now().format(timeFormat) + message + "\n"); // updates output
                    System.out.println(getTitle() + " " + LocalTime.now().format(timeFormat) + message);
                    JScrollBar scrollBar = outputAreaScroll.getVerticalScrollBar();
                    scrollBar.setValue(scrollBar.getMaximum());
                }
        );
    }

    /**
     * Display any status to be shown to the user inbetween decks of cards
     *
     * @param newStatus status message to be shown
     */
    private void setStatus(final String newStatus) {
        SwingUtilities.invokeLater(() -> {
            status.setText(newStatus);
            self.validate();
            self.repaint();
        });
    }

    /**
     * Set the hit and stay buttons disabled or enabled based on status property
     *
     * @param status each button is setEnabled to given status
     */
    private void setButtonsActive(boolean status) {
        displayMessage("Buttons set to " + status);
        hitButton.setEnabled(status);
        stayButton.setEnabled(status);
    }

    /**
     * Class to represent the image of card, pulled from the local "img" directory
     * and rescaled as needed to fit within the context of the GUI.
     */
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
            Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
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

    /**
     * A class to represent visual elements of Blackjack. Holds a row for each
     * players cards, as well as a label for their total. Also holds methods to
     * update each part, and paints a card table surface in the background as an
     * extension of a {@link JPanel}
     *
     * @see JPanel
     */
    private class Background extends JPanel {
        BufferedImage bgImage;
        JLayeredPane playerCardStack = new JLayeredPane();
        JLayeredPane opponentCardStack = new JLayeredPane();

        /**
         * initialize the many needed GUI elements on screen.
         *
         * @param url the url of the card table background
         */
        Background(String url) {

            try {
                bgImage = ImageIO.read(this.getClass().getResource(url));
            } catch (IOException e) {
                e.printStackTrace();
            }

            status = new JLabel();
            status.setFont(new Font("Veranda", 1, 30));
            status.setForeground(Color.white);
            status.setHorizontalAlignment(SwingConstants.CENTER);

            setBorder(BorderFactory.createLineBorder(Color.black));
            setPreferredSize(new Dimension(392, 500));
            setLayout(new BorderLayout());

            JPanel inPanel = new JPanel();
            inPanel.setPreferredSize(new Dimension(352, 426));
            inPanel.setBackground(new Color(0, 0, 0, 0));
            inPanel.setLayout(new BoxLayout(inPanel, BoxLayout.Y_AXIS));

            opponentSpace = new JPanel();
            opponentSpace.setBorder(BorderFactory.createLineBorder(Color.black));
            opponentSpace.setBackground(Color.WHITE);
            opponentSpace.setMaximumSize(new Dimension(352, 142));
            opponentSpace.setMinimumSize(new Dimension(352, 142));
            opponentSpace.setPreferredSize(new Dimension(352, 142));
            opponentSpace.setLayout(new FlowLayout(FlowLayout.LEADING, 15, 15));

            //opponentCardStack.setBorder(BorderFactory.createLineBorder(Color.black));
            opponentCardStack.setPreferredSize(new Dimension(250, 110));

            opponentTotal = new JLabel();
            opponentTotal.setFont(new Font("Veranda", 1, 28));
            opponentTotal.setText(Integer.toString(opponentPile.getBlackjackTotal()));

            JLabel opponentLabel = new JLabel("Other:");
            opponentLabel.setFont(new Font("Veranda", 1, 14));

            JPanel opponentLabelSet = new JPanel();
            opponentLabelSet.setLayout(new BoxLayout(opponentLabelSet, BoxLayout.Y_AXIS));
            //opponentLabelSet.setBorder(BorderFactory.createLineBorder(Color.black));
            opponentLabelSet.setBackground(new Color(0, 0, 0, 0));

            opponentLabelSet.add(opponentLabel);
            opponentLabelSet.add(opponentTotal);

            opponentSpace.add(opponentCardStack);
            opponentSpace.add(opponentLabelSet);

            JPanel middle = new JPanel();
            middle.setBackground(new Color(0, 0, 0, 0));
            middle.setPreferredSize(new Dimension(352, 70));
            middle.setLayout(new BorderLayout());
            middle.add(status, BorderLayout.CENTER);

            playerSpace = new JPanel();
            playerSpace.setBorder(BorderFactory.createLineBorder(Color.black));
            playerSpace.setBackground(Color.WHITE);
            playerSpace.setMaximumSize(new Dimension(352, 142));
            playerSpace.setMinimumSize(new Dimension(352, 142));
            playerSpace.setPreferredSize(new Dimension(352, 142));
            playerSpace.setLayout(new FlowLayout(FlowLayout.LEADING, 15, 15));

            //playerCardStack.setBorder(BorderFactory.createLineBorder(Color.black));
            playerCardStack.setPreferredSize(new Dimension(250, 110));

            playerTotal = new JLabel();
            playerTotal.setFont(new Font("Veranda", 1, 28));
            playerTotal.setText(Integer.toString(playerPile.getBlackjackTotal()));

            JLabel playerLabel = new JLabel("You:");
            playerLabel.setFont(new Font("Veranda", 1, 14));

            JPanel playerLabelSet = new JPanel();
            playerLabelSet.setLayout(new BoxLayout(playerLabelSet, BoxLayout.Y_AXIS));
            //playerLabelSet.setBorder(BorderFactory.createLineBorder(Color.black));
            playerLabelSet.setBackground(new Color(0, 0, 0, 0));

            playerLabelSet.add(playerLabel);
            playerLabelSet.add(playerTotal);

            playerSpace.add(playerCardStack);
            playerSpace.add(playerLabelSet);

            inPanel.add(opponentSpace);
            inPanel.add(middle);
            inPanel.add(playerSpace);

            JPanel top = new JPanel();
            top.setBackground(new Color(0, 0, 0, 0));
            top.setPreferredSize(new Dimension(392, 37));

            JPanel bot = new JPanel();
            bot.setBackground(new Color(0, 0, 0, 0));
            bot.setPreferredSize(new Dimension(392, 37));

            add(inPanel, BorderLayout.CENTER);
            add(top, BorderLayout.NORTH);
            add(bot, BorderLayout.SOUTH);

        }

        /**
         * Restack the cards on screen that are currently held in the opponents
         * stored pile within {@link Client}
         */
        private void refreshOpponent() {
            SwingUtilities.invokeLater(() -> {
                opponentTotal.setText("\n" + Integer.toString(opponentPile.getBlackjackTotal()));
                opponentCardStack.removeAll();
                int i = 0;
                for (String card : opponentPile.pileAsStrings()) {
                    CardImage tmp = new CardImage(card.charAt(0), card.charAt(1), i);
                    tmp.setBounds(i * 25, 0, 65, 100);
                    opponentCardStack.add(tmp, new Integer(i * 100));
                    i++;
                }
                self.validate();
                self.repaint();
            });
        }

        /**
         * Restack the cards on screen that are currently held in the opponents
         * stored pile within {@link Client}
         */
        private void refreshPlayer() {
            SwingUtilities.invokeLater(() -> {
                playerTotal.setText("\n" + Integer.toString(playerPile.getBlackjackTotal()));
                playerCardStack.removeAll();
                int i = 0;
                for (String card : playerPile.pileAsStrings()) {
                    CardImage tmp = new CardImage(card.charAt(0), card.charAt(1), i);
                    tmp.setBounds(i * 25, 0, 65, 100);
                    playerCardStack.add(tmp, new Integer(i * 100));
                    i++;
                }
                self.validate();
                self.repaint();
            });
        }

        /**
         * Paint card background surface underneath GUI elements
         *
         * @param g background image to be painted underneath
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bgImage, 0, 0, null);
        }

    }

}
