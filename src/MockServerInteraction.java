import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Formatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* Purpose of this class is just to start a new server on it's own thread,
and run two sample buttons acting as "clients" that interact with the server,
proving it's capability to lock database access and queue requests.
 */
public class MockServerInteraction {

    public static void main(String[] args) {
        ExecutorService worker = Executors.newFixedThreadPool(3);
        worker.execute(new ServerRunnable());
        // new server and 2 buttons to interact and test thread transfer
        new testButton(1);
        new testButton(2);
    }

    /*
    A class that's only meant to send a signal to the server as an example
     */
    private static class testButton extends JFrame {

        private int num;
        private Socket connection;
        private Formatter output;

        public testButton(int num) {
            this.num = num;

            JButton onlyButton = new JButton(String.valueOf(num));
            onlyButton.addActionListener(e -> sendPress());

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            add(onlyButton);
            setSize(new Dimension(200, 200));
            setResizable(false);
            setAlwaysOnTop(true);
            setVisible(true);

            try {
                Socket connection = new Socket(InetAddress.getByName("127.0.0.1"), 23517);
                output = new Formatter(connection.getOutputStream());
            } catch (IOException ioException) {
                System.out.println("\nConnection failed? Did you try starting a server first?\n");
                ioException.printStackTrace();
                System.exit(1);
            }
        }

        private void sendPress() {
            //System.out.println("Button:" + num + " sending button pressed");
            output.format("Button Pressed\n");
            output.flush();
        }

    }
}
