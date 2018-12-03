public class ServerRunnable implements Runnable {
    // A quick solution to create a runnable edition of our server,
    // used for MainDemo, but otherwise server instances can be created on their
    // own if you'd like to create them yourself
    public void run() {
        Server server = new Server();
    }
}
