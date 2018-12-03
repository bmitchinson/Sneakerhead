import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* The purpose of this class is purely to create 4 new clients and a server for
   Demo purposes. Right now it's just a print statement to get everyone a branching
   point, but eventually it could look something like what's commented below
*/
public class MainDemo {
    public static void main(String[] args){

        ExecutorService worker = Executors.newCachedThreadPool();
        worker.execute(new ServerRunnable());

        HomeFrame client_one = new HomeFrame();

        /*while(true){
            System.out.println("Waiting 15 seconds to see if there's an update " +
                    "to client_one");
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Calling update");
            client_one.updateAllItems();
        }*/
    }
}
