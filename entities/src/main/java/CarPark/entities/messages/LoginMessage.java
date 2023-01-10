package CarPark.entities.messages;

public class LoginMessage extends Message {
    private String user_name;
    private String password;

    public LoginMessage(String user_name, String pass, MessageType message_type) {
        super(message_type);
        this.user_name = user_name;
        this.password = pass;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
