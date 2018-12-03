import java.io.*;
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
    private DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm:ss.S");

    //Streams used for logging by Inner Clients
    private FileOutputStream serverLogStream;
    private FileOutputStream transactionLogStream;

    // Space for InternalClients to be held upon each connection
    private List<InternalClient> allInternalClients = new ArrayList<>();
    private int clientIndex;

    // Thread Management
    private ExecutorService runClients;
    private Lock dbLock;
    private Condition dbFree;

    // Database
    private final Database db = new Database();

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

        String logPath = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();

        File logDirectory = new File(logPath + File.separator + "log");

        if(!logDirectory.exists()){
            logDirectory.mkdir();
        }

        File serverLog = new File(logDirectory + File.separator + "server_log.log");
        File transactionLog = new File(logDirectory + File.separator + "transaction_log.log");

        try{
            serverLogStream = new FileOutputStream(serverLog, false);
            transactionLogStream = new FileOutputStream(transactionLog, false);
            serverLogStream.write(("Server started at " + LocalTime.now().format(timeFormat)).getBytes());
            transactionLogStream.write(("Server started at " + LocalTime.now().format(timeFormat)).getBytes());
            serverLogStream.close();
            transactionLogStream.close();
            serverLogStream = new FileOutputStream(serverLog, true);
            transactionLogStream = new FileOutputStream(transactionLog, true);

        }catch (IOException e){
            System.out.println(e);
        }
        execute();
    }

    public void execute() {
        print("Server beginning infinite loop of waiting for clients  to connect");
        while (true) {
            try {
                print("Waiting for connection #" + (clientIndex + 1));
                allInternalClients.add(new InternalClient(server.accept()));
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
        private int clientNum;
        private Formatter formatterOutput;

        private int activeUserType = 0;
        private String activeUsername = "";

        public InternalClient(Socket socket) {
            this.connection = socket;
            clientNum = clientIndex;

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
                    break;
                } catch (ClassNotFoundException e) {
                    System.out.println(e);
                    break;
                }


            }
        }

        private void processMessage(Request request) {
            synchronized (db) {
                try {
                    if (request instanceof AddUserRequest) {
                        serverLogStream.write(("Client " + clientNum + " made a AddUserRequest at " + LocalTime.now().format(timeFormat)).getBytes());
                        AddUserRequest addUserRequest = (AddUserRequest) request;
                        boolean result = db.createUser(addUserRequest.getUsername(),
                                addUserRequest.getPassword(), addUserRequest.getType());
                        if (result) {
                            activeUsername = addUserRequest.getUsername();
                            activeUserType = addUserRequest.getType();
                        }
                        output.writeObject(result);
                    } else if (request instanceof LoginRequest) {
                        serverLogStream.write(("Client " + clientNum + " made a LoginRequest at " + LocalTime.now().format(timeFormat)).getBytes());
                        LoginRequest loginRequest = (LoginRequest) request;
                        activeUserType = db.login(loginRequest.getUsername(), loginRequest.getPassword());
                        output.writeObject(activeUserType);
                    } else if (request instanceof LogoutRequest) {
                        serverLogStream.write(("Client " + clientNum + " made a LogoutRequest at " + LocalTime.now().format(timeFormat)).getBytes());
                        activeUserType = 0;
                        activeUsername = "";
                        output.writeObject(new Boolean(true));
                    } else if (request instanceof GetAllItemsRequest) {
                        serverLogStream.write(("Client " + clientNum + " made a GetAllItemRequest at " + LocalTime.now().format(timeFormat)).getBytes());
                        output.writeObject(db.getAllItems());
                    } else if (request instanceof GetItemRequest) {
                        serverLogStream.write(("Client " + clientNum + " made a GetItemRequest at " + LocalTime.now().format(timeFormat)).getBytes());
                        GetItemRequest getItemRequest = (GetItemRequest) request;
                        output.writeObject(db.getItemInfo(getItemRequest.getItem()));
                    } else if (request instanceof BuyItemRequest) {
                        serverLogStream.write(("Client " + clientNum + " made a BuyItemRequest at " + LocalTime.now().format(timeFormat)).getBytes());
                        BuyItemRequest buyItemRequest = (BuyItemRequest) request;
                        Boolean successful = db.buyItem(buyItemRequest.getItemId());
                        if(successful){
                            transactionLogStream.write((activeUsername + " bought item " + buyItemRequest.getItem().getItemName() +
                                    " from " + buyItemRequest.getItem().getSeller()).getBytes() );
                        }
                        output.writeObject(successful);
                    } else if (request instanceof SellItemRequest) {
                        serverLogStream.write(("Client " + clientNum + " made a SellItemRequest at " + LocalTime.now().format(timeFormat)).getBytes());
                        SellItemRequest sellItemRequest = (SellItemRequest) request;
                        output.writeObject(db.sellItem(activeUsername, sellItemRequest.getItem()));
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
