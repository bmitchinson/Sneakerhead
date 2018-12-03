import Requests.AddUserRequest;
import Requests.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

//server requester can take requests in its makeRequest method and pass them to the server it is connected to
public class ServerRequester {
    //socket used to connect to server
    private Socket socket;
    //holds ip of server
    private String ip;
    //input stream
    private ObjectInputStream input;
    //output stream
    private ObjectOutputStream output;

    //create requester
    public ServerRequester(String ip) {
        this.ip = ip;
    }

    //start requester and connect to server
    public boolean start() {
        try {
            connect();
            setupStreams();
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    //connect to Server
    public void connect() throws IOException {
        socket = new Socket(InetAddress.getByName(ip), 23517);
        System.out.println("SeverRequester: Connection Successful...");
    }

    //setup input and output streams
    public void setupStreams() throws IOException {
        output = new ObjectOutputStream(socket.getOutputStream());
        output.flush();
        input = new ObjectInputStream(socket.getInputStream());
    }

    //send request to server and return as Object, the user will need to cast it to what they can expect
    public Object makeRequest(Request request) {
        Object response = new Object();
        try {
            output.writeObject(request);
            response = input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }
        return response;
    }

}
