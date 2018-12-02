import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ServerRequester {
    private Socket socket;
    private String ip;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public ServerRequester(String ip){
        this.ip = ip;
    }

    public void start(){
        try{
            connect();
            setupStreams();
        }catch (IOException e){
            System.out.println(e);
        }
    }

    public void connect() throws IOException{
        socket = new Socket(InetAddress.getByName(ip), 23517);
        System.out.println("Connection Successful...");
    }

    public void setupStreams() throws IOException{
        output = new ObjectOutputStream(socket.getOutputStream());

        input = new ObjectInputStream(socket.getInputStream());
    }

    public Object makeRequest(Request request){
        Object response = new Object();
        try{
            output.writeObject(request);
        }catch (IOException e){
            System.out.println(e);
        }
        return response;
    }

    public static void main(String[] args){
        ServerRequester requester = new ServerRequester("172.17.108.210");
        requester.start();
    }

}
