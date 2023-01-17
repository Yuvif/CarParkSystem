package CarPark.entities.messages;

import CarPark.entities.Customer;
import CarPark.entities.Membership;

public class RegisterMessage extends Message{

    public RequestType request_type;
    public ResponseType response_type;
    public Membership newMembership;
    public Customer current_customer;

    public enum RequestType {
        REGISTER
    }

    public enum ResponseType {
        REGISTRATION_FAILED,
        REGISTRATION_SUCCEEDED
    }

    public RegisterMessage(MessageType message_type, RequestType request_type, Membership membership, Customer customer)
    {
        super(message_type);
        this.request_type = request_type;
        this.newMembership = membership;
        this.current_customer = customer;
    }

    public RegisterMessage(MessageType message_type, RegisterMessage.ResponseType response_type) {
        super(message_type);
        this.response_type = response_type;
    }

}
