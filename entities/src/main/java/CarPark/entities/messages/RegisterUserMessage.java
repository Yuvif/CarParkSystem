package CarPark.entities.messages;

import CarPark.entities.Customer;

public class RegisterUserMessage extends Message{

    public RequestType request_type;
    public ResponseType response_type;
    public Customer newCustomer;


    public enum RequestType {
        REGISTER
    }

    public enum ResponseType
    {
        REGISTRATION_FAILED,
        REGISTRATION_SUCCEEDED
    }

    public RegisterUserMessage(MessageType message_type, RequestType request_type, Customer customer)
    {
        super(message_type);
        this.request_type = request_type;
        this.newCustomer = customer;
    }

    public RegisterUserMessage(MessageType message_type, RegisterUserMessage.ResponseType response_type) {
        super(message_type);
        this.response_type = response_type;
    }


}
