import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    private final Wrapper db = new Wrapper();

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
                        new InternalClient(server.accept())
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
        System.out.println(LocalTime.now().format(timeFormat) + " Server:" + message);
    }

    public void print(String message, String who) {
        System.out.println(LocalTime.now().format(timeFormat) + " " + who + ":" + message);
    }


    private class InternalClient implements Runnable {

        // Network socket +  needed object streams
        private Socket connection;
        private Scanner scannerInput;
        private ObjectInputStream input;
        private ObjectOutputStream output;
        private Formatter formatterOutput;

        private int userType;
        private String activeUsername;
        private int activeUserType;

        public InternalClient(Socket socket) {

            this.connection = socket;

            try {
                this.input = new ObjectInputStream(socket.getInputStream());
                this.output = new ObjectOutputStream(socket.getOutputStream());
                output.flush();
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        public void run() {
            Request request = null;
            while (true) {
                try {
                    request = (Request) input.readObject();
                    processMessage(request);
                } catch (IOException e) {
                    System.out.println(e);
                } catch (ClassNotFoundException e) {
                    System.out.println(e);
                }


            }
        }


        private void processMessage(Request request) {
            synchronized (db) {
                try {
                    if (request instanceof AddUserRequest) {
                        AddUserRequest addUserRequest = (AddUserRequest) request;
                        output.writeObject(db.createUser(addUserRequest.getUsername(), addUserRequest.getPassword(), addUserRequest.getType()));
                    }
                    else if (request instanceof GetAllItemsRequest){
                        output.writeObject(db.getAllItems());
                    }
                    else if(request instanceof GetItemRequest){
                        GetItemRequest getItemRequest = (GetItemRequest) request;
                        output.writeObject(db.getItemInfo(getItemRequest.getItem()));
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }

                System.out.println("\nNotifying all others who may be waiting");
                db.notifyAll();
            }
            System.out.println("Exiting synchronized");
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
    }


}
