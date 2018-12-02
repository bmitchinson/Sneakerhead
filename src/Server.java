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
    private int clientIndex;

    // Thread Management
    private ExecutorService runClients;
    private Lock dbLock;
    private Condition dbFree;

    // Database
    Wrapper db = new Wrapper();

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

        execute();
    }

    public void execute() {
        print("Server beginning infinite loop of waiting for clients  to connect");
        while (true) {
            try {
                print("Waiting for connection #" + (clientIndex + 1));
                allInternalClients.add(
                        new InternalClient(server.accept(), clientIndex)
                );
                runClients.execute(allInternalClients.get(clientIndex));
            } catch (IOException e) {
                print("Connection failed in server execute");
                e.printStackTrace();
                System.exit(1);
            } finally {
                clientIndex++;
            }

        }
    }

    public void print(String message) {
        System.out.println(LocalTime.now().format(timeFormat) + message);
    }

    private void processMessage(String message) {
        synchronized (db) {

            // Simulating time it takes to access the database
            for (int i = 5; i > 0; i--) {
                System.out.print(i + "..");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("\nNotifying all others who may be waiting");
            db.notifyAll();
        }
        System.out.println("Exiting synchronized");
    }

    private class InternalClient implements Runnable {

        // Network socket +  needed object streams
        private Socket connection;
        private Scanner scannerInput;
        private Formatter formatterOutput;

        private int num;

        public InternalClient(Socket socket, int num) {
            print("Client " + (num + 1) + " initialized");
            this.connection = socket;
            this.num = num;

            try {
                scannerInput = new Scanner(connection.getInputStream());
            } catch (IOException e) {
                System.out.println("Somehow a Scanner object couldn't be created" +
                        "from the input stream from connection");
                e.printStackTrace();
            }
        }

        public void run() {
            print("Client " + (num + 1) + " waiting for input");
            while (true) {
                if (scannerInput.hasNextLine()) {
                    processMessage(scannerInput.nextLine());
                }
            }
        }

    }

}
