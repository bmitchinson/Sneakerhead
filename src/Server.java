import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    // Networking
    private ServerSocket server;
    private DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm:ss.SSS - ");

    // Space for InternalClients to be held upon each connection
    private List<InternalClient> allInternalClients = new ArrayList<>();
    private int clientCount;

    // Thread Management
    private ExecutorService runClients;
    private Lock dbLock;
    private Condition dbFree;

    // Database
    // Wrapper db = new Wrapper();

    public Server() {
        print("Constructing Server");

        runClients = Executors.newCachedThreadPool();
        dbLock = new ReentrantLock();
        dbFree = dbLock.newCondition();

        try {
            server = new ServerSocket(23517, 8); // Setup socket
        } catch (IOException ioException) {
            print("\nPort 23517 is already in use, " +
                    "do you already have a server running?\n");
            ioException.printStackTrace();
            System.exit(1);
        } finally {
            print("Server opened on port 23517");
        }
    }

    public void execute() {
        print("Server beginning infinite loop of waiting for clients to connect");
        while (true) {
            try {
                print("Waiting for connection #" + (clientCount + 1));
                allInternalClients.add(
                        new InternalClient(server.accept(), clientCount)
                );
                runClients.execute(allInternalClients.get(clientCount));
            } catch (IOException e) {
                print("Connection failed in server execute");
                e.printStackTrace();
                System.exit(1);
            } finally {
                clientCount++;
            }

        }
    }

    public void dev(){
        // TODO: A dummy execute() to simulate GUI's connecting?
    }

    public void print(String message) {
        System.out.println(LocalTime.now().format(timeFormat) + message);
    }

    private class InternalClient implements Runnable {

        // Network socket +  needed file streams
        private Socket connection;
        private FileInputStream fileInput;
        private Scanner scannerInput;
        private Formatter formatterOutput;

        private int num;

        public InternalClient(Socket socket, int num) {

            print("Client " + num + " initialized");
            this.connection = socket;
            this.num = num;

            // Initialize streams out of connection

        }

        public void run() {
            // Initial object delivery

            // Loop of while connection is open?
            // Wait for incoming string messages
        }

        private void processMessage(String message) {
            System.out.println("Client-" + num +
                    " received the following:\nMessage:");
        }

    }

}
