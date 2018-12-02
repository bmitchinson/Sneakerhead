public class AddUserRequest extends Request {
    private final String username;
    private final String password;

    public AddUserRequest(String username, String password){
        this.username = username;
        this.password = password;
    }
}
