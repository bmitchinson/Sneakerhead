import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * the NewGame class's sole purpose is to set up the 3 required instances of
 * one {@link Dealer} and two {@link Client} for parallel execution. They're created
 * using the {@link StartClient} and {@link StartDealer} runnables.
 * <p>
 * It is a completion of the 28-18_Blackjack_Hard assignment
 *
 * @author Ben Mitchinson
 * @see Dealer
 * @see Client
 * @see StartClient
 * @see StartDealer
 */
public class NewGame {
    public static void main(String args[]) {
        ExecutorService worker = Executors.newFixedThreadPool(3);
        worker.execute(new StartDealer());
        worker.execute(new StartClient());
        worker.execute(new StartClient());
    }
}
