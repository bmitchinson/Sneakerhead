import Requests.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    // Database
    private final Database db = new Database();

    //construct Server
    public Server() {
        print("Constructing Server");

        runClients = Executors.newCachedThreadPool();

        try {
            //create port for Server
            server = new ServerSocket(23517, 8); // Setup socket
        } catch (IOException ioException) {
            print("\nPort 23517 is already in use, " +
                    "do you already have a server running?\n");
            ioException.printStackTrace();
            System.exit(1);
        } finally {
            print("Server opened on port 23517");
        }


        //create log files and get output streams ready so the clients can write the requests they make
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
            serverLogStream.write(("Server started at " + LocalTime.now().format(timeFormat) + "\n").getBytes());
            transactionLogStream.write(("Server started at " + LocalTime.now().format(timeFormat) + "\n").getBytes());
            serverLogStream.close();
            transactionLogStream.close();
            serverLogStream = new FileOutputStream(serverLog, true);
            transactionLogStream = new FileOutputStream(transactionLog, true);

        }catch (IOException e){
            System.out.println(e);
        }
        //start server
        execute();
    }

    //method starts the server and tells it to wait for connection
    public void execute() {
        print("Server beginning infinite loop of waiting for clients  to connect");
        while (true) {
            try {
                print("Waiting for connection #" + (clientIndex + 1));
                //wait for Client to connect
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

    //IternalClient is a runnable inner class that will get requests and pass them to the database and retrun a result
    private class InternalClient implements Runnable {

        // Network socket +  needed object streams
        private ObjectInputStream input;
        private ObjectOutputStream output;

        //holds what type of user is logged in
        private int activeUserType = 0;
        //holds username of user
        private String activeUsername = "";

        //constructor initializes the ObjectInput and OutputStream
        public InternalClient(Socket socket) {
            try {
                this.input = new ObjectInputStream(socket.getInputStream());
                this.output = new ObjectOutputStream(socket.getOutputStream());
                output.flush();
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        //method will run and constantly listen for a Request from the Client and then process the Request
        public void run() {
            Request request = null;
            while (true) {
                try {
                    //get Request and Process it
                    request = (Request) input.readObject();
                    processRequest(request);
                } catch (IOException e) {
                    System.out.println(e);
                    break;
                } catch (ClassNotFoundException e) {
                    System.out.println(e);
                    break;
                }


            }
        }

        //processRequest will take a Request and figure out what type of Request it is then query the database and return the appropriate type of Object
        private void processRequest(Request request) {
            synchronized (db) {
                try {
                    //Ifs test what kind of request was made then query the database and return the appropriate result
                    if (request instanceof AddUserRequest) {
                        //log the request was made
                        serverLogStream.write(("Client made a AddUserRequest at " + LocalTime.now().format(timeFormat) + "\n").getBytes());
                        //Cast request to appropriate class
                        AddUserRequest addUserRequest = (AddUserRequest) request;
                        //query database by pulling needed info from the Request
                        boolean result = db.createUser(addUserRequest.getUsername(),
                                addUserRequest.getPassword(), addUserRequest.getType());
                        //process results from database if needed
                        if (result) {
                            activeUsername = addUserRequest.getUsername();
                            activeUserType = addUserRequest.getType();
                        }
                        //return something to user
                        output.writeObject(result);

                    //each of these requests follows a similar algorithm to the above request
                    } else if (request instanceof LoginRequest) {
                        serverLogStream.write(("Client made a LoginRequest at " + LocalTime.now().format(timeFormat) + "\n").getBytes());
                        LoginRequest loginRequest = (LoginRequest) request;
                        activeUserType = db.login(loginRequest.getUsername(), loginRequest.getPassword());
                        if(activeUserType != 0){
                            activeUsername = loginRequest.getUsername();
                        }
                        output.writeObject(activeUserType);
                    } else if (request instanceof LogoutRequest) {
                        serverLogStream.write(("Client made a LogoutRequest at " + LocalTime.now().format(timeFormat) + "\n").getBytes());
                        activeUserType = 0;
                        activeUsername = "";
                        output.writeObject(new Boolean(true));
                    } else if (request instanceof GetAllItemsRequest) {
                        serverLogStream.write(("Client made a GetAllItemRequest at " + LocalTime.now().format(timeFormat) + "\n").getBytes());
                        output.writeObject(db.getAllItems());
                    } else if (request instanceof BuyItemRequest) {
                        serverLogStream.write(("Client made a BuyItemRequest at " + LocalTime.now().format(timeFormat) + "\n").getBytes());
                        BuyItemRequest buyItemRequest = (BuyItemRequest) request;
                        Boolean successful = db.buyItem(buyItemRequest.getItemId());
                        if(successful){
                            transactionLogStream.write((activeUsername + " bought item " + buyItemRequest.getItem().getItemName() +
                                    " from " + buyItemRequest.getItem().getSeller() + "\n").getBytes() );
                        }
                        output.writeObject(successful);
                    } else if (request instanceof SellItemRequest) {
                        serverLogStream.write(("Client made a SellItemRequest at " + LocalTime.now().format(timeFormat) + "\n").getBytes());
                        SellItemRequest sellItemRequest = (SellItemRequest) request;
                        output.writeObject(db.sellItem(activeUsername, sellItemRequest.getItem()));
                    }

                } catch (IOException e) {
                    System.out.println("IOException");
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
