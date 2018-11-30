import javax.swing.*;

/**
 * StartDealer launches a new Dealer, and is a {@link Runnable} so that it can be
 * called alongside others in {@link NewGame}.
 */
public class StartDealer implements Runnable {
    /**
     * @param args - unused
     * @see StartDealer
     */
    public static void main(String args[]) {
        Dealer dealer = new Dealer();
        dealer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dealer.execute();
    }

    /**
     * @see StartDealer
     */
    public void run() {
        Dealer dealer = new Dealer();
        dealer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dealer.execute();
    }

}
