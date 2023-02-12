package CarPark.entities.messages;

import CarPark.entities.User;

import javax.persistence.ManyToOne;

public class LoginMessage extends Message {
    public RequestType request_type;
    public ResponseType response_type;
    private String user_id;
    private String password;
    @ManyToOne
    private User user;

    public LoginMessage(MessageType request, RequestType logout, String id) {
        super(request);
        this.request_type = logout;
        this.user_id = id;
    }

    public User getUser() {
        return user;
    }

    public LoginMessage(MessageType message_type, RequestType request_type, String user_id, String pass) {
        super(message_type);
        this.request_type = request_type;
        this.user_id = user_id;
        this.password = pass;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public enum RequestType {
        LOGIN,
        LOGOUT_NORMALLY,
        LOGOUT_BY_TERMINATION
    }

    public enum ResponseType {
        LOGIN_SUCCEED_EMPLOYEE,
        LOGIN_SUCCEED_CUSTOMER,
        LOGIN_FAILED,
        ALREADY_LOGGED,
        LOGGED_OUT
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}