import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* The purpose of this class is purely to create 4 new clients and a server for
   Demo purposes. Right now it's just a print statement to get everyone a branching
   point, but eventually it could look something like what's commented below
*/
public class MainDemo {
    public static void main(String[] args){

        // Enter however many clients you would like to use
        int CLIENT_COUNT = 3;

        ExecutorService worker = Executors.newCachedThreadPool();
        worker.execute(new ServerRunnable());

        HomeFrame[] clients = new HomeFrame[CLIENT_COUNT];

        for (int i = 0; i < CLIENT_COUNT; i++){
            clients[i] = new HomeFrame();
        }

        // Every 20 seconds, the clients will update their info in order to stay
        // up to date.
        /*while(true){
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < CLIENT_COUNT; i++){
                clients[i].updateAllItems();
            }
        }*/
    }
}
