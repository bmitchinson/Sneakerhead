import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The Dealer class is an extension of the {@link JFrame} object that fulfils many
 * purposes in the operation of running the game of BlackJack. It initializes
 * all of the Game Logic needed to play, including the two {@link Player} runnables,
 * a deck of cards (using the {@link Pile} class), all gui elements configured in
 * {@link #Dealer()}, thread management with Locks for sleep and wake each Player,
 * and the {@link Socket} connections used to deliver input to each player.
 * <p>
 * This class and it's lock and thread management is heavily modeled after the
 * provided textbook example: "28_11_14".
 *
 * @author Ben Mitchinson
 * @see JFrame
 * @see Player
 * @see #Dealer()
 * @see Socket
 */
public class Dealer extends JFrame {

    // Game Logic
    private Pile deck;
    private Player[] players;
    private String currentPlayer;
    private Pile[] playersHands = {new Pile(false),
            new Pile(false)};

    // GUI Elements
    private JTextArea outputArea;

    // Networking + Threading
    private ServerSocket server;
    private DateTimeFormatter timeFormat;
    private ExecutorService runGame;
    private Lock gameLock;
    private Condition otherPlayerConnected;
    private Condition otherPlayerTurn;

    /**
     * The dealer constructor configures the GUI of the server log using
     * {@link JFrame} elements. It then initializes both {@link Player} objects,
     * and configures a newly shuffled {@link Pile} to act as the deck for dealing.
     * It also opens up the initial conditions for thread notifications upon later
     * locks, and initializes the {@link Socket} connections to port 23516. It
     * also deals 2 cards from the pile to the players for their opening hand.
     *
     * @see JFrame
     * @see Player
     * @see Socket
     * @see Player
     * @see ReentrantLock
     * @see Executors
     */
    public Dealer() {
        super("Dealer (Server Log)");

        runGame = Executors.newFixedThreadPool(2);
        gameLock = new ReentrantLock();
        otherPlayerConnected = gameLock.newCondition();
        otherPlayerTurn = gameLock.newCondition();
        timeFormat = DateTimeFormatter.ofPattern("hh:mm:ss.SSS - ");

        players = new Player[2];
        currentPlayer = "Player One";

        deck = new Pile(true);
        playersHands[0] = deck.removeFromPile(2);
        playersHands[1] = deck.removeFromPile(2);

        try {
            server = new ServerSocket(23516, 12); // Setup socket
        } catch (IOException ioException) {
            System.out.println("\nPort 23516 is already in use, " +
                    "do you already have a server running?\n");
            ioException.printStackTrace();
            System.exit(1);
        }

        outputArea = new JTextArea();
        outputArea.setText(LocalTime.now().format(timeFormat) +
                "Server opened and awaiting connections\n");
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
        setSize(400, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setVisible(true);

    }

    /**
     * The execute method initializes two new player objects with the proper
     * connections and titles, suspends until both are connected, and executes
     * both threads, detailed in the {@link Player} runnables.
     */
    public void execute() {
        try {
            displayMessage("Waiting for first connection");
            players[0] = new Player(server.accept(), "Player One");
            runGame.execute(players[0]);
            displayMessage("Waiting for second connection");
            players[1] = new Player(server.accept(), "Player Two");
            runGame.execute(players[1]);

        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.exit(1);
        }
        gameLock.lock();
        try {
            players[0].setSuspended(false);
            otherPlayerConnected.signal();
        } finally {
            gameLock.unlock();
        }
    }

    /**
     * DisplayMessage is a method used throughout the class to properly log a
     * message for display in the main server log text area.
     *
     * @param message the desired message to be displayed.
     * @see SwingUtilities
     */
    private void displayMessage(final String message) {
        SwingUtilities.invokeLater(
                () -> {
                    outputArea.append(LocalTime.now().format(timeFormat) + message + "\n");
                    System.out.println(LocalTime.now().format(timeFormat) + message);
                }
        );
    }

    /**
     * isGameOver calculates the total of each players hand using the {@link Pile}
     * method getBlackjackTotal. Depending on which {@link Player} holds a hand
     * over 21, the method updates the required GUI elements of the game, and
     * returns true so that dependent threads know to stop playing. Otherwise,
     * if no players are over 21, the method returns false and the game continues.
     *
     * @return if the game is over or not based on player totals
     */
    private boolean isGameOver() {
        if (playersHands[0].getBlackjackTotal() > 21) {
            players[0].sendMessage("GameOver", "Lose");
            players[1].sendMessage("OpCards", playersHands[0].pileAsStrings());
            players[1].sendMessage("GameOver", "Win");
            return true;
        } else if (playersHands[1].getBlackjackTotal() > 21) {
            players[0].sendMessage("OpCards", playersHands[1].pileAsStrings());
            players[0].sendMessage("GameOver", "Win");
            players[1].sendMessage("GameOver", "Lose");
            return true;
        } else if (playersHands[0].getBlackjackTotal() == 21 &&
                playersHands[1].getBlackjackTotal() == 21) {
            players[0].sendMessage("GameOver", "Tie");
            players[1].sendMessage("GameOver", "Tie");
            return true;
        } else {
            return false;
        }
    }

    /**
     * The Player class implements the {@link Runnable} interface so that it may
     * be executed upon {@link Socket} connection in {@link #execute()}. It's
     * initialized with connections to the server ({@link Dealer}), and executes
     * with logic further detailed in {@link #run()}. It holds methods to deliver
     * messages to the connected {@link Client}, and receive button choices from
     * {@link #getHit()}.
     *
     * @see Runnable
     * @see Socket
     * @see #execute()
     * @see Dealer
     * @see Client
     * @see #run()
     */
    private class Player implements Runnable {
        private Socket connection;
        private Scanner input;
        private Formatter output;
        private String playerName;
        private String otherPlayerName;
        private int playerIndex;
        private int otherPlayerIndex;
        private boolean suspended = true;
        private boolean hit;

        /**
         * The player constructor creates {@link Scanner} and {@link Formatter}
         * objects for {@link Socket} interactions with the {@link Client}. It then
         * assigns itself the needed player names for reference during execution.
         *
         * @param socket Socket object to hold a connection to the server
         * @param name   player name to reference during execution
         */
        private Player(Socket socket, String name) {
            this.playerName = name;
            if (playerName.equals("Player One")) {
                otherPlayerName = "Player Two";
            } else {
                otherPlayerName = "Player One";
            }

            this.connection = socket;

            try {
                input = new Scanner(connection.getInputStream());
                output = new Formatter(connection.getOutputStream());
            } catch (IOException ioException) {
                ioException.printStackTrace();
                System.exit(1);
            }
        }

        /**
         * The run() method that is executed during thread launch, sends the
         * needed messages to the {@link Client} for interpretation. It is reliant
         * on the {@link #isGameOver()} function to continue, and awaits for
         * {@link Player} objects to be connected, as well as closes the connections
         * when the game ends.
         *
         * @see #isGameOver()
         * @see Client
         * @see Player
         */
        public void run() {
            try {
                displayMessage(playerName + " running");
                sendMessage("Title", playerName);

                if (playerName.equals("Player One")) {
                    otherPlayerIndex = 1;
                    playerIndex = 0;
                    String cardsDevMessage = "Sending Player One cards:";
                    for (String card : playersHands[0].pileAsStrings()) {
                        cardsDevMessage += (" " + card);
                    }
                    cardsDevMessage += " to Player One";
                    displayMessage(cardsDevMessage);
                    sendMessage("Cards", playersHands[0].pileAsStrings());
                    displayMessage(playerName + " waiting for Player Two");
                    gameLock.lock();
                    try {
                        while (suspended) {
                            otherPlayerConnected.await();
                        }
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    } finally {
                        gameLock.unlock();
                    }
                } else {
                    otherPlayerIndex = 0;
                    playerIndex = 1;
                    String cardsDevMessage = "Sending Player Two cards:";
                    for (String card : playersHands[1].pileAsStrings()) {
                        cardsDevMessage += (" " + card);
                    }
                    cardsDevMessage += " to Player Two";
                    displayMessage(cardsDevMessage);
                    sendMessage("Cards", playersHands[1].pileAsStrings());
                    cardsDevMessage = "Sending Player One cards:";
                    for (String card : playersHands[0].pileAsStrings()) {
                        cardsDevMessage += (" " + card);
                    }
                    cardsDevMessage += " to Player Two";
                    displayMessage(cardsDevMessage);
                    sendMessage("OpCards", playersHands[0].pileAsStrings());
                }

                displayMessage(playerName + " is entering the gameplay loop");
                while (!isGameOver()) {
                    if (currentPlayer.equals(playerName)) {
                        displayMessage("Sending signal to enable buttons on " +
                                playerName);
                        sendMessage("Buttons", "On");
                        // Send other players previous choices:
                        String cardsDevMessage = "Sending " + otherPlayerName + " cards:";
                        for (String card : playersHands[otherPlayerIndex].pileAsStrings()) {
                            cardsDevMessage += (" " + card);
                        }
                        cardsDevMessage += " to " + playerName;
                        displayMessage(cardsDevMessage);
                        sendMessage("OpCards",
                                playersHands[otherPlayerIndex].pileAsStrings());
                        //
                        displayMessage(playerName + " is waiting on button hit");
                        hit = getHit();
                        displayMessage(playerName + " chose " + hit);
                        // calculate that input
                        if (hit) {
                            playersHands[playerIndex].addToPile(deck.removeFromPile(1));
                            cardsDevMessage = "Sending " + playerName + " cards:";
                            for (String card : playersHands[playerIndex].pileAsStrings()) {
                                cardsDevMessage += (" " + card);
                            }
                            cardsDevMessage += " to " + playerName;
                            displayMessage(cardsDevMessage);
                            sendMessage("Cards",
                                    playersHands[playerIndex].pileAsStrings());
                        }
                        sendMessage("Buttons", "Off");
                        sendMessage("Message", "Turn finished, so waiting for other player");
                        currentPlayer = otherPlayerName;
                        gameLock.lock();
                        otherPlayerTurn.signal();
                        otherPlayerTurn.await();
                        gameLock.unlock();
                    } else {
                        sendMessage("Message",
                                "Waiting for " + otherPlayerName);
                        displayMessage(playerName + " waiting for " + otherPlayerName);
                        gameLock.lock();
                        otherPlayerTurn.await();
                        gameLock.unlock();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                displayMessage(playerName + " left gameplay loop");
                try {
                    connection.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    System.exit(1);
                }
            }
        }

        /**
         * Send message uses the {@link Formatter} object created in
         * {@link #Player(Socket, String)} to send messages to connected
         * {@link Client}.
         *
         * @param type    a prefix to the message to represent the type of following
         *                message
         * @param message contents of message type "type" to be interpreted by
         *                the client.
         * @see #Player(Socket, String)
         */
        private void sendMessage(String type, String message) {
            output.format(type + "\n");
            output.flush();
            output.format(message + "\n");
            output.flush();
        }

        /**
         * An alternative to {@link #sendMessage(String, String)} that handles the same,
         * except can deliver an array of messages instead of one string.
         *
         * @param type     a prefix to the message to represent the type of following
         *                 messages for the client
         * @param messages a set of messages to be delivered following the prefix
         */
        private void sendMessage(String type, String[] messages) {
            output.format(type + "\n");
            output.flush();
            for (String message : messages) {
                output.format(message + "\n");
                output.flush();
            }
            output.format("END\n");
            output.flush();
        }

        /**
         * Utilizes the {@link Scanner} object created earlier to get input from
         * the {@link Client} on if they would like to "hit" or "stay" on their
         * active turn.
         *
         * @return true if the player would like to "hit" or false otherwise.
         */
        private boolean getHit() {
            if (input.hasNext()) {
                return input.nextLine().equals("Hit");
            }
            return false;
        }

        /**
         * a lock safety as advised to use, so that the Player object knows if
         * the dealer has suspended it upon being signaled.
         *
         * @param status saftey for permission of player to operate
         */
        private void setSuspended(boolean status) {
            suspended = status;
        }
    }
}
