package Requests;

// extends the Serializable object Request to send a request to "add a user" through the output stream

public class AddUserRequest extends Request {
    private final String username;
    private final String password;
    private final int type;

    public AddUserRequest(String username, String password, int type){
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getType() {
        return type;
    }
}
