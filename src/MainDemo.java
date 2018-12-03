import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
   ALL INFO ON HOW TO WALKTHROUGH OUR SOLUTION IS AVAILABLE IN OUR README, BOTH
   IN THE ZIP ON ICON, AND ON OUR MASTER BRANCH. - Team 37

   The purpose of this class is purely to create 3 new clients and a server for
   Demo purposes. Running MainDemo shows off the major features of our solution,
   and additional HomeFrame (client) objects can be instantiated individually
   as desired afterwords.
*/
public class MainDemo {
    public static void main(String[] args){

        // Enter however many clients you would like to use
        int CLIENT_COUNT = 3;

        // To test user functionality we've created three user accounts for use
        // Buyer:
        ExecutorService worker = Executors.newCachedThreadPool();

        worker.execute(new ServerRunnable());
        try{
            Thread.sleep(3000);
        }catch(InterruptedException e){

        }
        HomeFrame[] clients = new HomeFrame[CLIENT_COUNT];

        for (int i = 0; i < CLIENT_COUNT; i++){
            clients[i] = new HomeFrame();
        }

    }
}
