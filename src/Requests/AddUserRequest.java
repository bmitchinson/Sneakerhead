package Requests;

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
