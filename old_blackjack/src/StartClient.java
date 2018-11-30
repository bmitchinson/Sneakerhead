import javax.swing.*;

/**
 * StartClient launches a new Client, and is a {@link Runnable} so that it can be
 * called alongside others in {@link NewGame}.
 */
public class StartClient implements Runnable {
    /**
     * @param args - unused
     * @see StartClient
     */
    public static void main(String args[]){
        Client client = new Client();
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * @see StartClient
     */
    public void run(){
        Client client = new Client();
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
