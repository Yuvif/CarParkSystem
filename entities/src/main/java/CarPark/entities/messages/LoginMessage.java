package CarPark.entities.messages;

import CarPark.entities.Customer;
import CarPark.entities.Employee;

public class LoginMessage extends Message {
    public RequestType request_type;
    public ResponseType response_type;
    private long user_id;
    private String password;
    public Employee employee;
    public Customer customer;

    public LoginMessage(MessageType message_type, RequestType request_type,long user_id, String pass) {
        super(message_type);
        this.request_type = request_type;
        this.user_id = user_id;
        this.password = pass;
    }

    public enum RequestType {
        LOGIN
    }

    public enum ResponseType {
        LOGIN_SUCCEED_CUSTOMER,
        LOGIN_SUCCEED_EMPLOYEE,
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
