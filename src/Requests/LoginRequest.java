package Requests;

import Requests.Request;

// extends the Serializable object Request to send a request to "login" through the output stream

public class LoginRequest extends Request {
    private String username;
    private String password;

    public LoginRequest(String username, String password){
        this.username = username;
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
