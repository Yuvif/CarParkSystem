package CarPark.entities.messages;

import CarPark.entities.User;

import javax.persistence.ManyToOne;

public class LoginMessage extends Message {
    public RequestType request_type;
    public ResponseType response_type;
    private long user_id;
    private String password;
    @ManyToOne
    private User user;

    public User getUser() {
        return user;
    }

    public LoginMessage(MessageType message_type, RequestType request_type,long user_id, String pass) {
        super(message_type);
        this.request_type = request_type;
        this.user_id = user_id;
        this.password = pass;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public enum RequestType {
        LOGIN
    }

    public enum ResponseType {
        LOGIN_SUCCEED,
        LOGIN_FAILED
    }

    public long getUserId() {
        return user_id;
    }

    public void setUserId(long user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
